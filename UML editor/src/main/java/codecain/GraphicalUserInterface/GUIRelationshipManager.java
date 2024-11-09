package codecain.GraphicalUserInterface;

import codecain.BackendCode.Relationship;
import codecain.BackendCode.UMLClass;

import javax.swing.*;

public class GUIRelationshipManager {

    /**
     * Adds a relationship between two classes.
     */
    public void addRelationship() {
        String sourceClass = JOptionPane.showInputDialog("Enter the source class name:");
        String destinationClass = JOptionPane.showInputDialog("Enter the destination class name:");

        if (sourceClass == null || destinationClass == null || sourceClass.trim().isEmpty() || destinationClass.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Both class names are required.");
            return;
        }

        if (!UMLClass.classMap.containsKey(sourceClass)) {
            JOptionPane.showMessageDialog(null, "Source class '" + sourceClass + "' does not exist.");
            return;
        }

        if (!UMLClass.classMap.containsKey(destinationClass)) {
            JOptionPane.showMessageDialog(null, "Destination class '" + destinationClass + "' does not exist.");
            return;
        }

        boolean added = Relationship.addRelationship(sourceClass, destinationClass);
        if (added) {
            JOptionPane.showMessageDialog(null, "Relationship added between '" + sourceClass + "' and '" + destinationClass + "'.");
        } else {
            JOptionPane.showMessageDialog(null, "Unable to add relationship. It may already exist.");
        }
    }

    /**
     * Removes a relationship between two classes.
     */
    public void deleteRelationship() {
        String sourceClass = JOptionPane.showInputDialog("Enter the source class name:");
        String destinationClass = JOptionPane.showInputDialog("Enter the destination class name:");

        if (sourceClass == null || destinationClass == null || sourceClass.trim().isEmpty() || destinationClass.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Both class names are required.");
            return;
        }

        boolean removed = Relationship.removeRelationship(sourceClass, destinationClass);
        if (removed) {
            JOptionPane.showMessageDialog(null, "Relationship removed between '" + sourceClass + "' and '" + destinationClass + "'.");
        } else {
            JOptionPane.showMessageDialog(null, "Relationship not found or could not be removed.");
        }
    }
}
