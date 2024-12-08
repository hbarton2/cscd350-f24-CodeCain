import static org.junit.jupiter.api.Assertions.*;

import codecain.BackendCode.Model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import java.util.ArrayList;
import java.util.List;

/**
 * The UMLClassTests class contains unit tests to validate the functionality of the UMLClass class.
 * It tests adding, removing, renaming UML classes, and operations like listing and retrieving class information.
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
     * Verifies the class is correctly added.
     */
    @Test
    public void testAddClass() {
        UMLClass.addClass("DOG");
        assertTrue(UMLClass.classMap.containsKey("DOG"), "Class DOG should be added to the map.");
    }

    /**
     * Tests adding a duplicate class to the class map.
     * Ensures that duplicate entries are not allowed.
     */
    @Test
    public void testAddDuplicatedClass() {
        UMLClass.addClass("DOG");
        UMLClass.addClass("DOG");
        assertEquals(1, UMLClass.classMap.size(), "Duplicate class should not be added to the map.");
    }

    /**
     * Tests adding a class with a null name.
     * Verifies the operation is safely ignored.
     */
    @Test
    public void testAddClassNullName() {
        UMLClass.addClass(null);
        assertFalse(UMLClass.classMap.containsKey(null), "Class with null name should not be added.");
    }

    /**
     * Tests adding a class with a blank name.
     * Verifies the operation is safely ignored.
     */
    @Test
    public void testAddClassBlankName() {
        UMLClass.addClass("");
        assertFalse(UMLClass.classMap.containsKey(""), "Class with blank name should not be added.");
    }

    /**
     * Tests removing a class from the class map.
     * Ensures the class is successfully removed.
     */
    @Test
    public void testRemoveClass() {
        UMLClass.addClass("DOG");
        UMLClass.removeClass("DOG");
        assertFalse(UMLClass.classMap.containsKey("DOG"), "Class DOG should be removed from the map.");
    }

    /**
     * Tests removing a non-existing class from the class map.
     * Verifies that the operation does not affect the map.
     */
    @Test
    public void testRemoveNonExistingClass() {
        UMLClass.removeClass("DOG");
        assertFalse(UMLClass.classMap.containsKey("DOG"), "Non-existing class should not affect the map.");
    }

    /**
     * Tests renaming an existing class in the class map.
     * Verifies the class is renamed correctly.
     */
    @Test
    public void testRenamingClass() {
        UMLClass.addClass("DOG");
        UMLClass.renameClass("DOG", "CAT");
        assertFalse(UMLClass.classMap.containsKey("DOG"), "Old class name should not exist.");
        assertTrue(UMLClass.classMap.containsKey("CAT"), "New class name should exist.");
    }

    /**
     * Tests renaming a class to a name that already exists.
     * Verifies that the operation is not allowed.
     */
    @Test
    public void testRenamingThatAlreadyExists() {
        UMLClass.addClass("DOG");
        UMLClass.addClass("CAT");
        UMLClass.renameClass("DOG", "CAT");
        assertTrue(UMLClass.classMap.containsKey("DOG"), "Old class name should still exist.");
        assertTrue(UMLClass.classMap.containsKey("CAT"), "New class name should remain unchanged.");
    }

    /**
     * Tests renaming a non-existing class in the class map.
     * Verifies that no changes are made.
     */
    @Test
    public void testRenamingNonExistingClass() {
        UMLClass.renameClass("DOG", "CAT");
        assertFalse(UMLClass.classMap.containsKey("DOG"), "Non-existing old class name should not be added.");
        assertFalse(UMLClass.classMap.containsKey("CAT"), "Non-existing new class name should not be added.");
    }

    /**
     * Tests listing all classes when multiple classes exist.
     * Verifies that all classes are listed correctly.
     */
    @Test
    public void testListAllClasses() {
        UMLClass.addClass("DOG");
        UMLClass.addClass("CAT");
        String res = UMLClass.listAllClassesInfo();
        assertTrue(res.contains("DOG"), "Listed classes should include DOG.");
        assertTrue(res.contains("CAT"), "Listed classes should include CAT.");
    }

    /**
     * Tests listing all classes when no classes exist.
     * Verifies the operation returns an appropriate message.
     */
    @Test
    public void testListAllClassesWithNoClasses() {
        String res = UMLClass.listAllClassesInfo();
        assertEquals("No classes to display.", res, "Should return a message indicating no classes exist.");
    }

    /**
     * Tests retrieving class information for a specific class.
     * Verifies the correct class information is returned.
     */
    @Test
    public void testGetClassInfo() {
        UMLClass.addClass("DOG");
        UMLClassInfo info = UMLClass.getClassInfo("DOG");
        assertNotNull(info, "Class information should be retrieved for DOG.");
        assertEquals("DOG", info.getClassName(), "Class name should match DOG.");
    }

    /**
     * Tests retrieving class information and validating its fields and methods.
     * Ensures fields and methods are empty initially, and updates are reflected correctly.
     */
    @Test
    public void testGetClassInfoFieldsAndMethods() {
        UMLClass.addClass("DOG");
        UMLClassInfo info = UMLClass.getClassInfo("DOG");

        // Initially empty fields and methods
        assertTrue(info.getFields().isEmpty(), "Fields should be empty initially.");
        assertTrue(info.getMethods().isEmpty(), "Methods should be empty initially.");

        // Add fields
        List<UMLFieldInfo> fields = new ArrayList<>();
        fields.add(new UMLFieldInfo("NAME", "String"));
        fields.add(new UMLFieldInfo("TYPE", "String"));
        info.getFields().addAll(fields);
        assertEquals(2, info.getFields().size(), "Two fields should be added.");

        // Add methods
        List<UMLMethodInfo> methods = new ArrayList<>();
        methods.add(new UMLMethodInfo("getAge", new ArrayList<>()));
        methods.add(new UMLMethodInfo("setName", List.of(new UMLParameterInfo("NAME", "String"))));
        info.getMethods().addAll(methods);
        assertEquals(2, info.getMethods().size(), "Two methods should be added.");
    }

    /**
     * Tests setting a new class name for a class.
     * Verifies the class name is updated correctly.
     */
    @Test
    public void testSetClassName() {
        UMLClass.addClass("DOG");
        UMLClassInfo info = UMLClass.getClassInfo("DOG");
        info.setClassName("CAT");
        assertEquals("CAT", info.getClassName(), "Class name should be updated to CAT.");
    }

    /**
     * Tests setting the position (x, y) of a class.
     * Verifies the position is updated correctly.
     */
    @Test
    public void testSetPosition() {
        UMLClass.addClass("DOG");
        UMLClassInfo info = UMLClass.getClassInfo("DOG");
        info.setX(100);
        info.setY(200);
        assertEquals(100, info.getX(), "X-coordinate should match 100.");
        assertEquals(200, info.getY(), "Y-coordinate should match 200.");
    }
}
