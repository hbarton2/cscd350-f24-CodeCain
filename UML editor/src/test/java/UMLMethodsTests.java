import static org.junit.jupiter.api.Assertions.*;

import codecain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The UMLMethodsTests class contains test cases to validate the functionality of UMLMethods class.
 * It tests scenarios like adding, removing, renaming methods, and managing method parameters.
 */
public class UMLMethodsTests {
    private UMLMethods umlMethods;

    /**
     * Sets up the test environment by initializing the UMLMethods object and clearing the UMLClass map.
     */
    @BeforeEach
    void setUp() {
        UMLClass.classMap.clear();
        umlMethods = new UMLMethods();
    }

    /**
     * Cleans up the test environment by clearing the UMLClass map.
     */
    @AfterEach
    void tearDown() {
        UMLClass.classMap.clear();
    }

    /**
     * Tests adding a method to an existing class.
     */
    @Test
    public void testAddMethod(){
        UMLClass.addClass("Test");
        umlMethods.addMethod("Test", "AddDog", Arrays.asList("int","String"));

        UMLClassInfo classInfo = UMLClass.classMap.get("Test");
        assertNotNull(classInfo);
        assertEquals(1, classInfo.getMethods().size());
    }

    /**
     * Tests adding a duplicate method to an existing class.
     */
    @Test
    public void testAddDuplicateMethod(){
        UMLClass.addClass("Test");
        umlMethods.addMethod("Test", "AddDog", Arrays.asList("int","String"));
        umlMethods.addMethod("Test", "AddDog", Arrays.asList("int","String"));

        UMLClassInfo classInfo = UMLClass.classMap.get("Test");
        assertEquals(1, classInfo.getMethods().size(), "Should be prompted to not be able to add a duplicate method");
    }

    /**
     * Tests adding a method to a non-existing class.
     */
    @Test
    public void testAddMethodToNonExistingClass() {
        umlMethods.addMethod("Test", "AddDog", Arrays.asList("int","String"));
        assertFalse(UMLClass.classMap.containsKey("Test"), "Should be prompted you can't add a method to a class that doesn't exist");
    }

    /**
     * Tests removing a method from an existing class.
     */
    @Test
    public void testRemoveMethod(){
        UMLClass.addClass("Test");
        umlMethods.addMethod("Test", "AddDog", Arrays.asList("int","String"));
        umlMethods.removeMethod("Test", "AddDog");

        UMLClassInfo classInfo = UMLClass.classMap.get("Test");
        assertEquals(0, classInfo.getMethods().size());
    }

    /**
     * Tests removing a method from a non-existing class.
     */
    @Test
    public void testRemoveMethodFromNonExistingClass() {
        umlMethods.removeMethod("Test", "AddDog");
        assertFalse(UMLClass.classMap.containsKey("Test"), "Should be prompted the class doesn't exist");
    }

    /**
     * Tests removing a non-existing method from an existing class.
     */
    @Test
    public void testRemoveMethodFromNonExistingMethod() {
        UMLClass.addClass("Test");
        umlMethods.addMethod("Test", "AddDog", Arrays.asList("int","String"));
        umlMethods.removeMethod("Test", "RemoveCat");

        UMLClassInfo classInfo = UMLClass.classMap.get("Test");
        assertEquals(1, classInfo.getMethods().size(), "Shouldn't be prompted to enter a valid method");
    }

    /**
     * Tests renaming an existing method within a class.
     */
    @Test
    public void testRenameMethod(){
        UMLClass.addClass("Test");
        umlMethods.addMethod("Test", "AddDog", Arrays.asList("int","String"));
        umlMethods.renameMethod("Test", "AddDog", "AddMultipleDogs");

        UMLClassInfo classInfo = UMLClass.classMap.get("Test");
        UMLMethodInfo methodInfo = classInfo.getMethodByName("AddMultipleDogs");

        assertNotNull(methodInfo);
        assertEquals("AddMultipleDogs", methodInfo.getMethodName());
    }

    /**
     * Tests renaming a non-existing method within a class.
     */
    @Test
    public void testRenameMethodFromNonExistingMethod() {
        UMLClass.addClass("Test");
        umlMethods.renameMethod("Test", "AddDog", "AddMultipleDogs");
        UMLClassInfo classInfo = UMLClass.classMap.get("Test");
        assertNull(classInfo.getMethodByName("AddMultipleDogs"));
    }

    /**
     * Tests renaming a method to an already existing method's name within the same class.
     */
    @Test
    public void testRenameMethodToExistingName(){
        UMLClass.addClass("Test");
        umlMethods.addMethod("Test", "AddDog", Arrays.asList("int","String"));
        umlMethods.addMethod("Test", "RemoveDog", Arrays.asList("float"));
        umlMethods.renameMethod("Test", "AddDog", "RemoveDog");

        UMLClassInfo classInfo = UMLClass.classMap.get("Test");
        // Should not change either name
        assertNotNull(classInfo.getMethodByName("AddDog"));
        assertNotNull(classInfo.getMethodByName("RemoveDog"));
        assertEquals(2, classInfo.getMethods().size());
    }

    /**
     * Tests adding a parameter to an existing method within a class.
     */
    @Test
    public void testAddParm(){
        UMLClass.addClass("Test");
        umlMethods.addMethod("Test", "AddDog", Arrays.asList("DogCounter"));

        umlMethods.addParameter("Test", "AddDog", "PuppyCounter");
        UMLClassInfo classInfo = UMLClass.classMap.get("Test");
        UMLMethodInfo methodInfo = classInfo.getMethodByName("AddDog");
        assertTrue(methodInfo.getParameters().contains("PuppyCounter"));
    }

    /**
     * Tests removing a parameter from an existing method within a class.
     */
    @Test
    public void testRemoveParm(){
        UMLClass.addClass("Test");
        umlMethods.addMethod("Test", "AddDog", Arrays.asList("DogCounter"));
        umlMethods.removeParameter("Test", "AddDog", "DogCounter");

        UMLClassInfo classInfo = UMLClass.classMap.get("Test");
        UMLMethodInfo methodInfo = classInfo.getMethodByName("AddDog");
        assertFalse(methodInfo.getParameters().contains("DogCounter"));
    }

    /**
     * Tests changing the parameters of an existing method within a class.
     */
    @Test
    public void testChangeParm(){
        UMLClass.addClass("Test");
        umlMethods.addMethod("Test", "AddDog", Arrays.asList("DogCounter"));
        umlMethods.addParameter("Test", "AddDog", "PuppyCounter");
        umlMethods.changeParameter("Test", "AddDog", "PuppyCounter", "CutePuppyCounter");
        UMLClassInfo classInfo = UMLClass.classMap.get("Test");
        UMLMethodInfo methodInfo = classInfo.getMethodByName("AddDog");
        List<String> expectedParameters = Arrays.asList("DogCounter","CutPuppyCounter");
        assertEquals(expectedParameters, methodInfo.getParameters());
    }
}
