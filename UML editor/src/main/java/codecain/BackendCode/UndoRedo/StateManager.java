package codecain.BackendCode.UndoRedo;

import codecain.BackendCode.Relationship;
import codecain.BackendCode.UMLClass;

import java.util.*;

public class StateManager {
    private final Caretaker caretaker = new Caretaker();

    public void saveState() {
        caretaker.saveState(UMLClass.classMap, Relationship.relationshipList);
    }

    public boolean undo() {
        Memento memento = caretaker.undo();
        if (memento != null) {
            UMLClass.classMap = memento.getClassMapSnapshot();
            Relationship.relationshipList = new ArrayList<>(memento.getRelationshipListSnapshot());
            return true;
        }
        return false;
    }

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