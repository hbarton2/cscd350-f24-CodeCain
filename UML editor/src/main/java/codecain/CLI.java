
package codecain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

/**
 * The GUI class represents a UML editor with a graphical user interface (GUI)
 * built using Java Swing components. It allows users to interact with UML models
 * through command-line-style input.
 */
public class CLI {

    /**
     * The main method initializes the application, setting up the main JFrame and GUI components.
     *
     * @param args command-line arguments (not used in this application)
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("UML editor Command Line");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.BLACK);
        frame.add(panel);
        placeComponents(panel);
        frame.setResizable(true);
        frame.setVisible(true);
    }

    /**
     * Initializes and adds the components to the specified JPanel.
     *
     * @param panel the JPanel where the components are placed
     */
    private static void placeComponents(JPanel panel) {
        JTextArea commandOutput = new JTextArea();
        commandOutput.setFont(new Font("Monospaced", Font.PLAIN, 14));
        commandOutput.setEditable(false);
        commandOutput.setBackground(Color.BLACK);
        commandOutput.setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(commandOutput);
        panel.add(scrollPane, BorderLayout.CENTER);

        JTextField commandInput = new JTextField();
        commandInput.setBackground(Color.BLACK);
        commandInput.setForeground(Color.WHITE);
        panel.add(commandInput, BorderLayout.SOUTH);

        String welcomeMessage = "CSCD 350 UML Editor\n" + "Group: Code Cain\n" + "Type 'help' to see available commands.\n\n";
        commandOutput.append(welcomeMessage);

        commandInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputCommand = commandInput.getText();
                if (!inputCommand.trim().isEmpty()) {
                    int helpStartPosition = commandOutput.getDocument().getLength();
                    String output = ">> " + inputCommand + "\n" + executeCommand(inputCommand) + "\n";
                    commandOutput.append(output);
                    commandInput.setText("");
                    if (inputCommand.equalsIgnoreCase("help")) {
                        commandOutput.setCaretPosition(helpStartPosition);
                    } else {
                        commandOutput.setCaretPosition(commandOutput.getDocument().getLength());
                    }
                }
            }
        });
    }

    /**
     * Parses and executes the command entered by the user.
     *
     * @param command the input command entered by the user
     * @return the result of the command execution as a string
     */
    private static String executeCommand(String command) {
        String[] tokens = command.split(" ");
        if (tokens.length == 0) {
            return "No command entered.";
        }

        String commandName = tokens[0].toLowerCase();

        switch (commandName) {
            case "help":
                return showHelp();
            case "add":
                return handleAddCommand(tokens);
            case "delete":
                return handleDeleteCommand(tokens);
            case "rename":
                return handleRenameCommand(tokens);
            case "list":
                return handleListCommand(tokens);
            case "exit":
                System.exit(0);
            case "save":
                return handleSaveLoadCommand(tokens, "save");
            case "load":
                return handleSaveLoadCommand(tokens, "load");
            default:
                return "Unknown command. Type 'help' to see available commands.";
        }
    }

    private static String handleSaveLoadCommand(String[] tokens, String operation) {
        if (tokens.length == 2) {
            String fileName = tokens[1];
            if (operation.equals("save")) {
                return saveDiagram(fileName);
            } else {
                return loadDiagram(fileName);
            }
        } else {
            return "Usage: " + operation + " <filename>";
        }
    }


    /**
     * Saves the current UML diagram to a specified file.
     *
     * @param fileName the name of the file to save the UML diagram
     * @return success or error message
     */
    private static String saveDiagram(String fileName) {
        try {
            SaveManager.saveToJSON(fileName);
            return "UML diagram saved to " + fileName;
        } catch (Exception e) {
            return "Error saving UML diagram: " + e.getMessage();
        }
    }

    /**
     * Loads a UML diagram from a specified file.
     *
     * @param fileName the name of the file to load the UML diagram from
     * @return success or error message
     */
    private static String loadDiagram(String fileName) {
        try {
            SaveManager.loadFromJSON(fileName);
            return "UML diagram loaded from " + fileName;
        } catch (Exception e) {
            return "Error loading UML diagram: " + e.getMessage();
        }
    }

    /**
     * Displays the help message with available commands.
     *
     * @return a string containing the help message
     */
    private static String showHelp() {
        String helpMessage = """
                Available commands:

                Class Operations:
                1. add class 'name'                  - Adds a new class with a unique name.
                2. delete class 'name'               - Deletes the class with the specified name.
                3. rename class 'oldName' 'newName'  - Renames the class from 'oldName' to 'newName'.

                Relationship Operations:
                1. add relationship 'source' 'destination'   - Adds a relationship between 'source' and 'destination' classes.
                2. delete relationship 'source' 'destination' - Deletes the relationship between 'source' and 'destination'.

                Field Operations:
                1. add field 'className' 'fieldName'  - Adds a unique field to the specified class.
                2. delete field 'className' 'fieldName' - Removes a field from the specified class.
                3. rename field 'className' 'oldFieldName' 'newFieldName' - Renames a field in the specified class.

                Method Operations:
                1. add method 'className' 'methodName' 'parameters' - Adds a unique method to the specified class.
                2. delete method 'className' 'methodName' - Removes the method from the specified class.
                3. rename method 'className' 'oldMethodName' 'newMethodName' - Renames a method in the specified class.
                4. add parameter 'className' 'methodName' 'parameter' - Adds a parameter to a method.
                5. delete parameter 'className' 'methodName' 'parameter' - Removes a parameter from a method.
                6. rename parameter 'className' 'methodName' 'oldParameterName' 'newParameterName' - Renames a parameter in a method.

                Save/Load Operations:
                1. save                                - Saves the current state of the project.
                2. load                                - Loads the project state from a file.

                Listing Operations:
                1. list classes                        - Lists all the classes in the project.
                2. list class 'className'              - Lists the contents (fields and methods) of the specified class.
                3. list relationships                  - Lists all the relationships between classes.

                Other Commands:
                1. help                                - Shows this help message.
                2. exit                                - Exits the application.

                Examples:
                - add class Person
                - add relationship Person Address
                - add field Person name
                - add method Person setName String name
                - add parameter Person setName String name
                - delete parameter Person setName String name
                - rename parameter Person setName oldName newName
                - delete class Person
                - rename class Person Employee
                - list classes
                - list relationships
                - help
                - exit

                """;

        return """
                +---------------------------------------------------------------+
                |                                                               |
                |  Command Help Menu                                            |
                |                                                               |
                +---------------------------------------------------------------+
                """ + helpMessage + """
                +---------------------------------------------------------------+
                """;
    }

    /**
     * Handles the 'add' command for adding classes, relationships, fields, methods, or parameters.
     *
     * @param tokens an array of command tokens
     * @return the result of the add operation
     */
    private static String handleAddCommand(String[] tokens) {
        if (tokens.length < 3) {
            return "Usage: add <type> <name> [additional parameters]. Use 'help' for available commands.";
        }

        String type = tokens[1].toLowerCase();

        switch (type) {
            case "class":
                if (tokens.length == 3) {
                    return addClass(tokens[2]);
                }
                break;
            case "relationship":
                if (tokens.length == 4) {
                    return addRelationship(tokens[2], tokens[3]);
                }
                break;
            case "field":
                if (tokens.length == 5) {
                    return addField(tokens[2], tokens[3],tokens[4]);
                }
                break;
            case "method":
                if (tokens.length >= 5) {
                    return addMethod(tokens[2], tokens[3], Arrays.copyOfRange(tokens, 4, tokens.length));
                }
                break;
            case "parameter":
                if (tokens.length == 5) {
                    return addParameter(tokens[2], tokens[3], tokens[4]);
                }
                break;
            default:
                return "Unknown add type: " + type + ". Use 'help' for available commands.";
        }
        return "Invalid command. Use 'help' for available commands.";
    }

    /**
     * Adds a class to the UML diagram.
     *
     * @param className the name of the class to add
     * @return a message indicating the success of the operation
     */
    private static String addClass(String className) {
        UMLClass.addClass(className);
        return "Class '" + className + "' added.";
    }

    /**
     * Adds a relationship between two classes.
     *
     * @param class1 the source class of the relationship
     * @param class2 the destination class of the relationship
     * @return a message indicating the success of the operation
     */
    private static String addRelationship(String class1, String class2) {
        try {
            Relationship.addRelationship(class1, class2);
            return "Relationship between '" + class1 + "' and '" + class2 + "' added.";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Adds a field to a class.
     *
     * @param className the name of the class
     * @param fieldName the name of the field to add
     * @return a message indicating the success of the operation
     */
    private static String addField(String className, String fieldName, String fieldType) {
        UMLFields fields = new UMLFields();
        fields.addField(className, fieldName, fieldType);
        return "Field '" + fieldName + "' added to class '" + className + "'.";
    }

    /**
     * Adds a method to a class with parameters.
     *
     * @param className the name of the class
     * @param methodName the name of the method
     * @param parameters the parameters of the method
     * @return a message indicating the success of the operation
     */
    private static String addMethod(String className, String methodName, String[] parameters) {
        String parametersString = String.join(",", parameters);
        return addMethod(className, methodName, parametersString);
    }

    /**
     * Adds a method to a class.
     *
     * @param className the name of the class
     * @param methodName the name of the method
     * @param parametersString the parameters as a comma-separated string
     * @return a message indicating the success of the operation
     */
    private static String addMethod(String className, String methodName, String parametersString) {
        String[] parameters = parametersString.split("\\s*,\\s*");
        UMLMethods methods = new UMLMethods();
        methods.addMethod(className, methodName, Arrays.asList(parameters));
        return "Method '" + methodName + "' added to class '" + className + "' with parameters: " + Arrays.toString(parameters) + ".";
    }

    /**
     * Adds a parameter to a method in a class.
     *
     * @param className the name of the class
     * @param methodName the name of the method
     * @param parameter the parameter to add
     * @return a message indicating the success of the operation
     */
    private static String addParameter(String className, String methodName, String parameter) {
        UMLMethods methods = new UMLMethods();
        methods.addParameter(className, methodName, parameter);
        return "Parameter '" + parameter + "' added to method '" + methodName + "' in class '" + className + "'.";
    }

    /**
     * Handles the 'delete' command for deleting classes, relationships, fields, methods, or parameters.
     *
     * @param tokens an array of command tokens
     * @return the result of the delete operation
     */
    private static String handleDeleteCommand(String[] tokens) {
        if (tokens.length < 3) {
            return "Usage: delete <type> <name>. Use 'help' for available commands.";
        }

        String type = tokens[1].toLowerCase();

        switch (type) {
            case "class":
                if (tokens.length == 3) {
                    return deleteClass(tokens[2]);
                }
                break;
            case "relationship":
                if (tokens.length == 4) {
                    return deleteRelationship(tokens[2], tokens[3]);
                }
                break;
            case "field":
                if (tokens.length == 5) {
                    return deleteField(tokens[2], tokens[3],tokens[4]);
                }
                break;
            case "method":
                if (tokens.length == 4) {
                    return deleteMethod(tokens[2], tokens[3]);
                }
                break;
            case "parameter":
                if (tokens.length == 5) {
                    return deleteParameter(tokens[2], tokens[3], tokens[4]);
                }
                break;
            default:
                return "Unknown delete type: " + type + ". Use 'help' for available commands.";
        }
        return "Invalid command. Use 'help' for available commands.";
    }

    /**
     * Deletes a class and its attached relationships.
     *
     * @param className the name of the class to delete
     * @return a message indicating the success of the operation
     */
    private static String deleteClass(String className) {
        UMLClass.removeClass(className);
        Relationship.removeAttachedRelationships(className);
        return "Class '" + className + "' and its relationships deleted.";
    }

    /**
     * Deletes a relationship between two classes.
     *
     * @param class1 the source class of the relationship
     * @param class2 the destination class of the relationship
     * @return a message indicating the success of the operation
     */
    private static String deleteRelationship(String class1, String class2) {
        try {
            Relationship.removeRelationship(class1, class2);
            return "Relationship between '" + class1 + "' and '" + class2 + "' deleted.";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Deletes a field from a class.
     *
     * @param className the name of the class
     * @param fieldName the name of the field to delete
     * @return a message indicating the success of the operation
     */
    private static String deleteField(String className, String fieldName, String fieldType) {
        UMLFields fields = new UMLFields();
        fields.removeField(className, fieldName, fieldType);
        return "Field '" + fieldName + "' removed from class '" + className + "'.";
    }

    /**
     * Deletes a method from a class.
     *
     * @param className the name of the class
     * @param methodName the name of the method to delete
     * @return a message indicating the success of the operation
     */
    private static String deleteMethod(String className, String methodName) {
        UMLMethods methods = new UMLMethods();
        methods.removeMethod(className, methodName);
        return "Method '" + methodName + "' removed from class '" + className + "'.";
    }

    /**
     * Deletes a parameter from a method in a class.
     *
     * @param className the name of the class
     * @param methodName the name of the method
     * @param parameter the parameter to delete
     * @return a message indicating the success of the operation
     */
    private static String deleteParameter(String className, String methodName, String parameter) {
        UMLMethods methods = new UMLMethods();
        methods.removeParameter(className, methodName, parameter);
        return "Parameter '" + parameter + "' removed from method '" + methodName + "' in class '" + className + "'.";
    }

    /**
     * Handles the 'rename' command for renaming classes, fields, or methods.
     *
     * @param tokens an array of command tokens
     * @return the result of the rename operation
     */
    private static String handleRenameCommand(String[] tokens) {
        if (tokens.length < 4) {
            return "Usage: rename <type> <oldName> <newName>. Use 'help' for available commands.";
        }

        String type = tokens[1].toLowerCase();

        switch (type) {
            case "class":
                if (tokens.length == 4) {
                    return renameClass(tokens[2], tokens[3]);
                }
                break;
            case "field":
                if (tokens.length == 7) {
                    return renameField(tokens[2], tokens[3], tokens[4], tokens[5],tokens[6]);
                }
                break;
            case "method":
                if (tokens.length == 5) {
                    return renameMethod(tokens[2], tokens[3], tokens[4]);
                }
                break;
            default:
                return "Unknown rename type: " + type + ". Use 'help' for available commands.";
        }
        return "Invalid command. Use 'help' for available commands.";
    }


    /**
     * Renames a class.
     *
     * @param oldName the current name of the class
     * @param newName the new name for the class
     * @return a message indicating the success of the operation
     */
    private static String renameClass(String oldName, String newName) {
        UMLClass.renameClass(oldName, newName);
        return "Class '" + oldName + "' renamed to '" + newName + "'.";
    }

    /**
     * Renames a field in a class.
     *
     * @param className the name of the class
     * @param oldFieldName the current name of the field
     * @param newFieldName the new name for the field
     * @return a message indicating the success of the operation
     */
    private static String renameField(String className, String oldFieldName, String oldFieldType, String newFieldName, String newFieldType) {
        UMLFields fields = new UMLFields();
        fields.renameField(className, oldFieldName, oldFieldType, newFieldName, newFieldType);
        return "Field '" + oldFieldName + "' renamed to '" + newFieldName + "' in class '" + className + "'.";
    }

    /**
     * Renames a method in a class.
     *
     * @param className the name of the class
     * @param oldMethodName the current name of the method
     * @param newMethodName the new name for the method
     * @return a message indicating the success of the operation
     */
    private static String renameMethod(String className, String oldMethodName, String newMethodName) {
        UMLMethods methods = new UMLMethods();
        methods.renameMethod(className, oldMethodName, newMethodName);
        return "Method '" + oldMethodName + "' renamed to '" + newMethodName + "' in class '" + className + "'.";
    }

    /**
     * Handles the 'list' command for listing classes or relationships.
     *
     * @param tokens an array of command tokens
     * @return the result of the list operation
     */
    private static String handleListCommand(String[] tokens) {
        if (tokens.length == 2 && tokens[1].equalsIgnoreCase("classes")) {
            return listClasses();
        } else if (tokens.length == 2 && tokens[1].equalsIgnoreCase("relationships")) {
            return listRelationships();
        } else {
            return "Invalid command. Use: list classes.";
        }
    }

    /**
     * Lists all classes in the UML diagram.
     *
     * @return a string listing all the classes
     */
    private static String listClasses() {
        if (UMLClass.classMap.isEmpty()) {
            return "No classes available.";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Classes:\n");
            for (Object className : UMLClass.classMap.keySet()) {
                sb.append("- ").append(className).append("\n");
            }
            return sb.toString();
        }
    }

    /**
     * Lists all relationships between classes.
     *
     * @return a string listing all the relationships
     */
    private static String listRelationships() {
        try {
            String relationships = Relationship.listToString();
            if (relationships.isEmpty()) {
                return "No relationships available.";
            } else {
                return "Relationships:\n" + relationships;
            }
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
