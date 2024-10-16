import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class represents a simple GUI-based UML editor command-line tool.
 * It allows the user to perform class operations (add, delete, rename) and manage relationships via a text command input.
 */
public class GUI {

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

    private static void placeComponents(JPanel panel) {
        JTextArea commandOutput = new JTextArea();
        commandOutput.setFont(new Font("Monospaced", Font.PLAIN, 14));
        commandOutput.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(commandOutput);
        panel.add(scrollPane, BorderLayout.CENTER);

        JTextField commandInput = new JTextField();
        panel.add(commandInput, BorderLayout.SOUTH);

        String welcomeMessage = """
                CSCD 350 UML Editor
                Group: Code Cain
                Type 'help' to see available commands.\n\n
                """;
        commandOutput.append(welcomeMessage);

        // Listener for handling input commands
        commandInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputCommand = commandInput.getText();
                if (!inputCommand.trim().isEmpty()) {
                    String output = ">> " + inputCommand + "\n" + executeCommand(inputCommand) + "\n";
                    commandOutput.append(output);
                    commandInput.setText("");
                    commandOutput.setCaretPosition(commandOutput.getDocument().getLength());
                }
            }
        });
    }

    private static String executeCommand(String command) {
        String[] tokens = command.split(" ");
        if (tokens.length == 0) {
            return "No command entered.";
        }

        String commandName = tokens[0].toLowerCase();

        switch (commandName) {
            case "help":
                return """
                        Available commands:

                        Class Operations:
                        1. add class 'name'                  - Adds a new class with a unique name.
                        2. delete class 'name'               - Deletes the class with the specified name.
                        3. rename class 'oldName' 'newName'  - Renames the class from 'oldName' to 'newName'.

                        Relationship Operations:
                        1. add relationship 'class1' 'class2'   - Adds a relationship between 'class1' and 'class2'.
                        2. delete relationship 'class1' 'class2' - Deletes the relationship between 'class1' and 'class2'.
                        3. list relationships                  - Lists all the relationships between classes.

                        Field Operations:
                        1. add field 'className' 'fieldName'  - Adds a unique field to the specified class.
                        2. remove field 'className' 'fieldName' - Removes a field from the specified class.

                        Save/Load Operations:
                        1. save                                - Saves the current state of the project.
                        2. load                                - Loads the project state from a file.

                        Listing Operations:
                        1. list classes                        - Lists all the classes in the project.
                        2. list class 'className'              - Lists the contents (fields and methods) of the specified class.
                        3. list relationships                  - Lists all relationships.

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
                return "Invalid command. Use: add class 'name' or add relationship 'class1' 'class2'.";

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
                return "Invalid command. Use: list classes or list relationships.";

            default:
                return "Unknown command. Type 'help' to see available commands.";
        }
    }
}
