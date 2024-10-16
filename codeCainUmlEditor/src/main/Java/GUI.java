import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        JFrame frame = new JFrame("UML editor Command Line");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        frame.add(panel);
        placeComponents(panel);

        frame.setResizable(true);
        frame.setVisible(true);
    }

    /**
     * Places all the necessary components (command input field, command output area, and scroll pane)
     * inside the provided panel. The output area displays the results of commands executed,
     * and the input field accepts user commands.
     * <p>
     * The welcome message is displayed initially, and the user can enter commands which are then processed
     * and the output is shown in the output area.
     *
     * @param panel the panel to which components are added
     */
    private static void placeComponents(JPanel panel) {
        // Command output area (non-editable, monospace font) with scroll functionality
        JTextArea commandOutput = new JTextArea();
        commandOutput.setFont(new Font("Monospaced", Font.PLAIN, 14));
        commandOutput.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(commandOutput);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Command input field where users can enter their commands
        JTextField commandInput = new JTextField();
        panel.add(commandInput, BorderLayout.SOUTH);

        // Welcome message displayed at the start of the application
        String welcomeMessage = """
                CSCD 350 UML Editor
                Group: Code Cain
                Type 'help' to see available commands.\n\n
                """;
        commandOutput.append(welcomeMessage);

        // Event listener to handle user input commands
        commandInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputCommand = commandInput.getText();
                if (!inputCommand.trim().isEmpty()) {
                    String output = ">> " + inputCommand + "\n" + executeCommand(inputCommand) + "\n";
                    commandOutput.append(output);
                    commandInput.setText("");
                    commandOutput.setCaretPosition(commandOutput.getDocument().getLength()); // Scroll to the bottom
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
                return """
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
                        2. remove field 'className' 'fieldName' - Removes a field from the specified class.
                        3. rename field 'className' 'oldFieldName' 'newFieldName' - Renames a field in the specified class.

                        Method Operations:
                        1. add method 'className' 'methodName' 'parameters' - Adds a unique method to the specified class.
                        2. remove method 'className' 'methodName' - Removes the method from the specified class.
                        3. rename method 'className' 'oldMethodName' 'newMethodName' - Renames a method in the specified class.
                        4. add parameter 'className' 'methodName' 'parameterName' 'parameterType' - Adds a parameter to a method.
                        5. remove parameter 'className' 'methodName' 'parameterName' - Removes a parameter from a method.

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
                        """;

            case "add":
                if (tokens.length == 3 && tokens[1].equalsIgnoreCase("class")) {
                    String className = tokens[2];
                    Class.addClass(className);
                    return "Class '" + className + "' added.";
                } else if (tokens.length == 4 && tokens[1].equalsIgnoreCase("relationship")) {
                    try {
                        String class1 = tokens[2];
                        String class2 = tokens[3];
                        Relationship.addRelationship(class1, class2);
                        return "Relationship between '" + class1 + "' and '" + class2 + "' added.";
                    } catch (Exception e) {
                        return "Error: " + e.getMessage();
                    }
                }
            case "delete":
                if (tokens.length == 3 && tokens[1].equalsIgnoreCase("class")) {
                    String className = tokens[2];
                    Class.removeClass(className);
                    Relationship.removeAttachedRelationships(className); // Remove any relationships involving this class
                    return "Class '" + className + "' and its relationships deleted.";
                } else if (tokens.length == 4 && tokens[1].equalsIgnoreCase("relationship")) {
                    try {
                        String class1 = tokens[2];
                        String class2 = tokens[3];
                        Relationship.removeRelationship(class1, class2);
                        return "Relationship between '" + class1 + "' and '" + class2 + "' deleted.";
                    } catch (Exception e) {
                        return "Error: " + e.getMessage();
                    }
                }
                return "Invalid command. Use: delete class 'name' or delete relationship 'class1' 'class2'.";

            case "rename":
                if (tokens.length == 4 && tokens[1].equalsIgnoreCase("class")) {
                    String oldName = tokens[2];
                    String newName = tokens[3];
                    Class.renameClass(oldName, newName);
                    return "Class '" + oldName + "' renamed to '" + newName + "'.";
                }
                return "Invalid command. Use: rename class 'oldName' 'newName'.";

            case "list":
                if (tokens.length == 2 && tokens[1].equalsIgnoreCase("classes")) {
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
                } else if (tokens.length == 2 && tokens[1].equalsIgnoreCase("relationships")) {
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

                return "Invalid command. Use: list classes.";

            default:
                return "Unknown command. Type 'help' to see available commands.";
        }
    }
}
