package codecain.GraphicalUserInterface.View;

import codecain.BackendCode.Model.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Represents a visual node in the UML editor for a single UML class.
 * The ClassNode includes a class name, a list of fields, and a list of methods.
 * It is designed to be editable and draggable, with features for renaming,
 * adding, and removing fields and methods.
 */
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

    /**
     * Constructs a ClassNode instance for a specific UML class.
     *
     * @param classInfo The {@link UMLClassInfo} containing the data for the class.
     */
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
        shadowEffect.setColor(Color.rgb(0, 0, 0, 0.2)); // Light gray shadow

        setEditableCellFactory(this.fields);
        setEditableCellFactoryForMethods(this.methods);

        this.setLayoutX(classInfo.getX());
        this.setLayoutY(classInfo.getY());

        // Draggable
        this.setOnMousePressed(this::onMousePressed);
        this.setOnMouseDragged(this::onMouseDragged);
        this.classNameLabel.setOnMouseClicked(this::onLabelDoubleClick);

    }

    /**
     * Initializes the fields and methods from the underlying {@link UMLClassInfo}.
     */
    private void initializeFieldsAndMethods() {
        classInfo.getFields().forEach(field -> fields.getItems().add(field));
        classInfo.getMethods().forEach(method -> methods.getItems().add(method));
    }

    /**
     * Configures the class name label and TextField for editing functionality.
     */
    private void configureClassName() {
        // Set up TextField for editing with actions
        classNameField.setVisible(false); // Initially hide TextField
        classNameField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                saveClassName();
            } else if (event.getCode() == KeyCode.ESCAPE) {
                cancelClassNameEdit();
            }
        });

        classNameField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                cancelClassNameEdit(); // Cancel edit when focus is lost
            }
        });
    }

    /**
     * Handles double-clicking the class name label to start editing.
     *
     * @param event The mouse event that triggered the action.
     */
    private void onLabelDoubleClick(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            startClassNameEdit();
        }
    }

    /**
     * Starts editing the class name by displaying a TextField.
     */
    private void startClassNameEdit() {
        classNameField.setText(classNameLabel.getText());
        classNameLabel.setVisible(false);
        this.getChildren().set(0, classNameField); // Replace label with TextField
        classNameField.setVisible(true);
        classNameField.requestFocus();
    }

    /**
     * Saves the edited class name and updates the underlying {@link UMLClassInfo}.
     */
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

    }

    /**
     * Cancels the class name editing and restores the label.
     */
    private void cancelClassNameEdit() {
        classNameField.setVisible(false);
        this.getChildren().set(0, classNameLabel); // Replace TextField with label
        classNameLabel.setVisible(true);
    }

    /**
     * Synchronizes the node's position, fields, and methods with the underlying
     * {@link UMLClassInfo}.
     */
    public void syncWithUMLClassInfo() {
        classInfo.getFields().clear();
        fields.getItems().forEach(fieldInfo -> classInfo.getFields().add(fieldInfo));

        classInfo.getMethods().clear();
        methods.getItems().forEach(methodInfo -> classInfo.getMethods().add(methodInfo));

        classInfo.setX((int) this.getLayoutX());
        classInfo.setY((int) this.getLayoutY());
    }

    /**
     * Selects the ClassNode, applying a shadow effect and highlighting the
     * background.
     */
    public void select() {
        isSelected = true;

        // Apply a slight color change and shadow effect
        this.setStyle("-fx-background-color: #e0f7fa; -fx-border-color: #02769e; -fx-border-width: 4;");
        this.setEffect(shadowEffect);
    }

    /**
     * Deselects the ClassNode, removing the shadow effect and restoring the default
     * background.
     */
    public void deselect() {
        isSelected = false;
        // Revert to the default background and remove shadow
        this.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 3;");
        this.setEffect(null);
    }

    /**
     * Toggles the selection state of the ClassNode.
     *
     * @param event The mouse event that triggered the toggle.
     */
    public void toggleSelection(MouseEvent event) {
        System.out.println("toggleSelection called");
        isSelected = !isSelected;
        if (isSelected) {
            this.setStyle("-fx-background-color: #e0f7fa; -fx-border-color: #02769e; -fx-border-width: 4;");
        } else {
            this.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 3;");
        }
    }

    /**
     * Checks if the ClassNode is currently selected.
     *
     * @return {@code true} if the node is selected, otherwise {@code false}.
     */
    public boolean isSelected() {
        return isSelected;
        // Apply a slight color change and shadow effect
    }

    /**
     * Handles the initial press of the mouse when dragging the ClassNode.
     * Records the offset between the mouse position and the node's position
     * to ensure smooth dragging.
     *
     * @param event The {@link MouseEvent} that triggered this action.
     */
    private void onMousePressed(MouseEvent event) {
        mouseXOffset = event.getSceneX() - this.getLayoutX();
        mouseYOffset = event.getSceneY() - this.getLayoutY();
    }

    /**
     * Handles the dragging of the ClassNode by updating its position on the screen.
     * Ensures the node stays within the boundaries of its parent container.
     *
     * @param event The {@link MouseEvent} that triggered this action.
     */
    private void onMouseDragged(MouseEvent event) {
        double newX = event.getSceneX() - mouseXOffset;
        double newY = event.getSceneY() - mouseYOffset;

        // Get pane's width and height from the parent (nodeContainer should be the
        // parent)
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

        // Update the ScrollPane if necessary
        ScrollPane scrollPane = findScrollPane(parentPane);
        if(scrollPane != null) {
            ensureVisibleInScrollPane(scrollPane);
        }

        syncWithUMLClassInfo();
    }

    /**
     * Ensures the ClassNode stays visible within the viewport of the ScrollPane
     * by minimally adjusting the viewport to bring the node into view.
     *
     * @param scrollPane The {@link ScrollPane} containing the parent pane.
     */
    private void ensureVisibleInScrollPane(ScrollPane scrollPane) {
        // Get the bounds of the ClassNode relative to the ScrollPane's content
        double nodeMinX = this.getLayoutX();
        double nodeMaxX = nodeMinX + this.getPrefWidth();
        double nodeMinY = this.getLayoutY();
        double nodeMaxY = nodeMinY + this.getPrefHeight();

        // Get the visible viewport dimensions
        double viewportWidth = scrollPane.getViewportBounds().getWidth();
        double viewportHeight = scrollPane.getViewportBounds().getHeight();

        // Calculate the current visible region in the ScrollPane
        double contentWidth = scrollPane.getContent().getBoundsInLocal().getWidth();
        double contentHeight = scrollPane.getContent().getBoundsInLocal().getHeight();
        double viewportMinX = scrollPane.getHvalue() * (contentWidth - viewportWidth);
        double viewportMaxX = viewportMinX + viewportWidth;
        double viewportMinY = scrollPane.getVvalue() * (contentHeight - viewportHeight);
        double viewportMaxY = viewportMinY + viewportHeight;

        // Adjust horizontal scroll
        if (nodeMinX < viewportMinX) {
            scrollPane.setHvalue((nodeMinX) / (contentWidth - viewportWidth));
        } else if (nodeMaxX > viewportMaxX) {
            scrollPane.setHvalue((nodeMaxX - viewportWidth) / (contentWidth - viewportWidth));
        }

        // Adjust vertical scroll
        if (nodeMinY < viewportMinY) {
            scrollPane.setVvalue((nodeMinY) / (contentHeight - viewportHeight));
        } else if (nodeMaxY > viewportMaxY) {
            scrollPane.setVvalue((nodeMaxY - viewportHeight) / (contentHeight - viewportHeight));
        }
    }

    /**
     * Finds the nearest {@link ScrollPane} ancestor of the specified {@link Pane}.
     * This method traverses the parent hierarchy of the given {@link Pane} to locate
     * the first {@link ScrollPane} that contains it. If no {@link ScrollPane} is found,
     * the method returns {@code null}.
     *
     * @param pane The {@link Pane} whose parent hierarchy is to be traversed.
     * @return The nearest {@link ScrollPane} ancestor if one exists, otherwise {@code null}.
     */
    private ScrollPane findScrollPane(Pane pane) {
        while (pane != null) {
            if (pane.getParent() instanceof ScrollPane) {
                return (ScrollPane) pane.getParent();
            }
            pane = (Pane) pane.getParent();
        }

        return null;
    }

    /**
     * Configures a {@link ListView} for editing fields of a UML class.
     * Converts user input into {@link UMLFieldInfo} objects and updates the view.
     *
     * @param listView The {@link ListView} to make editable.
     */
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

    /**
     * Configures a {@link ListView} for editing methods of a UML class.
     * Converts user input into {@link UMLMethodInfo} objects and updates the view.
     *
     * @param listView The {@link ListView} to make editable.
     */
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

    /**
     * Displays an informational alert dialog to the user.
     *
     * @param title   The title of the alert window.
     * @param message The content of the alert message.
     */
    private void alert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Retrieves the name of the class represented by this ClassNode.
     *
     * @return The name of the class as a {@link String}.
     */
    public String getName() {
        return classNameLabel.getText();
    }

    /**
     * Updates the name of the class represented by this ClassNode.
     *
     * @param name The new name of the class.
     */
    public void setName(String name) {
        classNameLabel.setText(name);
    }

    /**
     * Adds a field to the UML class represented by this ClassNode.
     *
     * @param fieldInfo The {@link UMLFieldInfo} representing the field to add.
     */
    public void addField(UMLFieldInfo fieldInfo) {
        fields.getItems().add(fieldInfo);
    }

    /**
     * Removes a field from the UML class represented by this ClassNode.
     *
     * @param fieldName The name of the field to remove.
     * @return {@code true} if the field was removed successfully, otherwise
     *         {@code false}.
     */
    public boolean removeField(String fieldName) {
        // Check if the field exists in the GUI ListView
        boolean removedFromListView = fields.getItems().removeIf(field -> field.getFieldName().equals(fieldName));

        if (removedFromListView) {
            // If removed from ListView, also remove it from the backend classInfo
            boolean removedFromBackend = classInfo.getFields()
                    .removeIf(field -> field.getFieldName().equals(fieldName));

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

    /**
     * Renames a field in the UML class represented by this ClassNode.
     *
     * @param oldFieldName The current name of the field.
     * @param newFieldType The new type of the field.
     * @param newFieldName The new name of the field.
     * @return {@code true} if the field was renamed successfully, otherwise
     *         {@code false}.
     */
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

    /**
     * Adds a method to the UML class represented by this ClassNode.
     *
     * @param method The {@link UMLMethodInfo} representing the method to add.
     */
    public void addMethod(UMLMethodInfo method) {
        methods.getItems().add(method); // Add the method to the ListView in ClassNode
    }

    /**
     * Removes a method from the UML class represented by this ClassNode.
     *
     * @param methodName The name of the method to remove.
     * @return {@code true} if the method was removed successfully, otherwise
     *         {@code false}.
     */
    public boolean removeMethod(String methodName) {
        // Use update-like logic to handle removal
        boolean removed = methods.getItems().removeIf(m -> m.getMethodName().equals(methodName));
        if (removed) {
            classInfo.getMethods().removeIf(m -> m.getMethodName().equals(methodName)); // Sync with backend
        }
        return removed;
    }

    /**
     * Renames a method in the UML class represented by this ClassNode.
     *
     * @param oldMethodName The current name of the method.
     * @param newMethodName The new name of the method.
     * @return {@code true} if the method was renamed successfully, otherwise
     *         {@code false}.
     */
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

    /**
     * Updates an existing method in the UML class represented by this ClassNode.
     * Replaces the old method with the updated method in the {@link ListView}.
     *
     * @param method The {@link UMLMethodInfo} object representing the updated
     *               method.
     */
    public void updateMethod(UMLMethodInfo method) {
        methods.getItems().removeIf(m -> m.getMethodName().equals(method.getMethodName())); // Remove old method
        methods.getItems().add(method); // Add updated method with new parameter
    }
}