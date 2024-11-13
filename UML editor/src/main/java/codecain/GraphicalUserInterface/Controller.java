package codecain.GraphicalUserInterface;

import codecain.BackendCode.SaveManager;
import codecain.BackendCode.UMLClass;
import codecain.BackendCode.UMLClassInfo;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.Alert;

import java.io.IOException;

public class Controller {

    @FXML
    private ClassNode currentlySelectedNode = null;

    @FXML
    private AnchorPane classContainer;

    @FXML
    private Pane nodeContainer;

    @FXML
    private void addClassBtn() {

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Class");
        dialog.setHeaderText("Enter the name of the class to add:");
        dialog.setContentText("Class Name:");

        String className = dialog.showAndWait().orElse(null);
        if(className == null || className.trim().isEmpty()) {
            showAlert("Error", "Invalid class name", "Class not added.");
        } else if(UMLClass.exists(className)) {
            showAlert("Error", "Class '" + className + "' already exists.", "");
        } else {
                UMLClass.addClass(className);
                ClassNode classNode = new ClassNode(UMLClass.getClassInfo(className));

                // Position nodes (e.g., center as a placeholder; adjust as needed)
                double centerX = (nodeContainer.getWidth() - classNode.getPrefWidth()) / 2;
                double centerY = (nodeContainer.getHeight() - classNode.getPrefHeight()) / 2;
                classNode.setLayoutX(centerX);
                classNode.setLayoutY(centerY);

                // Add click event for selection
                classNode.setOnMouseClicked(event -> selectClassNode(classNode));

                // Add the new ClassNode to the node container
                nodeContainer.getChildren().add(classNode);
        }
    }

    @FXML
    private void deleteClassBtn() {
        if(currentlySelectedNode == null) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Delete Class");
            dialog.setHeaderText("Enter the name of the class to delete:");
            dialog.setContentText("Class Name:");

            String className = dialog.showAndWait().orElse(null);
            if(className == null || className.trim().isEmpty()) {
                showAlert("Error", "Invalid class name", "Deletion canceled.");
            } else if(!UMLClass.exists(className)) {
                showAlert("Error", "Class '" + className + "' does not exist.", "Deletion canceled.");
            } else {
                UMLClass.removeClass(className);
                for(Node classNode : nodeContainer.getChildren()) {
                    if(classNode instanceof ClassNode) {
                        if(((ClassNode) classNode).getName().equals(className)) {
                            nodeContainer.getChildren().remove(classNode);
                            break;
                        }
                    }
                }

            }

        } else {
            for(Node classNode : nodeContainer.getChildren()) {
                if(classNode instanceof ClassNode) {
                    if(((ClassNode) classNode).isSelected()) {
                        nodeContainer.getChildren().remove(classNode);
                        UMLClass.removeClass(((ClassNode) classNode).getName());
                        System.out.println("Storage size: " + UMLClass.classMap.size());
                        break;
                    }
                }
            }
        }        
    }

    @FXML
    private void saveBtn() throws IOException {
        nodeContainer.getChildren().forEach(node -> {
           if (node instanceof ClassNode) {
               ((ClassNode) node).syncWithUMLClassInfo();
           }
        });

        SaveManager.saveToJSON("new_uml_diagram.json");
    }

    @FXML
    private void loadBtn() throws IOException {
        String filePath = "new_uml_diagram.json"; // Set your file path
        SaveManager.loadFromJSON(filePath);

        // After loading, add the ClassNodes to the AnchorPane
        nodeContainer.getChildren().clear();
        UMLClass.classMap.values().forEach(classInfo -> {
           ClassNode classNode = new ClassNode(classInfo);

            // Position nodes (e.g., center as a placeholder; adjust as needed)
            double centerX = (nodeContainer.getWidth() - classNode.getPrefWidth()) / 2;
            double centerY = (nodeContainer.getHeight() - classNode.getPrefHeight()) / 2;
            classNode.setLayoutX(centerX);
            classNode.setLayoutY(centerY);

            // Add click event for selection
            classNode.setOnMouseClicked(event -> selectClassNode(classNode));

            // Add the new ClassNode to the node container
            nodeContainer.getChildren().add(classNode);
        });

        System.out.println("UML diagram loaded and nodes displayed.");
    }

    private void selectClassNode(ClassNode classNode) {
        // Deselect the previously selected node, if any
        if (currentlySelectedNode != null) {
            currentlySelectedNode.deselect();
        }

        // Select the new node
        classNode.select();
        currentlySelectedNode = classNode;
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
