package codecain.GraphicalUserInterface;

import codecain.BackendCode.UMLClass;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;

public class ClassManager {
	// Add a new class
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

		// Position nodes (e.g., center as a placeholder; adjust as needed)
		double centerX = (nodeContainer.getWidth() - classNode.getPrefWidth()) / 2;
		double centerY = (nodeContainer.getHeight() - classNode.getPrefHeight()) / 2;
		classNode.setLayoutX(centerX);
		classNode.setLayoutY(centerY);

		// Add the new ClassNode to the node container
		nodeContainer.getChildren().add(classNode);

		return classNode;
	}

	// Remove a class
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

	// Rename a class
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

	// Helper to show alerts
	private static void showAlert(AlertType type, String title, String header, String content) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
}
