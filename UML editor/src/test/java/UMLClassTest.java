
import static org.junit.jupiter.api.Assertions.*;

import codecain.Fields;
import codecain.Methods;
import codecain.Class;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

public class UMLClassTest {

    Fields fieldsManager;
    Methods methodsManager;

    @BeforeEach
    public void setUp() {
        fieldsManager = new Fields();
        methodsManager = new Methods();

        // Reset the static maps before each test
        Class.classMap = new HashMap<>();
        Fields.classFields = new HashMap<>();
        Methods.classMethods = new HashMap<>();
    }

    // Test for Class methods

    @Test
    public void testAddClass() {
        Class.addClass("TestClass1");
        assertTrue(Class.classMap.containsKey("TestClass1"), "Class should be added to classMap");
    }

    @Test
    public void testAddDuplicateClass() {
        Class.addClass("TestClass1");
        Class.addClass("TestClass1");
        assertEquals(1, Class.classMap.size(), "Duplicate class should not be added");
    }

    @Test
    public void testRemoveClass() {
        Class.addClass("TestClass1");
        Class.removeClass("TestClass1");
        assertFalse(Class.classMap.containsKey("TestClass1"), "Class should be removed from classMap");
    }

    @Test
    public void testRemoveNonExistentClass() {
        Class.removeClass("NonExistentClass");
        assertFalse(Class.classMap.containsKey("NonExistentClass"), "Non-existent class removal should not affect classMap");
    }

    @Test
    public void testRenameClass() {
        Class.addClass("OldClass");
        Class.renameClass("OldClass", "NewClass");
        assertTrue(Class.classMap.containsKey("NewClass"), "Class should be renamed");
        assertFalse(Class.classMap.containsKey("OldClass"), "Old class name should be removed from classMap");
    }

    // Test for Fields methods

    @Test
    public void testAddField() {
        Class.addClass("TestClass1");
        fieldsManager.addField("TestClass1", "field1");
        assertTrue(Fields.classFields.get("TestClass1").containsKey("field1"), "Field should be added to class");
    }

    @Test
    public void testAddDuplicateField() {
        Class.addClass("TestClass1");
        fieldsManager.addField("TestClass1", "field1");
        fieldsManager.addField("TestClass1", "field1");
        assertEquals(1, Fields.classFields.get("TestClass1").size(), "Duplicate field should not be added");
    }

    @Test
    public void testRemoveField() {
        Class.addClass("TestClass1");
        fieldsManager.addField("TestClass1", "field1");
        fieldsManager.removeField("TestClass1", "field1");
        assertFalse(Fields.classFields.get("TestClass1").containsKey("field1"), "Field should be removed from class");
    }

    @Test
    public void testRenameField() {
        Class.addClass("TestClass1");
        fieldsManager.addField("TestClass1", "oldField");
        fieldsManager.renameField("TestClass1", "oldField", "newField");
        assertTrue(Fields.classFields.get("TestClass1").containsKey("newField"), "Field should be renamed");
        assertFalse(Fields.classFields.get("TestClass1").containsKey("oldField"), "Old field name should not exist");
    }

    // Test for Methods methods

    @Test
    public void testAddMethod() {
        Class.addClass("TestClass1");
        methodsManager.addMethod("TestClass1", "method1", Arrays.asList("int param1"));
        assertTrue(Methods.classMethods.get("TestClass1").containsKey("method1"), "Method should be added to class");
    }

    @Test
    public void testAddDuplicateMethod() {
        Class.addClass("TestClass1");
        methodsManager.addMethod("TestClass1", "method1", Arrays.asList("int param1"));
        methodsManager.addMethod("TestClass1", "method1", Arrays.asList("int param1"));
        assertEquals(1, Methods.classMethods.get("TestClass1").size(), "Duplicate method should not be added");
    }

    @Test
    public void testRemoveMethod() {
        Class.addClass("TestClass1");
        methodsManager.addMethod("TestClass1", "method1", Arrays.asList("int param1"));
        methodsManager.removeMethod("TestClass1", "method1");
        assertFalse(Methods.classMethods.get("TestClass1").containsKey("method1"), "Method should be removed from class");
    }

    @Test
    public void testRenameMethod() {
        Class.addClass("TestClass1");
        methodsManager.addMethod("TestClass1", "oldMethod", Arrays.asList("int param1"));
        methodsManager.renameMethod("TestClass1", "oldMethod", "newMethod");
        assertTrue(Methods.classMethods.get("TestClass1").containsKey("newMethod"), "Method should be renamed");
        assertFalse(Methods.classMethods.get("TestClass1").containsKey("oldMethod"), "Old method name should not exist");
    }

    @Test
    public void testAddParameter() {
        Class.addClass("TestClass1");
        methodsManager.addMethod("TestClass1", "method1", new ArrayList<>(Arrays.asList("int param1")));
        methodsManager.addParameter("TestClass1", "method1", "String param2");
        List<Object> parameters = Methods.classMethods.get("TestClass1").get("method1");
        assertTrue(parameters.contains("String param2"), "Parameter should be added to method");
    }


    @Test
    public void testRemoveParameter() {
        Class.addClass("TestClass1");
        methodsManager.addMethod("TestClass1", "method1", new ArrayList<>(Arrays.asList("int param1")));
        methodsManager.addParameter("TestClass1", "method1", "String param2");
        methodsManager.removeParameter("TestClass1", "method1", "String param2");
        List<Object> parameters = Methods.classMethods.get("TestClass1").get("method1");
        assertFalse(parameters.contains("String param2"), "Parameter should be removed from method");
    }


    @Test
    public void testChangeParameters() {
        Class.addClass("TestClass1");
        methodsManager.addMethod("TestClass1", "method1", Arrays.asList("int param1"));
        methodsManager.changeParameters("TestClass1", "method1", Arrays.asList("String newParam"));
        List<Object> parameters = Methods.classMethods.get("TestClass1").get("method1");
        assertEquals(1, parameters.size(), "Parameters should be replaced");
        assertTrue(parameters.contains("String newParam"), "New parameter should be set");

    }
}