package codecain.CommandLineInterface;

import codecain.GraphicalUserInterface.GUIClassManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Command Line Interface (CLI) for the UML Editor application.
 * Provides a command-driven interface for interacting with the UML model.
 */
public class CLI extends JFrame {

    /** Text area to display command outputs and logs. */
    private JTextArea commandOutput;

    /** Text field for user input commands. */
    private JTextField commandInput;

    /** Label for displaying autocomplete suggestions. */
    private JLabel suggestionLabel;

    /** The CommandManager instance responsible for parsing and executing commands. */
    private CommandManager commandManager;

    /** The GUIClassManager instance for managing the graphical representation of classes. */
    private GUIClassManager classManager;

    /** List of available commands for the autocomplete feature. */
    private static final List<String> COMMANDS = List.of(
            "add", "delete", "rename", "list", "help", "exit",
            "add class", "add field", "add method", "add parameter", "add relationship",
            "delete class", "delete field", "delete method", "delete parameter", "delete relationship",
            "rename class", "rename field", "rename method", "rename parameter", "rename all_parameters",
            "list classes", "list relationships"
    );


    /**
     * Holds the list of current autocomplete suggestions based on the user's input.
     * Updated each time the input text changes.
     * This list is used to provide matching commands for the autocomplete feature.
     */
    private List<String> currentSuggestions = new ArrayList<>();


    /**
     * Index of the currently highlighted suggestion in the {@code currentSuggestions} list.
     * Used to track which suggestion is displayed in the preview or selected for autocompletion.
     * A value of -1 indicates that there are no valid suggestions.
     */
    private int suggestionIndex = -1;

    /**
     * Constructs the CLI, initializing components and setting up the UI.
     */
    public CLI() {
        setTitle("UML Editor Command Line");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.BLACK);
        add(panel);

        commandOutput = new JTextArea();
        commandOutput.setFont(new Font("Monospaced", Font.PLAIN, 14));
        commandOutput.setEditable(false);
        commandOutput.setBackground(Color.BLACK);
        commandOutput.setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(commandOutput);
        panel.add(scrollPane, BorderLayout.CENTER);

        commandInput = new JTextField();
        commandInput.setBackground(Color.BLACK);
        commandInput.setForeground(Color.WHITE);

        suggestionLabel = new JLabel();
        suggestionLabel.setForeground(Color.GRAY);
        suggestionLabel.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(Color.BLACK);
        inputPanel.add(commandInput, BorderLayout.CENTER);
        inputPanel.add(suggestionLabel, BorderLayout.NORTH);

        panel.add(inputPanel, BorderLayout.SOUTH);

        JPanel canvas = new JPanel();
        classManager = new GUIClassManager(canvas);
        commandManager = new CommandManager(commandOutput, classManager);

        setupCommandInputListener();
        setupGlobalKeyDispatcher();

        String welcomeMessage = """
        Welcome to the CSCD 350 UML Editor!
        
        Developed by: Code Cain
        
        Type 'help' to view available commands and get started.
        
        Note: To use autocomplete, look to the right of the text box. 
        When you see the word you want to autocomplete, press Tab.
        
        Enjoy designing your UML diagrams!
        """;
        commandOutput.append(welcomeMessage);

        setResizable(true);
        setVisible(true);
    }

    /**
     * Sets up the command input listener to handle commands when the user presses Enter.
     */
    private void setupCommandInputListener() {
        commandInput.addActionListener(e -> {
            String inputCommand = commandInput.getText();
            if (!inputCommand.trim().isEmpty()) {
                commandManager.parseAndExecute(inputCommand);
                commandInput.setText("");
                resetAutocomplete();
            }
        });
        commandInput.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                updateSuggestions(commandInput.getText().trim());
            }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                updateSuggestions(commandInput.getText().trim());
            }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                updateSuggestions(commandInput.getText().trim());
            }
        });
    }

    /**
     * Sets up a global key dispatcher to handle special key events, such as confirming an autocomplete suggestion.
     */
    private void setupGlobalKeyDispatcher() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_TAB) {
                e.consume();
                confirmSuggestion();
                return true;
            }
            return false;
        });
    }

    /**
     * Updates the autocomplete suggestions based on the current text input by the user.
     * @param currentText The current text in the command input field.
     */
    private void updateSuggestions(String currentText) {
        currentSuggestions = getAutocompleteSuggestions(currentText);
        suggestionIndex = currentSuggestions.isEmpty() ? -1 : 0;
        showPreview();
    }

    /**
     * Displays the current suggestion as a preview next to the command input field.
     */
    private void showPreview() {
        if (suggestionIndex != -1 && suggestionIndex < currentSuggestions.size()) {
            String suggestion = currentSuggestions.get(suggestionIndex);
            suggestionLabel.setText(suggestion.substring(commandInput.getText().trim().length()));
            suggestionLabel.setForeground(Color.CYAN);
        } else {
            suggestionLabel.setText("");
        }
    }

    /**
     * Confirms the current autocomplete suggestion by setting it as the command input's text.
     */
    private void confirmSuggestion() {
        if (suggestionIndex != -1 && suggestionIndex < currentSuggestions.size()) {
            String suggestion = currentSuggestions.get(suggestionIndex);
            commandInput.setText(suggestion);
        }
        suggestionLabel.setText("");
    }

    /**
     * Gets a list of autocomplete suggestions based on the current text input.
     * @param currentText The current text in the command input field.
     * @return A list of matching autocomplete suggestions.
     */
    private List<String> getAutocompleteSuggestions(String currentText) {
        List<String> matches = new ArrayList<>();
        for (String command : COMMANDS) {
            if (command.startsWith(currentText)) {
                matches.add(command);
            }
        }
        return matches;
    }

    /**
     * Resets the autocomplete suggestions, clearing any current suggestions and resetting the index.
     */
    private void resetAutocomplete() {
        currentSuggestions.clear();
        suggestionIndex = -1;
        suggestionLabel.setText("");
    }

    /**
     * Entry point of the CLI application.
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(CLI::new);
    }

    /**
     * Gets the CommandManager instance associated with this CLI.
     * Main use is for the CLI Testing Class.
     * @return The CommandManager instance.
     */
    public CommandManager getCommandManager() {
        return commandManager;
    }


}
