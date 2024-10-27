import static org.junit.jupiter.api.Assertions.*;

import codecain.UMLClass;
import codecain.UMLClassInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import codecain.Relationship;
import org.junit.jupiter.api.AfterEach;
import java.util.ArrayList;

public class RelationshipTests {
    @BeforeEach
    public void setUp() {
        Relationship.relationshipList = new ArrayList<>();
        UMLClass.classMap.clear();
    }

    @AfterEach
    public void tearDown() {
        Relationship.relationshipList.clear();
        UMLClass.classMap.clear();
    }

    @Test
    public void testAddRelationship(){
        UMLClass.classMap.put("DOG", new UMLClassInfo("DOG"));
        UMLClass.classMap.put("BIRD", new UMLClassInfo("BIRD"));

        boolean result = Relationship.addRelationship("DOG","BIRD");
        assertTrue(result);
        assertTrue(Relationship.relationshipExists("DOG","BIRD"));
        assertEquals(1,Relationship.relationshipList.size());
    }
    @Test
    public void testAddExisitingRelationship(){
        UMLClass.classMap.put("DOG", new UMLClassInfo("DOG"));
        UMLClass.classMap.put("BIRD", new UMLClassInfo("BIRD"));
        Relationship.addRelationship("DOG","BIRD");

        boolean result = Relationship.addRelationship("DOG","BIRD");
        assertFalse(result, "Should be prompted that you cant add an exisiting class");
    }
    @Test
    public void testRemoveRelationship(){
        UMLClass.classMap.put("DOG", new UMLClassInfo("DOG"));
        UMLClass.classMap.put("BIRD", new UMLClassInfo("BIRD"));
        Relationship.addRelationship("DOG","BIRD");
        boolean result = Relationship.removeRelationship("DOG","BIRD");
        assertTrue(result);
        assertFalse(Relationship.relationshipExists("DOG","BIRD"));
        assertEquals(0,Relationship.relationshipList.size());
    }
    @Test
    public void testRemoveNonExistingRelationship(){
        UMLClass.classMap.put("DOG", new UMLClassInfo("DOG"));
        UMLClass.classMap.put("BIRD", new UMLClassInfo("BIRD"));

        boolean result = Relationship.removeRelationship("DOG","BIRD");
        assertFalse(result);
    }
    @Test
    public void testRemoveAttachedRelationship(){
        UMLClass.classMap.put("DOG", new UMLClassInfo("DOG"));
        UMLClass.classMap.put("BIRD", new UMLClassInfo("BIRD"));
        UMLClass.classMap.put("CAT", new UMLClassInfo("CAT"));

        Relationship.addRelationship("DOG","BIRD");
        Relationship.addRelationship("DOG","CAT");
        Relationship.removeAttachedRelationships("DOG");

        assertFalse(Relationship.relationshipExists("DOG","BIRD"));
        assertFalse(Relationship.relationshipExists("DOG","CAT"));
        assertEquals(0,Relationship.relationshipList.size());
    }
    @Test
    public void testInvalidClassesForAddingRelationship(){
        UMLClass.classMap.put("DOG", new UMLClassInfo("DOG"));

        assertFalse(Relationship.addRelationship("DOG","BIRD"));
        assertFalse(Relationship.addRelationship("DOG","CAT"));
    }
    @Test
    public void testListToString(){
        UMLClass.classMap.put("DOG", new UMLClassInfo("DOG"));
        UMLClass.classMap.put("BIRD", new UMLClassInfo("BIRD"));
        UMLClass.classMap.put("CAT", new UMLClassInfo("CAT"));

        Relationship.addRelationship("DOG","BIRD");
        Relationship.addRelationship("DOG","CAT");

        String relationships = Relationship.listToString();
        String expected = "BIRD ------- DOG\nCAT ------- DOG\n";
        assertEquals(expected,relationships);
    }
    @Test
    public void testAddRelationshipWithEmptyClasses(){
        assertFalse(Relationship.addRelationship("","BIRD"));
        assertFalse(Relationship.addRelationship(null,"BIRD"));
    }
    @Test
    public void testRemoveRelationshipWithEmptyClasses(){
        assertFalse(Relationship.removeRelationship("","BIRD"));
        assertFalse(Relationship.removeRelationship(null,"BIRD"));
    }




}
