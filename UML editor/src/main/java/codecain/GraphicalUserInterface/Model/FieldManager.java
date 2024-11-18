package codecain.GraphicalUserInterface.Model;

import java.util.Optional;

import codecain.BackendCode.Model.UMLClass;
import codecain.BackendCode.Model.UMLClassInfo;
import codecain.BackendCode.Model.UMLFieldInfo;
import codecain.BackendCode.Model.UMLFields;
import codecain.GraphicalUserInterface.View.AlertHelper;
import codecain.GraphicalUserInterface.View.ClassNode;
import codecain.GraphicalUserInterface.View.DialogUtils;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 * The {@code FieldManager} class provides functionality to manage UML class
 * fields
 * in the UML editor. It includes methods to add, delete, and rename fields in a
 * UML class,
 * with updates to both the GUI and the backend data model.
 */
public class FieldManager {

	/**
	 * Adds a new field to a specified UML class.
	 * <p>
	 * Displays a dialog for the user to input the class name, field name, and field
	 * type.
	 * Validates the input and updates the backend and GUI accordingly.
	 *
	 * @param nodeContainer The container holding the class nodes in the GUI.
	 */
	public static void addField(Pane nodeContainer) {
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);

		TextField classNameField = DialogUtils.addTextField(grid, "Class Name", 0);
		TextField fieldNameField = DialogUtils.addTextField(grid, "Field Name", 1);
		TextField fieldTypeField = DialogUtils.addTextField(grid, "Field Type", 2);

		Dialog<ButtonType> dialog = DialogUtils.createDialog("Add Field",
				"Enter the class name, field name, and field type:", grid);

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

	/**
	 * Deletes a field from a specified UML class.
	 * <p>
	 * Displays a dialog for the user to input the class name and field name.
	 * Validates the input and updates the backend and GUI accordingly.
	 *
	 * @param nodeContainer The container holding the class nodes in the GUI.
	 */
	public static void deleteField(Pane nodeContainer) {
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);

		TextField classNameField = DialogUtils.addTextField(grid, "Class Name", 0);
		TextField fieldNameField = DialogUtils.addTextField(grid, "Field Name", 1);

		Dialog<ButtonType> dialog = DialogUtils.createDialog("Delete Field",
				"Enter the class name and field name to delete:", grid);

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

	/**
	 * Renames a field in a specified UML class.
	 * <p>
	 * Displays a dialog for the user to input the class name, current field name,
	 * new field name, and new field type.
	 * Validates the input and updates the backend and GUI accordingly.
	 *
	 * @param nodeContainer The container holding the class nodes in the GUI.
	 */
	public static void renameField(Pane nodeContainer) {
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);

		TextField classNameField = DialogUtils.addTextField(grid, "Class Name", 0);
		TextField oldFieldNameField = DialogUtils.addTextField(grid, "Current Field Name", 1);
		TextField newFieldNameField = DialogUtils.addTextField(grid, "New Field Name", 2);
		TextField newFieldTypeField = DialogUtils.addTextField(grid, "New Field Type", 3);

		Dialog<ButtonType> dialog = DialogUtils.createDialog("Rename Field",
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
}
