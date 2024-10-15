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
        JScrollPane scrollPane = new JScrollPane(commandOutput);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Create a text field to accept user input commands
        JTextField commandInput = new JTextField();
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

                    String output = ">> " + inputCommand + "\n" + executeCommand(inputCommand) + "\n";
                    commandOutput.append(output);
                    commandInput.setText("");


                    commandOutput.setCaretPosition(commandOutput.getDocument().getLength());
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

            default:
                return "Unknown command. Type 'help' to see available commands.";
        }
    }
}
