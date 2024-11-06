package codecain;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Controller {

    @FXML
    private Label helloLabel;

    @FXML
    private void onClickMeButton() {
        helloLabel.setText("Welcome!!!");
    }
}
