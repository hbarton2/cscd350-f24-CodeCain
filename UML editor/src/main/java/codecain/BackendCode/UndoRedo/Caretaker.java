package codecain.BackendCode.UndoRedo;

import codecain.BackendCode.Relationship;
import codecain.BackendCode.UMLClass;
import codecain.BackendCode.UMLClassInfo;

import java.util.*;

/**
 * The Caretaker class is responsible for managing the undo and redo functionality.
 * It maintains two stacks: one for undo operations and another for redo operations.
 * Each stack stores Memento objects that represent the state of the UML diagram
 * at a given point in time.
 */
public class Caretaker {
    private final Stack<Memento> undoStack = new Stack<>();
    private final Stack<Memento> redoStack = new Stack<>();

    /**
     * Saves the current state of the UML diagram to the undo stack.
     * Clears the redo stack to ensure that redo operations are only
     * available after an undo operation.
     *
     * @param classMap        The current mapping of class names to UMLClassInfo objects.
     * @param relationshipList The current list of relationships between UML classes.
     */
    public void saveState(Map<String, UMLClassInfo> classMap, List<Relationship> relationshipList) {
        undoStack.push(new Memento(new HashMap<>(classMap), new ArrayList<>(relationshipList))); // Deep copy
        redoStack.clear();
    }

    /**
     * Performs an undo operation by restoring the last saved state.
     * Moves the current state to the redo stack for potential redo operations.
     *
     * @return The Memento representing the last saved state, or null if undo is not possible.
     */
    public Memento undo() {
        if (!undoStack.isEmpty()) {
            Memento state = undoStack.pop();
            redoStack.push(new Memento(UMLClass.classMap, Relationship.relationshipList));
            return state;
        }
        return null;
    }

    /**
     * Performs a redo operation by restoring the last undone state.
     * Moves the current state to the undo stack for potential further undo operations.
     *
     * @return The Memento representing the last undone state, or null if redo is not possible.
     */
    public Memento redo() {
        if (!redoStack.isEmpty()) {
            Memento state = redoStack.pop();
            undoStack.push(new Memento(UMLClass.classMap, Relationship.relationshipList));
            return state;
        }
        return null;
    }

    /**
     * Gets the size of the undo stack.
     *
     * @return The number of states currently stored in the undo stack.
     */
    public int undoStackSize() {
        return undoStack.size();
    }

    /**
     * Gets the size of the redo stack.
     *
     * @return The number of states currently stored in the redo stack.
     */
    public int redoStackSize() {
        return redoStack.size();
    }
}
