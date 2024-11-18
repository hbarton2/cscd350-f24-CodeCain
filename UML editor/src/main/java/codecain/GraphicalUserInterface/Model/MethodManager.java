package codecain.GraphicalUserInterface.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import codecain.BackendCode.Model.UMLClass;
import codecain.BackendCode.Model.UMLClassInfo;
import codecain.BackendCode.Model.UMLMethodInfo;
import codecain.BackendCode.Model.UMLMethods;
import codecain.BackendCode.Model.UMLParameterInfo;
import codecain.GraphicalUserInterface.View.AlertHelper;
import codecain.GraphicalUserInterface.View.ClassNode;
import codecain.GraphicalUserInterface.View.DialogUtils;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class MethodManager {
	public static void addMethod(Pane nodeContainer) {
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);

		TextField classNameField = DialogUtils.addTextField(grid, "Class Name", 0);
		TextField methodNameField = DialogUtils.addTextField(grid, "Method Name", 1);
		TextField parameterField = DialogUtils.addTextField(grid, "Parameters (comma-separated, e.g., int x, String y)", 2);
		parameterField.setPrefWidth(300);

		Dialog<ButtonType> dialog = DialogUtils.createDialog("Add Method", "Enter details for the new method", grid);

		Optional<ButtonType> result = dialog.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			String className = classNameField.getText().trim();
			String methodName = methodNameField.getText().trim();
			String parametersInput = parameterField.getText().trim();

			if (className.isEmpty() || methodName.isEmpty()) {
				AlertHelper.showAlert(AlertType.ERROR, "Error", "All fields are required", "Method not added.");
				return;
			}

			UMLClassInfo classInfo = UMLClass.getClassInfo(className);
			if (classInfo == null) {
				AlertHelper.showAlert(AlertType.ERROR, "Error", "Class not found", "Class '" + className + "' does not exist.");
				return;
			}

			List<UMLParameterInfo> parameters = parseParameters(parametersInput);

			UMLMethods methodManager = new UMLMethods();
			if (classInfo.getMethods().stream()
					.anyMatch(m -> m.getMethodName().equals(methodName) && m.getParameters().equals(parameters))) {
				AlertHelper.showAlert(AlertType.WARNING, "Duplicate Method", "Method already exists",
						"A method with name '" + methodName + "' and the same parameters already exists in the class.");
				return;
			}

			methodManager.addMethod(className, methodName, parameters);
			for (Node node : nodeContainer.getChildren()) {
				if (node instanceof ClassNode) {
					ClassNode classNode = (ClassNode) node;
					if (classNode.getName().equals(className)) {
						UMLMethodInfo newMethod = new UMLMethodInfo(methodName, parameters);
						classNode.addMethod(newMethod); // Add method to the UI component
						AlertHelper.showAlert(AlertType.INFORMATION, "Success", "Method Added",
								"Method '" + methodName + "' added to class '" + className + "'.");
						break;
					}
				}
			}
		}
	}

	public static void deleteMethod(Pane nodeContainer) {
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);

		TextField classNameField = DialogUtils.addTextField(grid, "Class Name", 0);
		TextField methodNameField = DialogUtils.addTextField(grid, "Method Name", 1);

		Dialog<ButtonType> dialog = DialogUtils.createDialog("Delete Method",
				"Enter the class name and method name to delete:", grid);

		Optional<ButtonType> result = dialog.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			String className = classNameField.getText().trim();
			String methodName = methodNameField.getText().trim();

			if (className.isEmpty() || methodName.isEmpty()) {
				AlertHelper.showAlert(Alert.AlertType.ERROR, "Error", "Invalid Input",
						"Both class name and method name are required.");
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
			UMLMethods methodManager = new UMLMethods();
			methodManager.removeMethod(className, methodName);
			boolean removed = targetClassNode.removeMethod(methodName);
			if (removed) {
				targetClassNode.syncWithUMLClassInfo();
				AlertHelper.showAlert(Alert.AlertType.INFORMATION, "Method Deleted", "Success",
						"Method '" + methodName + "' was successfully deleted from class '" + className + "'.");
			} else {
				AlertHelper.showAlert(Alert.AlertType.ERROR, "Error", "Method Not Found",
						"Method '" + methodName + "' does not exist in class '" + className + "'.");
			}
		}
	}

	public static void renameMethod(Pane nodeContainer) {
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);

		TextField classNameField = DialogUtils.addTextField(grid, "Class Name", 0);
		TextField oldMethodNameField = DialogUtils.addTextField(grid, "Current Method Name", 1);
		TextField newMethodNameField = DialogUtils.addTextField(grid, "New Method Name", 2);

		Dialog<ButtonType> dialog = DialogUtils.createDialog("Rename Method",
				"Enter the class name, current method name, and new method name:", grid);

		Optional<ButtonType> result = dialog.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			String className = classNameField.getText().trim();
			String oldMethodName = oldMethodNameField.getText().trim();
			String newMethodName = newMethodNameField.getText().trim();

			if (className.isEmpty() || oldMethodName.isEmpty() || newMethodName.isEmpty()) {
				AlertHelper.showAlert(Alert.AlertType.ERROR, "Error", "Invalid Input", "All fields are required.");
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

			UMLMethods methodsManager = new UMLMethods();
			methodsManager.renameMethod(className, oldMethodName, newMethodName);

			boolean renamed = targetClassNode.renameMethod(oldMethodName, newMethodName);
			if (renamed) {
				targetClassNode.syncWithUMLClassInfo();
				AlertHelper.showAlert(Alert.AlertType.INFORMATION, "Success", "Method Renamed",
						"Method '" + oldMethodName + "' renamed to '" + newMethodName + "'.");
			} else {
				AlertHelper.showAlert(Alert.AlertType.ERROR, "Error", "Rename Failed",
						"Method '" + oldMethodName + "' could not be renamed in the GUI.");
			}
		}
	}

	private static List<UMLParameterInfo> parseParameters(String parametersInput) {
		List<UMLParameterInfo> parameters = new ArrayList<>();
		if (parametersInput.isEmpty()) {
			return parameters; // No parameters to add
		}

		// Split parameters by comma and process each
		String[] paramParts = parametersInput.split(",");
		for (String part : paramParts) {
			String[] typeAndName = part.trim().split(" ");
			if (typeAndName.length == 2) {
				String type = typeAndName[0].trim();
				String name = typeAndName[1].trim();
				parameters.add(new UMLParameterInfo(type, name));
			} else {
				System.out.println("Invalid parameter format: " + part);
			}
		}
		return parameters;
	}
}
