import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import codecain.BackendCode.Model.UMLClass;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import codecain.BackendCode.Model.Relationship;
import codecain.BackendCode.Model.RelationshipType;
import codecain.BackendCode.Model.UMLClassInfo;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The RelationshipTests class contains test cases to validate the functionality of the Relationship class.
 * It tests scenarios such as adding, removing, and listing relationships between UML classes.
 */
public class RelationshipTests {

    /**import java.util.ArrayList;

     import org.junit.jupiter.api.AfterEach;
     import static org.junit.jupiter.api.Assertions.assertEquals;
     import static org.junit.jupiter.api.Assertions.assertFalse;
     import static org.junit.jupiter.api.Assertions.assertTrue;
     import org.junit.jupiter.api.BeforeEach;
     import org.junit.jupiter.api.Test;

     import codecain.BackendCode.Model.Relationship;
     import codecain.BackendCode.Model.RelationshipType;
     import codecain.BackendCode.Model.UMLClass;
     import codecain.BackendCode.Model.UMLClassInfo;

     /**
     * The RelationshipTests class contains test cases to validate the functionality of the Relationship class.
     * It tests scenarios such as adding, removing, and listing relationships between UML classes.
     */
    @Nested
    class relationshipTests {

        /**
         * Sets up the test environment by initializing the Relationship list and clearing the UMLClass map.
         */
        @BeforeEach
        public void setUp() {
            Relationship.relationshipList = new ArrayList<>();
            UMLClass.classMap.clear();
        }

        /**
         * Cleans up the test environment by clearing the Relationship list and the UMLClass map.
         */
        @AfterEach
        public void tearDown() {
            Relationship.relationshipList.clear();
            UMLClass.classMap.clear();
        }

        /**
         * Tests adding a relationship between two existing classes.
         */
        @Test
        public void testAddRelationship() {
            UMLClass.classMap.put("DOG", new UMLClassInfo("DOG"));
            UMLClass.classMap.put("BIRD", new UMLClassInfo("BIRD"));

            boolean result = Relationship.addRelationship("DOG", "BIRD", RelationshipType.GENERALIZATION);
            assertTrue(result);
            assertTrue(Relationship.relationshipExists("DOG", "BIRD"));
            assertEquals(1, Relationship.relationshipList.size());
        }

        /**
         * Tests adding an existing relationship between two classes.
         */
        @Test
        public void testAddExisitingRelationship() {
            UMLClass.classMap.put("DOG", new UMLClassInfo("DOG"));
            UMLClass.classMap.put("BIRD", new UMLClassInfo("BIRD"));
            Relationship.addRelationship("DOG", "BIRD", RelationshipType.GENERALIZATION);

            boolean result = Relationship.addRelationship("DOG", "BIRD", RelationshipType.GENERALIZATION);
            assertFalse(result, "Should be prompted that you can't add an existing class relationship");
        }

        /**
         * Tests removing an existing relationship between two classes.
         */
        @Test
        public void testRemoveRelationship() {
            UMLClass.classMap.put("DOG", new UMLClassInfo("DOG"));
            UMLClass.classMap.put("BIRD", new UMLClassInfo("BIRD"));
            Relationship.addRelationship("DOG", "BIRD", RelationshipType.GENERALIZATION);
            boolean result = Relationship.removeRelationship("DOG", "BIRD");
            assertTrue(result);
            assertFalse(Relationship.relationshipExists("DOG", "BIRD"));
            assertEquals(0, Relationship.relationshipList.size());
        }

        /**
         * Tests removing a non-existing relationship between two classes.
         */
        @Test
        public void testRemoveNonExistingRelationship() {
            UMLClass.classMap.put("DOG", new UMLClassInfo("DOG"));
            UMLClass.classMap.put("BIRD", new UMLClassInfo("BIRD"));

            boolean result = Relationship.removeRelationship("DOG", "BIRD");
            assertFalse(result);
        }

        /**
         * Tests removing all relationships attached to a specific class.
         */
        @Test
        public void testRemoveAttachedRelationship() {
            UMLClass.classMap.put("DOG", new UMLClassInfo("DOG"));
            UMLClass.classMap.put("BIRD", new UMLClassInfo("BIRD"));
            UMLClass.classMap.put("CAT", new UMLClassInfo("CAT"));

            Relationship.addRelationship("DOG", "BIRD", RelationshipType.GENERALIZATION);
            Relationship.addRelationship("DOG", "CAT", RelationshipType.GENERALIZATION);
            Relationship.removeAttachedRelationships("DOG");

            assertFalse(Relationship.relationshipExists("DOG", "BIRD"));
            assertFalse(Relationship.relationshipExists("DOG", "CAT"));
            assertEquals(0, Relationship.relationshipList.size());
        }

        /**
         * Tests adding a relationship with invalid class entries.
         */
        @Test
        public void testInvalidClassesForAddingRelationship() {
            UMLClass.classMap.put("DOG", new UMLClassInfo("DOG"));

            assertFalse(Relationship.addRelationship("DOG", "BIRD", RelationshipType.GENERALIZATION));
            assertFalse(Relationship.addRelationship("DOG", "CAT", RelationshipType.GENERALIZATION));
        }

        /**
         * Tests generating a string representation of all relationships in the system.
         */
        @Test
        public void testListToString() {
            UMLClass.classMap.put("DOG", new UMLClassInfo("DOG"));
            UMLClass.classMap.put("BIRD", new UMLClassInfo("BIRD"));
            UMLClass.classMap.put("CAT", new UMLClassInfo("CAT"));

            Relationship.addRelationship("DOG", "BIRD", RelationshipType.GENERALIZATION);
            Relationship.addRelationship("DOG", "CAT", RelationshipType.GENERALIZATION);

            String relationships = Relationship.listToString();
            String expected = "DOG -----|> BIRD Generalization\nDOG -----|> CAT Generalization\n";
            assertEquals(expected, relationships);
        }

        /**
         * Tests adding a relationship with empty class names.
         */
        @Test
        public void testAddRelationshipWithEmptyClasses() {
            assertFalse(Relationship.addRelationship("", "BIRD", RelationshipType.GENERALIZATION));
            assertFalse(Relationship.addRelationship(null, "BIRD", RelationshipType.GENERALIZATION));
        }

        /**
         * Tests removing a relationship with empty class names.
         */
        @Test
        public void testRemoveRelationshipWithEmptyClasses() {
            assertFalse(Relationship.removeRelationship("", "BIRD"));
            assertFalse(Relationship.removeRelationship(null, "BIRD"));
        }
    }


    @BeforeEach
    public void setUp() {
        Relationship.relationshipList = new ArrayList<>();
        UMLClass.classMap.clear();
    }

    /**
     * Cleans up the test environment by clearing the Relationship list and the UMLClass map.
     */
    @AfterEach
    public void tearDown() {
        Relationship.relationshipList.clear();
        UMLClass.classMap.clear();
    }

    /**
     * Tests adding a relationship between two existing classes.
     */
    @Test
    public void testAddRelationship() {
        UMLClass.classMap.put("DOG", new UMLClassInfo("DOG"));
        UMLClass.classMap.put("BIRD", new UMLClassInfo("BIRD"));

        boolean result = Relationship.addRelationship("DOG", "BIRD", RelationshipType.GENERALIZATION);
        assertTrue(result);
        assertTrue(Relationship.relationshipExists("DOG", "BIRD"));
        assertEquals(1, Relationship.relationshipList.size());
    }

    /**
     * Tests adding an existing relationship between two classes.
     */
    @Test
    public void testAddExisitingRelationship() {
        UMLClass.classMap.put("DOG", new UMLClassInfo("DOG"));
        UMLClass.classMap.put("BIRD", new UMLClassInfo("BIRD"));
        Relationship.addRelationship("DOG", "BIRD", RelationshipType.GENERALIZATION);

        boolean result = Relationship.addRelationship("DOG", "BIRD", RelationshipType.GENERALIZATION);
        assertFalse(result, "Should be prompted that you can't add an existing class relationship");
    }

    /**
     * Tests removing an existing relationship between two classes.
     */
    @Test
    public void testRemoveRelationship() {
        UMLClass.classMap.put("DOG", new UMLClassInfo("DOG"));
        UMLClass.classMap.put("BIRD", new UMLClassInfo("BIRD"));
        Relationship.addRelationship("DOG", "BIRD", RelationshipType.GENERALIZATION);
        boolean result = Relationship.removeRelationship("DOG", "BIRD");
        assertTrue(result);
        assertFalse(Relationship.relationshipExists("DOG", "BIRD"));
        assertEquals(0, Relationship.relationshipList.size());
    }

    /**
     * Tests removing a non-existing relationship between two classes.
     */
    @Test
    public void testRemoveNonExistingRelationship() {
        UMLClass.classMap.put("DOG", new UMLClassInfo("DOG"));
        UMLClass.classMap.put("BIRD", new UMLClassInfo("BIRD"));

        boolean result = Relationship.removeRelationship("DOG", "BIRD");
        assertFalse(result);
    }

    /**
     * Tests removing all relationships attached to a specific class.
     */
    @Test
    public void testRemoveAttachedRelationship() {
        UMLClass.classMap.put("DOG", new UMLClassInfo("DOG"));
        UMLClass.classMap.put("BIRD", new UMLClassInfo("BIRD"));
        UMLClass.classMap.put("CAT", new UMLClassInfo("CAT"));

        Relationship.addRelationship("DOG", "BIRD", RelationshipType.GENERALIZATION);
        Relationship.addRelationship("DOG", "CAT", RelationshipType.GENERALIZATION);
        Relationship.removeAttachedRelationships("DOG");

        assertFalse(Relationship.relationshipExists("DOG", "BIRD"));
        assertFalse(Relationship.relationshipExists("DOG", "CAT"));
        assertEquals(0, Relationship.relationshipList.size());
    }

    /**
     * Tests adding a relationship with invalid class entries.
     */
    @Test
    public void testInvalidClassesForAddingRelationship() {
        UMLClass.classMap.put("DOG", new UMLClassInfo("DOG"));

        assertFalse(Relationship.addRelationship("DOG", "BIRD", RelationshipType.GENERALIZATION));
        assertFalse(Relationship.addRelationship("DOG", "CAT", RelationshipType.GENERALIZATION));
    }

    /**
     * Tests generating a string representation of all relationships in the system.
     */
    @Test
    public void testListToString() {
        UMLClass.classMap.put("DOG", new UMLClassInfo("DOG"));
        UMLClass.classMap.put("BIRD", new UMLClassInfo("BIRD"));
        UMLClass.classMap.put("CAT", new UMLClassInfo("CAT"));

        Relationship.addRelationship("DOG", "BIRD", RelationshipType.GENERALIZATION);
        Relationship.addRelationship("DOG", "CAT", RelationshipType.GENERALIZATION);

        String relationships = Relationship.listToString();
        String expected = "DOG -----|> BIRD Generalization\nDOG -----|> CAT Generalization\n";
        assertEquals(expected, relationships);
    }

    /**
     * Tests adding a relationship with empty class names.
     */
    @Test
    public void testAddRelationshipWithEmptyClasses() {
        assertFalse(Relationship.addRelationship("", "BIRD", RelationshipType.GENERALIZATION));
        assertFalse(Relationship.addRelationship(null, "BIRD", RelationshipType.GENERALIZATION));
    }

    /**
     * Tests removing a relationship with empty class names.
     */
    @Test
    public void testRemoveRelationshipWithEmptyClasses() {
        assertFalse(Relationship.removeRelationship("", "BIRD"));
        assertFalse(Relationship.removeRelationship(null, "BIRD"));
    }
    @Test
    public void testGetType(){
        UMLClass.classMap.put("DOG", new UMLClassInfo("DOG"));
        UMLClass.classMap.put("BIRD", new UMLClassInfo("BIRD"));
        Relationship.addRelationship("DOG", "BIRD", RelationshipType.GENERALIZATION);

        Relationship relationship = Relationship.relationshipList.getFirst();
        assertEquals(RelationshipType.GENERALIZATION, relationship.getType());
    }
    @Test
    public void testSetType(){
        UMLClass.classMap.put("DOG", new UMLClassInfo("DOG"));
        UMLClass.classMap.put("BIRD", new UMLClassInfo("BIRD"));
        Relationship.addRelationship("DOG", "BIRD", RelationshipType.GENERALIZATION);

        Relationship relationship = Relationship.relationshipList.getFirst();
        relationship.setType(RelationshipType.AGGREGATION);
        assertEquals(RelationshipType.AGGREGATION, relationship.getType());
    }
    @Test
    public void testGetClassNameAsArray(){
        UMLClass.classMap.put("DOG", new UMLClassInfo("DOG"));
        UMLClass.classMap.put("BIRD", new UMLClassInfo("BIRD"));
        Relationship.addRelationship("DOG", "BIRD", RelationshipType.COMPOSITION);

        Relationship relationship = Relationship.relationshipList.getFirst();
        String[] classes = relationship.getClassNamesAsArray();
        assertEquals(2, classes.length);
        assertEquals("BIRD", classes[1]);
        assertEquals("DOG", classes[0]);
    }
    @Test
    public void testGetRelationship(){
        UMLClass.classMap.put("DOG", new UMLClassInfo("DOG"));
        UMLClass.classMap.put("BIRD", new UMLClassInfo("BIRD"));
        Relationship.addRelationship("DOG", "BIRD", RelationshipType.AGGREGATION);

        Relationship relationship = Relationship.relationshipList.getFirst();
        assertNotNull(relationship);
        assertEquals(RelationshipType.AGGREGATION, relationship.getType());
    }
    @Test
    public void testGettingNonExistingRelationships(){
        UMLClass.classMap.put("DOG", new UMLClassInfo("DOG"));
        UMLClass.classMap.put("BIRD", new UMLClassInfo("BIRD"));
        assertThrows(IllegalArgumentException.class, () -> Relationship.getRelationship("Dog", "BIRD", RelationshipType.COMPOSITION));
    }
    @Test
    public void testRemoveAttachedRelationshipsWithNoRelationships(){
        UMLClass.classMap.put("DOG", new UMLClassInfo("DOG"));

        Relationship.removeAttachedRelationships("DOG");
        assertEquals(0, Relationship.relationshipList.size());
    }
    @Test
    public void testSetSource(){
        UMLClass.classMap.put("DOG", new UMLClassInfo("DOG"));
        UMLClass.classMap.put("BIRD", new UMLClassInfo("BIRD"));
        Relationship.addRelationship("DOG", "BIRD", RelationshipType.REALIZATION);

        Relationship relationship = Relationship.relationshipList.getFirst();

        relationship.setSource("BIRD");
        assertEquals("BIRD", relationship.getSource());

        assertThrows(IllegalArgumentException.class , () -> relationship.setSource("CAT"));

    }
    @Test
    public void testConstructor(){
        Relationship relationship = new Relationship();
        assertNotNull(relationship.getClassNames());
        assertTrue(relationship.getClassNames().isEmpty());
        assertEquals("",relationship.getSource());
        assertEquals("",relationship.getDestination());
        assertNull(relationship.getType());
    }

    @Test
    public void testGetDestination(){
        UMLClass.classMap.put("DOG", new UMLClassInfo("DOG"));
        UMLClass.classMap.put("BIRD", new UMLClassInfo("BIRD"));
        Relationship.addRelationship("DOG", "BIRD", RelationshipType.REALIZATION);

        Relationship relationship = Relationship.relationshipList.getFirst();
        assertEquals("BIRD", relationship.getDestination());
    }
    @Test
    public void testGetClassNames(){
        UMLClass.classMap.put("DOG", new UMLClassInfo("DOG"));
        UMLClass.classMap.put("BIRD", new UMLClassInfo("BIRD"));
        Relationship.addRelationship("DOG", "BIRD", RelationshipType.REALIZATION);

        Relationship relationship = Relationship.relationshipList.getFirst();
        Collection<String> classNames = relationship.getClassNames();

        assertEquals(2, classNames.size());
        assertTrue(classNames.contains("DOG"));
        assertTrue(classNames.contains("BIRD"));
    }
    @Test
    public void testSetClassNames(){
        Relationship relationship = new Relationship();
        Collection<String> classNames = new HashSet<>();
        classNames.add("DOG");
        classNames.add("BIRD");

        relationship.setClassNames(classNames);
        assertTrue(relationship.getClassNames().contains("DOG"));
        assertTrue(relationship.getClassNames().contains("BIRD"));

        assertEquals(2, relationship.getClassNames().size());
    }
    @Test
    public void testRelationshipHasClass(){
        UMLClass.classMap.put("DOG", new UMLClassInfo("DOG"));
        UMLClass.classMap.put("BIRD", new UMLClassInfo("BIRD"));
        Relationship.addRelationship("DOG", "BIRD", RelationshipType.REALIZATION);

        assertTrue(Relationship.relationshipHasClass("DOG"));
        assertTrue(Relationship.relationshipHasClass("BIRD"));
        assertFalse(Relationship.relationshipHasClass("IGUANA"));
    }
    @Test
    public void testRelationshipHasType(){
        UMLClass.classMap.put("DOG", new UMLClassInfo("DOG"));
        UMLClass.classMap.put("BIRD", new UMLClassInfo("BIRD"));
        Relationship.addRelationship("DOG", "BIRD", RelationshipType.COMPOSITION);

        assertTrue(Relationship.relationshipExists("DOG", "BIRD", RelationshipType.COMPOSITION));
        assertFalse(Relationship.relationshipExists( "DOG", "BIRD", RelationshipType.AGGREGATION));
    }
    @Test
    public void testGetAttachedRelationships(){
        UMLClass.classMap.put("DOG", new UMLClassInfo("DOG"));
        UMLClass.classMap.put("BIRD", new UMLClassInfo("BIRD"));
        UMLClass.classMap.put("TIGER", new UMLClassInfo("TIGER"));

        Relationship.addRelationship("DOG", "BIRD", RelationshipType.GENERALIZATION);
        Relationship.addRelationship("DOG","TIGER", RelationshipType.COMPOSITION);

        Relationship relationship = new Relationship();
        ArrayList<Relationship> attachedRelationships = relationship.getAttachedRelationships("DOG");

        assertEquals(2, attachedRelationships.size());
        assertTrue(attachedRelationships.stream().anyMatch(r -> r.getClassNames().contains("BIRD") && r.getType() == RelationshipType.GENERALIZATION));
        assertTrue(attachedRelationships.stream().anyMatch(r -> r.getClassNames().contains("TIGER") && r.getType() == RelationshipType.COMPOSITION));
    }

    @Test
    public void testGetClassNamesWithIncompleteRelationships(){
        PrintStream out = System.out;

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        try {
            Relationship relationship = new Relationship();
            relationship.setClassNames(new HashSet<>());

            String[] res = relationship.getClassNamesAsArray();

            assertEquals(0, res.length);

            String line = output.toString().trim();
            assertEquals("There are no classes to print out", line);
        } finally {
            System.setOut(out);
        }

    }







}
