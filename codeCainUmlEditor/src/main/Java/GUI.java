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
                    // Shows command execution and display the result
                    String output = ">> " + inputCommand + "\n" + executeCommand(inputCommand) + "\n";
                    commandOutput.append(output);
                    commandInput.setText("");  // Clear input field

                    // Scroll to the bottom of the output
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

        switch (command.toLowerCase()) {
            case "help":
                return "Available commands: help, exit";
            case "exit":
                return "Exiting... (but this is a simulation, so it won't really close)";
            default:
                return "Unknown command: " + command;
        }
    }
}
