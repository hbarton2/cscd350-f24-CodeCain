package codecain.GraphicalUserInterface;

import codecain.BackendCode.SaveManager;

import javax.swing.*;
import java.io.IOException;

public class GUIController {
    private final GUIClassManager classManager;
    private final GUIFieldManager fieldManager;
    private final GUIMethodManager methodManager;
    private final GUIRelationshipManager relationshipManager;

    public GUIController(GUIClassManager classManager, GUIFieldManager fieldManager, GUIMethodManager methodManager, GUIRelationshipManager relationshipManager) {
        this.classManager = classManager;
        this.fieldManager = fieldManager;
        this.methodManager = methodManager;
        this.relationshipManager = relationshipManager;
    }

    public void handleAddClass() {
        classManager.addClass();
    }

    public void handleDeleteClass() {
        classManager.deleteClass();
    }

    public void handleRenameClass() {
        classManager.renameClass();
    }

    public void handleAddField() {
        fieldManager.addField();
    }

    public void handleDeleteField() {
        fieldManager.deleteField();
    }

    public void handleRenameField() {
        fieldManager.renameField();
    }

    public void handleAddMethod() {
        methodManager.addMethod();
    }

    public void handleDeleteMethod() {
        methodManager.deleteMethod();
    }

    public void handleRenameMethod() {
        methodManager.renameMethod();
    }

    public void handleAddParameter() {
        methodManager.addMethod();
    }

    public void handleDeleteParameter() {
        methodManager.deleteMethod();
    }

    public void handleRenameParameter() {
        methodManager.renameMethod();
    }

    public void handleAddRelationship() {
        relationshipManager.addRelationship();
    }

    public void handleDeleteRelationship() {
        relationshipManager.deleteRelationship();
    }


    /**
     * Saves the current UML diagram to a JSON file.
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


    public void handleLoad() {
        String fileName = JOptionPane.showInputDialog(null, "Enter file name to load:");
        if (fileName != null && !fileName.trim().isEmpty()) {
            try {
                SaveManager.loadFromJSON(fileName + ".json");

                // Backend updated, no need to modify canvas
                JOptionPane.showMessageDialog(null, "Diagram loaded from " + fileName + ".json");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error loading diagram: " + ex.getMessage(), "Load Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Invalid file name. Diagram not loaded.");
        }
    }

}
