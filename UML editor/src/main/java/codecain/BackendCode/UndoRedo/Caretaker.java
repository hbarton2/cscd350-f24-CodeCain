package codecain.BackendCode.UndoRedo;

import codecain.BackendCode.Relationship;
import codecain.BackendCode.UMLClass;
import codecain.BackendCode.UMLClassInfo;

import java.util.*;

public class Caretaker {
    private final Stack<Memento> undoStack = new Stack<>();
    private final Stack<Memento> redoStack = new Stack<>();

    public void saveState(Map<String, UMLClassInfo> classMap, List<Relationship> relationshipList) {
        undoStack.push(new Memento(new HashMap<>(classMap), new ArrayList<>(relationshipList))); // Deep copy
        redoStack.clear();
    }

    public Memento undo() {
        if (!undoStack.isEmpty()) {
            Memento state = undoStack.pop();
            redoStack.push(new Memento(UMLClass.classMap, Relationship.relationshipList));
            return state;
        }
        return null;
    }

    public Memento redo() {
        if (!redoStack.isEmpty()) {
            Memento state = redoStack.pop();
            undoStack.push(new Memento(UMLClass.classMap, Relationship.relationshipList));
            return state;
        }
        return null;
    }
    public int undoStackSize() {
        return undoStack.size();
    }

    public int redoStackSize() {
        return redoStack.size();
    }
}
