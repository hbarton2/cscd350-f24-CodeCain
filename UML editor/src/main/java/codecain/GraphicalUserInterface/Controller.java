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
        String filePath = "new_uml_diagram.json"; // Set your file path
        SaveManager.loadFromJSON(filePath);

        // After loading, add the ClassNodes to the AnchorPane
        nodeContainer.getChildren().clear();
        UMLClass.classMap.values().forEach(classInfo -> {
            ClassNode classNode = new ClassNode(classInfo);
            classNode.setLayoutX(classInfo.getX());
            classNode.setLayoutY(classInfo.getY());

            // Add click event for selection
            classNode.setOnMouseClicked(event -> selectClassNode(classNode));

            // Add the new ClassNode to the node container
            nodeContainer.getChildren().add(classNode);
        });

        System.out.println("UML diagram loaded and nodes displayed.");
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
