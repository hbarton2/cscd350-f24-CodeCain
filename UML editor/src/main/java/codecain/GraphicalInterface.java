package codecain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class GraphicalInterface extends JFrame {
    private JPanel canvas;
    private JPanel controlsPanel;
    private JPanel classesPanel;

    public GraphicalInterface() {
        setTitle("Class Diagram Editor");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        controlsPanel = new JPanel();
        controlsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton saveButton = new JButton("Save");
        JButton loadButton = new JButton("Load");

        controlsPanel.add(saveButton);
        controlsPanel.add(loadButton);

        add(controlsPanel, BorderLayout.NORTH);

        canvas = new JPanel();
        canvas.setBackground(Color.WHITE);
        add(canvas, BorderLayout.CENTER);

        classesPanel = new JPanel();
        classesPanel.setLayout(new BoxLayout(classesPanel, BoxLayout.Y_AXIS));

        JButton addClassButton = new JButton("Add Class");
        JButton deleteClassButton = new JButton("Remove Class");
        JButton renameClassButton = new JButton("Rename Class");

        classesPanel.add(addClassButton);
        classesPanel.add(deleteClassButton);
        classesPanel.add(renameClassButton);

        classesPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton addFieldButton = new JButton("Add Field");
        JButton deleteFieldButton = new JButton("Remove Field");
        JButton renameFieldButton = new JButton("Rename Field");

        classesPanel.add(addFieldButton);
        classesPanel.add(deleteFieldButton);
        classesPanel.add(renameFieldButton);

        classesPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton addRelationshipButton = new JButton("Add Relationship");
        JButton deleteRelationshipButton = new JButton("Remove Relationship");

        classesPanel.add(addRelationshipButton);
        classesPanel.add(deleteRelationshipButton);

        classesPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton addMethodButton = new JButton("Add Method");
        JButton deleteMethodButton = new JButton("Delete Method");
        JButton renameMethodButton = new JButton("Rename Method");
        JButton addParameterButton = new JButton("Add Parameter");
        JButton deleteParameterButton = new JButton("Delete Parameter");
        JButton renameParameterButton = new JButton("Rename Parameter");

        classesPanel.add(addMethodButton);
        classesPanel.add(deleteMethodButton);
        classesPanel.add(renameMethodButton);
        classesPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        classesPanel.add(addParameterButton);
        classesPanel.add(deleteParameterButton);
        classesPanel.add(renameParameterButton);

        add(classesPanel, BorderLayout.EAST);

        addClassButton.addActionListener(e -> addClass());
        deleteClassButton.addActionListener(e -> deleteClass());
        renameClassButton.addActionListener(e -> renameClass());
        addFieldButton.addActionListener(e -> addField());
        deleteFieldButton.addActionListener(e -> deleteField());
        renameFieldButton.addActionListener(e -> renameField());
        addRelationshipButton.addActionListener(e -> addRelationship());
        deleteRelationshipButton.addActionListener(e -> deleteRelationship());
        saveButton.addActionListener(e -> saveDiagram());
        loadButton.addActionListener(e -> loadDiagram());

        addMethodButton.addActionListener(e -> addMethod());
        deleteMethodButton.addActionListener(e -> deleteMethod());
        renameMethodButton.addActionListener(e -> renameMethod());
        addParameterButton.addActionListener(e -> addParameter());
        deleteParameterButton.addActionListener(e -> deleteParameter());
        renameParameterButton.addActionListener(e -> renameParameter());
    }


    private void addMethod() {
        String className = JOptionPane.showInputDialog(this, "Enter the class name to add a method:");
        String methodName = JOptionPane.showInputDialog(this, "Enter the method name:");
        String parametersInput = JOptionPane.showInputDialog(this, "Enter parameters (comma-separated):");

        List<String> parameters = parametersInput == null ? new ArrayList<>() : List.of(parametersInput.split(","));
        new UMLMethods().addMethod(className, methodName, parameters); // Directly calls UMLMethods
    }

    private void deleteMethod() {
        String className = JOptionPane.showInputDialog(this, "Enter the class name to delete a method:");
        String methodName = JOptionPane.showInputDialog(this, "Enter the method name to delete:");
        new UMLMethods().removeMethod(className, methodName);
    }

    private void renameMethod() {
        String className = JOptionPane.showInputDialog(this, "Enter the class name to rename a method:");
        String oldMethodName = JOptionPane.showInputDialog(this, "Enter the current method name:");
        String newMethodName = JOptionPane.showInputDialog(this, "Enter the new method name:");
        new UMLMethods().renameMethod(className, oldMethodName, newMethodName);
    }

    private void addParameter() {
        String className = JOptionPane.showInputDialog(this, "Enter the class name:");
        String methodName = JOptionPane.showInputDialog(this, "Enter the method name to add a parameter:");
        String parameter = JOptionPane.showInputDialog(this, "Enter the parameter to add:");
        new UMLMethods().addParameter(className, methodName, parameter);
    }

    private void deleteParameter() {
        String className = JOptionPane.showInputDialog(this, "Enter the class name:");
        String methodName = JOptionPane.showInputDialog(this, "Enter the method name to delete a parameter:");
        String parameter = JOptionPane.showInputDialog(this, "Enter the parameter to delete:");
        new UMLMethods().removeParameter(className, methodName, parameter);
    }

    private void renameParameter() {
        String className = JOptionPane.showInputDialog(this, "Enter the class name:");
        String methodName = JOptionPane.showInputDialog(this, "Enter the method name:");
        String oldParameterName = JOptionPane.showInputDialog(this, "Enter the current parameter name:");
        String newParameterName = JOptionPane.showInputDialog(this, "Enter the new parameter name:");
        new UMLMethods().changeParameter(className, methodName, oldParameterName, newParameterName);
    }

    private void addClass() {
        String className = JOptionPane.showInputDialog(this, "Enter the name of the class to add:");
        if (className != null && !className.trim().isEmpty()) {
            UMLClassInfo newClass = new UMLClassInfo(className);
            UMLClass.classMap.put(className, newClass);
            JOptionPane.showMessageDialog(this, "Class '" + className + "' added.");
        } else {
            JOptionPane.showMessageDialog(this, "Invalid class name. Class not added.");
        }
    }

    private void deleteClass() {
        String className = JOptionPane.showInputDialog(this, "Enter the name of the class to delete:");
        if (className != null && !className.trim().isEmpty() && UMLClass.classMap.containsKey(className)) {
            UMLClass.classMap.remove(className);
            Relationship.removeAttachedRelationships(className);
            JOptionPane.showMessageDialog(this, "Class '" + className + "' deleted along with its relationships.");
        } else {
            JOptionPane.showMessageDialog(this, "Class not found or invalid name. Deletion canceled.");
        }
    }

    private void renameClass() {
        String oldClassName = JOptionPane.showInputDialog(this, "Enter the name of the class to rename:");
        if (oldClassName != null && UMLClass.classMap.containsKey(oldClassName)) {
            String newClassName = JOptionPane.showInputDialog(this, "Enter the new name for the class:");
            if (newClassName != null && !newClassName.trim().isEmpty()) {
                UMLClassInfo classInfo = UMLClass.classMap.remove(oldClassName);
                classInfo.setClassName(newClassName);
                UMLClass.classMap.put(newClassName, classInfo);
                JOptionPane.showMessageDialog(this, "Class '" + oldClassName + "' renamed to '" + newClassName + "'.");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid new class name. Rename canceled.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Class not found or invalid name. Rename canceled.");
        }
    }


    private void addField() {
        String className = JOptionPane.showInputDialog(this, "Enter the class name to add a field to:");
        UMLClassInfo classInfo = UMLClass.classMap.get(className);
        if (classInfo != null) {
            String fieldName = JOptionPane.showInputDialog(this, "Enter the name of the field to add:");
            String fieldType = JOptionPane.showInputDialog(this, "Enter the type of the field:");
            if (fieldName != null && !fieldName.trim().isEmpty() && fieldType != null && !fieldType.trim().isEmpty()) {
                UMLFieldInfo newField = new UMLFieldInfo(fieldName, fieldType);
                classInfo.getFields().add(newField);
                JOptionPane.showMessageDialog(this, "Field '" + fieldName + "' added to class '" + className + "'.");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid field name or type. Field not added.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Class not found. Field not added.");
        }
    }

    private void deleteField() {
        String className = JOptionPane.showInputDialog(this, "Enter the class name to delete a field from:");
        UMLClassInfo classInfo = UMLClass.classMap.get(className);
        if (classInfo != null) {
            String fieldName = JOptionPane.showInputDialog(this, "Enter the name of the field to delete:");
            UMLFieldInfo fieldToRemove = classInfo.getFields().stream()
                    .filter(field -> field.getFieldName().equals(fieldName))
                    .findFirst().orElse(null);
            if (fieldToRemove != null) {
                classInfo.getFields().remove(fieldToRemove);
                JOptionPane.showMessageDialog(this, "Field '" + fieldName + "' deleted from class '" + className + "'.");
            } else {
                JOptionPane.showMessageDialog(this, "Field not found. Deletion canceled.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Class not found. Field not deleted.");
        }
    }

    private void renameField() {
        String className = JOptionPane.showInputDialog(this, "Enter the class name to rename a field in:");
        UMLClassInfo classInfo = UMLClass.classMap.get(className);
        if (classInfo != null) {
            String oldFieldName = JOptionPane.showInputDialog(this, "Enter the current name of the field:");
            UMLFieldInfo fieldToRename = classInfo.getFields().stream()
                    .filter(field -> field.getFieldName().equals(oldFieldName))
                    .findFirst().orElse(null);
            if (fieldToRename != null) {
                String newFieldName = JOptionPane.showInputDialog(this, "Enter the new name for the field:");
                if (newFieldName != null && !newFieldName.trim().isEmpty()) {
                    fieldToRename.setFieldName(newFieldName);
                    JOptionPane.showMessageDialog(this, "Field '" + oldFieldName + "' renamed to '" + newFieldName + "' in class '" + className + "'.");
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid new field name. Rename canceled.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Field not found. Rename canceled.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Class not found. Field not renamed.");
        }
    }

    private void addRelationship() {
        String sourceClass = JOptionPane.showInputDialog(this, "Enter the source class name for the relationship:");
        String destinationClass = JOptionPane.showInputDialog(this, "Enter the destination class name for the relationship:");

        if (sourceClass == null || sourceClass.trim().isEmpty() || !UMLClass.classMap.containsKey(sourceClass)) {
            JOptionPane.showMessageDialog(this, "Source class '" + sourceClass + "' does not exist or is invalid.");
            return;
        }

        if (destinationClass == null || destinationClass.trim().isEmpty() || !UMLClass.classMap.containsKey(destinationClass)) {
            JOptionPane.showMessageDialog(this, "Destination class '" + destinationClass + "' does not exist or is invalid.");
            return;
        }
        if (sourceClass == null || sourceClass.trim().isEmpty() || !UMLClass.classMap.containsKey(sourceClass)) {
            JOptionPane.showMessageDialog(this, "Source class '" + sourceClass + "' does not exist or is invalid.");
            return;
        }

        if (destinationClass == null || destinationClass.trim().isEmpty() || !UMLClass.classMap.containsKey(destinationClass)) {
            JOptionPane.showMessageDialog(this, "Destination class '" + destinationClass + "' does not exist or is invalid.");
            return;
        }

        if (sourceClass != null && destinationClass != null && Relationship.addRelationship(sourceClass, destinationClass)) {
            JOptionPane.showMessageDialog(this, "Relationship added between '" + sourceClass + "' and '" + destinationClass + "'.");
        } else {
            JOptionPane.showMessageDialog(this, "Unable to add relationship. Check class names or existing relationships.");
        }
    }

    private void deleteRelationship() {
        String sourceClass = JOptionPane.showInputDialog(this, "Enter the source class name for the relationship:");
        String destinationClass = JOptionPane.showInputDialog(this, "Enter the destination class name for the relationship:");
        if (sourceClass != null && destinationClass != null && Relationship.removeRelationship(sourceClass, destinationClass)) {
            JOptionPane.showMessageDialog(this, "Relationship removed between '" + sourceClass + "' and '" + destinationClass + "'.");
        } else {
            JOptionPane.showMessageDialog(this, "Relationship not found or unable to remove.");
        }
    }

    private void saveDiagram() {
        String fileName = JOptionPane.showInputDialog(this, "Enter file name to save:");
        if (fileName != null && !fileName.trim().isEmpty()) {
            try {
                SaveManager.saveToJSON(fileName + ".json");
                JOptionPane.showMessageDialog(this, "Diagram saved as " + fileName + ".json");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving diagram: " + ex.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid file name. Diagram not saved.");
        }
    }

    private void loadDiagram() {
        String fileName = JOptionPane.showInputDialog(this, "Enter file name to load:");
        if (fileName != null && !fileName.trim().isEmpty()) {
            try {
                SaveManager.loadFromJSON(fileName + ".json");
                JOptionPane.showMessageDialog(this, "Diagram loaded from " + fileName + ".json");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error loading diagram: " + ex.getMessage(), "Load Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid file name. Diagram not loaded.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GraphicalInterface window = new GraphicalInterface();
            window.setVisible(true);
        });
    }
}
