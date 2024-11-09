package codecain.GraphicalUserInterface;

import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {
    public ControlPanel(GUIController controller) {
        setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton saveButton = new JButton("Save");
        JButton loadButton = new JButton("Load");

        saveButton.addActionListener(e -> controller.handleSave());
        loadButton.addActionListener(e -> controller.handleLoad());

        add(saveButton);
        add(loadButton);
    }
}
