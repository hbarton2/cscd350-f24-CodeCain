package codecain.CommandLineInterface.View;

import codecain.CommandLineInterface.Controller.CLIController;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main entry point for the UML Editor Command Line Interface application using JavaFX.
 * This class initializes the CLI and starting the JavaFX application.
 */
public class CLI extends Application {

    /**
     * The controller for this CLI application. Manages interactions between the CLIView and the underlying model.
     */
    private CLIController cliController;

    /**
     * JavaFX lifecycle method that initializes the application stage and sets up the CLIView and CLIController.
     * This method is automatically called by the JavaFX runtime when the application is launched.
     * It creates the CLIView and CLIController instances, linking them to establish MVC relationships.
     *
     * @param primaryStage the main stage for this JavaFX application, provided by the JavaFX runtime.
     */
    @Override
    public void start(Stage primaryStage) {
        CLIView view = new CLIView(primaryStage);
        cliController = new CLIController(view);
        view.setController(cliController);
        cliController.initialize();
    }

    /**
     * Main method that serves as the entry point for the Java application.
     * Calls {@link #launch(String...)} to start the JavaFX application.
     *
     * @param args command-line arguments passed to the application (not used).
     */
    public static void main(String[] args) {
        launch(args);
    }
}
