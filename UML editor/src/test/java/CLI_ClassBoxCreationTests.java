
import codecain.CommandLineInterface.CommandManager;
import codecain.GraphicalUserInterface.GUIClassManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the integration between the Command Line Interface (CLI) commands
 * and the creation of graphical ClassBox components in the GUI.
 * Verifies that the GUIClassManager's classPanels map is correctly updated
 * and that the expected GUI elements are created when CLI commands are executed.
 */
class CLI_ClassBoxCreationTests {

    /** The CommandManager instance used for parsing and executing CLI commands. */
    private CommandManager commandManager;

    /** The JTextArea that serves as a simulated command output log for testing. */
    private JTextArea commandOutput;

    /** The GUIClassManager instance that manages ClassBox components on a canvas. */
    private GUIClassManager guiClassManager;

    /**
     * Sets up the testing environment before each test.
     * Initializes the command output, GUIClassManager, and CommandManager.
     */
    @BeforeEach
    void setUp() {
        commandOutput = new JTextArea();
        JPanel canvas = new JPanel(); // Simulated canvas for GUI testing
        guiClassManager = new GUIClassManager(canvas);
        commandManager = new CommandManager(commandOutput, guiClassManager);
    }

    /**
     * Tests that when a new class is added via the CLI, it is also reflected in the GUIClassManager's classPanels map.
     * Ensures that the newly added class is stored in the map.
     */
    @Test
    void testHandleAddClassUpdatesClassPanels() {
        String className = "TestClass";
        commandManager.parseAndExecute("add class " + className);
        HashMap<String, JPanel> classPanels = guiClassManager.getClassPanels();
        assertTrue(classPanels.containsKey(className), "classPanels should contain the newly added class.");
    }

    /**
     * Tests that when a new class is added via the CLI, a ClassBox is created
     * and displayed on the GUI canvas.
     * Verifies that the ClassBox is present in the classPanels map and has the correct attributes.
     */
    @Test
    void testHandleAddClassCreatesClassBoxOnCanvas() {
        String className = "TestClass";
        commandManager.parseAndExecute("add class " + className);
        JPanel classBox = guiClassManager.getClassPanels().get(className);
        assertNotNull(classBox, "ClassBox should be created and added to classPanels.");
        assertEquals(className, ((JLabel) classBox.getComponent(0)).getText(),
                "ClassBox should display the correct class name.");
    }

    /**
     * Tests that when a class is added via the CLI, the corresponding action
     * is logged in the command output.
     * Ensures the command output contains the class name to confirm the action was executed.
     */
    @Test
    void testHandleAddClassLogsOutput() {
        String className = "TestClass";
        commandManager.parseAndExecute("add class " + className);
        String output = commandOutput.getText();
        assertTrue(output.contains("TestClass"),
                "Command output should log the creation of the class.");
    }
}

