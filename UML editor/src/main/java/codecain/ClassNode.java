package codecain;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;

public class ClassNode extends VBox {

    private boolean isSelected = false;

    // Create a shadow effect for the selected state
    private final DropShadow shadowEffect = new DropShadow();

    private double mouseXOffset = 0;
    private double mouseYOffset = 0;

    private Label className;
    private final ListView<String> fields;
    private final ListView<String> methods;

    public ClassNode(String className) {
        this.className = new Label(className);
        this.fields = new ListView<>();
        this.methods = new ListView<>();

        // Add dummy data to fields
        this.fields.getItems().addAll("int x", "String name", "double salary", "int y", "in age", "int hours", "String fullName");
        // Add dummy data to methods
        this.methods.getItems().addAll("void setName(String name)", "int getX()", "double getSalary()");


        // Configure class name style
        this.className.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 5 0 5 0;");
        this.className.setAlignment(Pos.CENTER);

        this.setAlignment(Pos.TOP_CENTER);

        this.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 4;");
        this.setPrefSize(200, 300);
        this.getChildren().addAll(this.className, this.fields, this.methods);

        // Configure the shadow effect
        shadowEffect.setRadius(10);
        shadowEffect.setOffsetX(5);
        shadowEffect.setOffsetY(5);
        shadowEffect.setColor(Color.rgb(0, 0, 0, 0.2));  // Light gray shadow

        setEditableCellFactory(this.fields);
        setEditableCellFactory(this.methods);

        // Draggable
        this.setOnMousePressed(this::onMousePressed);
        this.setOnMouseDragged(this::onMouseDragged);


    }

    public void select() {
        isSelected = true;

        // Apply a slight color change and shadow effect
        this.setStyle("-fx-background-color: #e0f7fa; -fx-border-color: #02769e; -fx-border-width: 4;");
        this.setEffect(shadowEffect);
    }

    public void deselect() {
        isSelected = false;
        // Revert to the default background and remove shadow
        this.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 3;");
        this.setEffect(null);
    }

    // Toggle selection on mouse click
    public void toggleSelection(MouseEvent event) {
        System.out.println("toggleSelection called");
        isSelected = !isSelected;
        if (isSelected) {
            this.setStyle("-fx-background-color: #e0f7fa; -fx-border-color: #02769e; -fx-border-width: 4;");
        } else {
            this.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 3;");
        }
    }

    public boolean isSelected() {
        return isSelected;
        // Apply a slight color change and shadow effect
    }

    private void onMousePressed(MouseEvent event) {
        mouseXOffset = event.getSceneX() - this.getLayoutX();
        mouseYOffset = event.getSceneY() - this.getLayoutY();
    }

    private void onMouseDragged(MouseEvent event) {
        this.setLayoutX(event.getSceneX() - mouseXOffset);
        this.setLayoutY(event.getSceneY() - mouseYOffset);
    }

    // Set TextFieldListCell for editable ListView
    private void setEditableCellFactory(ListView<String> listView) {
        listView.setEditable(true);
        listView.setCellFactory(TextFieldListCell.forListView());
    }


}
