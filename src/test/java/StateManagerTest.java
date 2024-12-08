import codecain.BackendCode.Model.Relationship;
import codecain.BackendCode.Model.UMLClass;
import codecain.BackendCode.Model.UMLClassInfo;
import codecain.BackendCode.Model.RelationshipType;
import codecain.BackendCode.UndoRedo.StateManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;

/**
 * Unit tests for the {@link StateManager} class.
 * This class tests the functionality of saving, undoing, and redoing states of the UML diagram,
 * including the behavior of the {@code UMLClass.classMap} and {@code Relationship.relationshipList}.
 */
public class StateManagerTest {

    /**
     * Instance of StateManager used in the tests.
     */
    private StateManager stateManager;

    /**
     * Sets up the test environment before each test.
     * Resets the {@code UMLClass.classMap} and {@code Relationship.relationshipList} to ensure a clean state.
     */
    @BeforeEach
    void setUp() {
        stateManager = new StateManager();
        UMLClass.classMap.clear();
        Relationship.relationshipList.clear();
    }

    /**
     * Tests the {@link StateManager#saveState()} method.
     * Verifies that the current state of {@code UMLClass.classMap} and {@code Relationship.relationshipList} is saved.
     */
    @Test
    public void testSaveState() {
        UMLClass.addClass("Dog");
        UMLClass.addClass("Cat");
        Relationship.addRelationship("Dog", "Cat", RelationshipType.GENERALIZATION);

        stateManager.saveState();

        assertFalse(UMLClass.classMap.isEmpty(), "The class map should not be empty after saving state.");
        assertFalse(Relationship.relationshipList.isEmpty(), "The relationship list should not be empty after saving state.");
    }

    /**
     * Tests the {@link StateManager#undo()} method.
     * Verifies that the previous state of the UML diagram is restored after an undo operation.
     */
    @Test
    public void testUndo() {
        UMLClass.addClass("Dog");
        UMLClass.addClass("Cat");
        Relationship.addRelationship("Dog", "Cat", RelationshipType.GENERALIZATION);

        stateManager.saveState();

        UMLClass.removeClass("Dog");

        boolean undo = stateManager.undo();

        assertTrue(undo, "The undo operation should succeed.");
        assertEquals(2, UMLClass.classMap.size(), "The class map should have two entries after undo.");
        assertEquals(1, Relationship.relationshipList.size(), "The relationship list should have one entry after undo.");
    }

    /**
     * Tests the {@link StateManager#undo()} method when no state has been saved.
     * Verifies that the method returns {@code false} and no changes are made.
     */
    @Test
    public void testUndoNoState() {
        boolean undo = stateManager.undo();
        assertFalse(undo, "Undo should fail when there is no state to undo.");
    }

    /**
     * Tests the {@link StateManager#redo()} method.
     * Verifies that the most recently undone state of the UML diagram is restored.
     */
    @Test
    public void testRedo() {
        UMLClass.addClass("DOG");
        UMLClass.addClass("Cat");
        Relationship.addRelationship("Dog", "Cat", RelationshipType.GENERALIZATION);

        stateManager.saveState();

        UMLClass.removeClass("DOG");
        stateManager.undo();

        boolean redo = stateManager.redo();

        assertTrue(redo, "The redo operation should succeed.");
        assertEquals(1, UMLClass.classMap.size(), "The class map should have one entry after redo.");
        assertEquals(0, Relationship.relationshipList.size(), "The relationship list should be empty after redo.");
    }

    /**
     * Tests the {@link StateManager#redo()} method when no state has been undone.
     * Verifies that the method returns {@code false} and no changes are made.
     */
    @Test
    public void testRedoNoState() {
        boolean redo = stateManager.redo();
        assertFalse(redo, "Redo should fail when there is no state to redo.");
    }

    /**
     * Tests the {@link StateManager#undo()} and {@link StateManager#redo()} methods with multiple states saved.
     * Verifies that undo and redo operations correctly traverse through the saved states.
     */
    @Test
    public void testUndoRedoWithMultipleStates() {
        UMLClass.addClass("Dog");
        stateManager.saveState();

        UMLClass.addClass("Cat");
        stateManager.saveState();

        UMLClass.removeClass("Dog");
        UMLClass.removeClass("Cat");

        assertEquals(0, UMLClass.classMap.size(), "The class map should be empty after removing all classes.");

        stateManager.undo();
        assertEquals(2, UMLClass.classMap.size(), "The class map should have two entries after the first undo.");
        assertTrue(UMLClass.classMap.containsKey("Dog"), "The class map should contain 'Dog' after undo.");

        stateManager.redo();
        assertEquals(0, UMLClass.classMap.size(), "The class map should be empty after redo to the most recent state.");

        stateManager.undo();
        assertEquals(2, UMLClass.classMap.size(), "The class map should have two entries after the second undo.");
        assertTrue(UMLClass.classMap.containsKey("Dog"), "The class map should contain 'Dog' after undo.");
        assertTrue(UMLClass.classMap.containsKey("Cat"), "The class map should contain 'Cat' after undo.");
    }
}
