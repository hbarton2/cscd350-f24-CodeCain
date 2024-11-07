package codecain;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class Controller {

    @FXML
    private ClassNode currentlySelectedNode = null;

    @FXML
    private AnchorPane classContainer;

    @FXML
    private AnchorPane nodeContainer;

    @FXML
    private void addClassBtn() {
        String className = "New Class #" + (Storage.storage.size() + 1);
        ClassNode classNode = new ClassNode(className);

        // Calculate the center of the canvas
        double centerX = (nodeContainer.getWidth() - classNode.getPrefWidth()) / 2;
        double centerY = (nodeContainer.getHeight() - classNode.getPrefHeight()) / 2;

        // Set the position of the classNode to the center of the canvas
        classNode.setLayoutX(centerX);
        classNode.setLayoutY(centerY);

        classNode.setOnMouseClicked(event -> selectClassNode(classNode));
        nodeContainer.getChildren().add(classNode);

        Storage.addClass(className);
        System.out.println("Storage size: " + Storage.storage.size());


    }

    @FXML
    private void deleteClassBtn() {
        for(Node classNode : nodeContainer.getChildren()) {
            if(classNode instanceof ClassNode) {
                if(((ClassNode) classNode).isSelected()) {
                    nodeContainer.getChildren().remove(classNode);
                    break;
                }
            }
        }
    }

    @FXML
    private void saveBtn() throws IOException {
        SaveManager.saveToJSONFX("uml_diagram.json");
    }

    @FXML
    private void loadBtn() throws IOException {
        String filePath = "uml_diagram.json"; // Set your file path
        SaveManager.loadFromJSONFX(filePath);

        // After loading, add the ClassNodes to the AnchorPane
        nodeContainer.getChildren().clear();
        Storage.storage.values().forEach(classNode -> nodeContainer.getChildren().add(classNode));
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
