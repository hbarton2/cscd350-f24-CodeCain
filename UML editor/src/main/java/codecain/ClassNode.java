package codecain;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ClassNode extends VBox {

    private boolean isSelected = false;

    // Create a shadow effect for the selected state
    private final DropShadow shadowEffect = new DropShadow();

    private double mouseXOffset = 0;
    private double mouseYOffset = 0;

    private Label classNameLabel;
    private TextField classNameField;
    final ListView<String> fields;
    final ListView<String> methods;

    public ClassNode(String className) {
        this.classNameLabel = new Label(className);
        this.classNameField = new TextField(className);
        this.fields = new ListView<>();
        this.methods = new ListView<>();

        configureClassName();

        // Add dummy data to fields
//        this.fields.getItems().addAll("int x", "String name", "double salary", "int y", "in age", "int hours", "String fullName");
        // Add dummy data to methods
//        this.methods.getItems().addAll("void setName(String name)", "int getX()", "double getSalary()");


        // Configure class name style
        this.classNameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 5 0 5 0;");
        this.classNameLabel.setAlignment(Pos.CENTER);

        this.setAlignment(Pos.TOP_CENTER);

        this.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 4;");
        this.setPrefSize(200, 300);
        this.getChildren().addAll(this.classNameLabel, this.fields, this.methods);

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
        this.classNameLabel.setOnMouseClicked(this::onLabelDoubleClick);


    }

    private void configureClassName() {
        // Set up TextField for editing with actions
        classNameField.setVisible(false);  // Initially hide TextField
        classNameField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                saveClassName();
            } else if (event.getCode() == KeyCode.ESCAPE) {
                cancelClassNameEdit();
            }
        });

        classNameField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                cancelClassNameEdit();  // Cancel edit when focus is lost
            }
        });
    }

    private void onLabelDoubleClick(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            startClassNameEdit();
        }
    }

    private void startClassNameEdit() {
        classNameField.setText(classNameLabel.getText());
        classNameLabel.setVisible(false);
        this.getChildren().set(0, classNameField);  // Replace label with TextField
        classNameField.setVisible(true);
        classNameField.requestFocus();
    }

    private void saveClassName() {
        String newName = classNameField.getText().trim();
        Result result = Storage.renameClass(newName);
        if (result.getStatus() == Status.ERROR || result.getStatus() == Status.WARNING) {
            alert("WARNING", result.getMessage());
        } else {
            classNameLabel.setText(newName);
            cancelClassNameEdit();
        }

    }

    private void cancelClassNameEdit() {
        classNameField.setVisible(false);
        this.getChildren().set(0, classNameLabel);  // Replace TextField with label
        classNameLabel.setVisible(true);
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

    private void alert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public String getName() {
        return classNameLabel.getText();
    }

    public ClassNodeDTO toDTO() {
        return new ClassNodeDTO(
                classNameLabel.getText(),
                fields.getItems(),
                methods.getItems()
        );
    }

}
