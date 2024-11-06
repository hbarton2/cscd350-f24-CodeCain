package codecain;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class Controller {

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

        classContainer.getChildren().add(classNode);


    }
}
