package codecain.BackendCode.UndoRedo;

import codecain.BackendCode.Relationship;
import codecain.BackendCode.UMLClassInfo;

import java.util.*;

public class Memento {
    private final Map<String, UMLClassInfo> classMapSnapshot;
    private final List<Relationship> relationshipListSnapshot;

    public Memento(Map<String, UMLClassInfo> classMap, List<Relationship> relationshipList) {
        this.classMapSnapshot = new HashMap<>(classMap);
        this.relationshipListSnapshot = new ArrayList<>(relationshipList);
    }

    public Map<String, UMLClassInfo> getClassMapSnapshot() {
        return classMapSnapshot;
    }

    public List<Relationship> getRelationshipListSnapshot() {
        return relationshipListSnapshot;
    }
}