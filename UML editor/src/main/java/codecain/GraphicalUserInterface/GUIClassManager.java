package codecain.GraphicalUserInterface;

import codecain.BackendCode.UMLClass;
import codecain.BackendCode.UMLClassInfo;

import javax.swing.*;
import java.util.HashMap;

/**
 * The {@code GUIClassManager} class is responsible for managing the graphical representation
 * and backend synchronization of classes in the Class Diagram Editor.
 * It handles adding, deleting, and renaming class representations on the canvas.
 */
public class GUIClassManager extends JPanel {

    /** A map linking class names to their corresponding {@code JPanel} representations. */
    private final HashMap<String, JPanel> classPanels = new HashMap<>();

    /** The canvas panel where class representations are displayed. */
    private final JPanel canvas;

    /**
     * Constructs a {@code GUIClassManager} to manage class panels on the specified canvas.
     *
     * @param canvas the canvas panel where class boxes will be displayed.
     */
    public GUIClassManager(JPanel canvas) {
        this.canvas = canvas;
    }

    /**
     * Prompts the user to add a new class.
     * The class is added both to the backend and visually represented on the canvas as a {@code ClassBox}.
     * Displays an error dialog if the operation fails.
     */
    public void addClass() {
        String className = JOptionPane.showInputDialog("Enter the name of the class to add:");
        if (className != null && !className.trim().isEmpty()) {
            UMLClass.addClass(className);

            if (UMLClass.exists(className)) {
                ClassBox classBox = new ClassBox(className, canvas);
                classPanels.put(className, classBox);
                canvas.add(classBox);
                canvas.repaint();
                JOptionPane.showMessageDialog(null, "Class '" + className + "' added.");
            } else {
                JOptionPane.showMessageDialog(null, "Class '" + className + "' could not be added.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Invalid class name. Class not added.");
        }
    }

    /**
     * Prompts the user to delete an existing class.
     * Removes the class from both the backend and the canvas.
     * Displays an error dialog if the class does not exist or the operation is canceled.
     */
    public void deleteClass() {
        String className = JOptionPane.showInputDialog("Enter the name of the class to delete:");
        if (className != null && !className.trim().isEmpty()) {
            if (UMLClass.exists(className)) {
                UMLClass.removeClass(className);

                JPanel classBox = classPanels.remove(className);
                if (classBox != null) {
                    canvas.remove(classBox);
                    canvas.revalidate();
                    canvas.repaint();
                }

                JOptionPane.showMessageDialog(null, "Class '" + className + "' deleted.");
            } else {
                JOptionPane.showMessageDialog(null, "Class '" + className + "' does not exist. Deletion canceled.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Invalid class name. Deletion canceled.");
        }
    }

    /**
     * Prompts the user to rename an existing class.
     * Updates the backend and the visual representation on the canvas with the new class name.
     * Displays an error dialog if the class does not exist or the operation fails.
     */
    public void renameClass() {
        String oldClassName = JOptionPane.showInputDialog("Enter the name of the class to rename:");
        if (oldClassName != null && UMLClass.exists(oldClassName)) {
            String newClassName = JOptionPane.showInputDialog("Enter the new name for the class:");
            if (newClassName != null && !newClassName.trim().isEmpty()) {
                UMLClass.renameClass(oldClassName, newClassName);

                if (UMLClass.exists(newClassName)) {
                    JPanel classPanel = classPanels.remove(oldClassName);
                    if (classPanel != null) {
                        classPanels.put(newClassName, classPanel);
                        JLabel classLabel = (JLabel) classPanel.getComponent(0);
                        classLabel.setText(newClassName);
                    }

                    JOptionPane.showMessageDialog(null, "Class '" + oldClassName + "' renamed to '" + newClassName + "'.");
                } else {
                    JOptionPane.showMessageDialog(null, "Rename operation failed.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invalid new class name. Rename canceled.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Class '" + oldClassName + "' not found. Rename canceled.");
        }
    }

    /**
     * Returns a map of class names to their corresponding {@code JPanel} representations.
     *
     * @return the map of class panels.
     */
    public HashMap<String, JPanel> getClassPanels() {
        return classPanels;
    }


    /**
     * Creates a {@code ClassBox} for the given {@code UMLClassInfo} and adds it to the canvas.
     * Updates the classPanels map with the new class box.
     *
     * @param umlClassInfo the UMLClassInfo object containing details about the class.
     */
    void createClassBox(UMLClassInfo umlClassInfo) {
        ClassBox classBox = new ClassBox(umlClassInfo.getClassName(), canvas);
        classPanels.put(umlClassInfo.getClassName(), classBox);
        classBox.updateDetails();
    }

    /**
     * Clears all class boxes from the canvas and resets the {@code classPanels} map.
     * This method is typically used before loading a new diagram to ensure the canvas is clean.
     */
    public void clearCanvas() {
        canvas.removeAll();
        classPanels.clear();
    }
}

