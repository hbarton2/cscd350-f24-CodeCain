import codecain.BackendCode.UndoRedo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

public class UndoRedoTests {
    private UndoRedo undoRedo;

    @BeforeEach
    public void setUp() {
        undoRedo = new UndoRedo();
    }

    @Test
    public void testUndoStackInitializesEmpty() {
        assertTrue(undoRedo.undoStack.isEmpty(), "Undo stack should be initialized empty");
    }

    @Test
    public void testRedoStackInitializesEmpty() {
        Stack<Object> redoStack = undoRedo.redoStack;
        assertTrue(redoStack.isEmpty(), "Redo stack should be initialized empty");
    }

    @Test
    public void testAddToUndoStack() {
        Object state = "Test State";
        undoRedo.addToUndoStack(state);
        assertFalse(undoRedo.undoStack.isEmpty(), "Undo stack should not be empty after adding state");
        assertEquals(state, undoRedo.undoStack.peek(), "Undo stack should contain the last added state");
    }

    @Test
    public void testAddToRedoStack() {
        Object state = "Redo State";
        undoRedo.addToRedoStack(state);
        assertFalse(undoRedo.redoStack.isEmpty(), "Redo stack should not be empty after adding state");
        assertEquals(state, undoRedo.redoStack.peek(), "Redo stack should contain the last added state");
    }

    @Test
    public void testUndoMovesStateToRedoStack() {
        Object state = "Undo State";
        undoRedo.addToUndoStack(state);
        undoRedo.undo();
        assertTrue(undoRedo.undoStack.isEmpty(), "Undo stack should be empty after undo");
        assertFalse(undoRedo.redoStack.isEmpty(), "Redo stack should not be empty after undo");
        assertEquals(state, undoRedo.redoStack.peek(), "Redo stack should contain the undone state");
    }

    @Test
    public void testRedoMovesStateToUndoStack() {
        Object state = "Redo State";
        undoRedo.addToRedoStack(state);
        undoRedo.redo();
        assertTrue(undoRedo.redoStack.isEmpty(), "Redo stack should be empty after redo");
        assertFalse(undoRedo.undoStack.isEmpty(), "Undo stack should not be empty after redo");
        assertEquals(state, undoRedo.undoStack.peek(), "Undo stack should contain the redone state");
    }

    @Test
    public void testUndoDoesNothingWhenUndoStackIsEmpty() {
        undoRedo.undo();
        assertTrue(undoRedo.redoStack.isEmpty(), "Redo stack should remain empty if undo is called with empty undo stack");
    }

    @Test
    public void testRedoDoesNothingWhenRedoStackIsEmpty() {
        undoRedo.redo();
        assertTrue(undoRedo.undoStack.isEmpty(), "Undo stack should remain empty if redo is called with empty redo stack");
    }
}
