package codecain.GraphicalUserInterface;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class DialogUtils {
	/**
	 * Creates a reusable dialog with a grid pane.
	 *
	 * @param title      the title of the dialog
	 * @param headerText the header text of the dialog
	 * @param grid       the grid pane to set as the dialog's content
	 * @return a configured dialog
	 */
	public static Dialog<ButtonType> createDialog(String title, String headerText, GridPane grid) {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle(title);
		dialog.setHeaderText(headerText);
		dialog.getDialogPane().setContent(grid);
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		return dialog;
	}

	/**
	 * Adds a labeled TextField to a grid pane at the specified row.
	 *
	 * @param grid  the grid pane to add the TextField to
	 * @param label the label for the TextField
	 * @param row   the row position in the grid
	 * @return the created TextField
	 */
	public static TextField addTextField(GridPane grid, String label, int row) {
		TextField textField = new TextField();
		textField.setPromptText(label);
		grid.add(new Label(label + ":"), 0, row);
		grid.add(textField, 1, row);
		return textField;
	}
}
