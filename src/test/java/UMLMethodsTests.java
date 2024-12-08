import static org.junit.jupiter.api.Assertions.*;

import codecain.BackendCode.Model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The UMLMethodsTests class contains unit tests for the UMLMethods class and related functionality.
 * This includes adding, removing, renaming methods, and managing method parameters in UML class structures.
 */
public class UMLMethodsTests {
    private UMLMethods umlMethods;

    /**
     * Sets up the test environment by initializing the UMLMethods object and clearing the UMLClass map.
     * This ensures a clean slate for each test.
     */
    @BeforeEach
    void setUp() {
        UMLClass.classMap.clear();
        umlMethods = new UMLMethods();
    }

    /**
     * Cleans up the test environment by clearing the UMLClass map after each test.
     */
    @AfterEach
    void tearDown() {
        UMLClass.classMap.clear();
    }

    /**
     * Tests adding a method to an existing class.
     * Verifies the method is added to the class's methods list.
     */
    @Test
    public void testAddMethod() {
        UMLClass.addClass("Test");
        List<UMLParameterInfo> parameters = Arrays.asList(new UMLParameterInfo("int", "age"), new UMLParameterInfo("String", "name"));
        umlMethods.addMethod("Test", "AddDog", parameters);

        UMLClassInfo classInfo = UMLClass.classMap.get("Test");
        assertNotNull(classInfo);
        assertEquals(1, classInfo.getMethods().size());
    }

    /**
     * Tests adding a duplicate method to an existing class.
     * Ensures that duplicate methods with the same name and parameters are not added.
     */
    @Test
    public void testAddDuplicateMethod() {
        UMLClass.addClass("Test");
        List<UMLParameterInfo> parameters = Arrays.asList(new UMLParameterInfo("int", "age"), new UMLParameterInfo("String", "name"));
        umlMethods.addMethod("Test", "AddDog", parameters);
        umlMethods.addMethod("Test", "AddDog", parameters);

        UMLClassInfo classInfo = UMLClass.classMap.get("Test");
        assertEquals(1, classInfo.getMethods().size(), "Should not allow duplicate methods.");
    }

    /**
     * Tests adding a method to a non-existing class.
     * Ensures that the method is not added if the class does not exist.
     */
    @Test
    public void testAddMethodToNonExistingClass() {
        List<UMLParameterInfo> parameters = Arrays.asList(new UMLParameterInfo("int", "age"), new UMLParameterInfo("String", "name"));
        umlMethods.addMethod("Test", "AddDog", parameters);
        assertFalse(UMLClass.classMap.containsKey("Test"), "Should not add methods to a non-existing class.");
    }

    /**
     * Tests removing a method from an existing class.
     * Ensures the method is removed from the class's methods list.
     */
    @Test
    public void testRemoveMethod() {
        UMLClass.addClass("Test");
        List<UMLParameterInfo> parameters = Arrays.asList(new UMLParameterInfo("int", "age"), new UMLParameterInfo("String", "name"));
        umlMethods.addMethod("Test", "AddDog", parameters);
        umlMethods.removeMethod("Test", "AddDog");

        UMLClassInfo classInfo = UMLClass.classMap.get("Test");
        assertEquals(0, classInfo.getMethods().size());
    }

    /**
     * Tests removing a method from a non-existing class.
     * Verifies that the removal action is safely ignored.
     */
    @Test
    public void testRemoveMethodFromNonExistingClass() {
        umlMethods.removeMethod("Test", "AddDog");
        assertFalse(UMLClass.classMap.containsKey("Test"), "Should handle non-existing class gracefully.");
    }

    /**
     * Tests removing a non-existing method from an existing class.
     * Ensures that no action is taken if the method does not exist.
     */
    @Test
    public void testRemoveMethodFromNonExistingMethod() {
        UMLClass.addClass("Test");
        List<UMLParameterInfo> parameters = Arrays.asList(new UMLParameterInfo("int", "age"), new UMLParameterInfo("String", "name"));
        umlMethods.addMethod("Test", "AddDog", parameters);
        umlMethods.removeMethod("Test", "RemoveCat");

        UMLClassInfo classInfo = UMLClass.classMap.get("Test");
        assertEquals(1, classInfo.getMethods().size(), "Should not affect class when removing non-existing method.");
    }

    /**
     * Tests renaming an existing method within a class.
     * Ensures the method name is updated correctly.
     */
    @Test
    public void testRenameMethod() {
        UMLClass.addClass("Test");
        List<UMLParameterInfo> parameters = Arrays.asList(new UMLParameterInfo("int", "age"), new UMLParameterInfo("String", "name"));
        umlMethods.addMethod("Test", "AddDog", parameters);
        umlMethods.renameMethod("Test", "AddDog", "AddMultipleDogs");

        UMLClassInfo classInfo = UMLClass.classMap.get("Test");
        UMLMethodInfo methodInfo = classInfo.getMethodByName("AddMultipleDogs");

        assertNotNull(methodInfo);
        assertEquals("AddMultipleDogs", methodInfo.getMethodName());
    }

    /**
     * Tests renaming a non-existing method within a class.
     * Ensures no changes occur if the method does not exist.
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
     * Ensures no changes occur in such cases.
     */
    @Test
    public void testRenameMethodToExistingName() {
        UMLClass.addClass("Test");
        umlMethods.addMethod("Test", "AddDog", Arrays.asList(new UMLParameterInfo("int", "age")));
        umlMethods.addMethod("Test", "RemoveDog", Arrays.asList(new UMLParameterInfo("float", "weight")));
        umlMethods.renameMethod("Test", "AddDog", "RemoveDog");

        UMLClassInfo classInfo = UMLClass.classMap.get("Test");
        assertNotNull(classInfo.getMethodByName("AddDog"));
        assertNotNull(classInfo.getMethodByName("RemoveDog"));
        assertEquals(2, classInfo.getMethods().size());
    }

    /**
     * Tests adding a parameter to an existing method within a class.
     * Ensures the parameter is added correctly.
     */
    @Test
    public void testAddParm() {
        UMLClass.addClass("Test");
        List<UMLParameterInfo> parameters = Arrays.asList(new UMLParameterInfo("int", "DogCounter"));
        umlMethods.addMethod("Test", "AddDog", parameters);

        umlMethods.addParameter("Test", "AddDog", "String", "PuppyCounter");
        UMLClassInfo classInfo = UMLClass.classMap.get("Test");
        UMLMethodInfo methodInfo = classInfo.getMethodByName("AddDog");

        assertTrue(methodInfo.getParameters().contains(new UMLParameterInfo("String", "PuppyCounter")));
    }

    /**
     * Tests changing a parameter of a method.
     * Verifies the parameter is replaced correctly.
     */
    @Test
    public void testChangeParameters() {
        UMLMethodInfo methodInfo = new UMLMethodInfo("addDog", new ArrayList<>());

        UMLParameterInfo old = new UMLParameterInfo("int", "DogCounter");
        UMLParameterInfo old1 = new UMLParameterInfo("boolean", "IsPuppy");
        methodInfo.addParameter(old);
        methodInfo.addParameter(old1);

        UMLParameterInfo newParam = new UMLParameterInfo("string", "dogString");

        methodInfo.changeParameter(old, newParam);

        List<UMLParameterInfo> expected = Arrays.asList(newParam, old1);
        assertEquals(expected, methodInfo.getParameters());
    }
}
