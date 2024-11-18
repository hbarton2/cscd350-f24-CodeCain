package codecain.GraphicalUserInterface;

import java.util.Optional;

import codecain.BackendCode.UMLClass;
import codecain.BackendCode.UMLClassInfo;
import codecain.BackendCode.UMLFieldInfo;
import codecain.BackendCode.UMLFields;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class FieldManager {
	public static void addField(Pane nodeContainer) {
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);

		TextField classNameField = addTextField(grid, "Class Name", 0);
		TextField fieldNameField = addTextField(grid, "Field Name", 1);
		TextField fieldTypeField = addTextField(grid, "Field Type", 2);

		Dialog<ButtonType> dialog = createDialog("Add Field", "Enter the class name, field name, and field type:", grid);

		Optional<ButtonType> result = dialog.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			String className = classNameField.getText().trim();
			String fieldName = fieldNameField.getText().trim();
			String fieldType = fieldTypeField.getText().trim();

			if (className.isEmpty() || fieldName.isEmpty() || fieldType.isEmpty()) {
				AlertHelper.showAlert(Alert.AlertType.ERROR, "Error", "All fields are required", "Field not added.");
				return;
			}

			UMLClassInfo classInfo = UMLClass.getClassInfo(className);
			if (classInfo == null) {
				AlertHelper.showAlert(Alert.AlertType.ERROR, "Error", "Class not found",
						"Class '" + className + "' does not exist.");
				return;
			}

			if (classInfo.getFields().stream()
					.anyMatch(f -> f.getFieldName().equals(fieldName) && f.getFieldType().equals(fieldType))) {
				AlertHelper.showAlert(Alert.AlertType.WARNING, "Duplicate Field", "Field already exists",
						"A field with name '" + fieldName + "' and type '" + fieldType
								+ "' already exists in the class.");
				return;
			}

			UMLFields fieldManager = new UMLFields();
			fieldManager.addField(className, fieldType, fieldName);

			for (Node node : nodeContainer.getChildren()) {
				if (node instanceof ClassNode) {
					ClassNode classNode = (ClassNode) node;
					if (classNode.getName().equals(className)) {
						UMLFieldInfo newField = new UMLFieldInfo(fieldType, fieldName);
						classNode.addField(newField);
						break;
					}
				}
			}
		}
	}

	public static void deleteField(Pane nodeContainer) {
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);

		TextField classNameField = addTextField(grid, "Class Name", 0);
		TextField fieldNameField = addTextField(grid, "Field Name", 1);

		Dialog<ButtonType> dialog = createDialog("Delete Field", "Enter the class name and field name to delete:", grid);

		Optional<ButtonType> result = dialog.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			String className = classNameField.getText().trim();
			String fieldName = fieldNameField.getText().trim();
			if (className.isEmpty() || fieldName.isEmpty()) {
				AlertHelper.showAlert(Alert.AlertType.ERROR, "Error", "All fields are required", "Field not deleted.");
				return;
			}
			ClassNode targetClassNode = null;
			for (Node classNode : nodeContainer.getChildren()) {
				if (classNode instanceof ClassNode && ((ClassNode) classNode).getName().equals(className)) {
					targetClassNode = (ClassNode) classNode;
					break;
				}
			}
			if (targetClassNode == null) {
				AlertHelper.showAlert(Alert.AlertType.ERROR, "Error", "Class Not Found",
						"Class '" + className + "' does not exist.");
				return;
			}
			boolean removed = targetClassNode.removeField(fieldName);
			if (removed) {
				targetClassNode.syncWithUMLClassInfo();
				AlertHelper.showAlert(Alert.AlertType.INFORMATION, "Field Deleted", "Success",
						"Field '" + fieldName + "' was successfully deleted.");
			} else {
				AlertHelper.showAlert(Alert.AlertType.ERROR, "Error", "Field Not Found",
						"Field '" + fieldName + "' does not exist in the class '" + className + "'.");
			}
		}
	}

	public static void renameField(Pane nodeContainer) {
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);

		TextField classNameField = addTextField(grid, "Class Name", 0);
		TextField oldFieldNameField = addTextField(grid, "Current Field Name", 1);
		TextField newFieldNameField = addTextField(grid, "New Field Name", 2);
		TextField newFieldTypeField = addTextField(grid, "New Field Type", 3);

		Dialog<ButtonType> dialog = createDialog("Rename Field",
				"Enter the class name, current field name, new field name, and new field type:", grid);

		Optional<ButtonType> result = dialog.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			String className = classNameField.getText().trim();
			String oldFieldName = oldFieldNameField.getText().trim();
			String newFieldName = newFieldNameField.getText().trim();
			String newFieldType = newFieldTypeField.getText().trim();

			if (className.isEmpty() || oldFieldName.isEmpty() || newFieldName.isEmpty() || newFieldType.isEmpty()) {
				AlertHelper.showAlert(Alert.AlertType.ERROR, "Error", "Invalid Input", "All fields are required.");
				return;
			}

			// Find the target class node
			ClassNode targetClassNode = null;
			for (Node classNode : nodeContainer.getChildren()) {
				if (classNode instanceof ClassNode && ((ClassNode) classNode).getName().equals(className)) {
					targetClassNode = (ClassNode) classNode;
					break;
				}
			}

			if (targetClassNode == null) {
				AlertHelper.showAlert(Alert.AlertType.ERROR, "Error", "Class Not Found",
						"Class '" + className + "' does not exist.");
				return;
			}

			// Rename the field
			boolean renamed = targetClassNode.renameField(oldFieldName, newFieldType, newFieldName);
			if (renamed) {
				targetClassNode.syncWithUMLClassInfo(); // Sync changes with backend
				AlertHelper.showAlert(Alert.AlertType.INFORMATION, "Field Renamed", "Success",
						"The field '" + oldFieldName + "' was renamed to '" + newFieldName + "' with type '"
								+ newFieldType + "'.");
			} else {
				AlertHelper.showAlert(Alert.AlertType.ERROR, "Error", "Rename Failed",
						"The field '" + oldFieldName + "' could not be renamed. Ensure it exists in the class.");
			}
		}
	}

	// Reusable method to create a dialog with a grid
	private static Dialog<ButtonType> createDialog(String title, String headerText, GridPane grid) {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle(title);
		dialog.setHeaderText(headerText);
		dialog.getDialogPane().setContent(grid);
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		return dialog;
	}

	// Reusable method to create a labeled TextField and add it to the grid
	private static TextField addTextField(GridPane grid, String label, int row) {
		TextField textField = new TextField();
		textField.setPromptText(label);
		grid.add(new Label(label + ":"), 0, row);
		grid.add(textField, 1, row);
		return textField;
	}
}
