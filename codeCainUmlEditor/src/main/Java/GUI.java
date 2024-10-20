import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

/**
 * The GUI class represents a command-line-based UML editor with a graphical user interface (GUI)
 * built using Java Swing components. It provides an interface where users can enter commands
 * to manipulate UML class models and their relationships through a command line interface.
 */
public class GUI {

    /**
     * The main method is the entry point of the application. It sets up the main JFrame
     * and initializes the GUI components, including a command input field and a command output area.
     *
     * @param args command-line arguments (not used in this application)
     */
    public static void main(String[] args) {
        // Create the frame
        JFrame frame = new JFrame("UML editor Command Line");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.BLACK); // Set background color to black
        frame.add(panel);
        placeComponents(panel);

        frame.setResizable(true);

        frame.setVisible(true);
    }

    /**
     * placeComponents sets up the GUI and adds the box you can type in as well as a welcome message.
     * @param panel
     */

    private static void placeComponents(JPanel panel) {

        JTextArea commandOutput = new JTextArea();
        commandOutput.setFont(new Font("Monospaced", Font.PLAIN, 14));
        commandOutput.setEditable(false);
        commandOutput.setBackground(Color.BLACK); // Set background color to black
        commandOutput.setForeground(Color.WHITE); // Set text color to white
        JScrollPane scrollPane = new JScrollPane(commandOutput);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Create a text field to accept user input commands
        JTextField commandInput = new JTextField();
        commandInput.setBackground(Color.BLACK); // Set background color to black
        commandInput.setForeground(Color.WHITE); // Set text color to white
        panel.add(commandInput, BorderLayout.SOUTH);

        //welcome message
        String welcomeMessage = "CSCD 350 UML Editor\n" + "Group: Code Cain\n" +
                "Type 'help' to see available commands.\n\n";
        commandOutput.append(welcomeMessage);

        commandInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputCommand = commandInput.getText();
                if (!inputCommand.trim().isEmpty()) {

                    // Capture the current caret position before appending the command output
                    int helpStartPosition = commandOutput.getDocument().getLength();

                    // Execute the command and append the result to the text area
                    String output = ">> " + inputCommand + "\n" + executeCommand(inputCommand) + "\n";
                    commandOutput.append(output);
                    commandInput.setText("");


                    commandOutput.setCaretPosition(commandOutput.getDocument().getLength());
                    // Check if the entered command is "help"
                    if (inputCommand.equalsIgnoreCase("help")) {
                        // Scroll to the position where the help output starts
                        commandOutput.setCaretPosition(helpStartPosition);
                    } else {
                        // Scroll to the bottom for all other commands
                        commandOutput.setCaretPosition(commandOutput.getDocument().getLength());
                    }
                }
            }
        });
    }

    /**
     * Processes the user input command, parses it, and performs the appropriate operation.
     * This method supports various UML operations such as adding classes, deleting classes,
     * listing classes, and showing help information.
     *
     * @param command the input command string entered by the user
     * @return the result of the command execution as a string to be displayed in the output area
     */
    private static String executeCommand(String command) {
        String[] tokens = command.split(" ");
        if (tokens.length == 0) {
            return "No command entered.";
        }

        String commandName = tokens[0].toLowerCase();

        // Handling different commands by name
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
            default:
                return "Unknown command. Type 'help' to see available commands.";
        }
    }

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
                4. add parameter 'className' 'methodName' 'parameterName' 'parameterType' - Adds a parameter to a method.
                5. delete parameter 'className' 'methodName' 'parameterName' - Removes a parameter from a method.
    
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

    private static String handleAddCommand(String[] tokens) {
        if (tokens.length == 3 && tokens[1].equalsIgnoreCase("class")) {
            return addClass(tokens[2]);
        } else if (tokens.length == 4 && tokens[1].equalsIgnoreCase("relationship")) {
            return addRelationship(tokens[2], tokens[3]);
        } else if (tokens.length == 4 && tokens[1].equalsIgnoreCase("field")) {
            return addField(tokens[2], tokens[3]);
        } else if (tokens.length >= 5 && tokens[1].equalsIgnoreCase("method")) {
            return addMethod(tokens[2], tokens[3], Arrays.copyOfRange(tokens, 4, tokens.length));
        } else {
            return "Invalid command. Use 'help' for available commands.";
        }
    }

    private static String addClass(String className) {
        Class.addClass(className);
        return "Class '" + className + "' added.";
    }

    private static String addRelationship(String class1, String class2) {
        try {
            Relationship.addRelationship(class1, class2);
            return "Relationship between '" + class1 + "' and '" + class2 + "' added.";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private static String addField(String className, String fieldName) {
        Fields fields = new Fields();
        fields.addField(className, fieldName);
        return "Field '" + fieldName + "' added to class '" + className + "'.";
    }

    private static String addMethod(String className, String methodName, String[] parameters) {
        // Join the parameters into a single string
        String parametersString = String.join(",", parameters);
        return addMethod(className, methodName, parametersString);
    }

    private static String addMethod(String className, String methodName, String parametersString) {
        // Split the parameters by commas and trim any whitespace
        String[] parameters = parametersString.split("\\s*,\\s*");

        Methods methods = new Methods();
        methods.addMethod(className, methodName, Arrays.asList(parameters));
        return "Method '" + methodName + "' added to class '" + className + "' with parameters: " + Arrays.toString(parameters) + ".";
    }


    private static String handleDeleteCommand(String[] tokens) {
        if (tokens.length == 3 && tokens[1].equalsIgnoreCase("class")) {
            return deleteClass(tokens[2]);
        } else if (tokens.length == 4 && tokens[1].equalsIgnoreCase("relationship")) {
            return deleteRelationship(tokens[2], tokens[3]);
        } else if (tokens.length == 4 && tokens[1].equalsIgnoreCase("field")) {
            return deleteField(tokens[2], tokens[3]);
        } else if (tokens.length == 4 && tokens[1].equalsIgnoreCase("method")) {
            return deleteMethod(tokens[2], tokens[3]);
        } else {
            return "Invalid command. Use 'help' for available commands.";
        }
    }

    private static String deleteClass(String className) {
        Class.removeClass(className);
        Relationship.removeAttachedRelationships(className); // Remove any relationships involving this class
        return "Class '" + className + "' and its relationships deleted.";
    }

    private static String deleteRelationship(String class1, String class2) {
        try {
            Relationship.removeRelationship(class1, class2);
            return "Relationship between '" + class1 + "' and '" + class2 + "' deleted.";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private static String deleteField(String className, String fieldName) {
        Fields fields = new Fields();
        fields.removeField(className, fieldName);
        return "Field '" + fieldName + "' removed from class '" + className + "'.";
    }

    private static String deleteMethod(String className, String methodName) {
        Methods methods = new Methods();
        methods.removeMethod(className, methodName);
        return "Method '" + methodName + "' removed from class '" + className + "'.";
    }

    private static String handleRenameCommand(String[] tokens) {
        if (tokens.length == 4 && tokens[1].equalsIgnoreCase("class")) {
            return renameClass(tokens[2], tokens[3]);
        } else if (tokens.length == 5 && tokens[1].equalsIgnoreCase("field")) {
            return renameField(tokens[2], tokens[3], tokens[4]);
        } else if (tokens.length == 5 && tokens[1].equalsIgnoreCase("method")) {
            return renameMethod(tokens[2], tokens[3], tokens[4]);
        } else {
            return "Invalid command. Use 'help' for available commands.";
        }
    }

    private static String renameClass(String oldName, String newName) {
        Class.renameClass(oldName, newName);
        return "Class '" + oldName + "' renamed to '" + newName + "'.";
    }

    private static String renameField(String className, String oldFieldName, String newFieldName) {
        Fields fields = new Fields();
        fields.renameField(className, oldFieldName, newFieldName);
        return "Field '" + oldFieldName + "' renamed to '" + newFieldName + "' in class '" + className + "'.";
    }

    private static String renameMethod(String className, String oldMethodName, String newMethodName) {
        Methods methods = new Methods();
        methods.renameMethod(className, oldMethodName, newMethodName);
        return "Method '" + oldMethodName + "' renamed to '" + newMethodName + "' in class '" + className + "'.";
    }

    private static String handleListCommand(String[] tokens) {
        if (tokens.length == 2 && tokens[1].equalsIgnoreCase("classes")) {
            return listClasses();
        } else if (tokens.length == 2 && tokens[1].equalsIgnoreCase("relationships")) {
            return listRelationships();
        } else {
            return "Invalid command. Use: list classes.";
        }
    }

    private static String listClasses() {
        if (Class.classMap.isEmpty()) {
            return "No classes available.";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Classes:\n");
            for (String className : Class.classMap.keySet()) {
                sb.append("- ").append(className).append("\n");
            }
            return sb.toString();
        }
    }

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
