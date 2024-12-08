package codecain.GraphicalUserInterface.View;

import codecain.CommandLineInterface.View.CLI;
import codecain.GraphicalUserInterface.Controller.RelationshipLines.GridManager;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


/**
 * The MenuGUI class serves as the main menu for the Code Cain UML Editor.
 * It provides options to launch the Command Line Interface (CLI) or the
 * Graphical User Interface (GUI) for managing UML diagrams.
 */
public class MenuGUI extends Application {

    /**
     * Initializes and displays the main menu window with options to launch the CLI or GUI.
     *
     * @param primaryStage The primary stage for this application.
     */
    @Override
    public void start(Stage primaryStage) {
        Text welcomeText = new Text("Welcome to Code Cain UML Editor");
        welcomeText.setFont(Font.font("Arial", 20));

        Button cliButton = new Button("CLI");
        cliButton.setOnAction(event -> {
            launchCLI(primaryStage); 
        });

        Button guiButton = new Button("Graphical Interface");
        guiButton.setOnAction(event -> {
            launchGraphicalInterface(primaryStage);
        });
        VBox layout = new VBox(20, welcomeText, cliButton, guiButton);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setTitle("UML Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Launches the Command Line Interface (CLI) in a new stage.
     * The main menu is hidden while the CLI window is active and shown again
     * when the CLI window is closed.
     *
     * @param parentStage The main menu stage to hide while the CLI is active.
     */
    public static void launchCLI(Stage parentStage) {
        try {
            Stage cliStage = new Stage();
            CLI cli = new CLI();
            cli.start(cliStage);
            if (parentStage != null) parentStage.hide(); // Hide the main menu

            cliStage.setOnCloseRequest(event1 -> {
                if (parentStage != null) parentStage.show();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Launches the Graphical User Interface (GUI) in a new stage.
     * The main menu is hidden while the GUI window is active and shown again
     * when the GUI window is closed.
     *
     * @param parentStage The main menu stage to hide while the GUI is active.
     */
    public static void launchGraphicalInterface(Stage parentStage) {
        try {
            Stage guiStage = new Stage();
            GraphicalInterfaceJavaFX gui = new GraphicalInterfaceJavaFX();
            gui.start(guiStage);
            if (parentStage != null) parentStage.hide();

            guiStage.setOnCloseRequest(event1 -> {
                GridManager.getInstance().resetGrid(); // Reset the grid when GUI is closed
                if (parentStage != null) parentStage.show();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Hides the specified stage.
     * 
     * @param stage The stage to hide.
     */
    public static void hideStage(Stage stage) {
        if (stage != null) {
            stage.hide();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
