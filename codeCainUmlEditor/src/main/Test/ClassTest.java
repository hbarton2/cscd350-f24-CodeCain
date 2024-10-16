import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for the Class class, which handles adding, removing, renaming,
 * and case sensitivity of UML classes stored in a HashMap.
 */
public class ClassTest {

    /**
     * Tests that a class is successfully added to the class map.
     * Ensures that the class name exists in the map after addition.
     */
    @Test
    public void testAddClass() {
        Class.addClass("Panthera");
        assertTrue(Class.classMap.containsKey("Panthera"), "Class should be added to the map.");
    }

    /**
     * Tests that a duplicate class is not added to the class map.
     * Ensures that adding a class with the same name twice does not change the map size.
     */
    @Test
    public void testAddDuplicateClass() {
        Class.addClass("Tiger");
        Class.addClass("Tiger");
        assertEquals(1, Class.classMap.size(), "The second Tiger class should not be added.");
    }

    /**
     * Tests that a class is successfully removed from the class map.
     * Ensures that the class no longer exists in the map after removal.
     */
    @Test
    public void testRemoveClass() {
        Class.addClass("Dog");
        Class.removeClass("Dog");
        assertFalse(Class.classMap.containsKey("Dog"), "Dog class should be removed from the map.");
    }

    /**
     * Tests that removing a non-existent class does not affect the class map.
     * Ensures the map size remains unchanged when attempting to remove a class that doesn't exist.
     */
    @Test
    public void testRemoveNonExistentClass() {
        int initialSize = Class.classMap.size();
        Class.removeClass("CasperClass");
        assertEquals(initialSize, Class.classMap.size(), "Map size should not change when removing a non-existent class.");
    }

    /**
     * Tests the renaming functionality of a class in the class map.
     * Ensures that after renaming, the class exists under the new name, and the old name is no longer in the map.
     */
    @Test
    public void testRenameClass() {
        Class.addClass("Lion");
        Class.renameClass("Lion", "Liger");
        assertTrue(Class.classMap.containsKey("Liger"), "Class should be renamed in the map.");
        assertFalse(Class.classMap.containsKey("Lion"), "Old class name should be removed from the map.");
    }

    /**
     * Tests case sensitivity of class names in the class map.
     * Ensures that class names are treated as case-sensitive (e.g., "TestClass" and "testclass" are distinct).
     */
    @Test
    public void testCaseSensitivity() {
        Class.addClass("Big O");
        Class.addClass("big O");
        assertEquals(2, Class.classMap.size(), "Class names should be case-sensitive (TestClass and testclass should be distinct).");
        assertTrue(Class.classMap.containsKey("Big O"), "Uppercase class should be in the map.");
        assertTrue(Class.classMap.containsKey("big o"), "Lowercase class should be in the map.");
    }
}
