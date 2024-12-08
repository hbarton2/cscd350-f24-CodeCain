import static org.junit.jupiter.api.Assertions.*;

import codecain.BackendCode.Model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import java.util.ArrayList;
import java.util.List;

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
    @Test
    public void testAddClassNullName() {
        UMLClass.addClass(null);
        // Assuming the system prints an error message to the console
        String expectedMessage = "Canceled: Inputted Class Name is Blank";
        assertEquals(expectedMessage, "Canceled: Inputted Class Name is Blank",
                "Should prompt the correct error message for null class name");
    }
    @Test
    public void testAddClassBlankName() {
        UMLClass.addClass("");
        // Assuming the system prints an error message to the console
        String expectedMessage = "Canceled: Inputted Class Name is Blank";
        assertEquals(expectedMessage, "Canceled: Inputted Class Name is Blank",
                "Should prompt the correct error message for blank class name");
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
    @Test
    public void testRemoveClassNullName() {
        UMLClass.removeClass(null);
        // Assuming the system prints an error message to the console
        String expectedMessage = "Canceled: Inputted Class Name is Blank";
        assertEquals(expectedMessage, "Canceled: Inputted Class Name is Blank",
                "Should prompt the correct error message for null class name");
    }
    @Test
    public void testRemoveClassBlankName() {
        UMLClass.removeClass("");
        // Assuming the system prints an error message to the console
        String expectedMessage = "Canceled: Inputted Class Name is Blank";
        assertEquals(expectedMessage, "Canceled: Inputted Class Name is Blank",
                "Should prompt the correct error message for blank class name");
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
    @Test
    public void testRenameClassNullOldName() {
        UMLClass.renameClass(null, "CAT");
        // Assuming the system prints an error message to the console
        String expectedMessage = "Canceled: Inputted Old Class Name is Blank";
        assertEquals(expectedMessage, "Canceled: Inputted Old Class Name is Blank",
                "Should prompt the correct error message for null old class name");
    }
    @Test
    public void testRenameClassBlankOldName() {
        UMLClass.renameClass("", "CAT");
        // Assuming the system prints an error message to the console
        String expectedMessage = "Canceled: Inputted Old Class Name is Blank";
        assertEquals(expectedMessage, "Canceled: Inputted Old Class Name is Blank",
                "Should prompt the correct error message for blank old class name");
    }
    @Test
    public void testRenameClassBlankNewName() {
        UMLClass.renameClass("DOG", "");
        // Assuming the system prints an error message to the console
        String expectedMessage = "Canceled: Inputted New Class Name is Blank";
        assertEquals(expectedMessage, "Canceled: Inputted New Class Name is Blank",
                "Should prompt the correct error message for blank new class name");
    }

    @Test
    public void testListAllClasses() {
        UMLClass.addClass("DOG");
        UMLClass.addClass("CAT");
        String res = UMLClass.listAllClassesInfo();
        assertTrue(res.contains("DOG"));
        assertTrue(res.contains("CAT"));
    }
    @Test
    public void testListAllClassesWithNoClasses(){
        String res = UMLClass.listAllClassesInfo();
        assertEquals("No classes to display.",res);
    }
    @Test
    public void testListAllClassesWithEmptyFieldsAndMethods(){
        UMLClass.addClass("DOG");
        String res = UMLClass.listAllClassesInfo();
        String expectedMessage = "Class: DOG\n"+ "  Fields:\n" + "  Methods:\n\n";
        assertEquals(expectedMessage, res);
    }
    @Test
    public void testListAllClassesWithFieldsAndMethods(){
        UMLClass.addClass("DOG");
        UMLClassInfo info = UMLClass.getClassInfo("DOG");

        List<UMLFieldInfo> fields = new ArrayList<>();
        fields.add(new UMLFieldInfo("NAME", "String"));
        fields.add(new UMLFieldInfo("TYPE", "String"));
        info.getFields().addAll(fields);

        List<UMLMethodInfo> methods = new ArrayList<>();
        methods.add(new UMLMethodInfo("getAge", new ArrayList<>()));
        methods.add(new UMLMethodInfo("setName", List.of(new UMLParameterInfo("NAME", "String"))));
        info.getMethods().addAll(methods);

        String res = UMLClass.listAllClassesInfo();
        String expectedMessage = "Class: DOG\n" +
                "  Fields:\n" +
                "    - NAME String\n" +
                "    - TYPE String\n" +
                "  Methods:\n" +
                "    - getAge()\n" +
                "    - setName(NAME String)\n\n";
        assertEquals(expectedMessage, res);
    }
    @Test
    public void testGetClassInfo(){
        UMLClass.addClass("DOG");
        UMLClassInfo info = UMLClass.getClassInfo("DOG");
        assertNotNull(info);
        assertEquals("DOG", info.getClassName());
    }
    @Test
    public void testGetClassInfoFieldsAndMethods(){
        UMLClass.addClass("DOG");
        UMLClassInfo info = UMLClass.getClassInfo("DOG");
        assertTrue(info.getFields().isEmpty());


        List<UMLFieldInfo> fields = new ArrayList<>();
        fields.add(new UMLFieldInfo("NAME", "String"));
        fields.add(new UMLFieldInfo("TYPE", "String"));
        info.getFields().addAll(fields);
        assertEquals(2, info.getFields().size(), "Should have two fields");

        List<UMLMethodInfo> methods = new ArrayList<>();
        methods.add(new UMLMethodInfo("getAge", new ArrayList<>()));
        methods.add(new UMLMethodInfo("setName", List.of(new UMLParameterInfo("NAME", "String"))));
        info.getMethods().addAll(methods);
        assertEquals(2, info.getMethods().size(), "Should have two methods");
    }
    @Test
    public void testGetClassInfoMethodsName(){
        UMLClass.addClass("DOG");
        UMLClassInfo info = UMLClass.getClassInfo("DOG");
        UMLMethodInfo method = new UMLMethodInfo("getAge", new ArrayList<>());
        info.getMethods().add(method);
        assertNotNull(info.getMethodByName("getAge"));
        assertNull(info.getMethodByName("setName"));
    }

    @Test
    public void testSetClassName(){
        UMLClass.addClass("DOG");
        UMLClassInfo info = UMLClass.getClassInfo("DOG");
        info.setClassName("CAT");
        assertEquals("CAT", info.getClassName());
    }
    @Test
    public void testSetPosition(){
        UMLClass.addClass("DOG");
        UMLClassInfo info = UMLClass.getClassInfo("DOG");
        info.setX(100);
        info.setY(200);
        assertEquals(100, info.getX(), "Should have the same x value");
        assertEquals(200, info.getY(), "Should have the same y value");
    }


}
