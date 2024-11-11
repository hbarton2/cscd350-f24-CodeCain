package codecain.GraphicalUserInterface;

import codecain.BackendCode.SaveManager;
import codecain.BackendCode.UMLClass;
import codecain.BackendCode.UMLClassInfo;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import codecain.GraphicalUserInterface.GUIClassManager;


/**
 * The {@code GUIController} class serves as the central controller for the Class Diagram Editor.
 * It coordinates actions between the GUI components and the backend, handling user interactions
 * such as adding, deleting, and renaming classes, fields, methods, parameters, and relationships.
 * It also facilitates saving and loading UML diagrams.
 */
public class GUIController {

    /** Manages operations related to classes in the UML diagram. */
    private final GUIClassManager classManager;

    /** Manages operations related to fields within classes. */
    private final GUIFieldManager fieldManager;

    /** Manages operations related to methods within classes. */
    private final GUIMethodManager methodManager;

    /** Manages operations related to relationships between classes. */
    private final GUIRelationshipManager relationshipManager;

    /**
     * Constructs a {@code GUIController} with the specified managers.
     *
     * @param classManager       the manager responsible for class operations.
     * @param fieldManager       the manager responsible for field operations.
     * @param methodManager      the manager responsible for method operations.
     * @param relationshipManager the manager responsible for relationship operations.
     */
    public GUIController(GUIClassManager classManager, GUIFieldManager fieldManager,
                         GUIMethodManager methodManager, GUIRelationshipManager relationshipManager) {
        this.classManager = classManager;
        this.fieldManager = fieldManager;
        this.methodManager = methodManager;
        this.relationshipManager = relationshipManager;
    }

    /**
     * Handles the addition of a new class to the UML diagram.
     */
    public void handleAddClass() {
        classManager.addClass();
    }

    /**
     * Handles the deletion of a class from the UML diagram.
     */
    public void handleDeleteClass() {
        classManager.deleteClass();
    }

    /**
     * Handles renaming an existing class in the UML diagram.
     */
    public void handleRenameClass() {
        classManager.renameClass();
    }

    /**
     * Handles the addition of a new field to a class.
     */
    public void handleAddField() {
        fieldManager.addField();
    }

    /**
     * Handles the deletion of a field from a class.
     */
    public void handleDeleteField() {
        fieldManager.deleteField();
    }

    /**
     * Handles renaming a field within a class.
     */
    public void handleRenameField() {
        fieldManager.renameField();
    }

    /**
     * Handles the addition of a new method to a class.
     */
    public void handleAddMethod() {
        methodManager.addMethod();
    }

    /**
     * Handles the deletion of a method from a class.
     */
    public void handleDeleteMethod() {
        methodManager.deleteMethod();
    }

    /**
     * Handles renaming a method within a class.
     */
    public void handleRenameMethod() {
        methodManager.renameMethod();
    }

    /**
     * Handles the addition of a parameter to a method.
     */
    public void handleAddParameter() {
        methodManager.addParameter();
    }

    /**
     * Handles the deletion of a parameter from a method.
     */
    public void handleDeleteParameter() {
        methodManager.deleteParameter();
    }

    /**
     * Handles renaming a parameter in a method.
     */
    public void handleRenameParameter() {
        methodManager.renameParameter();
    }

    /**
     * Handles the addition of a relationship between two classes.
     */
    public void handleAddRelationship() {
        relationshipManager.addRelationship();
    }

    /**
     * Handles the deletion of a relationship between two classes.
     */
    public void handleDeleteRelationship() {
        relationshipManager.deleteRelationship();
    }

    /**
     * Saves the current UML diagram to a JSON file.
     * Prompts the user for a file name and saves the diagram using {@code SaveManager}.
     * Displays an error dialog if the save operation fails.
     */
    public void handleSave() {
        String fileName = JOptionPane.showInputDialog(null, "Enter file name to save:");
        if (fileName != null && !fileName.trim().isEmpty()) {
            try {
                SaveManager.saveToJSON(fileName + ".json");
                JOptionPane.showMessageDialog(null, "Diagram saved as " + fileName + ".json");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error saving diagram: " + ex.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Invalid file name. Diagram not saved.");
        }
    }

    /**
     * Loads a UML diagram from a JSON file.
     * Prompts the user for a file name and loads the diagram using {@code SaveManager}.
     * Displays an error dialog if the load operation fails.
     */
    public void handleLoad() {
        String fileName = JOptionPane.showInputDialog(null, "Enter file name to load:");
        if (fileName != null && !fileName.trim().isEmpty()) {
            try {
                SaveManager.loadFromJSON(fileName + ".json");
                classManager.clearCanvas();
                UMLClass.classMap.values().forEach(classInfo -> {
                    classManager.createClassBox(classInfo);
                    JPanel classBox = classManager.getClassPanels().get(classInfo.getClassName());
                    if (classBox != null) {
                        classBox.setLocation(classInfo.getX(), classInfo.getY());
                    }
                });
                classManager.revalidate();
                classManager.repaint();
                JOptionPane.showMessageDialog(null, "Diagram loaded from " + fileName + ".json");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error loading diagram: " + ex.getMessage(), "Load Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Invalid file name. Diagram not loaded.");
        }
    }
}
