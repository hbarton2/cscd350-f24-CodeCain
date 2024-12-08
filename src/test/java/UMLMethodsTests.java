import static org.junit.jupiter.api.Assertions.*;

import codecain.BackendCode.Model.*;
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
        List<UMLParameterInfo> parameters = Arrays.asList(new UMLParameterInfo("int", "age"), new UMLParameterInfo("String", "name"));
        umlMethods.addMethod("Test", "AddDog", parameters);

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
        List<UMLParameterInfo> parameters = Arrays.asList(new UMLParameterInfo("int", "age"), new UMLParameterInfo("String", "name"));
        umlMethods.addMethod("Test", "AddDog", parameters);
        umlMethods.addMethod("Test", "AddDog", parameters);

        UMLClassInfo classInfo = UMLClass.classMap.get("Test");
        assertEquals(1, classInfo.getMethods().size(), "Should be prompted to not be able to add a duplicate method");
    }

    /**
     * Tests adding a method to a non-existing class.
     */
    @Test
    public void testAddMethodToNonExistingClass() {
        List<UMLParameterInfo> parameters = Arrays.asList(new UMLParameterInfo("int", "age"), new UMLParameterInfo("String", "name"));
        umlMethods.addMethod("Test", "AddDog", parameters);
        assertFalse(UMLClass.classMap.containsKey("Test"), "Should be prompted you can't add a method to a class that doesn't exist");
    }

    /**
     * Tests removing a method from an existing class.
     */
    @Test
    public void testRemoveMethod(){
        UMLClass.addClass("Test");
        List<UMLParameterInfo> parameters = Arrays.asList(new UMLParameterInfo("int", "age"), new UMLParameterInfo("String", "name"));
        umlMethods.addMethod("Test", "AddDog", parameters);
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
        List<UMLParameterInfo> parameters = Arrays.asList(new UMLParameterInfo("int", "age"), new UMLParameterInfo("String", "name"));
        umlMethods.addMethod("Test", "AddDog", parameters);
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
        umlMethods.addMethod("Test", "AddDog", Arrays.asList(new UMLParameterInfo("int", "age")));
        umlMethods.addMethod("Test", "RemoveDog", Arrays.asList(new UMLParameterInfo("float", "weight")));
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
        List<UMLParameterInfo> parameters = Arrays.asList(new UMLParameterInfo("int", "DogCounter"));
        umlMethods.addMethod("Test", "AddDog", parameters);

        umlMethods.addParameter("Test", "AddDog", "String", "PuppyCounter");
        UMLClassInfo classInfo = UMLClass.classMap.get("Test");
        UMLMethodInfo methodInfo = classInfo.getMethodByName("AddDog");

        assertTrue(methodInfo.getParameters().contains(new UMLParameterInfo("String", "PuppyCounter")));
    }

    /**
     * Tests removing a parameter from an existing method within a class.
     */
    @Test
    public void testRemoveParm(){
        UMLClass.addClass("Test");
        List<UMLParameterInfo> parameters = Arrays.asList(new UMLParameterInfo("int", "DogCounter"));
        umlMethods.addMethod("Test", "AddDog", parameters);
        umlMethods.removeParameter("Test", "AddDog", "DogCounter");

        UMLClassInfo classInfo = UMLClass.classMap.get("Test");
        UMLMethodInfo methodInfo = classInfo.getMethodByName("AddDog");
        assertFalse(methodInfo.getParameters().contains(new UMLParameterInfo("int", "DogCounter")));
    }

    /**
     * Tests changing a single parameter of an existing method within a class.
     */
    @Test
    public void testChangeSingleParameter() {
        UMLClass.addClass("Test");
        List<UMLParameterInfo> parameters = Arrays.asList(new UMLParameterInfo("int", "DogCounter"));
        umlMethods.addMethod("Test", "AddDog", parameters);
        umlMethods.addParameter("Test", "AddDog", "String", "PuppyCounter");
        umlMethods.changeSingleParameter("Test", "AddDog", "PuppyCounter", "String", "CutePuppyCounter");
        UMLClassInfo classInfo = UMLClass.classMap.get("Test");
        UMLMethodInfo methodInfo = classInfo.getMethodByName("AddDog");
        List<UMLParameterInfo> expectedParameters = Arrays.asList(
                new UMLParameterInfo("int", "DogCounter"),
                new UMLParameterInfo("String", "CutePuppyCounter")
        );
        assertEquals(expectedParameters, methodInfo.getParameters());
    }

    /**
     * Tests changing all parameters of an existing method within a class.
     */
    @Test
    public void testChangeAllParameters() {
        UMLClass.addClass("Test");
        List<UMLParameterInfo> initialParameters = Arrays.asList(new UMLParameterInfo("int", "DogCounter"));
        umlMethods.addMethod("Test", "AddDog", initialParameters);
        umlMethods.addParameter("Test", "AddDog", "String", "PuppyCounter");
        List<UMLParameterInfo> newParameters = Arrays.asList(
                new UMLParameterInfo("double", "BigDogCounter"),
                new UMLParameterInfo("boolean", "IsPuppy")
        );
        umlMethods.changeAllParameters("Test", "AddDog", newParameters);
        UMLClassInfo classInfo = UMLClass.classMap.get("Test");
        UMLMethodInfo methodInfo = classInfo.getMethodByName("AddDog");
        assertEquals(newParameters, methodInfo.getParameters());
    }
    /**
     * Tests that parameters are deeply copied when a method is created.
     */
    @Test
    public void testDeepCopyParameters() {
        UMLClass.addClass("Test");
        List<UMLParameterInfo> parameters = new ArrayList<>();
        parameters.add(new UMLParameterInfo("int", "DogCounter"));

        umlMethods.addMethod("Test", "AddDog", parameters);
        parameters.clear();

        UMLClassInfo classInfo = UMLClass.classMap.get("Test");
        UMLMethodInfo methodInfo = classInfo.getMethodByName("AddDog");
        assertEquals(1, methodInfo.getParameters().size());
    }

    /**
     * Tests the setParameters method of UMLMethodInfo.
     */
    @Test
    public void testSetParameters() {
        UMLClass.addClass("Test");
        List<UMLParameterInfo> parameters = Arrays.asList(
                new UMLParameterInfo("int", "DogCounter"),
                new UMLParameterInfo("boolean", "IsPuppy")
        );
        umlMethods.addMethod("Test", "AddDog", parameters);

        UMLClassInfo classInfo = UMLClass.classMap.get("Test");
        UMLMethodInfo methodInfo = classInfo.getMethodByName("AddDog");
        List<UMLParameterInfo> newParameters = Arrays.asList(
                new UMLParameterInfo("double", "treats"),
                new UMLParameterInfo("boolean", "type")
        );
        methodInfo.setParameters(newParameters);

        assertEquals(newParameters, methodInfo.getParameters());
    }

    /**
     * Tests the changeParameter method of UMLMethodInfo.
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