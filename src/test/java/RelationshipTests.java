import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;

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
 * Test class for {@link Relationship}.
 * Provides unit tests for validating the functionality of adding, removing, and managing relationships between UML classes.
 */
public class RelationshipTests {

    /**
     * Nested class containing tests specific to relationship management between UML classes.
     */
    @Nested
    class relationshipTests {

        /**
         * Sets up the test environment by initializing the relationship list and clearing the UMLClass map.
         * Ensures a clean slate before each test.
         */
        @BeforeEach
        public void setUp() {
            Relationship.relationshipList = new ArrayList<>();
            UMLClass.classMap.clear();
        }

        /**
         * Cleans up the test environment by clearing the relationship list and UMLClass map after each test.
         */
        @AfterEach
        public void tearDown() {
            Relationship.relationshipList.clear();
            UMLClass.classMap.clear();
        }

        /**
         * Tests adding a valid relationship between two existing UML classes.
         * Verifies that the relationship is successfully added and can be queried.
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
         * Tests attempting to add an existing relationship.
         * Verifies that duplicate relationships are not allowed.
         */
        @Test
        public void testAddExisitingRelationship() {
            UMLClass.classMap.put("DOG", new UMLClassInfo("DOG"));
            UMLClass.classMap.put("BIRD", new UMLClassInfo("BIRD"));
            Relationship.addRelationship("DOG", "BIRD", RelationshipType.GENERALIZATION);

            boolean result = Relationship.addRelationship("DOG", "BIRD", RelationshipType.GENERALIZATION);
            assertFalse(result, "Should not allow adding a duplicate relationship");
        }

        /**
         * Tests removing an existing relationship between two classes.
         * Verifies that the relationship is successfully removed and no longer exists.
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
         * Tests attempting to remove a relationship that does not exist.
         * Verifies that the operation fails and the state remains unchanged.
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
         * Verifies that all related relationships are correctly removed.
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
         * Tests adding relationships with invalid class names.
         * Verifies that relationships cannot be added if the class names are not valid or do not exist.
         */
        @Test
        public void testInvalidClassesForAddingRelationship() {
            UMLClass.classMap.put("DOG", new UMLClassInfo("DOG"));

            assertFalse(Relationship.addRelationship("DOG", "BIRD", RelationshipType.GENERALIZATION));
            assertFalse(Relationship.addRelationship("DOG", "CAT", RelationshipType.GENERALIZATION));
        }

        /**
         * Tests generating a string representation of all relationships in the system.
         * Verifies that the string output matches the expected format.
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
         * Tests adding relationships with empty or null class names.
         * Verifies that invalid inputs are correctly rejected.
         */
        @Test
        public void testAddRelationshipWithEmptyClasses() {
            assertFalse(Relationship.addRelationship("", "BIRD", RelationshipType.GENERALIZATION));
            assertFalse(Relationship.addRelationship(null, "BIRD", RelationshipType.GENERALIZATION));
        }

        /**
         * Tests removing relationships with empty or null class names.
         * Verifies that invalid inputs are correctly rejected.
         */
        @Test
        public void testRemoveRelationshipWithEmptyClasses() {
            assertFalse(Relationship.removeRelationship("", "BIRD"));
            assertFalse(Relationship.removeRelationship(null, "BIRD"));
        }
    }

    /**
     * Tests getting the type of a relationship.
     * Verifies that the correct relationship type is returned.
     */
    @Test
    public void testGetType() {
        UMLClass.classMap.put("DOG", new UMLClassInfo("DOG"));
        UMLClass.classMap.put("BIRD", new UMLClassInfo("BIRD"));
        Relationship.addRelationship("DOG", "BIRD", RelationshipType.COMPOSITION);

        Relationship relationship = Relationship.relationshipList.getFirst();
        assertEquals(RelationshipType.COMPOSITION, relationship.getType());
    }

    /**
     * Tests setting the type of a relationship.
     * Verifies that the relationship type is successfully updated.
     */
    @Test
    public void testSetType() {
        UMLClass.classMap.put("DOG", new UMLClassInfo("DOG"));
        UMLClass.classMap.put("BIRD", new UMLClassInfo("BIRD"));
        Relationship.addRelationship("DOG", "BIRD", RelationshipType.GENERALIZATION);

        Relationship relationship = Relationship.relationshipList.getFirst();
        relationship.setType(RelationshipType.AGGREGATION);
        assertEquals(RelationshipType.AGGREGATION, relationship.getType());
    }

    /**
     * Tests retrieving the class names in a relationship as an array.
     * Verifies that the correct names are returned in the expected order.
     */
    @Test
    public void testGetClassNameAsArray() {
        UMLClass.classMap.put("DOG", new UMLClassInfo("DOG"));
        UMLClass.classMap.put("BIRD", new UMLClassInfo("BIRD"));
        Relationship.addRelationship("DOG", "BIRD", RelationshipType.COMPOSITION);

        Relationship relationship = Relationship.relationshipList.getFirst();
        String[] classes = relationship.getClassNamesAsArray();
        assertEquals(2, classes.length);
        assertEquals("BIRD", classes[1]);
        assertEquals("DOG", classes[0]);
    }

    /**
     * Tests retrieving attached relationships for a class.
     * Verifies that all relationships associated with the specified class are correctly returned.
     */
    @Test
    public void testGetAttachedRelationships() {
        UMLClass.classMap.put("DOG", new UMLClassInfo("DOG"));
        UMLClass.classMap.put("CAT", new UMLClassInfo("CAT"));
        UMLClass.classMap.put("BIRD", new UMLClassInfo("BIRD"));
        UMLClass.classMap.put("TIGER", new UMLClassInfo("TIGER"));


        Relationship.addRelationship("DOG", "TIGER", RelationshipType.COMPOSITION);

        Relationship relationship = new Relationship();
        ArrayList<Relationship> attachedRelationships = relationship.getAttachedRelationships("DOG");

        assertEquals(2, attachedRelationships.size());

        boolean hasComp = attachedRelationships.stream().anyMatch(r -> "DOG".equals(r.getSource())
                && "TIGER".equals(r.getDestination()) && RelationshipType.COMPOSITION.equals(r.getType()));

        assertTrue(hasComp);

    }

    /**
     * Tests handling incomplete relationships with no class names.
     * Verifies that the system handles the edge case gracefully and outputs the expected message.
     */
    @Test
    public void testGetClassNamesWithIncompleteRelationships() {
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
    /**
     * Tests the functionality of checking if a class is part of any relationship.
     * Verifies that the relationship correctly identifies classes involved in it.
     */
    @Test
    public void testRelationshipHasClass() {
        // Add classes to the UMLClass map
        UMLClass.classMap.put("DOG", new UMLClassInfo("DOG"));
        UMLClass.classMap.put("BIRD", new UMLClassInfo("BIRD"));

        // Add a relationship between DOG and BIRD
        Relationship.addRelationship("DOG", "BIRD", RelationshipType.COMPOSITION);

        // Verify that the relationship recognizes the involved classes
        assertTrue(Relationship.relationshipHasClass("BIRD"), "Relationship should recognize the class 'BIRD'.");
        assertTrue(Relationship.relationshipHasClass("DOG"), "Relationship should recognize the class 'DOG'.");
        assertFalse(Relationship.relationshipHasClass("CAT"), "Relationship should not recognize the class 'CAT' as it's not part of any relationship.");
    }

    /**
     * Tests the functionality of verifying if a relationship exists between specific classes
     * with a specified relationship type.
     */
    @Test
    public void testRelationshipExistsWithType() {
        // Add classes to the UMLClass map
        UMLClass.classMap.put("DOG", new UMLClassInfo("DOG"));
        UMLClass.classMap.put("BIRD", new UMLClassInfo("BIRD"));

        // Add a COMPOSITION relationship between DOG and BIRD
        Relationship.addRelationship("DOG", "BIRD", RelationshipType.COMPOSITION);

        // Verify relationships with correct and incorrect types or classes
        assertTrue(Relationship.relationshipExists("DOG", "BIRD", RelationshipType.COMPOSITION), "Relationship of type COMPOSITION should exist between DOG and BIRD.");
        assertFalse(Relationship.relationshipExists("DOG", "CAT", RelationshipType.COMPOSITION), "No relationship should exist between DOG and CAT.");
        assertFalse(Relationship.relationshipExists("DOG", "BIRD", RelationshipType.AGGREGATION), "Relationship of type AGGREGATION should not exist between DOG and BIRD.");
    }

    /**
     * Tests the functionality of retrieving a specific relationship between classes.
     * Verifies that the retrieved relationship matches the specified type and involved classes.
     */
    @Test
    public void testGetRelationships() {
        // Add classes to the UMLClass map
        UMLClass.classMap.put("DOG", new UMLClassInfo("DOG"));
        UMLClass.classMap.put("BIRD", new UMLClassInfo("BIRD"));

        // Add a COMPOSITION relationship between DOG and BIRD
        Relationship.addRelationship("DOG", "BIRD", RelationshipType.COMPOSITION);

        // Retrieve the relationship and verify its properties
        Relationship relationship = Relationship.getRelationship("DOG", "BIRD", RelationshipType.COMPOSITION);
        assertNotNull(relationship, "The relationship should not be null.");
        assertEquals(RelationshipType.COMPOSITION, relationship.getType(), "The relationship type should match COMPOSITION.");
        assertEquals("BIRD", relationship.getDestination(), "The destination should match 'BIRD'.");
        assertEquals("DOG", relationship.getSource(), "The source should match 'DOG'.");
    }
}
