import static org.junit.jupiter.api.Assertions.*;

import codecain.BackendCode.UMLClass;
import codecain.BackendCode.UMLClassInfo;
import codecain.BackendCode.UMLFieldInfo;
import codecain.BackendCode.UMLFields;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

/**
 * The UMLFieldsTests class contains test cases to validate the functionality of UMLFields class.
 * It tests scenarios such as adding, removing, and renaming fields within UML classes.
 */
public class UMLFieldsTests {
    private UMLFields umlFields;

    /**
     * Sets up the test environment by initializing the UMLFields object and clearing the UMLClass map.
     */
    @BeforeEach
    public void setUp() {
        UMLClass.classMap.clear();
        umlFields = new UMLFields();
    }

    /**
     * Cleans up the test environment by clearing the UMLClass map.
     */
    @AfterEach
    public void tearDown() {
        UMLClass.classMap.clear();
    }

    /**
     * Tests adding a field to an existing class.
     */
    @Test
    public void testAddFieldToClass() {
        UMLClass.addClass("Test");
        umlFields.addField("Test", "DOG", "String");

        UMLClassInfo classInfo = UMLClass.classMap.get("Test");
        assertNotNull(classInfo);
        assertEquals(1, classInfo.getFields().size());
    }

    /**
     * Tests adding a field to a non-existing class.
     */
    @Test
    public void testAddFieldToNonExistingClass() {
        umlFields.addField("Test", "DOG", "String");
        assertFalse(UMLClass.classMap.containsKey("Test"));
    }

    /**
     * Tests adding a duplicate field to an existing class.
     */
    @Test
    public void testAddDuplicateField() {
        UMLClass.addClass("Test");
        umlFields.addField("Test", "DOG", "String");
        umlFields.addField("Test", "DOG", "String");

        UMLClassInfo classInfo = UMLClass.classMap.get("Test");
        assertEquals(1, classInfo.getFields().size(), "The duplicated field shouldn't be added");
    }

    /**
     * Tests removing a field from an existing class.
     */
    @Test
    public void testRemoveFieldFromClass() {
        UMLClass.addClass("Test");
        umlFields.addField("Test", "String", "DOG");
        umlFields.removeField("Test", "DOG");

        UMLClassInfo classInfo = UMLClass.classMap.get("Test");
        assertEquals(0, classInfo.getFields().size());
    }

    /**
     * Tests removing a non-existing field from an existing class.
     */
    @Test
    public void testRemoveFieldFromNonExistingField() {
        UMLClass.addClass("Test");
        umlFields.removeField("Test", "DOG");

        UMLClassInfo classInfo = UMLClass.classMap.get("Test");
        assertEquals(0, classInfo.getFields().size(), "Should give error that you cannot remove a non-existing field from class");
    }

    /**
     * Tests removing a field from a non-existing class.
     */
    @Test
    public void testRemoveFieldFromNonExistingClass() {
        umlFields.removeField("Test", "DOG");
        assertFalse(UMLClass.classMap.containsKey("Test"), "Should be prompted that you can't remove from a non-existing class");
    }

    /**
     * Tests renaming an existing field within a class.
     */
    @Test
    public void testRenameField() {
        UMLClass.addClass("Test");
        umlFields.addField("Test", "String", "DOG");
        umlFields.renameField("Test", "DOG",  "Int", "numOfCats");

        UMLClassInfo classInfo = UMLClass.classMap.get("Test");
        UMLFieldInfo newFieldName = classInfo.getFields().get(0);
        assertEquals("numOfCats", newFieldName.getFieldName());
        assertEquals("Int", newFieldName.getFieldType());
    }

    /**
     * Tests renaming a non-existing field within a class.
     */
    @Test
    public void testRenameFieldFromNonExistingField() {
        UMLClass.addClass("Test");
        umlFields.renameField("Test", "DOG", "CAT", "String");

        UMLClassInfo classInfo = UMLClass.classMap.get("Test");
        assertEquals(0, classInfo.getFields().size());
    }

    /**
     * Tests renaming a field to an already existing field's name and type within the same class.
     */
    @Test
    public void testRenameFieldToExistingField() {
        UMLClass.addClass("Test");
        umlFields.addField("Test", "DOG", "String");
        umlFields.addField("Test", "numOfCats", "Int");
        umlFields.renameField("Test", "DOG", "numOfCats", "Int");

        UMLClassInfo classInfo = UMLClass.classMap.get("Test");
        assertEquals(2, classInfo.getFields().size(), "Should be prompted you cannot rename to an existing field name");
    }
}
