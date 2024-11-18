package codecain.BackendCode.UndoRedo;

import codecain.BackendCode.Model.Relationship;
import codecain.BackendCode.Model.UMLClass;

import java.util.*;

/**
 * The StateManager class provides an interface for managing the state of the UML diagram.
 * It uses the Caretaker class to enable undo and redo functionality.
 * The state of the UML diagram, including class mappings and relationships, is saved and restored as needed.
 */
public class StateManager {
    private final Caretaker caretaker = new Caretaker();

    /**
     * Saves the current state of the UML diagram by storing the class map and relationship list.
     * This state is pushed onto the undo stack in the Caretaker.
     */
    public void saveState() {
        caretaker.saveState(UMLClass.classMap, Relationship.relationshipList);
    }

    /**
     * Restores the last saved state by performing an undo operation.
     * Updates the UMLClass and Relationship objects with the restored state.
     *
     * @return {@code true} if the undo operation was successful, {@code false} if there is no state to undo.
     */
    public boolean undo() {
        Memento memento = caretaker.undo();
        if (memento != null) {
            UMLClass.classMap = memento.getClassMapSnapshot();
            Relationship.relationshipList = new ArrayList<>(memento.getRelationshipListSnapshot());
            return true;
        }
        return false;
    }

    /**
     * Restores the most recently undone state by performing a redo operation.
     * Updates the UMLClass and Relationship objects with the restored state.
     *
     * @return {@code true} if the redo operation was successful, {@code false} if there is no state to redo.
     */
    public boolean redo() {
        Memento memento = caretaker.redo();
        if (memento != null) {
            UMLClass.classMap = memento.getClassMapSnapshot();
            Relationship.relationshipList = new ArrayList<>(memento.getRelationshipListSnapshot());
            return true;
        }
        return false;
    }
}
