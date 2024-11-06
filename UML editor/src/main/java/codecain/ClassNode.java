package codecain;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class ClassNode extends VBox {

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


        this.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 5;");
        this.setPrefSize(200, 300);
        this.getChildren().addAll(this.className, this.fields, this.methods);

        setEditableCellFactory(this.fields);

        this.setOnMousePressed(this::onMousePressed);
        this.setOnMouseDragged(this::onMouseDragged);
    }

    private void onMousePressed(MouseEvent event) {
        mouseXOffset = event.getSceneX() - this.getLayoutX();
        mouseYOffset = event.getSceneY() - this.getLayoutY();
    }

    private void onMouseDragged(MouseEvent event) {
        this.setLayoutX(event.getSceneX() - mouseXOffset);
        this.setLayoutY(event.getSceneY() - mouseYOffset);

    }

    // Helper method to set editable cell factory
    private void setEditableCellFactory(ListView<String> listView) {
        listView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<String> call(ListView<String> listView) {
                return new ListCell<>() {
                    private final TextField textField = new TextField();

                    {
                        // Set up double-click to start editing
                        setOnMouseClicked(event -> {
                            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                                startEdit();
                            }
                        });

                        // Commit edit on ENTER key press
                        textField.setOnAction(event -> commitEdit(textField.getText()));

                        // Cancel edit on losing focus
                        textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                            if (!isNowFocused) {
                                cancelEdit();
                            }
                        });
                    }

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                            setGraphic(null);
                        } else if (isEditing()) {
                            textField.setText(item);
                            setText(null);
                            setGraphic(textField);
                        } else {
                            setText(item);
                            setGraphic(null);
                        }
                    }

                    @Override
                    public void startEdit() {
                        super.startEdit();
                        if (getItem() != null) {
                            textField.setText(getItem());
                            setGraphic(textField);
                            textField.requestFocus();
                            textField.selectAll();
                        }
                    }

                    @Override
                    public void cancelEdit() {
                        super.cancelEdit();
                        setText(getItem());
                        setGraphic(null);
                    }

                    @Override
                    public void commitEdit(String newValue) {
                        super.commitEdit(newValue);
                        // Update the list item with the new value
                        listView.getItems().set(getIndex(), newValue);
                    }
                };
            }
        });
    }
}