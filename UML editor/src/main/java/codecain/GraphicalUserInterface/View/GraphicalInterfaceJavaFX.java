package codecain.GraphicalUserInterface.View;

import codecain.GraphicalUserInterface.Controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
            Controller controller = loader.getController();
            controller.populateGUIFromClassMap();
            stage.setTitle("UML Editor");
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
