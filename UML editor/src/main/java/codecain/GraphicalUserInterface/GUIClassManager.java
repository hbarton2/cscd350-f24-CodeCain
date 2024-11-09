package codecain.GraphicalUserInterface;

import codecain.BackendCode.UMLClassInfo;
import codecain.BackendCode.UMLClass;

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
            UMLClassInfo newClass = new UMLClassInfo(className);
            UMLClass.classMap.put(className, newClass);

            ClassBox classBox = new ClassBox(className, canvas);
            classPanels.put(className, classBox);
            canvas.add(classBox);
            canvas.repaint();

            JOptionPane.showMessageDialog(null, "Class '" + className + "' added.");
        } else {
            JOptionPane.showMessageDialog(null, "Invalid class name. Class not added.");
        }
    }

    public void deleteClass() {
        String className = JOptionPane.showInputDialog("Enter the name of the class to delete:");
        if (className != null && !className.trim().isEmpty() && UMLClass.classMap.containsKey(className)) {
            UMLClass.classMap.remove(className);

            JPanel classBox = classPanels.remove(className);
            if (classBox != null) {
                canvas.remove(classBox);
                canvas.revalidate();
                canvas.repaint();
            }

            JOptionPane.showMessageDialog(null, "Class '" + className + "' deleted.");
        } else {
            JOptionPane.showMessageDialog(null, "Class not found or invalid name. Deletion canceled.");
        }
    }

    public void renameClass() {
        String oldClassName = JOptionPane.showInputDialog("Enter the name of the class to rename:");
        if (oldClassName != null && UMLClass.classMap.containsKey(oldClassName)) {
            String newClassName = JOptionPane.showInputDialog("Enter the new name for the class:");
            if (newClassName != null && !newClassName.trim().isEmpty()) {
                UMLClassInfo classInfo = UMLClass.classMap.remove(oldClassName);
                classInfo.setClassName(newClassName);
                UMLClass.classMap.put(newClassName, classInfo);

                JPanel classPanel = classPanels.remove(oldClassName);
                if (classPanel != null) {
                    classPanels.put(newClassName, classPanel);
                    JLabel classLabel = (JLabel) classPanel.getComponent(0);
                    classLabel.setText(newClassName);
                }

                JOptionPane.showMessageDialog(null, "Class '" + oldClassName + "' renamed to '" + newClassName + "'.");
            } else {
                JOptionPane.showMessageDialog(null, "Invalid new class name. Rename canceled.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Class not found. Rename canceled.");
        }
    }
}
