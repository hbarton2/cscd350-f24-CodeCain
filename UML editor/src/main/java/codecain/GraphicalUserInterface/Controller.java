package codecain.GraphicalUserInterface;

import codecain.BackendCode.SaveManager;
import codecain.BackendCode.UMLClass;
import codecain.BackendCode.UMLClassInfo;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

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
        String className = "New Class #" + (UMLClass.classMap.size() + 1);
        ClassNode classNode = new ClassNode(new UMLClassInfo(className));

        // Calculate the center of the canvas
        double centerX = (nodeContainer.getWidth() - classNode.getPrefWidth()) / 2;
        double centerY = (nodeContainer.getHeight() - classNode.getPrefHeight()) / 2;

        // Set the position of the classNode to the center of the canvas
        classNode.setLayoutX(centerX);
        classNode.setLayoutY(centerY);

        classNode.setOnMouseClicked(event -> selectClassNode(classNode));
        nodeContainer.getChildren().add(classNode);

//        Storage.addClass(className);
        UMLClass.addClass(className);
        System.out.println("Storage size: " + UMLClass.classMap.size());


    }

    @FXML
    private void deleteClassBtn() {
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
}
