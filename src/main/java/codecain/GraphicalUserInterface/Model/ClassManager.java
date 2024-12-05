package codecain.GraphicalUserInterface.Model;

import codecain.BackendCode.Model.UMLClass;
import codecain.BackendCode.Model.UMLClassInfo;
import codecain.GraphicalUserInterface.Model.RelationshipLines.GridManager;
import codecain.GraphicalUserInterface.View.ClassNode;
import codecain.GraphicalUserInterface.View.GridVisualizer;
import codecain.GraphicalUserInterface.View.PositionUtils;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import codecain.BackendCode.Model.UMLClassInfo;

/**
 * The {@code ClassManager} class provides utility methods for managing UML class nodes
 * in the UML editor. It includes functionality for adding, removing, and renaming classes,
 * as well as displaying alerts for invalid actions.
 */
public class ClassManager {

    /**
     * Adds a new class to the UML diagram.
     *
     * @param className    The name of the class to add.
     * @param nodeContainer The container where the new class node will be added.
     * @return The newly created {@code ClassNode}, or {@code null} if the class could not be added.
     */
    public static ClassNode addClass(String className, Pane nodeContainer) {
        if (className == null || className.trim().isEmpty()) {
            showAlert(AlertType.ERROR, "Error", "Invalid Input", "Class name cannot be empty.");
            return null;
        }
        if (UMLClass.exists(className)) {
            showAlert(AlertType.ERROR, "Error", "Duplicate Class", "Class '" + className + "' already exists.");
            return null;
        }
        UMLClass.addClass(className);
        ClassNode classNode = new ClassNode(UMLClass.getClassInfo(className));
        UMLClassInfo classInfo = UMLClass.getClassInfo(className);

        // Position nodes (e.g., center as a placeholder; adjust as needed)
        double centerX = (nodeContainer.getWidth() - classNode.getPrefWidth()) / 2;
        double centerY = (nodeContainer.getHeight() - classNode.getPrefHeight()) / 2;
        classNode.setLayoutX(centerX);
        classNode.setLayoutY(centerY);
        PositionUtils.calculateAndSetPosition(classNode, classInfo, nodeContainer);

        PositionUtils.calculateAndSetPosition(classNode, classInfo, nodeContainer);

        // Add the new ClassNode to the node container
        nodeContainer.getChildren().add(classNode);

        GridManager.addClassListeners(classNode);

        return classNode;
    }

    /**
     * Removes a class from the UML diagram.
     *
     * @param className    The name of the class to remove.
     * @param nodeContainer The container where the class node is located.
     */
    public static void removeClass(String className, Pane nodeContainer) {
        if (className == null || className.trim().isEmpty()) {
            showAlert(AlertType.ERROR, "Error", "Invalid class name", "Deletion canceled.");
            return;
        }

        if (!UMLClass.exists(className)) {
            showAlert(AlertType.ERROR, "Error", "Class '" + className + "' does not exist.", "Deletion canceled.");
            return;
        }

        UMLClass.removeClass(className);
        nodeContainer.getChildren()
                .removeIf(node -> node instanceof ClassNode && ((ClassNode) node).getName().equals(className));
    }

    /**
     * Renames an existing class in the UML diagram.
     *
     * @param oldName      The current name of the class.
     * @param newName      The new name for the class.
     * @param nodeContainer The container where the class node is located.
     */
    public static void renameClass(String oldName, String newName, Pane nodeContainer) {
        if (oldName == null || oldName.trim().isEmpty() || newName == null || newName.trim().isEmpty()) {
            showAlert(AlertType.ERROR, "Error", "Invalid class name", "Renaming canceled.");
            return;
        }

        if (!UMLClass.exists(oldName)) {
            showAlert(AlertType.ERROR, "Error", "Class '" + oldName + "' does not exist.", "Renaming canceled.");
            return;
        }

        if (UMLClass.exists(newName)) {
            showAlert(AlertType.ERROR, "Error", "Duplicate Class", "Class '" + newName + "' already exists.");
            return;
        }

        UMLClass.renameClass(oldName, newName);
        nodeContainer.getChildren().stream()
                .filter(node -> node instanceof ClassNode && ((ClassNode) node).getName().equals(oldName))
                .findFirst()
                .ifPresent(node -> ((ClassNode) node).setName(newName));
    }

    /**
     * Displays an alert dialog with the specified properties.
     *
     * @param type    The type of alert (e.g., ERROR, WARNING, INFORMATION).
     * @param title   The title of the alert dialog.
     * @param header  The header text of the alert dialog.
     * @param content The content text of the alert dialog.
     */
    private static void showAlert(AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
