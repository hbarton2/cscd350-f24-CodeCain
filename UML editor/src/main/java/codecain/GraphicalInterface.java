package codecain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * GraphicalInterface class for creating and managing a GUI to edit and save UML class diagrams.
 */
public class GraphicalInterface extends JFrame {
    private JPanel canvas;
    private JPanel controlsPanel;
    private JPanel classesPanel;
    private HashMap<Object, JPanel> classPanels = new HashMap<>();

    /**
     * Constructor that sets up the GUI with controls, canvas, and class manipulation buttons.
     */
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

    /**
     * Creates a draggable panel representing a UML class, displaying its fields and methods.
     */
    private void createClassBox(UMLClassInfo umlClassInfo) {
        JPanel classBox = new JPanel();
        classBox.setLayout(new BorderLayout());
        classBox.setBounds(50, 50, 200, 150); // Set initial position and size
        classBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        classBox.setBackground(Color.LIGHT_GRAY);

        JLabel classNameLabel = new JLabel(String.valueOf(umlClassInfo.getClassName()), SwingConstants.CENTER);
        classBox.add(classNameLabel, BorderLayout.NORTH);

        JTextArea detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        classBox.add(detailsArea, BorderLayout.CENTER);

        canvas.setLayout(null); // Null layout for absolute positioning
        canvas.add(classBox);
        canvas.repaint();

        classPanels.put(String.valueOf(umlClassInfo.getClassName()), classBox);

        final Point initialClick = new Point();
        classBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                initialClick.setLocation(e.getPoint());
            }
        });
        classBox.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int xMove = e.getX() - initialClick.x;
                int yMove = e.getY() - initialClick.y;

                Point location = classBox.getLocation();

                int newX = location.x + xMove;
                int newY = location.y + yMove;

                int canvasWidth = canvas.getWidth();
                int canvasHeight = canvas.getHeight();
                int boxWidth = classBox.getWidth();
                int boxHeight = classBox.getHeight();

                newX = Math.max(0, Math.min(newX, canvasWidth - boxWidth));
                newY = Math.max(0, Math.min(newY, canvasHeight - boxHeight));

                classBox.setLocation(newX, newY);
                canvas.repaint();
            }
        });

        updateClassBoxDetails(umlClassInfo, detailsArea);
    }

    /**
     * updateClassBoxDetails edits the fields and methods in each class.
     * @param umlClassInfo
     * @param detailsArea
     */
    private void updateClassBoxDetails(UMLClassInfo umlClassInfo, JTextArea detailsArea) {
        StringBuilder detailsText = new StringBuilder("Fields:\n");
        for (UMLFieldInfo field : umlClassInfo.getFields()) {
            detailsText.append(field.getFieldName()).append(": ").append(field.getFieldType()).append("\n");
        }
        detailsText.append("\nMethods:\n");
        for (UMLMethodInfo method : umlClassInfo.getMethods()) {
            detailsText.append(method.getMethodName()).append("(");
            detailsText.append(method.getParameters().stream()
                    .map(param -> param.getParameterType() + " " + param.getParameterName())
                    .collect(Collectors.joining(", ")));
            detailsText.append(")\n");
        }
        detailsArea.setText(detailsText.toString());
    }



    /**
     * Adds a method to a specified class.
     */
    private void addMethod() {
        String className = JOptionPane.showInputDialog(this, "Enter the class name to add a method:");
        String methodName = JOptionPane.showInputDialog(this, "Enter the method name:");
        String parametersInput = JOptionPane.showInputDialog(this, "Enter parameters (comma-separated, format: type name):");
        List<UMLParameterInfo> parameters = new ArrayList<>();

        if (parametersInput != null && !parametersInput.trim().isEmpty()) {
            for (String param : parametersInput.split(",")) {
                String[] typeAndName = param.trim().split("\\s+");
                if (typeAndName.length == 2) {
                    parameters.add(new UMLParameterInfo(typeAndName[0], typeAndName[1]));
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid parameter format. Use 'type name' for each parameter.");
                    return;
                }
            }
        }

        UMLClassInfo classInfo = UMLClass.classMap.get(className);
        if (classInfo != null) {
            new UMLMethods().addMethod(className, methodName, parameters);
            JTextArea detailsArea = (JTextArea) classPanels.get(className).getComponent(1);
            updateClassBoxDetails(classInfo, detailsArea);
            JOptionPane.showMessageDialog(this, "Method '" + methodName + "' added to class '" + className + "'.");
        } else {
            JOptionPane.showMessageDialog(this, "Class not found. Method not added.");
        }
    }

    /**
     * Deletes a method from a specified class.
     */
    private void deleteMethod() {
        String className = JOptionPane.showInputDialog(this, "Enter the class name to delete a method:");
        String methodName = JOptionPane.showInputDialog(this, "Enter the name of the method to delete:");

        if (className == null || className.trim().isEmpty() || methodName == null || methodName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Class name and method name are required.");
            return;
        }

        UMLClassInfo classInfo = UMLClass.classMap.get(className);
        if (classInfo != null) {
            new UMLMethods().removeMethod(className, methodName);
            JTextArea detailsArea = (JTextArea) classPanels.get(className).getComponent(1);
            updateClassBoxDetails(classInfo, detailsArea);
            JOptionPane.showMessageDialog(this, "Method '" + methodName + "' deleted from class '" + className + "'.");
        } else {
            JOptionPane.showMessageDialog(this, "Class not found. Method not deleted.");
        }
    }

    /**
     * Renames a method in a specified class.
     */
    private void renameMethod() {
        String className = JOptionPane.showInputDialog(this, "Enter the class name to rename a method:");
        String oldMethodName = JOptionPane.showInputDialog(this, "Enter the current method name:");
        String newMethodName = JOptionPane.showInputDialog(this, "Enter the new method name:");

        if (className == null || className.trim().isEmpty() || oldMethodName == null || oldMethodName.trim().isEmpty() || newMethodName == null || newMethodName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All inputs are required.");
            return;
        }

        UMLClassInfo classInfo = UMLClass.classMap.get(className);
        if (classInfo != null) {
            new UMLMethods().renameMethod(className, oldMethodName, newMethodName);
            JTextArea detailsArea = (JTextArea) classPanels.get(className).getComponent(1);
            updateClassBoxDetails(classInfo, detailsArea);
            JOptionPane.showMessageDialog(this, "Method '" + oldMethodName + "' renamed to '" + newMethodName + "' in class '" + className + "'.");
        } else {
            JOptionPane.showMessageDialog(this, "Class not found. Method not renamed.");
        }
    }

    /**
     * Adds a parameter to a specified method in a class.
     */
    private void addParameter() {
        String className = JOptionPane.showInputDialog(this, "Enter the class name:");
        String methodName = JOptionPane.showInputDialog(this, "Enter the method name to add a parameter:");
        String parameterType = JOptionPane.showInputDialog(this, "Enter the parameter type:");
        String parameterName = JOptionPane.showInputDialog(this, "Enter the parameter name:");

        if (className == null || className.trim().isEmpty() || methodName == null || methodName.trim().isEmpty() ||
                parameterType == null || parameterType.trim().isEmpty() || parameterName == null || parameterName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All inputs are required.");
            return;
        }

        UMLClassInfo classInfo = UMLClass.classMap.get(className);
        if (classInfo != null) {
            new UMLMethods().addParameter(className, methodName, parameterType, parameterName);
            JTextArea detailsArea = (JTextArea) classPanels.get(className).getComponent(1);
            updateClassBoxDetails(classInfo, detailsArea);
            JOptionPane.showMessageDialog(this, "Parameter '" + parameterName + "' added to method '" + methodName + "' in class '" + className + "'.");
        } else {
            JOptionPane.showMessageDialog(this, "Class not found. Parameter not added.");
        }
    }

    /**
     * Deletes a parameter from a specified method in a class.
     */
    private void deleteParameter() {
        String className = JOptionPane.showInputDialog(this, "Enter the class name:");
        String methodName = JOptionPane.showInputDialog(this, "Enter the method name to delete a parameter:");
        String parameterName = JOptionPane.showInputDialog(this, "Enter the parameter name to delete:");

        if (className == null || className.trim().isEmpty() || methodName == null || methodName.trim().isEmpty() || parameterName == null || parameterName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All inputs are required.");
            return;
        }

        UMLClassInfo classInfo = UMLClass.classMap.get(className);
        if (classInfo != null) {
            new UMLMethods().removeParameter(className, methodName, parameterName);
            JTextArea detailsArea = (JTextArea) classPanels.get(className).getComponent(1);
            updateClassBoxDetails(classInfo, detailsArea);
            JOptionPane.showMessageDialog(this, "Parameter '" + parameterName + "' deleted from method '" + methodName + "' in class '" + className + "'.");
        } else {
            JOptionPane.showMessageDialog(this, "Class not found. Parameter not deleted.");
        }
    }

    private void Check(String className, String parameterType, String parameterName,String methodName) {
        if (className == null || className.trim().isEmpty() || methodName == null || methodName.trim().isEmpty() ||
                parameterType == null || parameterType.trim().isEmpty() || parameterName == null || parameterName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All inputs are required.");
        }
    }

    /**
     * Renames a parameter in a specified method in a class.
     */
    private void renameParameter() {
        String className = JOptionPane.showInputDialog(this, "Enter the class name:");
        String methodName = JOptionPane.showInputDialog(this, "Enter the method name:");
        String oldParameterName = JOptionPane.showInputDialog(this, "Enter the current parameter name:");
        String newParameterType = JOptionPane.showInputDialog(this, "Enter the new parameter type:");
        String newParameterName = JOptionPane.showInputDialog(this, "Enter the new parameter name:");

        if (className == null || className.trim().isEmpty() || methodName == null || methodName.trim().isEmpty() ||
                oldParameterName == null || oldParameterName.trim().isEmpty() || newParameterType == null || newParameterType.trim().isEmpty() || newParameterName == null || newParameterName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All inputs are required.");
            return;
        }

        UMLClassInfo classInfo = UMLClass.classMap.get(className);
        if (classInfo != null) {
            new UMLMethods().changeSingleParameter(className, methodName, oldParameterName, newParameterType, newParameterName);
            JTextArea detailsArea = (JTextArea) classPanels.get(className).getComponent(1);
            updateClassBoxDetails(classInfo, detailsArea);
            JOptionPane.showMessageDialog(this, "Parameter '" + oldParameterName + "' renamed to '" + newParameterName + "' in method '" + methodName + "' of class '" + className + "'.");
        } else {
            JOptionPane.showMessageDialog(this, "Class not found. Parameter not renamed.");
        }
    }

    /**
     * Adds a class to the UML diagram.
     */
    private void addClass() {
        String className = JOptionPane.showInputDialog(this, "Enter the name of the class to add:");
        if (className != null && !className.trim().isEmpty()) {
            UMLClassInfo newClass = new UMLClassInfo(className);
            UMLClass.classMap.put(className, newClass);

            createClassBox(newClass);

            JOptionPane.showMessageDialog(this, "Class '" + className + "' added.");
        } else {
            JOptionPane.showMessageDialog(this, "Invalid class name. Class not added.");
        }
    }

    /**
     * Deletes a class and its relationships from the UML diagram.
     */
    private void deleteClass() {
        String className = JOptionPane.showInputDialog(this, "Enter the name of the class to delete:");
        if (className != null && !className.trim().isEmpty() && UMLClass.classMap.containsKey(className)) {
            UMLClass.classMap.remove(className);
            Relationship.removeAttachedRelationships(className);

            JPanel classBox = classPanels.remove(className);
            if (classBox != null) {
                canvas.remove(classBox);
            }

            canvas.revalidate();
            canvas.repaint();
            JOptionPane.showMessageDialog(this, "Class '" + className + "' deleted along with its relationships.");
        } else {
            JOptionPane.showMessageDialog(this, "Class not found or invalid name. Deletion canceled.");
        }
    }


    /**
     * Renames a class in the UML diagram.
     */
    private void renameClass() {
        String oldClassName = JOptionPane.showInputDialog(this, "Enter the name of the class to rename:");
        if (oldClassName != null && UMLClass.classMap.containsKey(oldClassName)) {
            String newClassName = JOptionPane.showInputDialog(this, "Enter the new name for the class:");
            if (newClassName != null && !newClassName.trim().isEmpty()) {
                UMLClassInfo classInfo = UMLClass.classMap.remove(oldClassName);
                classInfo.setClassName(newClassName);
                UMLClass.classMap.put(newClassName, classInfo);
                UMLClass.classMap.remove(oldClassName);
                JPanel classPanel = classPanels.remove(oldClassName);
                if (classPanel != null) {
                    classPanels.put(newClassName, classPanel);
                    classPanel.setName(newClassName);
                    JLabel classLabel = (JLabel) classPanel.getComponent(0);
                    classLabel.setText(newClassName);
                    JTextArea detailsArea = (JTextArea) classPanel.getComponent(1);
                    updateClassBoxDetails(classInfo, detailsArea);
                }
                JOptionPane.showMessageDialog(this, "Class '" + oldClassName + "' renamed to '" + newClassName + "'.");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid new class name. Rename canceled.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Class not found or invalid name. Rename canceled.");
        }
    }

    /**
     * Adds a field to a specified class.
     */
    private void addField() {
        String className = JOptionPane.showInputDialog(this, "Enter the class name to add a field to:");
        String fieldName = JOptionPane.showInputDialog(this, "Enter the name of the field to add:");
        String fieldType = JOptionPane.showInputDialog(this, "Enter the type of the field:");

        if (className == null || fieldName == null || fieldType == null) {
            JOptionPane.showMessageDialog(this, "Operation canceled.");
            return;
        }

        UMLFields fieldManager = new UMLFields();
        fieldManager.addField(className, fieldType, fieldName);

        UMLClassInfo classInfo = UMLClass.classMap.get(className);
        if (classInfo != null) {
            JTextArea detailsArea = (JTextArea) classPanels.get(className).getComponent(1);
            updateClassBoxDetails(classInfo, detailsArea);
        }
    }


    /**
     * Deletes a field from a specified class.
     */
    private void deleteField() {
        String className = JOptionPane.showInputDialog(this, "Enter the class name to delete a field from:");
        String fieldName = JOptionPane.showInputDialog(this, "Enter the name of the field to delete:");

        if (className == null || fieldName == null) {
            JOptionPane.showMessageDialog(this, "Operation canceled.");
            return;
        }

        UMLFields fieldManager = new UMLFields();
        fieldManager.removeField(className, fieldName);

        UMLClassInfo classInfo = UMLClass.classMap.get(className);
        if (classInfo != null) {
            JTextArea detailsArea = (JTextArea) classPanels.get(className).getComponent(1);
            updateClassBoxDetails(classInfo, detailsArea);
        }
    }

    /**
     * Renames a field in a specified class.
     */
    private void renameField() {
        String className = JOptionPane.showInputDialog(this, "Enter the class name to rename a field in:");
        String oldFieldName = JOptionPane.showInputDialog(this, "Enter the current name of the field:");
        String newFieldName = JOptionPane.showInputDialog(this, "Enter the new name for the field:");
        String newFieldType = JOptionPane.showInputDialog(this, "Enter the new type for the field:");

        if (className == null || oldFieldName == null || newFieldName == null || newFieldType == null) {
            JOptionPane.showMessageDialog(this, "Operation canceled.");
            return;
        }

        UMLFields fieldManager = new UMLFields();
        fieldManager.renameField(className, oldFieldName, newFieldType, newFieldName);

        UMLClassInfo classInfo = UMLClass.classMap.get(className);
        if (classInfo != null) {
            JTextArea detailsArea = (JTextArea) classPanels.get(className).getComponent(1);
            updateClassBoxDetails(classInfo, detailsArea);
        }
    }

    /**
     * Adds a relationship between two classes in the UML diagram.
     */
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

        if (Relationship.addRelationship(sourceClass, destinationClass)) {
            JOptionPane.showMessageDialog(this, "Relationship added between '" + sourceClass + "' and '" + destinationClass + "'.");
        } else {
            JOptionPane.showMessageDialog(this, "Unable to add relationship. Check class names or existing relationships.");
        }
    }

    /**
     * Deletes a relationship between two classes in the UML diagram.
     */
    private void deleteRelationship() {
        String sourceClass = JOptionPane.showInputDialog(this, "Enter the source class name for the relationship:");
        String destinationClass = JOptionPane.showInputDialog(this, "Enter the destination class name for the relationship:");
        if (sourceClass != null && destinationClass != null && Relationship.removeRelationship(sourceClass, destinationClass)) {
            JOptionPane.showMessageDialog(this, "Relationship removed between '" + sourceClass + "' and '" + destinationClass + "'.");
        } else {
            JOptionPane.showMessageDialog(this, "Relationship not found or unable to remove.");
        }
    }

    /**
     * Saves the current UML diagram to a JSON file.
     */
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

    /**
     * Loads a UML diagram from a JSON file.
     */
    public void loadDiagram() {
        String fileName = JOptionPane.showInputDialog(this, "Enter file name to load:");
        if (fileName != null && !fileName.trim().isEmpty()) {
            try {
                SaveManager.loadFromJSON(fileName + ".json");

                // Clear existing canvas and reload
                canvas.removeAll();
                UMLClass.classMap.values().forEach(this::createClassBox); // Recreate class boxes with saved positions

                canvas.revalidate();
                canvas.repaint();
                JOptionPane.showMessageDialog(this, "Diagram loaded from " + fileName + ".json");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error loading diagram: " + ex.getMessage(), "Load Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid file name. Diagram not loaded.");
        }
    }
    /**
     * Main method to initialize and display the GraphicalInterface.
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GraphicalInterface window = new GraphicalInterface();
            window.setVisible(true);
        });
    }
}
