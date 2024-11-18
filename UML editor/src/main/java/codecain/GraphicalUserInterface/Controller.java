package codecain.GraphicalUserInterface;

import codecain.BackendCode.SaveManager;
import codecain.BackendCode.UMLClass;
import codecain.BackendCode.UMLClassInfo;
import codecain.BackendCode.UMLFieldInfo;
import codecain.BackendCode.UMLFields;
import codecain.BackendCode.UMLMethodInfo;
import codecain.BackendCode.UMLMethods;
import codecain.BackendCode.UMLParameterInfo;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Pair;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Controller {

    @FXML
    private ClassNode currentlySelectedNode = null;

    @FXML
    private AnchorPane classContainer;

    @FXML
    private Pane nodeContainer;

    @FXML
    private void addClassBtn() {
        String className = showTextInputDialog("Add Class", "Enter the name of the class to add:", "Class Name:");
        ClassNode classNode = ClassManager.addClass(className, nodeContainer);
        if (classNode != null) {
            classNode.setOnMouseClicked(event -> selectClassNode(classNode));
        }
    }
    
    

    @FXML
    private void deleteClassBtn() {
        String className = currentlySelectedNode != null
                ? currentlySelectedNode.getName()
                : showTextInputDialog("Delete Class", "Enter the name of the class to delete:", "Class Name:");

        ClassManager.removeClass(className, nodeContainer);
        
    }

    @FXML
    private void renameClassBtn() {
        String oldClassName = currentlySelectedNode != null
                ? currentlySelectedNode.getName()
                : showTextInputDialog("Rename Class", "Enter the name of the class to rename:", "Class Name:");

        String newClassName = showTextInputDialog("Rename Class", "Enter the new name for the class:",
                "New Class Name:");

        ClassManager.renameClass(oldClassName, newClassName, nodeContainer);
    }

    @FXML
    private void addFieldBtn() {
        FieldManager.addField(nodeContainer);
    }

    @FXML
    private void deleteFieldBtn() {
        FieldManager.deleteField(nodeContainer);
    }

    @FXML
    private void renameFieldBtn() {
        FieldManager.renameField(nodeContainer);
    }

    @FXML
    private void addMethodBtn() {
        MethodManager.addMethod(nodeContainer);
    }

    @FXML
    private void deleteMethodBtn() {
        MethodManager.deleteMethod(nodeContainer);
    }

    @FXML
    private void renameMethodBtn() {
       MethodManager.renameMethod(nodeContainer);
    }

    @FXML
    private void addParameterBtn() {
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
                showAlert(Alert.AlertType.ERROR, "Error", "All fields are required", "Parameter not added.");
                return;
            }

            UMLClassInfo classInfo = UMLClass.getClassInfo(className);
            if (classInfo == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Class not found",
                        "Class '" + className + "' does not exist.");
                return;
            }

            UMLMethodInfo methodInfo = classInfo.getMethodByName(methodName);
            if (methodInfo == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Method not found",
                        "Method '" + methodName + "' does not exist in class '" + className + "'.");
                return;
            }

            // Check if the parameter already exists
            UMLParameterInfo newParameter = new UMLParameterInfo(parameterType, parameterName);
            if (methodInfo.getParameters().contains(newParameter)) {
                showAlert(Alert.AlertType.WARNING, "Duplicate Parameter", "Parameter already exists",
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
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Parameter Added",
                                "Parameter '" + parameterName + "' added to method '" + methodName + "' in class '"
                                        + className + "'.");
                        break;
                    }
                }
            }
        }
    }

    @FXML
    private void deleteParameterBtn() {
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
                showAlert(Alert.AlertType.ERROR, "Error", "All fields are required", "Parameter not deleted.");
                return;
            }

            UMLClassInfo classInfo = UMLClass.getClassInfo(className);
            if (classInfo == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Class not found",
                        "Class '" + className + "' does not exist.");
                return;
            }

            UMLMethodInfo methodInfo = classInfo.getMethodByName(methodName);
            if (methodInfo == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Method not found",
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
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Parameter Deleted",
                                "Parameter '" + parameterName + "' deleted from method '" + methodName + "' in class '"
                                        + className + "'.");
                        return;
                    }
                }
            }

            showAlert(Alert.AlertType.ERROR, "Error", "GUI Update Failed",
                    "Parameter was deleted from the backend but the GUI could not be updated.");
        }
    }

    @FXML
    private void changeParameterBtn() {
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
                showAlert(Alert.AlertType.ERROR, "Error", "All fields are required", "Parameter not changed.");
                return;
            }

            UMLClassInfo classInfo = UMLClass.getClassInfo(className);
            if (classInfo == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Class not found",
                        "Class '" + className + "' does not exist.");
                return;
            }

            UMLMethodInfo methodInfo = classInfo.getMethodByName(methodName);
            if (methodInfo == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Method not found",
                        "Method '" + methodName + "' does not exist in class '" + className + "'.");
                return;
            }

            UMLMethods methodManager = new UMLMethods();
            methodManager.changeSingleParameter(className, methodName, oldParameterName, newParameterType,
                    newParameterName);

            // Update the UI
            for (Node node : nodeContainer.getChildren()) {
                if (node instanceof ClassNode) {
                    ClassNode classNode = (ClassNode) node;
                    if (classNode.getName().equals(className)) {
                        classNode.updateMethod(methodInfo); // Update the specific method in the UI
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Parameter Changed",
                                "Parameter '" + oldParameterName + "' updated to '" + newParameterName + "' with type '"
                                        + newParameterType +
                                        "' in method '" + methodName + "' of class '" + className + "'.");
                        return;
                    }
                }
            }

            showAlert(Alert.AlertType.ERROR, "Error", "GUI Update Failed", "Failed to update the method in the GUI.");
        }
    }

    @FXML
    private void changeAllParametersBtn() {
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
                showAlert(AlertType.ERROR, "Error", "All fields are required", "Parameters not changed.");
                return;
            }

            UMLClassInfo classInfo = UMLClass.getClassInfo(className);
            if (classInfo == null) {
                showAlert(AlertType.ERROR, "Error", "Class not found", "Class '" + className + "' does not exist.");
                return;
            }

            UMLMethodInfo methodInfo = classInfo.getMethodByName(methodName);
            if (methodInfo == null) {
                showAlert(AlertType.ERROR, "Error", "Method not found",
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
                        showAlert(AlertType.INFORMATION, "Success", "Parameters Changed",
                                "All parameters replaced for method '" + methodName + "' in class '" + className
                                        + "'.");
                        break;
                    }
                }
            }
        }
    }

    @FXML
    private void saveBtn() throws IOException {

        // Synchronize GUI with the backend model
        nodeContainer.getChildren().forEach(node -> {
            if (node instanceof ClassNode) {
                ((ClassNode) node).syncWithUMLClassInfo();
            }
        });

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save UML Diagram File");

        // Set filter for JSON files
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));

        // Set the initial directory to the current working directory
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));

        Window window = nodeContainer.getScene().getWindow();

        File file = fileChooser.showSaveDialog(window);
        if (file != null) {
            SaveManager.saveToJSON(file.getAbsolutePath());
        }
    }

    @FXML
    private void loadBtn() throws IOException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open UML Diagram File");

        // Set filter for JSON files
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));

        // Set the initial directory to the current working directory
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));

        // Get the main window for the file chooser
        Window window = nodeContainer.getScene().getWindow();

        File file = fileChooser.showOpenDialog(window);
        if (file != null) {
            SaveManager.loadFromJSON(file.getAbsolutePath());
            populateGUIFromClassMap();
        }
    }


    /**
     * Populates the GUI with `ClassNode` instances based on the current state of the UML class map.
     * 
     * This method clears the `nodeContainer` and iterates over all `UMLClassInfo` instances
     * in the `UMLClass.classMap`. If a class has default coordinates (0, 0), it calculates
     * and assigns a new non-overlapping position using {@link #calculateAndSetPosition(ClassNode, UMLClassInfo, Pane)}.
     * Otherwise, it uses the existing coordinates stored in the `UMLClassInfo`.
     * Each `ClassNode` is also set up with a click event for selection and added to the container.
     */
    public void populateGUIFromClassMap() {
        nodeContainer.getChildren().clear();
        UMLClass.classMap.values().forEach(classInfo -> {
            ClassNode classNode = new ClassNode(classInfo);
            if (classInfo.getX() == 0 && classInfo.getY() == 0) {
                calculateAndSetPosition(classNode, classInfo, nodeContainer);
            } else {
                classNode.setLayoutX(classInfo.getX());
                classNode.setLayoutY(classInfo.getY());
            }
            classNode.setOnMouseClicked(event -> selectClassNode(classNode));
            nodeContainer.getChildren().add(classNode);
        });

        System.out.println("GUI populated from class map.");
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

    private List<UMLParameterInfo> parseParameters(String parametersInput) {
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

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private String showTextInputDialog(String title, String header, String content) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);

        // Return the user's input, or null if canceled
        return dialog.showAndWait().orElse(null);
    }

}
