import codecain.BackendCode.UndoRedo.Caretaker;
import codecain.BackendCode.UndoRedo.StateManager;
import codecain.BackendCode.Model.Relationship;
import codecain.BackendCode.Model.UMLClassInfo;
import codecain.CommandLineInterface.Model.CommandManager;
import javafx.scene.control.TextArea;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class UndoRedoTests {

    private Map<String, UMLClassInfo> classMap;
    private List<Relationship> relationshipList;
    private Caretaker caretaker;

    @BeforeEach
    void setUp() {
        classMap = new HashMap<>();
        relationshipList = new ArrayList<>();
        caretaker = new Caretaker();
    }

    @Test
    void testSaveState() {
        classMap.put("Class1", new UMLClassInfo("Class1"));
        caretaker.saveState(classMap, relationshipList);

        assertFalse(caretaker.undo().getClassMapSnapshot().isEmpty());
        assertTrue(caretaker.redo().getRelationshipListSnapshot().isEmpty());
    }

    

    @Test
    void testUndoRedo() {
        classMap.put("Class1", new UMLClassInfo("Class1"));
        caretaker.saveState(classMap, relationshipList);

        classMap.put("Class2", new UMLClassInfo("Class2"));
        caretaker.saveState(classMap, relationshipList);

        assertEquals(2, caretaker.undoStackSize());

        caretaker.undo();
        assertEquals(1, caretaker.undoStackSize());
        assertEquals(1, caretaker.redoStackSize());

        caretaker.redo();
        assertEquals(2, caretaker.undoStackSize());
        assertEquals(0, caretaker.redoStackSize());
    }

}


class StateManagerTests {
    private StateManager stateManager;
    @BeforeEach
    void setUp() {
        stateManager = new StateManager();
    }
    @Test
    void testStateManagerIntegration() {
        assertFalse(stateManager.undo());
        assertFalse(stateManager.redo());
    }
}

class CommandManagerTest {
    private CommandManager commandManager;
    private TextArea commandOutput;

    @BeforeEach
    void setUp() {
        commandOutput = new TextArea();
        commandManager = new CommandManager(commandOutput);
    }




}
