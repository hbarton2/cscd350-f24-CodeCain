package codecain.CommandLineInterface;

import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

/**
 * View component for the UML Editor Command Line Interface.
 * Responsible for displaying command output, accepting command input, and providing autocomplete suggestions.
 * Acts as the interface between the user and the CLIController.
 */
public class CLIView {

    /**
     * TextArea for displaying command output to the user.
     */
    private final TextArea commandOutput;

    /**
     * TextField for accepting command input from the user.
     */
    private final TextField commandInput;

    /**
     * Controller associated with this view, which handles command processing and logic.
     */
    private CLIController controller;

    /**
     * Stores the current autocomplete suggestions for the command input.
     */
    private List<String> currentSuggestions;

    /**
     * Tracks the current index of autocomplete suggestions, allowing for cycling through suggestions.
     */
    private int suggestionIndex = -1;

    /**
     * Constructs the CLIView and sets up the GUI components.
     * Initializes the layout, command input, and output areas, and displays the main stage.
     *
     * @param primaryStage the main stage of the JavaFX application.
     */
    public CLIView(Stage primaryStage) {
        primaryStage.setTitle("UML Editor Command Line");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #000000;");

        commandOutput = createCommandOutput();
        root.setCenter(commandOutput);

        commandInput = createCommandInput();
        VBox inputContainer = new VBox(commandInput);
        inputContainer.setStyle("-fx-background-color: #000000;");
        root.setBottom(inputContainer);

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Creates and configures the TextArea for displaying command output.
     * Sets the font, size, background color, and prevents focus transfer to this component with the Tab key.
     *
     * @return a configured TextArea for command output.
     */
    private TextArea createCommandOutput() {
        TextArea output = new TextArea();
        output.setStyle("-fx-font-family: 'Monospaced'; -fx-font-size: 14; -fx-control-inner-background: #000000; -fx-text-fill: white;");
        output.setEditable(false);
        output.setFocusTraversable(false);
        return output;
    }

    /**
     * Creates and configures the TextField for accepting command input.
     * Sets up event listeners to handle autocomplete and command submission, allowing interaction through Tab and Enter keys.
     *
     * @return a configured TextField for command input.
     */
    private TextField createCommandInput() {
        TextField input = new TextField();
        input.setStyle("-fx-control-inner-background: #000000; -fx-text-fill: white;");
        input.setPromptText("Enter command...");

        input.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case TAB -> handleAutocomplete();
                case ENTER -> {
                    controller.handleCommandInput();
                    resetAutocomplete();
                }
                default -> resetAutocomplete();
            }
        });

        return input;
    }

    /**
     * Handles autocomplete functionality by fetching suggestions based on the current input and cycling through them.
     * Uses the Tab key to iterate through available suggestions.
     */
    private void handleAutocomplete() {
        String currentText = commandInput.getText();

        if (currentSuggestions == null || suggestionIndex == -1) {
            currentSuggestions = controller.getAutocompleteSuggestions(currentText);
            suggestionIndex = 0;
        }

        if (!currentSuggestions.isEmpty()) {
            String suggestion = currentSuggestions.get(suggestionIndex);
            commandInput.setText(suggestion);
            commandInput.positionCaret(suggestion.length());
            suggestionIndex = (suggestionIndex + 1) % currentSuggestions.size();
        }
    }

    /**
     * Resets autocomplete suggestions and the suggestion index to allow for new suggestions on subsequent input.
     */
    private void resetAutocomplete() {
        currentSuggestions = null;
        suggestionIndex = -1;
    }

    /**
     * Gets the TextArea used for command output.
     *
     * @return the command output TextArea.
     */
    public TextArea getCommandOutput() {
        return commandOutput;
    }

    /**
     * Gets the current text in the command input field.
     *
     * @return the text in the command input field.
     */
    public String getCommandInputText() {
        return commandInput.getText();
    }

    /**
     * Clears the text in the command input field.
     */
    public void clearCommandInput() {
        commandInput.clear();
    }

    /**
     * Sets the controller for this view, establishing a connection between the view and the controller.
     *
     * @param controller the CLIController that will handle logic for this view.
     */
    public void setController(CLIController controller) {
        this.controller = controller;
    }
}
