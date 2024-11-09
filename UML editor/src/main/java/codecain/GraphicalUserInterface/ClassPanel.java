package codecain.GraphicalUserInterface;

import javax.swing.*;
import java.awt.*;

/**
 * The {@code ClassPanel} class represents a control panel in the Class Diagram Editor.
 * It provides buttons for managing classes, fields, methods, parameters, and relationships.
 * Each button is linked to specific actions in the {@code GUIController}.
 */
public class ClassPanel extends JPanel {

    /**
     * Constructs a {@code ClassPanel} and initializes its layout and buttons.
     * The panel is organized using a vertical {@code BoxLayout}, and it provides
     * buttons for various management operations related to classes, fields, methods,
     * parameters, and relationships.
     *
     * @param controller the {@code GUIController} responsible for handling the actions triggered by the buttons.
     */
    public ClassPanel(GUIController controller) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Class management
        JButton addClassButton = new JButton("Add Class");
        JButton deleteClassButton = new JButton("Delete Class");
        JButton renameClassButton = new JButton("Rename Class");

        addClassButton.addActionListener(e -> controller.handleAddClass());
        deleteClassButton.addActionListener(e -> controller.handleDeleteClass());
        renameClassButton.addActionListener(e -> controller.handleRenameClass());

        add(addClassButton);
        add(deleteClassButton);
        add(renameClassButton);

        add(Box.createRigidArea(new Dimension(0, 15)));

        // Field management
        JButton addFieldButton = new JButton("Add Field");
        JButton deleteFieldButton = new JButton("Delete Field");
        JButton renameFieldButton = new JButton("Rename Field");

        addFieldButton.addActionListener(e -> controller.handleAddField());
        deleteFieldButton.addActionListener(e -> controller.handleDeleteField());
        renameFieldButton.addActionListener(e -> controller.handleRenameField());

        add(addFieldButton);
        add(deleteFieldButton);
        add(renameFieldButton);

        add(Box.createRigidArea(new Dimension(0, 15)));

        // Method management
        JButton addMethodButton = new JButton("Add Method");
        JButton deleteMethodButton = new JButton("Delete Method");
        JButton renameMethodButton = new JButton("Rename Method");

        addMethodButton.addActionListener(e -> controller.handleAddMethod());
        deleteMethodButton.addActionListener(e -> controller.handleDeleteMethod());
        renameMethodButton.addActionListener(e -> controller.handleRenameMethod());

        add(addMethodButton);
        add(deleteMethodButton);
        add(renameMethodButton);

        add(Box.createRigidArea(new Dimension(0, 15)));

        // Parameter management
        JButton addParameterButton = new JButton("Add Parameter");
        JButton deleteParameterButton = new JButton("Delete Parameter");
        JButton renameParameterButton = new JButton("Rename Parameter");

        addParameterButton.addActionListener(e -> controller.handleAddParameter());
        deleteParameterButton.addActionListener(e -> controller.handleDeleteParameter());
        renameParameterButton.addActionListener(e -> controller.handleRenameParameter());

        add(addParameterButton);
        add(deleteParameterButton);
        add(renameParameterButton);

        add(Box.createRigidArea(new Dimension(0, 15)));

        // Relationship management
        JButton addRelationshipButton = new JButton("Add Relationship");
        JButton deleteRelationshipButton = new JButton("Remove Relationship");

        addRelationshipButton.addActionListener(e -> controller.handleAddRelationship());
        deleteRelationshipButton.addActionListener(e -> controller.handleDeleteRelationship());

        add(addRelationshipButton);
        add(deleteRelationshipButton);
    }
}
