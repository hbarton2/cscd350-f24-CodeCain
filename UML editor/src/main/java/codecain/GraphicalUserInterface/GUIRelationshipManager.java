package codecain.GraphicalUserInterface;

import codecain.BackendCode.Relationship;
import codecain.BackendCode.UMLClass;
import codecain.RelationshipType;

import javax.swing.*;

/**
 * The {@code GUIRelationshipManager} class handles the creation and removal of relationships
 * between UML classes in the Class Diagram Editor. It interacts with the backend to update
 * relationships and provides user-friendly prompts for input.
 */
public class GUIRelationshipManager {

    private GUIClassManager classManager;

    public GUIRelationshipManager(GUIClassManager classManager){
        this.classManager = classManager;
    }

    /**
     * Adds a relationship between two UML classes.
     * Prompts the user for the source and destination class names. Validates the class names and
     * ensures that both exist in the system. Updates the backend to add the relationship and
     * displays a confirmation or error message based on the outcome.
     */
    public void addRelationship() {
        String sourceClass = JOptionPane.showInputDialog("Enter the source class name:");
        String destinationClass = JOptionPane.showInputDialog("Enter the destination class name:");
        String type = JOptionPane.showInputDialog("Enter the type of relationship");

        if (!RelationshipType.typeExists(type)){
            JOptionPane.showMessageDialog(null, type + " is not a valid relationship type.");
            return;
        }

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


        boolean added = Relationship.addRelationship(sourceClass, destinationClass, RelationshipType.fromString(type));

        if (added) {
            classManager.getClassPanels().get(sourceClass).addRelationshipPoint(Relationship.getRelationship(sourceClass,destinationClass,RelationshipType.fromString(type)));
            classManager.getClassPanels().get(destinationClass).addRelationshipPoint(Relationship.getRelationship(sourceClass,destinationClass,RelationshipType.fromString(type)));
            JOptionPane.showMessageDialog(null, "Relationship added between '" + sourceClass + "' and '" + destinationClass + "'.");
        } else {
            JOptionPane.showMessageDialog(null, "Unable to add relationship. It may already exist.");
        }
    }

    /**
     * Removes a relationship between two UML classes.
     * Prompts the user for the source and destination class names. Updates the backend to remove
     * the relationship and displays a confirmation or error message based on the outcome.
     */
    public void deleteRelationship() {
        String sourceClass = JOptionPane.showInputDialog("Enter the source class name:");
        String destinationClass = JOptionPane.showInputDialog("Enter the destination class name:");

        if (sourceClass == null || destinationClass == null || sourceClass.trim().isEmpty() || destinationClass.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Both class names are required.");
            return;
        }

        classManager.getClassPanels().get(sourceClass).removeRelationshipPoint(Relationship.getRelationship(sourceClass, destinationClass, null));
        classManager.getClassPanels().get(destinationClass).removeRelationshipPoint(Relationship.getRelationship(sourceClass, destinationClass, null));

        boolean removed = Relationship.removeRelationship(sourceClass, destinationClass);
        if (removed) {
            JOptionPane.showMessageDialog(null, "Relationship removed between '" + sourceClass + "' and '" + destinationClass + "'.");
        } else {
            JOptionPane.showMessageDialog(null, "Relationship not found or could not be removed.");
        }
    }
}
