package codecain.CommandLineInterface.Controller;

import codecain.CommandLineInterface.View.CLIView;
import codecain.GraphicalUserInterface.Controller.Controller;
import codecain.GraphicalUserInterface.Model.ExportImage;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import codecain.CommandLineInterface.Model.CLIExportImage;
import codecain.CommandLineInterface.Model.CommandManager;


import java.io.File;
import java.util.List;
import java.util.stream.Collectors;



/**
 * Controller for the Command Line Interface of the UML Editor.
 * This class handles user interactions, manages the command input, and provides autocomplete functionality.
 * It connects the CLIView with the CommandManager.
 */
public class CLIController {

    public static AnchorPane nodeContainer;

    public void setNodeContainer(AnchorPane nodeContainer) {
        this.nodeContainer = Controller.getContainer();
    }
    

    /**
     * The view component of the CLI, responsible for displaying output and accepting input from the user.
     */
    private final CLIView view;

    /**
     * Manages the execution of commands entered by the user.
     */
    private final CommandManager commandManager;

    /**
     * A predefined list of available commands in the CLI.
     * These are static commands that the user can execute, and they are used for autocomplete suggestions.
     */
    private final List<String> commands = List.of(
            "add class",
            "delete class",
            "rename class",
            "add field",
            "delete field",
            "rename field",
            "add method",
            "delete method",
            "rename method",
            "add parameter",
            "delete parameter",
            "rename parameter",
            "rename all_parameters",
            "add relationship",
            "delete relationship",
            "list relationships",
            "list classes",
            "export",
            "help"
    );

    /**
     * Constructs a CLIController instance, initializing the view and command manager.
     * Sets up the controller in the view to enable communication between them.
     *
     * @param view the CLIView instance responsible for displaying the CLI interface to the user.
     */
    public CLIController(CLIView view) {
        this.view = view;
        this.commandManager = new CommandManager(view.getCommandOutput());
        view.setController(this);
    }

    /**
     * Initializes the CLI by displaying a welcome message to the user in the command output area.
     */
    public void initialize() {
        String welcomeMessage = """
            Welcome to the CSCD 350 UML Editor!
            
            Developed by: Code Cain
            
            Type 'help' to view available commands and get started.
            
            Enjoy designing your UML diagrams!
            """;
        view.getCommandOutput().appendText(welcomeMessage);
    }

    /**
     * Retrieves a list of autocomplete suggestions based on the user's input.
     * Filters the available commands to return those that start with the current input text.
     *
     * @param input the current text in the command input field.
     * @return a list of command suggestions that start with the input text.
     */
    public List<String> getAutocompleteSuggestions(String input) {
        return commands.stream()
                .filter(command -> command.startsWith(input))
                .collect(Collectors.toList());
    }

    /**
     * Handles the command input when the user submits a command.
     * Passes the command to the CommandManager for execution, clears the input field, and resets autocomplete suggestions.
     */
    public void handleCommandInput() {
        String inputCommand = view.getCommandInputText();
        if (!inputCommand.trim().isEmpty()) {
            commandManager.parseAndExecute(inputCommand);
            view.clearCommandInput();
        }
    }
    public static void exportAsImage(AnchorPane nodeContainer, File file) {
        CLIExportImage.exportImage(nodeContainer, file);
    }
    
}
