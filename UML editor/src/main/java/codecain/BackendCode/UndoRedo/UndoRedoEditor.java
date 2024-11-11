<<<<<<<< HEAD:UML editor/src/main/java/codecain/BackendCode/UndoRedo/UndoRedoEditor.java
package codecain.BackendCode.UndoRedo;
========
package codecain.BackendCode;
>>>>>>>> 4f69d146818359bc6d010738de4eab4b093a68d2:UML editor/src/main/java/codecain/BackendCode/UndoRedo.java

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

