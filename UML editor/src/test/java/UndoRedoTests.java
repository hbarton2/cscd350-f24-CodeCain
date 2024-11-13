
import codecain.BackendCode.UndoRedo.History;
import codecain.BackendCode.UndoRedo.Memento;
import codecain.BackendCode.UndoRedo.UndoRedoEditor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UndoRedoTests {

    private History history;
    private UndoRedoEditor editor;

    @BeforeEach
    public void setUp() {
        history = new History();
        editor = new UndoRedoEditor();
    }

    @Test
    public void testSave() {
        editor.setState("State1");
        Memento memento = editor.saveStateToMemento();
        history.save(memento);

        assertTrue(history.canUndo());
        assertFalse(history.canRedo());
    }

    @Test
    public void testUndo() {
        editor.setState("State1");
        Memento memento1 = editor.saveStateToMemento();
        history.save(memento1);

        editor.setState("State2");
        Memento memento2 = editor.saveStateToMemento();
        history.save(memento2);

        Memento undoneMemento = history.undo();
        assertEquals("State2", undoneMemento.getState());
        assertTrue(history.canRedo());
    }

    @Test
    public void testRedo() {
        editor.setState("State1");
        Memento memento1 = editor.saveStateToMemento();
        history.save(memento1);

        editor.setState("State2");
        Memento memento2 = editor.saveStateToMemento();
        history.save(memento2);

        history.undo();
        Memento redoneMemento = history.redo();

        assertEquals("State2", redoneMemento.getState());
        assertTrue(history.canUndo());
    }

    @Test
    public void testUndoWhenEmpty() {
        assertNull(history.undo());
        assertFalse(history.canUndo());
    }

    @Test
    public void testRedoWhenEmpty() {
        assertNull(history.redo());
        assertFalse(history.canRedo());
    }

    @Test
    public void testCanUndoAndRedo() {
        assertFalse(history.canUndo());
        assertFalse(history.canRedo());

        editor.setState("State1");
        Memento memento = editor.saveStateToMemento();
        history.save(memento);

        assertTrue(history.canUndo());
        assertFalse(history.canRedo());

        history.undo();
        assertTrue(history.canRedo());
    }

    @Test
    public void testMultipleUndoRedo() {
        editor.setState("State1");
        history.save(editor.saveStateToMemento());

        editor.setState("State2");
        history.save(editor.saveStateToMemento());

        editor.setState("State3");
        history.save(editor.saveStateToMemento());

        assertEquals("State3", history.undo().getState());
        assertEquals("State2", history.undo().getState());
        assertEquals("State1", history.undo().getState());

        assertTrue(history.canRedo());

        assertEquals("State1", history.redo().getState());
        assertEquals("State2", history.redo().getState());
        assertEquals("State3", history.redo().getState());

        assertFalse(history.canRedo());
    }
}
