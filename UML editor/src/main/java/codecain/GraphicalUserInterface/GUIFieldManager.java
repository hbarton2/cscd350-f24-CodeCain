package codecain.GraphicalUserInterface;

import codecain.BackendCode.UMLFields;

import javax.swing.*;

/**
 * The {@code GUIFieldManager} class handles the management of fields within UML classes.
 * It provides methods to add, delete, and rename fields, updating both the backend and the
 * graphical representation of the class diagram.
 */
public class GUIFieldManager {

    /** The manager responsible for handling operations related to classes. */
    private final GUIClassManager classManager;

    /**
     * Constructs a {@code GUIFieldManager} with the specified {@code GUIClassManager}.
     *
     * @param classManager the manager responsible for class operations.
     */
    public GUIFieldManager(GUIClassManager classManager) {
        this.classManager = classManager;
    }

    /**
     * Adds a field to a specified class.
     * Prompts the user for the class name, field name, and field type. Updates both the backend
     * and the graphical representation if the operation succeeds. Displays a confirmation or
     * error message based on the outcome.
     */
    public void addField() {
        String className = JOptionPane.showInputDialog("Enter the class name to add a field:");
        String fieldName = JOptionPane.showInputDialog("Enter the field name:");
        String fieldType = JOptionPane.showInputDialog("Enter the field type:");

        if (className == null || fieldName == null || fieldType == null) {
            JOptionPane.showMessageDialog(null, "Operation canceled.");
            return;
        }

        UMLFields fieldManager = new UMLFields();
        fieldManager.addField(className, fieldType, fieldName);

        ClassBox classBox = (ClassBox) classManager.getClassPanels().get(className);
        if (classBox != null) {
            classBox.updateDetails();
        }

        JOptionPane.showMessageDialog(null, "Field '" + fieldName + "' added to class '" + className + "'.");
    }

    /**
     * Deletes a field from a specified class.
     * Prompts the user for the class name and field name to delete. Updates both the backend
     * and the graphical representation if the operation succeeds. Displays a confirmation or
     * error message based on the outcome.
     */
    public void deleteField() {
        String className = JOptionPane.showInputDialog("Enter the class name to delete a field from:");
        String fieldName = JOptionPane.showInputDialog("Enter the field name:");

        if (className == null || fieldName == null) {
            JOptionPane.showMessageDialog(null, "Operation canceled.");
            return;
        }

        UMLFields fieldManager = new UMLFields();
        fieldManager.removeField(className, fieldName);

        ClassBox classBox = (ClassBox) classManager.getClassPanels().get(className);
        if (classBox != null) {
            classBox.updateDetails();
        }

        JOptionPane.showMessageDialog(null, "Field '" + fieldName + "' deleted from class '" + className + "'.");
    }

    /**
     * Renames a field in a specified class.
     * Prompts the user for the class name, old field name, new field name, and new field type.
     * Updates both the backend and the graphical representation if the operation succeeds.
     * Displays a confirmation or error message based on the outcome.
     */
    public void renameField() {
        String className = JOptionPane.showInputDialog("Enter the class name to rename a field:");
        String oldFieldName = JOptionPane.showInputDialog("Enter the current field name:");
        String newFieldName = JOptionPane.showInputDialog("Enter the new field name:");
        String newFieldType = JOptionPane.showInputDialog("Enter the new field type:");

        if (className == null || oldFieldName == null || newFieldName == null || newFieldType == null) {
            JOptionPane.showMessageDialog(null, "Operation canceled.");
            return;
        }

        UMLFields fieldManager = new UMLFields();
        fieldManager.renameField(className, oldFieldName, newFieldType, newFieldName);

        ClassBox classBox = (ClassBox) classManager.getClassPanels().get(className);
        if (classBox != null) {
            classBox.updateDetails();
        }

        JOptionPane.showMessageDialog(null, "Field '" + oldFieldName + "' renamed to '" + newFieldName + "' in class '" + className + "'.");
    }
}

