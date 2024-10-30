package codecain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

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
        // Create a new JPanel for the class box
        JPanel classBox = new JPanel();
        classBox.setLayout(new BorderLayout());
        classBox.setBounds(50, 50, 200, 150); // Set initial position and size
        classBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        classBox.setBackground(Color.LIGHT_GRAY);

        // Add class name label
        JLabel classNameLabel = new JLabel(String.valueOf(umlClassInfo.getClassName()), SwingConstants.CENTER);
        classBox.add(classNameLabel, BorderLayout.NORTH);

        // Display fields and methods
        JTextArea detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        classBox.add(detailsArea, BorderLayout.CENTER);

        canvas.setLayout(null); // Null layout for absolute positioning
        canvas.add(classBox);
        canvas.repaint();

        // Store the reference to the class panel
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

        // Populate details initially
        updateClassBoxDetails(umlClassInfo, detailsArea);
    }

    private void updateClassBoxDetails(UMLClassInfo umlClassInfo, JTextArea detailsArea) {
        StringBuilder detailsText = new StringBuilder("Fields:\n");
        for (UMLFieldInfo field : umlClassInfo.getFields()) {
            detailsText.append(field.getFieldName()).append(": ").append(field.getFieldType()).append("\n");
        }
        detailsText.append("\nMethods:\n");
        for (UMLMethodInfo method : umlClassInfo.getMethods()) {
            detailsText.append(method.getMethodName()).append("(");
            detailsText.append(String.join(", ", method.getParameters())).append(")\n");
        }
        detailsArea.setText(detailsText.toString());
    }


        /**
         * Adds a method to a specified class.
         */
        private void addMethod() {
            String className = JOptionPane.showInputDialog(this, "Enter the class name to add a method:");
            String methodName = JOptionPane.showInputDialog(this, "Enter the method name:");
            String parametersInput = JOptionPane.showInputDialog(this, "Enter parameters (comma-separated):");

            List<String> parameters = parametersInput == null ? new ArrayList<>() : List.of(parametersInput.split(","));
            UMLClassInfo classInfo = UMLClass.classMap.get(className); // Get the class info
            if (classInfo != null) {
                new UMLMethods().addMethod(className, methodName, parameters);
                // Update the class box details
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
        String methodName = JOptionPane.showInputDialog(this, "Enter the method name to delete:");
        new UMLMethods().removeMethod(className, methodName);
    }

    /**
     * Renames a method in a specified class.
     */
    private void renameMethod() {
        String className = JOptionPane.showInputDialog(this, "Enter the class name to rename a method:");
        String oldMethodName = JOptionPane.showInputDialog(this, "Enter the current method name:");
        String newMethodName = JOptionPane.showInputDialog(this, "Enter the new method name:");
        new UMLMethods().renameMethod(className, oldMethodName, newMethodName);
    }

    /**
     * Adds a parameter to a specified method in a class.
     */
    private void addParameter() {
        String className = JOptionPane.showInputDialog(this, "Enter the class name:");
        String methodName = JOptionPane.showInputDialog(this, "Enter the method name to add a parameter:");
        String parameter = JOptionPane.showInputDialog(this, "Enter the parameter to add:");
        new UMLMethods().addParameter(className, methodName, parameter);
    }

    /**
     * Deletes a parameter from a specified method in a class.
     */
    private void deleteParameter() {
        String className = JOptionPane.showInputDialog(this, "Enter the class name:");
        String methodName = JOptionPane.showInputDialog(this, "Enter the method name to delete a parameter:");
        String parameter = JOptionPane.showInputDialog(this, "Enter the parameter to delete:");
        new UMLMethods().removeParameter(className, methodName, parameter);
    }

    /**
     * Renames a parameter in a specified method in a class.
     */
    private void renameParameter() {
        String className = JOptionPane.showInputDialog(this, "Enter the class name:");
        String methodName = JOptionPane.showInputDialog(this, "Enter the method name:");
        String oldParameterName = JOptionPane.showInputDialog(this, "Enter the current parameter name:");
        String newParameterName = JOptionPane.showInputDialog(this, "Enter the new parameter name:");
        new UMLMethods().changeParameter(className, methodName, oldParameterName, newParameterName);
    }

    /**
     * Adds a class to the UML diagram.
     */
    private void addClass() {
        String className = JOptionPane.showInputDialog(this, "Enter the name of the class to add:");
        if (className != null && !className.trim().isEmpty()) {
            UMLClassInfo newClass = new UMLClassInfo(className);
            UMLClass.classMap.put(className, newClass);

            // Create and add the class panel
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
        UMLClassInfo classInfo = UMLClass.classMap.get(className);
        if (classInfo != null) {
            String fieldName = JOptionPane.showInputDialog(this, "Enter the name of the field to add:");
            String fieldType = JOptionPane.showInputDialog(this, "Enter the type of the field:");
            if (fieldName != null && !fieldName.trim().isEmpty() && fieldType != null && !fieldType.trim().isEmpty()) {
                UMLFieldInfo newField = new UMLFieldInfo(fieldName, fieldType);
                classInfo.getFields().add(newField);
                // Update the class box details
                JTextArea detailsArea = (JTextArea) classPanels.get(className).getComponent(1);
                updateClassBoxDetails(classInfo, detailsArea);
                JOptionPane.showMessageDialog(this, "Field '" + fieldName + "' added to class '" + className + "'.");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid field name or type. Field not added.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Class not found. Field not added.");
        }
    }

    /**
     * Deletes a field from a specified class.
     */
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

    /**
     * Renames a field in a specified class.
     */
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
