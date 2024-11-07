package codecain;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class Controller {

    @FXML
    private ClassNode currentlySelectedNode = null;

    @FXML
    private AnchorPane classContainer;

    @FXML
    private void addClassBtn() {
        ClassNode classNode = new ClassNode("New Class");

        // Calculate the center of the canvas
        double centerX = (classContainer.getWidth() - classNode.getPrefWidth()) / 2;
        double centerY = (classContainer.getHeight() - classNode.getPrefHeight()) / 2;

        // Set the position of the classNode to the center of the canvas
        classNode.setLayoutX(centerX);
        classNode.setLayoutY(centerY);

        classNode.setOnMouseClicked(event -> selectClassNode(classNode));

        classContainer.getChildren().add(classNode);


    }

    @FXML
    private void deleteClassBtn() {
        for(Node classNode : classContainer.getChildren()) {
            if(classNode instanceof ClassNode) {
                if(((ClassNode) classNode).isSelected()) {
                    classContainer.getChildren().remove(classNode);
                    break;
                }
            }
        }
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
