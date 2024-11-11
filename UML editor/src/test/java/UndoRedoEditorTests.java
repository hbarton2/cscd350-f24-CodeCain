import codecain.BackendCode.UndoRedo.UndoRedoEditor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

public class UndoRedoEditorTests {
    private UndoRedoEditor undoRedoEditor;

    @BeforeEach
    public void setUp() {
        undoRedoEditor = new UndoRedoEditor();
    }

    @Test
    public void testUndoStackInitializesEmpty() {
        assertTrue(undoRedoEditor.undoStack.isEmpty(), "Undo stack should be initialized empty");
    }

    @Test
    public void testRedoStackInitializesEmpty() {
        Stack<Object> redoStack = undoRedoEditor.redoStack;
        assertTrue(redoStack.isEmpty(), "Redo stack should be initialized empty");
    }

    @Test
    public void testAddToUndoStack() {
        Object state = "Test State";
        undoRedoEditor.addToUndoStack(state);
        assertFalse(undoRedoEditor.undoStack.isEmpty(), "Undo stack should not be empty after adding state");
        assertEquals(state, undoRedoEditor.undoStack.peek(), "Undo stack should contain the last added state");
    }

    @Test
    public void testAddToRedoStack() {
        Object state = "Redo State";
        undoRedoEditor.addToRedoStack(state);
        assertFalse(undoRedoEditor.redoStack.isEmpty(), "Redo stack should not be empty after adding state");
        assertEquals(state, undoRedoEditor.redoStack.peek(), "Redo stack should contain the last added state");
    }

    @Test
    public void testUndoMovesStateToRedoStack() {
        Object state = "Undo State";
        undoRedoEditor.addToUndoStack(state);
        undoRedoEditor.undo();
        assertTrue(undoRedoEditor.undoStack.isEmpty(), "Undo stack should be empty after undo");
        assertFalse(undoRedoEditor.redoStack.isEmpty(), "Redo stack should not be empty after undo");
        assertEquals(state, undoRedoEditor.redoStack.peek(), "Redo stack should contain the undone state");
    }

    @Test
    public void testRedoMovesStateToUndoStack() {
        Object state = "Redo State";
        undoRedoEditor.addToRedoStack(state);
        undoRedoEditor.redo();
        assertTrue(undoRedoEditor.redoStack.isEmpty(), "Redo stack should be empty after redo");
        assertFalse(undoRedoEditor.undoStack.isEmpty(), "Undo stack should not be empty after redo");
        assertEquals(state, undoRedoEditor.undoStack.peek(), "Undo stack should contain the redone state");
    }

    @Test
    public void testUndoDoesNothingWhenUndoStackIsEmpty() {
        undoRedoEditor.undo();
        assertTrue(undoRedoEditor.redoStack.isEmpty(), "Redo stack should remain empty if undo is called with empty undo stack");
    }

    @Test
    public void testRedoDoesNothingWhenRedoStackIsEmpty() {
        undoRedoEditor.redo();
        assertTrue(undoRedoEditor.undoStack.isEmpty(), "Undo stack should remain empty if redo is called with empty redo stack");
    }
}
