import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {
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
     * Handles the commands
     * @param command
     * @return
     */

    private static String executeCommand(String command) {

        String[] tokens = command.split(" ");
        

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

            // Other cases for the commands
            case "add":
                if (tokens.length > 1) {
                    switch (tokens[1]) {
                        case "class":
                            if (tokens.length == 3) {
                                String className = tokens[2];
                                return "Class '" + className + "' added.";
                            }
                            return "Usage: add class 'name'";
                        case "relationship":
                            if (tokens.length == 4) {
                                String source = tokens[2];
                                String destination = tokens[3];
                                return "Relationship added between '" + source + "' and '" + destination + "'.";
                            }
                            return "Usage: add relationship 'source' 'destination'";
                        case "field":
                            if (tokens.length == 4) {
                                String className = tokens[2];
                                String fieldName = tokens[3];
                                return "Field '" + fieldName + "' added to class '" + className + "'.";
                            }
                            return "Usage: add field 'className' 'fieldName'";
                        case "method":
                            if (tokens.length == 5) {
                                String className = tokens[2];
                                String methodName = tokens[3];
                                String parameters = tokens[4];
                                return "Method '" + methodName + "' with parameters '" + parameters + "' added to class '" + className + "'.";
                            }
                            return "Usage: add method 'className' 'methodName' 'parameters'";
                        case "parameter":
                            if (tokens.length == 5) {
                                String className = tokens[2];
                                String methodName = tokens[3];
                                String parameterName = tokens[4];
                                return "Parameter '" + parameterName + "' added to method '" + methodName + "' in class '" + className + "'.";
                            }
                            return "Usage: add parameter 'className' 'methodName' 'parameterName' 'parameterType'";
                        default:
                            return "Unknown add operation.";
                    }
                }
                return "Usage: add 'type' ...";

            case "delete":
                if (tokens.length > 1) {
                    switch (tokens[1]) {
                        case "class":
                            if (tokens.length == 3) {
                                String className = tokens[2];
                                return "Class '" + className + "' deleted.";
                            }
                            return "Usage: delete class 'name'";
                        case "relationship":
                            if (tokens.length == 4) {
                                String source = tokens[2];
                                String destination = tokens[3];
                                return "Relationship between '" + source + "' and '" + destination + "' deleted.";
                            }
                            return "Usage: delete relationship 'source' 'destination'";
                        case "field":
                            if (tokens.length == 4) {
                                String className = tokens[2];
                                String fieldName = tokens[3];
                                return "Field '" + fieldName + "' removed from class '" + className + "'.";
                            }
                            return "Usage: remove field 'className' 'fieldName'";
                        case "method":
                            if (tokens.length == 4) {
                                String className = tokens[2];
                                String methodName = tokens[3];
                                return "Method '" + methodName + "' removed from class '" + className + "'.";
                            }
                            return "Usage: remove method 'className' 'methodName'";
                        default:
                            return "Unknown delete operation.";
                    }
                }
                return "Usage: delete 'type' ...";

            case "list":
                if (tokens.length == 2) {
                    switch (tokens[1]) {
                        case "classes":
                            return "Listing all classes...";
                        case "relationships":
                            return "Listing all relationships...";
                        default:
                            return "Unknown list operation.";
                    }
                } else if (tokens.length == 3 && tokens[1].equals("class")) {
                    String className = tokens[2];
                    return "Listing contents of class '" + className + "'...";
                }
                return "Usage: list 'classes' | 'relationships' | class 'className'";

            case "save":
                return "Project state saved.";

            case "load":
                return "Project state loaded.";

            case "exit":
                System.exit(0);
                return "Exiting application...";

            default:
                return "Unknown command. Type 'help' to see available commands.";
        }
    }
}
