package codecain.BackendCode.UndoRedo;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class History {
    private Stack<Memento> undoStack = new Stack<>();
    private Stack<Memento> redoStack = new Stack<>();

    public void save(Memento memento) {
        undoStack.push(memento);
        redoStack.clear();  // Clear redo stack on new action.
    }

    public Memento undo() {
        if (!undoStack.isEmpty()) {
            Memento memento = undoStack.pop();
            redoStack.push(memento);
            return memento;
        }
        return null;  // Or throw exception.
    }

    public Memento redo() {
        if (!redoStack.isEmpty()) {
            Memento memento = redoStack.pop();
            undoStack.push(memento);
            return memento;
        }
        return null;  // Or throw exception.
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }
}
