package codecain.GraphicalUserInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import codecain.BackendCode.UMLClass;
import codecain.BackendCode.UMLClassInfo;
import codecain.BackendCode.UMLMethodInfo;
import codecain.BackendCode.UMLMethods;
import codecain.BackendCode.UMLParameterInfo;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class ParameterManager {
	public static void addParameter(Pane nodeContainer) {
		// Dialog to gather class name, method name, parameter type, and parameter name
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Add Parameter");
		dialog.setHeaderText("Enter details for the new parameter");

		// Set up the input fields and grid layout
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);

		TextField classNameField = new TextField();
		classNameField.setPromptText("Class Name");
		TextField methodNameField = new TextField();
		methodNameField.setPromptText("Method Name");
		TextField parameterTypeField = new TextField();
		parameterTypeField.setPromptText("Parameter Type");
		TextField parameterNameField = new TextField();
		parameterNameField.setPromptText("Parameter Name");

		grid.add(new Label("Class Name:"), 0, 0);
		grid.add(classNameField, 1, 0);
		grid.add(new Label("Method Name:"), 0, 1);
		grid.add(methodNameField, 1, 1);
		grid.add(new Label("Parameter Type:"), 0, 2);
		grid.add(parameterTypeField, 1, 2);
		grid.add(new Label("Parameter Name:"), 0, 3);
		grid.add(parameterNameField, 1, 3);

		dialog.getDialogPane().setContent(grid);
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

		dialog.getDialogPane().setPrefWidth(400); // Set preferred width for the dialog pane

		// Show dialog and get user input
		Optional<ButtonType> result = dialog.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			String className = classNameField.getText().trim();
			String methodName = methodNameField.getText().trim();
			String parameterType = parameterTypeField.getText().trim();
			String parameterName = parameterNameField.getText().trim();

			if (className.isEmpty() || methodName.isEmpty() || parameterType.isEmpty() || parameterName.isEmpty()) {
				AlertHelper.showAlert(Alert.AlertType.ERROR, "Error", "All fields are required", "Parameter not added.");
				return;
			}

			UMLClassInfo classInfo = UMLClass.getClassInfo(className);
			if (classInfo == null) {
				AlertHelper.showAlert(Alert.AlertType.ERROR, "Error", "Class not found",
						"Class '" + className + "' does not exist.");
				return;
			}

			UMLMethodInfo methodInfo = classInfo.getMethodByName(methodName);
			if (methodInfo == null) {
				AlertHelper.showAlert(Alert.AlertType.ERROR, "Error", "Method not found",
						"Method '" + methodName + "' does not exist in class '" + className + "'.");
				return;
			}

			// Check if the parameter already exists
			UMLParameterInfo newParameter = new UMLParameterInfo(parameterType, parameterName);
			if (methodInfo.getParameters().contains(newParameter)) {
				AlertHelper.showAlert(Alert.AlertType.WARNING, "Duplicate Parameter", "Parameter already exists",
						"A parameter with type '" + parameterType + "' and name '" + parameterName
								+ "' already exists in the method.");
				return;
			}

			// Add parameter to the method
			UMLMethods methodManager = new UMLMethods();
			methodManager.addParameter(className, methodName, parameterType, parameterName);

			// Update the UI
			for (Node node : nodeContainer.getChildren()) {
				if (node instanceof ClassNode) {
					ClassNode classNode = (ClassNode) node;
					if (classNode.getName().equals(className)) {
						classNode.updateMethod(methodInfo); // Update the specific method in the UI
						AlertHelper.showAlert(Alert.AlertType.INFORMATION, "Success", "Parameter Added",
								"Parameter '" + parameterName + "' added to method '" + methodName + "' in class '"
										+ className + "'.");
						break;
					}
				}
			}
		}
	}

	public static void deleteParameter(Pane nodeContainer) {
		// Dialog to gather class name, method name, and parameter name
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Delete Parameter");
		dialog.setHeaderText("Enter details for the parameter to delete");

		// Set up the input fields and grid layout
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);

		TextField classNameField = new TextField();
		classNameField.setPromptText("Class Name");
		TextField methodNameField = new TextField();
		methodNameField.setPromptText("Method Name");
		TextField parameterNameField = new TextField();
		parameterNameField.setPromptText("Parameter Name");

		grid.add(new Label("Class Name:"), 0, 0);
		grid.add(classNameField, 1, 0);
		grid.add(new Label("Method Name:"), 0, 1);
		grid.add(methodNameField, 1, 1);
		grid.add(new Label("Parameter Name:"), 0, 2);
		grid.add(parameterNameField, 1, 2);

		dialog.getDialogPane().setContent(grid);
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

		// Show dialog and get user input
		Optional<ButtonType> result = dialog.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			String className = classNameField.getText().trim();
			String methodName = methodNameField.getText().trim();
			String parameterName = parameterNameField.getText().trim();

			if (className.isEmpty() || methodName.isEmpty() || parameterName.isEmpty()) {
				AlertHelper.showAlert(Alert.AlertType.ERROR, "Error", "All fields are required", "Parameter not deleted.");
				return;
			}

			UMLClassInfo classInfo = UMLClass.getClassInfo(className);
			if (classInfo == null) {
				AlertHelper.showAlert(Alert.AlertType.ERROR, "Error", "Class not found",
						"Class '" + className + "' does not exist.");
				return;
			}

			UMLMethodInfo methodInfo = classInfo.getMethodByName(methodName);
			if (methodInfo == null) {
				AlertHelper.showAlert(Alert.AlertType.ERROR, "Error", "Method not found",
						"Method '" + methodName + "' does not exist in class '" + className + "'.");
				return;
			}

			// Remove the parameter from the backend
			UMLMethods methodManager = new UMLMethods();
			methodManager.removeParameter(className, methodName, parameterName);

			// Update the GUI
			for (Node node : nodeContainer.getChildren()) {
				if (node instanceof ClassNode) {
					ClassNode classNode = (ClassNode) node;
					if (classNode.getName().equals(className)) {
						classNode.updateMethod(methodInfo); // Update the method in the GUI
						AlertHelper.showAlert(Alert.AlertType.INFORMATION, "Success", "Parameter Deleted",
								"Parameter '" + parameterName + "' deleted from method '" + methodName + "' in class '"
										+ className + "'.");
						return;
					}
				}
			}

			AlertHelper.showAlert(Alert.AlertType.ERROR, "Error", "GUI Update Failed",
					"Parameter was deleted from the backend but the GUI could not be updated.");
		}
	}

	public static void changeParameter(Pane nodeContainer) {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Change Parameter");
		dialog.setHeaderText("Enter details for the parameter to change");

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);

		TextField classNameField = new TextField();
		classNameField.setPromptText("Class Name");
		TextField methodNameField = new TextField();
		methodNameField.setPromptText("Method Name");
		TextField oldParameterNameField = new TextField();
		oldParameterNameField.setPromptText("Old Parameter Name");
		TextField newParameterTypeField = new TextField();
		newParameterTypeField.setPromptText("New Parameter Type");
		TextField newParameterNameField = new TextField();
		newParameterNameField.setPromptText("New Parameter Name");

		grid.add(new Label("Class Name:"), 0, 0);
		grid.add(classNameField, 1, 0);
		grid.add(new Label("Method Name:"), 0, 1);
		grid.add(methodNameField, 1, 1);
		grid.add(new Label("Old Parameter Name:"), 0, 2);
		grid.add(oldParameterNameField, 1, 2);
		grid.add(new Label("New Parameter Type:"), 0, 3);
		grid.add(newParameterTypeField, 1, 3);
		grid.add(new Label("New Parameter Name:"), 0, 4);
		grid.add(newParameterNameField, 1, 4);

		dialog.getDialogPane().setContent(grid);
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

		dialog.getDialogPane().setPrefWidth(400); // Set preferred width for the dialog pane

		// Show dialog and get user input
		Optional<ButtonType> result = dialog.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			String className = classNameField.getText().trim();
			String methodName = methodNameField.getText().trim();
			String oldParameterName = oldParameterNameField.getText().trim();
			String newParameterType = newParameterTypeField.getText().trim();
			String newParameterName = newParameterNameField.getText().trim();

			if (className.isEmpty() || methodName.isEmpty() || oldParameterName.isEmpty() ||
					newParameterType.isEmpty() || newParameterName.isEmpty()) {
				AlertHelper.showAlert(Alert.AlertType.ERROR, "Error", "All fields are required", "Parameter not changed.");
				return;
			}

			UMLClassInfo classInfo = UMLClass.getClassInfo(className);
			if (classInfo == null) {
				AlertHelper.showAlert(Alert.AlertType.ERROR, "Error", "Class not found",
						"Class '" + className + "' does not exist.");
				return;
			}

			UMLMethodInfo methodInfo = classInfo.getMethodByName(methodName);
			if (methodInfo == null) {
				AlertHelper.showAlert(Alert.AlertType.ERROR, "Error", "Method not found",
						"Method '" + methodName + "' does not exist in class '" + className + "'.");
				return;
			}

			UMLMethods methodManager = new UMLMethods();
			methodManager.changeSingleParameter(className, methodName, oldParameterName, newParameterType,
					newParameterName); // TODO: Bug when input is invalid

			// Update the UI
			for (Node node : nodeContainer.getChildren()) {
				if (node instanceof ClassNode) {
					ClassNode classNode = (ClassNode) node;
					if (classNode.getName().equals(className)) {
						classNode.updateMethod(methodInfo); // Update the specific method in the UI
						AlertHelper.showAlert(Alert.AlertType.INFORMATION, "Success", "Parameter Changed",
								"Parameter '" + oldParameterName + "' updated to '" + newParameterName + "' with type '"
										+ newParameterType +
										"' in method '" + methodName + "' of class '" + className + "'.");
						return;
					}
				}
			}

			AlertHelper.showAlert(Alert.AlertType.ERROR, "Error", "GUI Update Failed",
					"Failed to update the method in the GUI.");
		}
	}

	public static void changeAllParameters(Pane nodeContainer) {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Change All Parameters");
		dialog.setHeaderText("Enter details for the method to replace its parameters");

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);

		TextField classNameField = new TextField();
		classNameField.setPromptText("Class Name");
		TextField methodNameField = new TextField();
		methodNameField.setPromptText("Method Name");
		TextField parameterField = new TextField();
		parameterField.setPromptText("New Parameters (comma-separated, e.g., int x, String y)");
		parameterField.setPrefWidth(300);

		grid.add(new Label("Class Name:"), 0, 0);
		grid.add(classNameField, 1, 0);
		grid.add(new Label("Method Name:"), 0, 1);
		grid.add(methodNameField, 1, 1);
		grid.add(new Label("New Parameters:"), 0, 2);
		grid.add(parameterField, 1, 2);

		dialog.getDialogPane().setContent(grid);
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

		Optional<ButtonType> result = dialog.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			String className = classNameField.getText().trim();
			String methodName = methodNameField.getText().trim();
			String parametersInput = parameterField.getText().trim();

			if (className.isEmpty() || methodName.isEmpty()) {
				AlertHelper.showAlert(AlertType.ERROR, "Error", "All fields are required", "Parameters not changed.");
				return;
			}

			UMLClassInfo classInfo = UMLClass.getClassInfo(className);
			if (classInfo == null) {
				AlertHelper.showAlert(AlertType.ERROR, "Error", "Class not found", "Class '" + className + "' does not exist.");
				return;
			}

			UMLMethodInfo methodInfo = classInfo.getMethodByName(methodName);
			if (methodInfo == null) {
				AlertHelper.showAlert(AlertType.ERROR, "Error", "Method not found",
						"Method '" + methodName + "' does not exist in class '" + className + "'.");
				return;
			}

			List<UMLParameterInfo> newParameters = parseParameters(parametersInput);

			UMLMethods methodManager = new UMLMethods();
			methodManager.changeAllParameters(className, methodName, newParameters);

			for (Node node : nodeContainer.getChildren()) {
				if (node instanceof ClassNode) {
					ClassNode classNode = (ClassNode) node;
					if (classNode.getName().equals(className)) {
						methodInfo.getParameters().clear();
						methodInfo.getParameters().addAll(newParameters);
						classNode.updateMethod(methodInfo); // Update the specific method in the UI
						AlertHelper.showAlert(AlertType.INFORMATION, "Success", "Parameters Changed",
								"All parameters replaced for method '" + methodName + "' in class '" + className
										+ "'.");
						break;
					}
				}
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
