package codecain.GraphicalUserInterface;

import javafx.scene.control.Alert;

/**
 * A utility class for displaying alert dialogs in the JavaFX application.
 * This class provides a reusable method to create and show different types of alerts
 * with customizable titles, headers, and content.
 */
public class AlertHelper {

    /**
     * Displays an alert dialog with the specified properties.
     *
     * @param alertType The type of alert to display (e.g., INFORMATION, WARNING, ERROR, CONFIRMATION).
     * @param title     The title of the alert dialog.
     * @param header    The header text for the alert dialog (can be null for no header).
     * @param content   The content text for the alert dialog.
     */
    public static void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
