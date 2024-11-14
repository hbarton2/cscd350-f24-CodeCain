package codecain.GraphicalUserInterface;

import codecain.BackendCode.*;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class ClassNode extends VBox {

    private boolean isSelected = false;

    // Create a shadow effect for the selected state
    private final DropShadow shadowEffect = new DropShadow();

    private double mouseXOffset = 0;
    private double mouseYOffset = 0;

    private Label classNameLabel;
    private TextField classNameField;
    final ListView<UMLFieldInfo> fields;
    final ListView<UMLMethodInfo> methods;

    private UMLClassInfo classInfo;

    public ClassNode(UMLClassInfo classInfo) {
        this.classInfo = classInfo;
        this.classNameLabel = new Label(classInfo.getClassName().toString());
        this.classNameField = new TextField(classInfo.getClassName().toString());
        this.fields = new ListView<>();
        this.methods = new ListView<>();

        configureClassName();
        initializeFieldsAndMethods();

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
        setEditableCellFactoryForMethods(this.methods);

        this.setLayoutX(classInfo.getX());
        this.setLayoutY(classInfo.getY());

        // Draggable
        this.setOnMousePressed(this::onMousePressed);
        this.setOnMouseDragged(this::onMouseDragged);
        this.classNameLabel.setOnMouseClicked(this::onLabelDoubleClick);


    }

    // Load fields and methods from UMLClassInfo
    private void initializeFieldsAndMethods() {
        classInfo.getFields().forEach(field -> fields.getItems().add(field));
        classInfo.getMethods().forEach(method -> methods.getItems().add(method));
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
        String newName = classNameField.getText().toLowerCase().trim();

        // Check if the new name is already in use
        if (UMLClass.exists(newName)) {
            alert("Error", "Class name already exists. Choose a different name.");
            return;
        }

        // Attempt renaming in UMLClass map
        String oldName = classInfo.getClassName().toString();
        UMLClass.renameClass(oldName, newName);

        // Reflect the change in ClassNode and UMLClassInfo
        classInfo.setClassName(newName);
        classNameLabel.setText(newName);
        cancelClassNameEdit();
//        String newName = classNameField.getText().trim();
//        Result result = Storage.renameClass(newName);
//        if (result.getStatus() == Status.ERROR || result.getStatus() == Status.WARNING) {
//            alert("WARNING", result.getMessage());
//        } else {
//            classNameLabel.setText(newName);
//            cancelClassNameEdit();
//        }

    }

    private void cancelClassNameEdit() {
        classNameField.setVisible(false);
        this.getChildren().set(0, classNameLabel);  // Replace TextField with label
        classNameLabel.setVisible(true);
    }

    public void syncWithUMLClassInfo() {
        classInfo.getFields().clear();
        fields.getItems().forEach(fieldInfo -> classInfo.getFields().add(fieldInfo));

        classInfo.getMethods().clear();
        methods.getItems().forEach(methodInfo -> classInfo.getMethods().add(methodInfo));

        classInfo.setX((int) this.getLayoutX());
        classInfo.setY((int) this.getLayoutY());
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
        double newX = event.getSceneX() - mouseXOffset;
        double newY = event.getSceneY() - mouseYOffset;

        // Get pane's width and height from the parent (nodeContainer should be the parent)
        Pane parentPane = (Pane) this.getParent();
        double maxX = parentPane.getWidth() - this.getPrefWidth();
        double maxY = parentPane.getHeight() - this.getPrefHeight();

        // Restrict movement within pane boundaries
        if (newX >= 0 && newX <= maxX) {
            this.setLayoutX(newX);
        }
        if (newY >= 0 && newY <= maxY) {
            this.setLayoutY(newY);
        }

        syncWithUMLClassInfo();
    }

    // Set TextFieldListCell for editable ListView
    private void setEditableCellFactory(ListView<UMLFieldInfo> listView) {
        listView.setEditable(true);
        listView.setCellFactory(TextFieldListCell.forListView(new javafx.util.StringConverter<UMLFieldInfo>() {
            @Override
            public String toString(UMLFieldInfo fieldInfo) {
                return fieldInfo.toString();
            }

            @Override
            public UMLFieldInfo fromString(String string) {
                String[] parts = string.split(" ");
                if (parts.length == 2) {
                    return new UMLFieldInfo(parts[0], parts[1]);
                }
                return null;
            }
        }));
    }

    private void setEditableCellFactoryForMethods(ListView<UMLMethodInfo> listView) {
        listView.setEditable(true);
        listView.setCellFactory(TextFieldListCell.forListView(new javafx.util.StringConverter<UMLMethodInfo>() {
            @Override
            public String toString(UMLMethodInfo methodInfo) {
                return methodInfo.toString();
            }

            @Override
            public UMLMethodInfo fromString(String string) {
                // Parse the method name and parameters
                String[] parts = string.split("\\(");
                if (parts.length > 0) {
                    String methodName = parts[0].trim();
                    return new UMLMethodInfo(methodName, new ArrayList<>()); // Replace with parameter parsing if needed
                }
                return null;
            }
        }));
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

    public void setName(String name) {
        classNameLabel.setText(name);
    }

    public void addField(UMLFieldInfo fieldInfo) {
        fields.getItems().add(fieldInfo);
    }

    public boolean removeField(String fieldName) {
        // Check if the field exists in the GUI ListView
        boolean removedFromListView = fields.getItems().removeIf(field -> field.getFieldName().equals(fieldName));

        if (removedFromListView) {
            // If removed from ListView, also remove it from the backend classInfo
            boolean removedFromBackend = classInfo.getFields().removeIf(field -> field.getFieldName().equals(fieldName));

            if (removedFromBackend) {
                System.out.println("Field '" + fieldName + "' successfully removed from both GUI and backend.");
            } else {
                System.err.println("Field '" + fieldName + "' was removed from GUI but not found in the backend.");
            }

            return true; // Field removed successfully
        }

        System.err.println("Field '" + fieldName + "' not found in the GUI or backend.");
        return false; // Field not found
    }


    public boolean renameField(String oldFieldName, String newFieldType, String newFieldName) {
        UMLFields fieldManager = new UMLFields();
        String className = classInfo.getClassName();

        // Use the UMLFields.renameField method to update the backend
        fieldManager.renameField(className, oldFieldName, newFieldType, newFieldName);

        // Check if the field exists in the backend with the new name and type
        UMLFieldInfo renamedField = classInfo.getFields().stream()
                .filter(field -> field.getFieldName().equals(newFieldName) && field.getFieldType().equals(newFieldType))
                .findFirst()
                .orElse(null);

        if (renamedField != null) {
            // Update the ListView (fields) to reflect the renamed field
            fields.getItems().clear(); // Clear all items in the ListView
            fields.getItems().addAll(classInfo.getFields()); // Reload fields from the backend

            fields.refresh(); // Refresh the ListView to reflect the changes
            return true; // Rename successful
        }

        return false; // Rename failed
    }





    public void addMethod(UMLMethodInfo method) {
        methods.getItems().add(method); // Add the method to the ListView in ClassNode
    }
    public boolean removeMethod(String methodName) {
        // Use update-like logic to handle removal
        boolean removed = methods.getItems().removeIf(m -> m.getMethodName().equals(methodName));
        if (removed) {
            classInfo.getMethods().removeIf(m -> m.getMethodName().equals(methodName)); // Sync with backend
        }
        return removed;
    }

    public boolean renameMethod(String oldMethodName, String newMethodName) {
        // Find the method to rename in ListView
        UMLMethodInfo methodToRename = methods.getItems().stream()
                .filter(method -> method.getMethodName().equals(oldMethodName))
                .findFirst()
                .orElse(null);

        if (methodToRename == null) {
            System.out.println("Error: Method '" + oldMethodName + "' not found in GUI.");
            return false; // Method not found
        }

        // Check for duplicate method names
        boolean duplicate = methods.getItems().stream()
                .anyMatch(method -> method.getMethodName().equals(newMethodName));
        if (duplicate) {
            System.out.println("Error: Method '" + newMethodName + "' already exists in GUI.");
            return false; // Prevent duplicate names
        }

        // Update the method name in GUI
        methodToRename.setMethodName(newMethodName);
        methods.refresh(); // Refresh the ListView

        return true; // Renamed successfully
    }





    public void updateMethod(UMLMethodInfo method) {
        methods.getItems().removeIf(m -> m.getMethodName().equals(method.getMethodName())); // Remove old method
        methods.getItems().add(method); // Add updated method with new parameter
    }
}
