import static org.junit.jupiter.api.Assertions.*;

import codecain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

public class UMLFieldsTests {
    private UMLFields umlFields;

    @BeforeEach
    public void setUp() {
        UMLClass.classMap.clear();
        umlFields = new UMLFields();
    }
    @AfterEach
    public void tearDown() {
        UMLClass.classMap.clear();
    }

    @Test
    public void testAddFieldToClass(){
        UMLClass.addClass("Test");
        umlFields.addField("Test","DOG","String");

        UMLClassInfo classInfo = UMLClass.classMap.get("Test");
        assertNotNull(classInfo);
        assertEquals(1, classInfo.getFields().size());

    }
    @Test
    public void testAddFieldToNonExistingClass(){
        umlFields.addField("Test","DOG","String");
        assertFalse(UMLClass.classMap.containsKey("Test"));
    }

    @Test
    public void testAddDuplicateField(){
        UMLClass.addClass("Test");
        umlFields.addField("Test","DOG","String");
        umlFields.addField("Test","DOG","String");

        UMLClassInfo classInfo = UMLClass.classMap.get("Test");
        assertEquals(1, classInfo.getFields().size(), "The duplicated field shouldn't be added");
    }

    @Test
    public void testRemoveFieldFromClass(){
        UMLClass.addClass("Test");
        umlFields.addField("Test","DOG","String");
        umlFields.removeField("Test","DOG","String");
        UMLClassInfo classInfo = UMLClass.classMap.get("Test");
        assertEquals(0, classInfo.getFields().size());
    }
    @Test
    public void testRemoveFieldFromNonExistingField(){
        UMLClass.addClass("Test");
        umlFields.removeField("Test","DOG","String");
        UMLClassInfo classInfo = UMLClass.classMap.get("Test");

        assertEquals(0, classInfo.getFields().size(),"Should give error that you cannot remove a non existing field from class ");
    }
    @Test
    public void testRemoveFieldFromNonExistingClass(){
        umlFields.removeField("Test","DOG","String");
        assertFalse(UMLClass.classMap.containsKey("Test"), "Should be prompted that you can't remove from a non existing class");
    }

    @Test
    public void testRenameField(){
        UMLClass.addClass("Test");
        umlFields.addField("Test","DOG","String");
        umlFields.renameField("Test", "DOG","String","numOfCats","Int");

        UMLClassInfo classInfo = UMLClass.classMap.get("Test");
        UMLFieldInfo newFieldName = classInfo.getFields().get(0);
        assertEquals("numOfCats",newFieldName.getFieldName());
        assertEquals("Int",newFieldName.getFieldType());
    }
    @Test
    public void testRenameFieldFromNonExistingField(){
        UMLClass.addClass("Test");
        umlFields.renameField("Test","DOG","String","CAT","String");
        UMLClassInfo classInfo = UMLClass.classMap.get("Test");
        assertEquals(0,classInfo.getFields().size());
    }
    @Test
    public void testRenameFieldToExistingField(){
        UMLClass.addClass("Test");
        umlFields.addField("Test","DOG","String");
        umlFields.addField("Test","numOfCats","Int");
        umlFields.renameField("Test","DOG","String","numOfCats","Int");

        UMLClassInfo classInfo = UMLClass.classMap.get("Test");
        assertEquals(2, classInfo.getFields().size(),"Should be prompted you cannot rename to an existing field name");
    }

}
