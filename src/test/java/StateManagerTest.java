import codecain.BackendCode.Model.Relationship;
import codecain.BackendCode.Model.UMLClass;
import codecain.BackendCode.Model.UMLClassInfo;
import codecain.BackendCode.Model.RelationshipType;
import codecain.BackendCode.UndoRedo.StateManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;

public class StateManagerTest {
    private StateManager stateManager;

    @BeforeEach
    void setUp() {
        stateManager = new StateManager();
        UMLClass.classMap.clear();
        Relationship.relationshipList.clear();
    }

    @Test
    public void testSaveState(){
        UMLClass.addClass("Dog");
        UMLClass.addClass("Cat");
        Relationship.addRelationship("Dog","Cat",RelationshipType.GENERALIZATION);

        stateManager.saveState();
        assertFalse(UMLClass.classMap.isEmpty());
        assertFalse(Relationship.relationshipList.isEmpty());
    }

    @Test
    public void testUndo(){
        UMLClass.addClass("Dog");
        UMLClass.addClass("Cat");
        Relationship.addRelationship("Dog","Cat",RelationshipType.GENERALIZATION);

        stateManager.saveState();

        UMLClass.removeClass("Dog");

        boolean undo = stateManager.undo();
        assertTrue(undo);
        assertEquals(2, UMLClass.classMap.size());
        assertEquals(1, Relationship.relationshipList.size());
    }

    @Test
    public void testUndoNoState(){
        boolean undo = stateManager.undo();
        assertFalse(undo);
    }

    @Test
    public void testRedo(){
        UMLClass.addClass("DOG");
        UMLClass.addClass("Cat");
        Relationship.addRelationship("Dog","Cat",RelationshipType.GENERALIZATION);
        stateManager.saveState();

        UMLClass.removeClass("DOG");
        stateManager.undo();
        boolean redo = stateManager.redo();
        assertTrue(redo);
        assertEquals(1, UMLClass.classMap.size());
        assertEquals(0, Relationship.relationshipList.size());
    }
    @Test
    public void testRedoNoState(){
        boolean redo = stateManager.redo();
        assertFalse(redo);
    }

    @Test
    public void testUndoRedoWithMultipleStates(){
        UMLClass.addClass("Dog");
        stateManager.saveState();

        UMLClass.addClass("Cat");
        stateManager.saveState();

        UMLClass.removeClass("Dog");
        UMLClass.removeClass("Cat");
        assertEquals(0, UMLClass.classMap.size());

        stateManager.undo();
        assertEquals(2, UMLClass.classMap.size());
        assertTrue(UMLClass.classMap.containsKey("Dog"));

        stateManager.redo();
        assertEquals(0, UMLClass.classMap.size());

        stateManager.undo();
        assertEquals(2, UMLClass.classMap.size());
        assertTrue(UMLClass.classMap.containsKey("Dog"));
        assertTrue(UMLClass.classMap.containsKey("Cat"));

    }


}
