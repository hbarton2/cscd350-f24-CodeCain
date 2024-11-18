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
       ParameterManager.addParameter(nodeContainer);
    }

    @FXML
    private void deleteParameterBtn() {
        ParameterManager.deleteParameter(nodeContainer);
    }

    @FXML
    private void changeParameterBtn() {
        ParameterManager.changeParameter(nodeContainer);
    }

    @FXML
    private void changeAllParametersBtn() {
        ParameterManager.changeAllParameters(nodeContainer);
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

    private String showTextInputDialog(String title, String header, String content) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);

        // Return the user's input, or null if canceled
        return dialog.showAndWait().orElse(null);
    }

}
