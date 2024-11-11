package codecain.BackendCode.UndoRedo;

import jline.console.history.History;

import java.util.Stack;

public class UndoRedoEditor {
    public Stack<Object> undoStack;
    public Stack<Object> redoStack;

    private History history;

    public UndoRedoEditor() {
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }

    public void undo() {
    }


    public void addToRedoStack(Object o) {
    }

    public void addToUndoStack(Object o) {
    }

    public void redo() {
    }

    public String backup() {
        return null;
    }

    public void restore(String backup) {
    }
}

