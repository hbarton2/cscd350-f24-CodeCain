package codecain;

import jline.console.history.History;

import java.util.Stack;

public class UndoRedo {
    public Stack<Object> undoStack;
    public Stack<Object> redoStack;

    private History history;

    public UndoRedo() {
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }

    private void undo(){
        if(undoStack.isEmpty()){
            return;
        }
        Object o = undoStack.pop();
        redoStack.push(o);
    }
    public void undo() {
        undo();
    }


    public void addToRedoStack(Object o) {
    }

    public void addToUndoStack(Object o) {
    }

    public void redo() {
    }
}

