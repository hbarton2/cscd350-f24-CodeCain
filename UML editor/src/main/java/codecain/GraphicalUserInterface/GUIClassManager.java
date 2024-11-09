package codecain.GraphicalUserInterface;

import codecain.BackendCode.UMLClass;
import codecain.BackendCode.UMLClassInfo;

import javax.swing.*;
import java.util.HashMap;

public class GUIClassManager {
    private final HashMap<String, JPanel> classPanels = new HashMap<>();
    private final JPanel canvas;

    public GUIClassManager(JPanel canvas) {
        this.canvas = canvas;
    }

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

    public void deleteClass() {
        String className = JOptionPane.showInputDialog("Enter the name of the class to delete:");
        if (className != null && !className.trim().isEmpty()) {
            if (UMLClass.exists(className)) {
                UMLClass.removeClass(className); // Use UMLClass method to remove the class

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
}
