package codecain.GraphicalUserInterface;

import javax.swing.*;
import java.awt.*;

/**
 * The {@code ControlPanel} class represents a control panel in the Class Diagram Editor.
 * It provides buttons for saving and loading UML diagrams, which are linked to actions
 * defined in the {@code GUIController}.
 */
public class ControlPanel extends JPanel {

    /**
     * Constructs a {@code ControlPanel} and initializes its layout and buttons.
     * The panel is organized using a {@code FlowLayout}, and it provides buttons for
     * saving and loading UML diagrams.
     *
     * @param controller the {@code GUIController} responsible for handling save and load actions.
     */
    public ControlPanel(GUIController controller) {
        setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> controller.handleSave());

        JButton loadButton = new JButton("Load");
        loadButton.addActionListener(e -> controller.handleLoad());

        add(saveButton);
        add(loadButton);
    }
}

