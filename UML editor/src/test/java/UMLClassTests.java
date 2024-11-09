import static org.junit.jupiter.api.Assertions.*;

import codecain.BackendCode.UMLClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

/**
 * The UMLClassTests class contains test cases to validate the functionality of the UMLClass class.
 * It tests scenarios like adding, removing, and renaming UML classes in the class map.
 */
public class UMLClassTests {

    /**
     * Sets up the test environment by clearing the UMLClass map before each test.
     */
    @BeforeEach
    public void setUp() {
        UMLClass.classMap.clear();
    }

    /**
     * Cleans up the test environment by clearing the UMLClass map after each test.
     */
    @AfterEach
    public void tearDown() {
        UMLClass.classMap.clear();
    }

    /**
     * Tests adding a class to the class map.
     */
    @Test
    public void testAddClass() {
        UMLClass.addClass("DOG");
        assertTrue(UMLClass.classMap.containsKey("DOG"));
    }

    /**
     * Tests adding a duplicate class to the class map.
     */
    @Test
    public void testAddDuplicatedClass() {
        UMLClass.addClass("DOG");
        UMLClass.addClass("DOG");
        assertEquals(1, UMLClass.classMap.size(), "Should prompt you can't add a class that already exists");
    }

    /**
     * Tests removing a class from the class map.
     */
    @Test
    public void testRemoveClass() {
        UMLClass.addClass("DOG");
        UMLClass.removeClass("DOG");
        assertFalse(UMLClass.classMap.containsKey("DOG"));
    }

    /**
     * Tests removing a non-existing class from the class map.
     */
    @Test
    public void testRemoveNonExistingClass() {
        UMLClass.removeClass("DOG");
        assertFalse(UMLClass.classMap.containsKey("DOG"), "Should prompt you cannot delete a class that isn't there");
    }

    /**
     * Tests renaming an existing class in the class map.
     */
    @Test
    public void testRenamingClass() {
        UMLClass.addClass("DOG");
        UMLClass.renameClass("DOG", "CAT");
        assertFalse(UMLClass.classMap.containsKey("DOG"));
        assertTrue(UMLClass.classMap.containsKey("CAT"));
    }

    /**
     * Tests renaming a class to a name that already exists in the class map.
     */
    @Test
    public void testRenamingThatAlreadyExists() {
        UMLClass.addClass("DOG");
        UMLClass.addClass("CAT");
        UMLClass.renameClass("DOG", "CAT");
        // These should both be true because the renaming should fail
        assertTrue(UMLClass.classMap.containsKey("DOG"));
        assertTrue(UMLClass.classMap.containsKey("CAT"));
    }

    /**
     * Tests renaming a non-existing class in the class map.
     */
    @Test
    public void testRenamingNonExistingClass() {
        UMLClass.renameClass("DOG", "CAT");
        assertFalse(UMLClass.classMap.containsKey("DOG"));
        assertFalse(UMLClass.classMap.containsKey("CAT"));
    }
}
