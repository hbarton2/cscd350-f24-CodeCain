package codecain.GraphicalUserInterface;

import codecain.BackendCode.UMLFields;

import javax.swing.*;

public class GUIFieldManager {

    private final GUIClassManager classManager;

    public GUIFieldManager(GUIClassManager classManager) {
        this.classManager = classManager;
    }

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
