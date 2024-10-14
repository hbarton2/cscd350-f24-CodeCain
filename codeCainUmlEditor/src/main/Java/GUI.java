import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Custom Command Prompt");
        frame.setSize(600, 400);  // Set window size
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {

        panel.setLayout(null);

        // Create a non-editable text area to display "command-line" output
        JTextArea commandOutput = new JTextArea();
        commandOutput.setBounds(10, 10, 560, 300);
        commandOutput.setFont(new Font("Monospaced", Font.PLAIN, 14));
        commandOutput.setEditable(false);  // This acts as the "console"
        JScrollPane scrollPane = new JScrollPane(commandOutput);
        scrollPane.setBounds(10, 10, 560, 300);
        panel.add(scrollPane);

        JTextField commandInput = new JTextField(50);
        commandInput.setBounds(10, 320, 450, 30);
        panel.add(commandInput);

        // Create a "Run" button to simulate the "Enter" key
        JButton runButton = new JButton("Run");
        runButton.setBounds(470, 320, 100, 30);
        panel.add(runButton);

        // Define what happens when the "Run" button is clicked or the user presses Enter
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputCommand = commandInput.getText();
                if (!inputCommand.trim().isEmpty()) {
                    String output = ">> " + inputCommand + "\n" + executeCommand(inputCommand) + "\n";
                    commandOutput.append(output);  // Append output to the "command-line"
                    commandInput.setText("");  // Clear input field
                }
            }
        });

        // Support pressing "Enter" to submit commands
        commandInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runButton.doClick();  // Simulate clicking the "Run" button
            }
        });
    }

    // Method to simulate command execution
    private static String executeCommand(String command) {
        // In this method, you can define your custom logic to handle different commands
        switch (command.toLowerCase()) {
            case "help":
                return "Available commands: help, greet, exit";
            case "greet":
                return "Hello! This is your custom command prompt!";
            case "exit":
                return "Exiting... (but this is a simulation, so it won't really close)";
            default:
                return "Unknown command: " + command;
        }
    }

}
