package codecain.GraphicalUserInterface;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {
    private JPanel canvas;
    private GUIController controller;

    public GUI() {
        setTitle("Class Diagram Editor");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize canvas and controller
        canvas = new JPanel();
        canvas.setBackground(Color.WHITE);
        canvas.setLayout(null);

        GUIClassManager classManager = new GUIClassManager(canvas);
        GUIFieldManager fieldManager = new GUIFieldManager();
        GUIMethodManager methodManager = new GUIMethodManager();
        GUIRelationshipManager relationshipManager = new GUIRelationshipManager();
        controller = new GUIController(classManager, fieldManager, methodManager, relationshipManager);

        // Add panels
        add(new ControlPanel(controller), BorderLayout.NORTH);
        add(canvas, BorderLayout.CENTER);
        add(new ClassPanel(controller), BorderLayout.EAST);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUI gui = new GUI();
            gui.setVisible(true);
        });
    }
}
