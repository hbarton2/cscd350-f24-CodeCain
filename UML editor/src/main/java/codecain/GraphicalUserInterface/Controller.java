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

import java.io.IOException;
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

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Class");
        dialog.setHeaderText("Enter the name of the class to add:");
        dialog.setContentText("Class Name:");

        String className = dialog.showAndWait().orElse(null);
        if(className == null || className.trim().isEmpty()) {
            showAlert(AlertType.ERROR,"Error", "Invalid class name", "Class not added.");
        } else if(UMLClass.exists(className)) {
            showAlert(AlertType.ERROR,"Error", "Class '" + className + "' already exists.", "");
        } else {
                UMLClass.addClass(className);
                ClassNode classNode = new ClassNode(UMLClass.getClassInfo(className));

                // Position nodes (e.g., center as a placeholder; adjust as needed)
                double centerX = (nodeContainer.getWidth() - classNode.getPrefWidth()) / 2;
                double centerY = (nodeContainer.getHeight() - classNode.getPrefHeight()) / 2;
                classNode.setLayoutX(centerX);
                classNode.setLayoutY(centerY);

                // Add click event for selection
                classNode.setOnMouseClicked(event -> selectClassNode(classNode));

                // Add the new ClassNode to the node container
                nodeContainer.getChildren().add(classNode);
        }
    }

    @FXML
    private void deleteClassBtn() {
        if(currentlySelectedNode == null) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Delete Class");
            dialog.setHeaderText("Enter the name of the class to delete:");
            dialog.setContentText("Class Name:");

            String className = dialog.showAndWait().orElse(null);
            if(className == null || className.trim().isEmpty()) {
                showAlert(AlertType.ERROR,"Error", "Invalid class name", "Deletion canceled.");
            } else if(!UMLClass.exists(className)) {
                showAlert(AlertType.ERROR,"Error", "Class '" + className + "' does not exist.", "Deletion canceled.");
            } else {
                UMLClass.removeClass(className);
                for(Node classNode : nodeContainer.getChildren()) {
                    if(classNode instanceof ClassNode) {
                        if(((ClassNode) classNode).getName().equals(className)) {
                            nodeContainer.getChildren().remove(classNode);
                            break;
                        }
                    }
                }

            }

        } else {
            for(Node classNode : nodeContainer.getChildren()) {
                if(classNode instanceof ClassNode) {
                    if(((ClassNode) classNode).isSelected()) {
                        nodeContainer.getChildren().remove(classNode);
                        UMLClass.removeClass(((ClassNode) classNode).getName());
                        System.out.println("Storage size: " + UMLClass.classMap.size());
                        break;
                    }
                }
            }
        }
    }

    @FXML
    public void renameClassBtn() {
        if(currentlySelectedNode == null) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Rename Class");
            dialog.setHeaderText("Enter the name of the class to rename:");
            dialog.setContentText("Class Name:");

            String oldClassName = dialog.showAndWait().orElse(null);
            if(oldClassName == null || oldClassName.trim().isEmpty()) {
                showAlert(AlertType.ERROR,"Error", "Invalid class name", "Rename canceled.");
            } else if(!UMLClass.exists(oldClassName)) {
                showAlert(AlertType.ERROR,"Error", "Class '" + oldClassName + "' does not exist.", "Rename canceled.");
            } else {
                dialog = new TextInputDialog();
                dialog.setTitle("Rename Class");
                dialog.setHeaderText("Enter the new name for the class:");
                dialog.setContentText("New Class Name:");

                String newClassName = dialog.showAndWait().orElse(null);
                if(newClassName == null || newClassName.trim().isEmpty()) {
                    showAlert(AlertType.ERROR,"Error", "Invalid class name", "Rename canceled.");
                } else if(UMLClass.exists(newClassName)) {
                    showAlert(AlertType.ERROR,"Error", "Class '" + newClassName + "' already exists.", "Rename canceled.");
                } else {
                    UMLClass.renameClass(oldClassName, newClassName);
                    for(Node classNode : nodeContainer.getChildren()) {
                        if(classNode instanceof ClassNode) {
                            if(((ClassNode) classNode).getName().equals(oldClassName)) {
                                ((ClassNode) classNode).setName(newClassName);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    @FXML
    private void addFieldBtn() {
        // Create a custom dialog to get class name, field name, and field type
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Field");
        dialog.setHeaderText("Enter the class name, field name, and field type:");

        // Create input fields
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

        // Show dialog and get user input
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String className = classNameField.getText().trim();
            String fieldName = fieldNameField.getText().trim();
            String fieldType = fieldTypeField.getText().trim();

            if (className.isEmpty() || fieldName.isEmpty() || fieldType.isEmpty()) {
                showAlert( AlertType.ERROR,"Error", "All fields are required", "Field not added.");
                return;
            }

            UMLClassInfo classInfo = UMLClass.getClassInfo(className);
            if (classInfo == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Class not found", "Class '" + className + "' does not exist.");
                return;
            }

            // Check if field already exists in the class
            if (classInfo.getFields().stream()
                .anyMatch(f -> f.getFieldName().equals(fieldName) && f.getFieldType().equals(fieldType))) {
                showAlert(Alert.AlertType.WARNING, "Duplicate Field", "Field already exists",
                "A field with name '" + fieldName + "' and type '" + fieldType + "' already exists in the class.");
                return;
            }

            // Create an instance of UMLFields and attempt to add the field
            UMLFields fieldManager = new UMLFields();
            fieldManager.addField(className, fieldType, fieldName);

            // Find the ClassNode and update its fields
            for (Node node : nodeContainer.getChildren()) {
                if (node instanceof ClassNode) {
                    ClassNode classNode = (ClassNode) node;
                    if (classNode.getName().equals(className)) {
                        UMLFieldInfo newField = new UMLFieldInfo(fieldType, fieldName);
                        classNode.addField(newField); // Add field to the UI component
                        break;
                    }
                }
            }
        }
    }


    @FXML
    private void deleteFieldBtn() {
        for (Node classNode : nodeContainer.getChildren()) {
            if (classNode instanceof ClassNode && ((ClassNode) classNode).isSelected()) {
                ClassNode selectedClassNode = (ClassNode) classNode;

                // Prompt the user to enter the field name to delete
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Delete Field");
                dialog.setHeaderText("Enter the name of the field to delete:");
                dialog.setContentText("Field Name:");

                Optional<String> result = dialog.showAndWait();
                result.ifPresent(fieldName -> {
                    boolean removed = selectedClassNode.removeField(fieldName);

                    if (removed) {
                        selectedClassNode.syncWithUMLClassInfo(); // Sync changes to the backend
                        showAlert(Alert.AlertType.INFORMATION, "Field Deleted", "Success",
                                "Field '" + fieldName + "' was successfully deleted.");
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "Field Not Found",
                                "Field '" + fieldName + "' does not exist in the selected class.");
                    }
                });
                return;
            }
        }

        // If no ClassNode is selected, show an error
        showAlert(Alert.AlertType.ERROR, "Error", "No Class Selected", "Please select a class to delete a field from.");
    }


    @FXML
    private void renameFieldBtn() {
        for (Node classNode : nodeContainer.getChildren()) {
            if (classNode instanceof ClassNode && ((ClassNode) classNode).isSelected()) {
                ClassNode selectedClassNode = (ClassNode) classNode;

                // Prompt for current field name
                TextInputDialog currentFieldDialog = new TextInputDialog();
                currentFieldDialog.setTitle("Rename Field");
                currentFieldDialog.setHeaderText("Enter the current name of the field:");
                currentFieldDialog.setContentText("Current Field Name:");

                Optional<String> currentFieldNameResult = currentFieldDialog.showAndWait();
                currentFieldNameResult.ifPresent(oldFieldName -> {

                    // Prompt for new field name and type
                    Dialog<ButtonType> dialog = new Dialog<>();
                    dialog.setTitle("Rename Field");
                    dialog.setHeaderText("Enter the new name and type for the field:");

                    GridPane grid = new GridPane();
                    grid.setHgap(10);
                    grid.setVgap(10);

                    TextField newFieldNameField = new TextField();
                    newFieldNameField.setPromptText("New Field Name");
                    TextField newFieldTypeField = new TextField();
                    newFieldTypeField.setPromptText("New Field Type");

                    grid.add(new Label("New Field Name:"), 0, 0);
                    grid.add(newFieldNameField, 1, 0);
                    grid.add(new Label("New Field Type:"), 0, 1);
                    grid.add(newFieldTypeField, 1, 1);

                    dialog.getDialogPane().setContent(grid);
                    dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                    Optional<ButtonType> dialogResult = dialog.showAndWait();
                    if (dialogResult.isPresent() && dialogResult.get() == ButtonType.OK) {
                        String newFieldName = newFieldNameField.getText().trim();
                        String newFieldType = newFieldTypeField.getText().trim();

                        if (newFieldName.isEmpty() || newFieldType.isEmpty()) {
                            showAlert(Alert.AlertType.ERROR, "Error", "Invalid Input", "Both field name and type are required.");
                            return;
                        }

                        boolean renamed = selectedClassNode.renameField(oldFieldName, newFieldType, newFieldName);

                        if (renamed) {
                            selectedClassNode.syncWithUMLClassInfo(); // Sync changes with backend
                            showAlert(Alert.AlertType.INFORMATION, "Field Renamed", "Success",
                                    "The field '" + oldFieldName + "' was renamed to '" + newFieldName + "' with type '" + newFieldType + "'.");
                        } else {
                            showAlert(Alert.AlertType.ERROR, "Error", "Rename Failed",
                                    "The field '" + oldFieldName + "' could not be renamed.");
                        }
                    }
                });
                return;
            }
        }

        // If no ClassNode is selected, show an error
        showAlert(Alert.AlertType.ERROR, "Error", "No Class Selected", "Please select a class to rename a field.");
    }




    @FXML
    private void addMethodBtn() {
        // Dialog to gather class name, method name, and parameters
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Method");
        dialog.setHeaderText("Enter details for the new method");

        // Set up the input fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField classNameField = new TextField();
        classNameField.setPromptText("Class Name");
        TextField methodNameField = new TextField();
        methodNameField.setPromptText("Method Name");
        TextField parameterField = new TextField();
        parameterField.setPromptText("Parameters (comma-separated, e.g., int x, String y)");
        parameterField.setPrefWidth(300);

        grid.add(new Label("Class Name:"), 0, 0);
        grid.add(classNameField, 1, 0);
        grid.add(new Label("Method Name:"), 0, 1);
        grid.add(methodNameField, 1, 1);
        grid.add(new Label("Parameters:"), 0, 2);
        grid.add(parameterField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Show dialog and get user input
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String className = classNameField.getText().trim();
            String methodName = methodNameField.getText().trim();
            String parametersInput = parameterField.getText().trim();

            if (className.isEmpty() || methodName.isEmpty()) {
                showAlert(AlertType.ERROR, "Error", "All fields are required", "Method not added.");
                return;
            }

            UMLClassInfo classInfo = UMLClass.getClassInfo(className);
            if (classInfo == null) {
                showAlert(AlertType.ERROR, "Error", "Class not found", "Class '" + className + "' does not exist.");
                return;
            }

            // Parse parameters
            List<UMLParameterInfo> parameters = parseParameters(parametersInput);

            // Check if method already exists
            UMLMethods methodManager = new UMLMethods();
            if (classInfo.getMethods().stream().anyMatch(m -> m.getMethodName().equals(methodName) && m.getParameters().equals(parameters))) {
                showAlert(AlertType.WARNING, "Duplicate Method", "Method already exists",
                    "A method with name '" + methodName + "' and the same parameters already exists in the class.");
                return;
            }

            // Add method to the class
            methodManager.addMethod(className, methodName, parameters);

            // Find the ClassNode and update its methods
            for (Node node : nodeContainer.getChildren()) {
                if (node instanceof ClassNode) {
                    ClassNode classNode = (ClassNode) node;
                    if (classNode.getName().equals(className)) {
                        UMLMethodInfo newMethod = new UMLMethodInfo(methodName, parameters);
                        classNode.addMethod(newMethod); // Add method to the UI component
                        showAlert(AlertType.INFORMATION, "Success", "Method Added",
                                "Method '" + methodName + "' added to class '" + className + "'.");
                        break;
                    }
                }
            }
        }
    }

    @FXML
    private void deleteMethodBtn() {
        for (Node classNode : nodeContainer.getChildren()) {
            if (classNode instanceof ClassNode && ((ClassNode) classNode).isSelected()) {
                ClassNode selectedClassNode = (ClassNode) classNode;

                // Prompt the user to enter the name of the method to delete
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Delete Method");
                dialog.setHeaderText("Enter the name of the method to delete:");
                dialog.setContentText("Method Name:");

                Optional<String> result = dialog.showAndWait();
                result.ifPresent(methodName -> {
                    String className = selectedClassNode.getName();
                    UMLMethods methodManager = new UMLMethods();

                    // Remove the method in the backend
                    methodManager.removeMethod(className, methodName);

                    // Remove the method from the GUI
                    boolean removed = selectedClassNode.removeMethod(methodName);
                    if (removed) {
                        selectedClassNode.syncWithUMLClassInfo(); // Sync changes to the backend
                        showAlert(Alert.AlertType.INFORMATION, "Method Deleted", "Success",
                                "Method '" + methodName + "' was successfully deleted from class '" + className + "'.");
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "Method Not Found",
                                "Method '" + methodName + "' does not exist in class '" + className + "'.");
                    }
                });
                return;
            }
        }

        // If no ClassNode is selected, show an error alert
        showAlert(Alert.AlertType.ERROR, "Error", "No Class Selected", "Please select a class to delete a method from.");
    }


    @FXML
    private void renameMethodBtn() {
        for (Node classNode : nodeContainer.getChildren()) {
            if (classNode instanceof ClassNode && ((ClassNode) classNode).isSelected()) {
                ClassNode selectedClassNode = (ClassNode) classNode;

                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setTitle("Rename Method");
                dialog.setHeaderText("Enter the method name to rename:");

                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);

                TextField oldMethodNameField = new TextField();
                oldMethodNameField.setPromptText("Current Method Name");

                TextField newMethodNameField = new TextField();
                newMethodNameField.setPromptText("New Method Name");

                grid.add(new Label("Current Method Name:"), 0, 0);
                grid.add(oldMethodNameField, 1, 0);
                grid.add(new Label("New Method Name:"), 0, 1);
                grid.add(newMethodNameField, 1, 1);

                dialog.getDialogPane().setContent(grid);
                dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                Optional<ButtonType> result = dialog.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    String oldMethodName = oldMethodNameField.getText().trim();
                    String newMethodName = newMethodNameField.getText().trim();

                    if (oldMethodName.isEmpty() || newMethodName.isEmpty()) {
                        showAlert(Alert.AlertType.ERROR, "Error", "Invalid Input", "Both method names must be provided.");
                        return;
                    }

                    String className = selectedClassNode.getName();

// Update backend
                    UMLMethods methodsManager = new UMLMethods();
                    methodsManager.renameMethod(className, oldMethodName, newMethodName);

// Update GUI
                    boolean renamed = selectedClassNode.renameMethod(oldMethodName, newMethodName);
                    if (renamed) {
                        // Sync the GUI with backend data
                        selectedClassNode.syncWithUMLClassInfo();
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Method Renamed",
                                "Method '" + oldMethodName + "' renamed to '" + newMethodName + "'.");
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "Rename Failed",
                                "Method '" + oldMethodName + "' could not be renamed in the GUI.");
                    }

                }
                return;
            }
        }

        showAlert(Alert.AlertType.ERROR, "Error", "No Class Selected", "Please select a class to rename a method.");
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
                showAlert(Alert.AlertType.ERROR, "Error", "Class not found", "Class '" + className + "' does not exist.");
                return;
            }

            UMLMethodInfo methodInfo = classInfo.getMethodByName(methodName);
            if (methodInfo == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Method not found", "Method '" + methodName + "' does not exist in class '" + className + "'.");
                return;
            }

            // Check if the parameter already exists
            UMLParameterInfo newParameter = new UMLParameterInfo(parameterType, parameterName);
            if (methodInfo.getParameters().contains(newParameter)) {
                showAlert(Alert.AlertType.WARNING, "Duplicate Parameter", "Parameter already exists",
                        "A parameter with type '" + parameterType + "' and name '" + parameterName + "' already exists in the method.");
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
                                "Parameter '" + parameterName + "' added to method '" + methodName + "' in class '" + className + "'.");
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
                showAlert(Alert.AlertType.ERROR, "Error", "Class not found", "Class '" + className + "' does not exist.");
                return;
            }

            UMLMethodInfo methodInfo = classInfo.getMethodByName(methodName);
            if (methodInfo == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Method not found", "Method '" + methodName + "' does not exist in class '" + className + "'.");
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
                                "Parameter '" + parameterName + "' deleted from method '" + methodName + "' in class '" + className + "'.");
                        return;
                    }
                }
            }

            // If the class was not found in the GUI
            showAlert(Alert.AlertType.ERROR, "Error", "GUI Update Failed", "Parameter was deleted from the backend but the GUI could not be updated.");
        }
    }

    @FXML
    private void changeParameterBtn() {
        // Dialog to gather class name, method name, and parameter details
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Change Parameter");
        dialog.setHeaderText("Enter details for the parameter to change");

        // Set up the input fields and grid layout
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
                showAlert(Alert.AlertType.ERROR, "Error", "Class not found", "Class '" + className + "' does not exist.");
                return;
            }

            UMLMethodInfo methodInfo = classInfo.getMethodByName(methodName);
            if (methodInfo == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Method not found", "Method '" + methodName + "' does not exist in class '" + className + "'.");
                return;
            }

            UMLMethods methodManager = new UMLMethods();
            methodManager.changeSingleParameter(className, methodName, oldParameterName, newParameterType, newParameterName);

            // Update the UI
            for (Node node : nodeContainer.getChildren()) {
                if (node instanceof ClassNode) {
                    ClassNode classNode = (ClassNode) node;
                    if (classNode.getName().equals(className)) {
                        classNode.updateMethod(methodInfo); // Update the specific method in the UI
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Parameter Changed",
                                "Parameter '" + oldParameterName + "' updated to '" + newParameterName + "' with type '" + newParameterType +
                                        "' in method '" + methodName + "' of class '" + className + "'.");
                        return;
                    }
                }
            }

            showAlert(Alert.AlertType.ERROR, "Error", "GUI Update Failed", "Failed to update the method in the GUI.");
        }
    }





    @FXML
    private void saveBtn() throws IOException {
        nodeContainer.getChildren().forEach(node -> {
           if (node instanceof ClassNode) {
               ((ClassNode) node).syncWithUMLClassInfo();
           }
        });

        SaveManager.saveToJSON("new_uml_diagram.json");
    }

    @FXML
    private void loadBtn() throws IOException {
        SaveManager.loadFromJSON("new_uml_diagram.json");
        populateGUIFromClassMap();
    }

    public void populateGUIFromClassMap() {
        nodeContainer.getChildren().clear();
        UMLClass.classMap.values().forEach(classInfo -> {
            ClassNode classNode = new ClassNode(classInfo);
            classNode.setLayoutX(classInfo.getX());
            classNode.setLayoutY(classInfo.getY());
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

}
