package codecain;

import codecain.CommandLineInterface.CLI;
import codecain.GraphicalUserInterface.GraphicalInterfaceJavaFX;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MenuGUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Welcome message
        Text welcomeText = new Text("Welcome to Code Cain UML Editor");
        welcomeText.setFont(Font.font("Arial", 20));

        // CLI Button
        Button cliButton = new Button("CLI");
        cliButton.setOnAction(event -> {
            try {
                Stage cliStage = new Stage();
                CLI cli = new CLI();
                cli.start(cliStage); // Launch CLI directly
                primaryStage.hide(); // Hide the main menu

                // Show the main menu again when the CLI stage is closed
                cliStage.setOnCloseRequest(event1 -> primaryStage.show());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // GUI Button
        Button guiButton = new Button("Graphical Interface");
        guiButton.setOnAction(event -> {
            try {
                Stage guiStage = new Stage();
                GraphicalInterfaceJavaFX gui = new GraphicalInterfaceJavaFX();
                gui.start(guiStage); // Launch GUI directly
                primaryStage.hide(); // Hide the main menu

                // Show the main menu again when the GUI stage is closed
                guiStage.setOnCloseRequest(event1 -> primaryStage.show());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Layout setup
        VBox layout = new VBox(20, welcomeText, cliButton, guiButton);
        layout.setAlignment(Pos.CENTER); // Center everything horizontally and vertically

        // Scene setup
        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setTitle("UML Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}