package codecain.GraphicalUserInterface;

import codecain.RelationshipCanvas;

import javax.swing.*;
import java.awt.*;

/**
 * The {@code GUI} class represents the main graphical user interface for the Class Diagram Editor.
 * It sets up the main window, including the canvas for displaying class diagrams,
 * a control panel for user actions, and integrates various managers for handling the backend logic.
 */
public class GUI extends JFrame {

    /** The canvas panel where the class diagram is displayed. */
    private RelationshipCanvas canvas;

    /** The controller responsible for coordinating actions between the GUI and backend. */
    private GUIController controller;

    /**
     * Constructs the {@code GUI} for the Class Diagram Editor.
     * Initializes the main window, sets up the canvas, controller, and various GUI components.
     */
    public GUI() {
        setTitle("Class Diagram Editor");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize canvas and controller
        canvas = new RelationshipCanvas();
        canvas.setBackground(Color.WHITE);
        canvas.setLayout(null);

        // Initialize managers and controller
        GUIClassManager classManager = new GUIClassManager(canvas);
        canvas.setClassManager(classManager);
        GUIFieldManager fieldManager = new GUIFieldManager(classManager);
        GUIMethodManager methodManager = new GUIMethodManager(classManager);
        GUIRelationshipManager relationshipManager = new GUIRelationshipManager(classManager);
        controller = new GUIController(classManager, fieldManager, methodManager, relationshipManager);

        // Add panels
        add(new ControlPanel(controller), BorderLayout.NORTH);
        add(canvas, BorderLayout.CENTER);
        add(new ClassPanel(controller), BorderLayout.EAST);
    }

    /**
     * The main method that launches the application.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUI gui = new GUI();
            gui.setVisible(true);
        });
    }
}
