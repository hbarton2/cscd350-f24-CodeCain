package codecain.GraphicalUserInterface.Controller;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import codecain.BackendCode.Model.Relationship;
import codecain.BackendCode.Model.SaveManager;
import codecain.BackendCode.Model.UMLClass;
import codecain.GraphicalUserInterface.Model.ArrowManager;
import codecain.GraphicalUserInterface.Model.ClassManager;
import codecain.GraphicalUserInterface.Model.FieldManager;
import codecain.GraphicalUserInterface.Model.MethodManager;
import codecain.GraphicalUserInterface.Model.ParameterManager;
import codecain.GraphicalUserInterface.Model.RelationshipManager;
import codecain.GraphicalUserInterface.View.ClassNode;
import codecain.GraphicalUserInterface.View.PositionUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Window;

/**
 * Controller class for managing user interactions with the UML editor GUI.
 * This class handles user actions, such as adding, deleting, renaming classes,
 * fields, methods, and parameters,
 * as well as saving and loading UML diagrams.
 */
public class Controller {


    @FXML
    private ScrollPane scrollPane;

    /**
     * The currently selected ClassNode in the GUI.
     * This is used to track which class node is being interacted with.
     */
    @FXML
    private ClassNode currentlySelectedNode = null;

    /**
     * The container for the class nodes in the GUI.
     * This is the main layout area where the UML classes are displayed.
     */
    @FXML
    private AnchorPane classContainer;

    /**
     * The container for the graphical nodes representing UML elements.
     * This pane is used to add and manage visual representations of UML classes,
     * fields, and relationships.
     */
    @FXML
    private AnchorPane nodeContainer;

    /**
     * The manager for graphical arrows representing relationships between UML classes in the GUI.
     * This is used to add, update, and remove arrows when relationships are created, modified, or deleted.
     * It ensures that the graphical representation of relationships stays in sync with the data model.
     */
    @FXML
    private ArrowManager arrowManager;


    /**
     * Initializes the controller after the FXML file has been loaded.
     * <p>
     * Sets up event handlers for the GUI, including:
     * - Handling clicks on the {@code nodeContainer} to deselect the currently
     * selected {@code ClassNode}.
     * - Ensuring proper interactions between the UI and the backend.
     * </p>
     */
    @FXML
    public void initialize() {
        // Handle clicks on empty space in nodeContainer to deselect the selected node
        nodeContainer.setOnMouseClicked(event -> {
            // Check if the click was directly on the nodeContainer (empty space) and a node
            // is currently selected
            if (event.getTarget() == nodeContainer && currentlySelectedNode != null) {
                currentlySelectedNode.deselect(); // Deselect the currently selected node
                currentlySelectedNode = null; // Reset the currently selected node
            }
        });

        arrowManager = new ArrowManager(nodeContainer);
        RelationshipManager.setArrowManager(arrowManager);
        RelationshipManager.setController(this);

    }

    /**
     * Handles the action of adding a new class to the diagram.
     */
    @FXML
    private void addClassBtn() {
        String className = showTextInputDialog("Add Class", "Enter the name of the class to add:", "Class Name:");
        ClassNode classNode = ClassManager.addClass(className, nodeContainer);
        if (classNode != null) {
            classNode.setOnMouseClicked(event -> selectClassNode(classNode));
        }
    }
    
    

    /**
     * Handles the action of deleting the selected class or a specified class from
     * the diagram.
     */
    @FXML
    private void deleteClassBtn() {
        String className = currentlySelectedNode != null
                ? currentlySelectedNode.getName()
                : showTextInputDialog("Delete Class", "Enter the name of the class to delete:", "Class Name:");

        ClassManager.removeClass(className, nodeContainer);
    }

    /**
     * Handles the action of renaming the selected class or a specified class.
     */
    @FXML
    private void renameClassBtn() {
        String oldClassName = currentlySelectedNode != null
                ? currentlySelectedNode.getName()
                : showTextInputDialog("Rename Class", "Enter the name of the class to rename:", "Class Name:");

        String newClassName = showTextInputDialog("Rename Class", "Enter the new name for the class:",
                "New Class Name:");

        ClassManager.renameClass(oldClassName, newClassName, nodeContainer);
    }

    /**
     * Delegates the action of adding a new field to the FieldManager.
     */
    @FXML
    private void addFieldBtn() {
        FieldManager.addField(nodeContainer);
    }

    /**
     * Delegates the action of deleting a field to the FieldManager.
     */
    @FXML
    private void deleteFieldBtn() {
        FieldManager.deleteField(nodeContainer);
    }

    /**
     * Delegates the action of renaming a field to the FieldManager.
     */
    @FXML
    private void renameFieldBtn() {
        FieldManager.renameField(nodeContainer);
    }

    /**
     * Delegates the action of adding a method to the MethodManager.
     */
    @FXML
    private void addMethodBtn() {
        MethodManager.addMethod(nodeContainer);
    }

    /**
     * Delegates the action of deleting a method to the MethodManager.
     */
    @FXML
    private void deleteMethodBtn() {
        MethodManager.deleteMethod(nodeContainer);
    }

    /**
     * Delegates the action of renaming a method to the MethodManager.
     */
    @FXML
    private void renameMethodBtn() {
        MethodManager.renameMethod(nodeContainer);
    }

    /**
     * Delegates the action of adding a parameter to the ParameterManager.
     */
    @FXML
    private void addParameterBtn() {
        ParameterManager.addParameter(nodeContainer);
    }

    /**
     * Delegates the action of deleting a parameter to the ParameterManager.
     */
    @FXML
    private void deleteParameterBtn() {
        ParameterManager.deleteParameter(nodeContainer);
    }

    /**
     * Delegates the action of changing a parameter to the ParameterManager.
     */
    @FXML
    private void changeParameterBtn() {
        ParameterManager.changeParameter(nodeContainer);
    }

    /**
     * Delegates the action of changing all parameters to the ParameterManager.
     */
    @FXML
    private void changeAllParametersBtn() {
        ParameterManager.changeAllParameters(nodeContainer);
    }

    /**
     * prompts user, adds a relationship from a prompted user
     */
    @FXML
    private void addRelationshipBtn(){
        RelationshipManager.addRelationshipBtn();
    }

    /**
     * prompts user, removes a relationship inputted
     */
    @FXML
    private void removeRelationshipBtn(){
        RelationshipManager.removeRelationshipBtn();
    }

    /**
     * Handles saving the current UML diagram to a JSON file.
     *
     * @throws IOException if there is an error during the save process.
     */
    @FXML
    private void saveBtn() throws IOException {
        nodeContainer.getChildren().forEach(node -> {
            if (node instanceof ClassNode) {
                ((ClassNode) node).syncWithUMLClassInfo();
            }
        });

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save UML Diagram File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));

        Window window = nodeContainer.getScene().getWindow();

        File file = fileChooser.showSaveDialog(window);
        if (file != null) {
            SaveManager.saveToJSON(file.getAbsolutePath());
        }
    }


    /**
     * Handles loading a UML diagram from a JSON file.
     *
     * @throws IOException if there is an error during the load process.
     */
    @FXML
    private void loadBtn() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open UML Diagram File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));

        Window window = nodeContainer.getScene().getWindow();

        File file = fileChooser.showOpenDialog(window);
        if (file != null) {
            SaveManager.loadFromJSON(file.getAbsolutePath());
            populateGUIFromClassMap();
        }
    }

    /**
     * Populates the GUI with data from the backend UML class map.
     * Clears the existing nodes in the GUI and repopulates them based on the
     * backend state.
     */
    public void populateGUIFromClassMap() {
        nodeContainer.getChildren().clear();
        arrowManager.clearArrows();
        UMLClass.classMap.values().forEach(classInfo -> {
            ClassNode classNode = new ClassNode(classInfo);
            if (classInfo.getX() == 0 && classInfo.getY() == 0) {
                PositionUtils.calculateAndSetPosition(classNode, classInfo, nodeContainer);
            } else {
                classNode.setLayoutX(classInfo.getX());
                classNode.setLayoutY(classInfo.getY());
            }
            classNode.setOnMouseClicked(event -> selectClassNode(classNode));
            nodeContainer.getChildren().add(classNode);
        });
    
        // Trigger layout update for the container to ensure all nodes are positioned
        nodeContainer.applyCss();
        nodeContainer.layout();
    
        Relationship.relationshipList.forEach(relationship -> {
            ClassNode sourceNode = findClassNode(relationship.getSource());
            ClassNode destNode = findClassNode(relationship.getDestination());
    
            if (sourceNode != null && destNode != null) {
                arrowManager.addArrow(relationship, sourceNode, destNode);
                arrowManager.updateArrowPosition(
                    arrowManager.getArrow(relationship),
                    sourceNode,
                    destNode,
                    relationship.getType()
                );
            }
        });
    
        System.out.println("GUI populated from class map.");
    }
    

    /**
     * Finds and returns the ClassNode corresponding to the given class name.
     * 
     * @param className the name of the class to search for.
     * @return the ClassNode with the specified class name, or null if no matching node is found.
     */
    public ClassNode findClassNode(String className) {
        for (Node node : nodeContainer.getChildren()) {
            if (node instanceof ClassNode && ((ClassNode) node).getName().equals(className)) {
                return (ClassNode) node;
            }
        }
        return null;
    }
    

    /**
     * Selects a specific class node in the GUI, updating the selection state.
     *
     * @param classNode the class node to select.
     */
    private void selectClassNode(ClassNode classNode) {
        if (currentlySelectedNode != null) {
            currentlySelectedNode.deselect();
        }

        classNode.select();
        currentlySelectedNode = classNode;
    }

    /**
     * Displays a text input dialog and returns the user's input.
     *
     * @param title   the title of the dialog.
     * @param header  the header text for the dialog.
     * @param content the content text for the dialog.
     * @return the user's input as a String, or null if canceled.
     */
    private String showTextInputDialog(String title, String header, String content) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);

        return dialog.showAndWait().orElse(null);
    }

    @FXML
    private void exitBtn() {
        // Create a confirmation alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Confirmation");
        alert.setHeaderText("Are you sure you want to exit?");
        alert.setContentText("Unsaved changes will be lost.");

        // Show the confirmation dialog and wait for the user's response
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Perform cleanup tasks here if necessary
            System.out.println("Application is exiting...");

            // Exit the application
            System.exit(0);
        } else {
            System.out.println("Exit canceled.");
        }
    }

}
