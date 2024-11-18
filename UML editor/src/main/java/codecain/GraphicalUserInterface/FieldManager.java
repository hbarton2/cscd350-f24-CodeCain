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
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Add Field");
		dialog.setHeaderText("Enter the class name, field name, and field type:");

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);

		TextField classNameField = new TextField();
		classNameField.setPromptText("Class Name");
		TextField fieldNameField = new TextField();
		fieldNameField.setPromptText("Field Name");
		TextField fieldTypeField = new TextField();
		fieldTypeField.setPromptText("Field Type");

		grid.add(new Label("Class Name:"), 0, 0);
		grid.add(classNameField, 1, 0);
		grid.add(new Label("Field Name:"), 0, 1);
		grid.add(fieldNameField, 1, 1);
		grid.add(new Label("Field Type:"), 0, 2);
		grid.add(fieldTypeField, 1, 2);

		dialog.getDialogPane().setContent(grid);
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

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
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Delete Field");
		dialog.setHeaderText("Enter the class name and field name to delete:");

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);

		TextField classNameField = new TextField();
		classNameField.setPromptText("Class Name");
		TextField fieldNameField = new TextField();
		fieldNameField.setPromptText("Field Name");

		grid.add(new Label("Class Name:"), 0, 0);
		grid.add(classNameField, 1, 0);
		grid.add(new Label("Field Name:"), 0, 1);
		grid.add(fieldNameField, 1, 1);

		dialog.getDialogPane().setContent(grid);
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

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
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Rename Field");
		dialog.setHeaderText("Enter the class name, current field name, new field name, and new field type:");

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);

		TextField classNameField = new TextField();
		classNameField.setPromptText("Class Name");
		TextField oldFieldNameField = new TextField();
		oldFieldNameField.setPromptText("Current Field Name");
		TextField newFieldNameField = new TextField();
		newFieldNameField.setPromptText("New Field Name");
		TextField newFieldTypeField = new TextField();
		newFieldTypeField.setPromptText("New Field Type");

		grid.add(new Label("Class Name:"), 0, 0);
		grid.add(classNameField, 1, 0);
		grid.add(new Label("Current Field Name:"), 0, 1);
		grid.add(oldFieldNameField, 1, 1);
		grid.add(new Label("New Field Name:"), 0, 2);
		grid.add(newFieldNameField, 1, 2);
		grid.add(new Label("New Field Type:"), 0, 3);
		grid.add(newFieldTypeField, 1, 3);

		dialog.getDialogPane().setContent(grid);
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

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
}
