package codecain.GraphicalUserInterface.View;

import codecain.GraphicalUserInterface.Controller.Controller;
import codecain.GraphicalUserInterface.Model.ShortcutManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.Objects;

public class GraphicalInterfaceJavaFX extends Application {

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/codecain/GraphicalInterface.fxml"));
            String css = Objects.requireNonNull(this.getClass().getResource("/codecain/application.css")).toExternalForm();
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(css);

            // Set up the controller
            Controller controller = loader.getController();
            controller.populateGUIFromClassMap();

            // Initialize shortcuts
            ShortcutManager shortcutManager = new ShortcutManager(scene, controller);
            shortcutManager.initializeShortcuts();

            // Set up the stage
            stage.setTitle("UML Editor");

            Screen screen = Screen.getPrimary();
            stage.setWidth(screen.getBounds().getWidth());
            stage.setHeight(screen.getBounds().getHeight());
            stage.centerOnScreen();

            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
