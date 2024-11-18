package codecain.BackendCode.UndoRedo;

import codecain.BackendCode.Model.Relationship;
import codecain.BackendCode.Model.UMLClassInfo;

import java.util.*;

/**
 * The Memento class stores a snapshot of the UML diagram's state.
 * This includes the class map and the list of relationships at a given point in time.
 * The class is used in conjunction with the Caretaker class to enable undo and redo functionality.
 */
public class Memento {
    private final Map<String, UMLClassInfo> classMapSnapshot;
    private final List<Relationship> relationshipListSnapshot;

    /**
     * Constructs a new Memento object with the provided snapshots of the class map and relationship list.
     *
     * @param classMap        A snapshot of the current mapping of class names to UMLClassInfo objects.
     * @param relationshipList A snapshot of the current list of relationships between UML classes.
     */
    public Memento(Map<String, UMLClassInfo> classMap, List<Relationship> relationshipList) {
        this.classMapSnapshot = new HashMap<>(classMap);
        this.relationshipListSnapshot = new ArrayList<>(relationshipList);
    }

    /**
     * Returns the snapshot of the class map stored in this Memento.
     *
     * @return A deep copy of the class map at the time this Memento was created.
     */
    public Map<String, UMLClassInfo> getClassMapSnapshot() {
        return classMapSnapshot;
    }

    /**
     * Returns the snapshot of the relationship list stored in this Memento.
     *
     * @return A deep copy of the relationship list at the time this Memento was created.
     */
    public List<Relationship> getRelationshipListSnapshot() {
        return relationshipListSnapshot;
    }
}
