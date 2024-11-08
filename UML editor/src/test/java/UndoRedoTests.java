import static org.junit.jupiter.api.Assertions.*;
import codecain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

public class UndoRedoTests {
    private UndoRedo undoRedo;



    @BeforeEach
    void setUp() {
        undoRedo = new UndoRedo();
    }

    @AfterEach
    void tearDown() {
        undoRedo = null;
    }

    @Test
    public void testUndo() {
        // Test undo when undoStack is empty
        undoRedo.undo();
    }

    @Test
    public void testRedo() {
        // Test redo when redoStack is empty
        undoRedo.redo();
    }

    @Test
    public void testAddToUndoStack() {
        // Test adding an object to the undoStack
        undoRedo.addToUndoStack(new Object());
        assertEquals(1, undoRedo.undoStack.size());
    }

    @Test
    public void testAddToRedoStack() {
        // Test adding an object to the redoStack
        undoRedo.addToRedoStack(new Object());
        assertEquals(1, undoRedo.redoStack.size());
    }


}
