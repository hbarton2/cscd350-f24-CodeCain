package java;

import static org.junit.jupiter.api.Assertions.*;

import codecain.UMLFields;
import codecain.UMLMethods;
import codecain.UMLClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

public class UMLClassTest {

    UMLFields fieldsManager;
    UMLMethods methodsManager;

    @BeforeEach
    public void setUp() {
        fieldsManager = new UMLFields();
        methodsManager = new UMLMethods();

        // Reset the static maps before each test
        UMLClass.classMap = new HashMap<>();
        UMLFields.classFields = new HashMap<>();
        UMLMethods.classMethods = new HashMap<>();
    }

    // Test for Class methods

    @Test
    public void testAddClass() {
        UMLClass.addClass("TestClass1");
        assertTrue(UMLClass.classMap.containsKey("TestClass1"), "Class should be added to classMap");
    }

    @Test
    public void testAddDuplicateClass() {
        UMLClass.addClass("TestClass1");
        UMLClass.addClass("TestClass1");
        assertEquals(1, UMLClass.classMap.size(), "Duplicate class should not be added");
    }

    @Test
    public void testRemoveClass() {
        UMLClass.addClass("TestClass1");
        UMLClass.removeClass("TestClass1");
        assertFalse(UMLClass.classMap.containsKey("TestClass1"), "Class should be removed from classMap");
    }

    @Test
    public void testRemoveNonExistentClass() {
        UMLClass.removeClass("NonExistentClass");
        assertFalse(UMLClass.classMap.containsKey("NonExistentClass"), "Non-existent class removal should not affect classMap");
    }

    @Test
    public void testRenameClass() {
        UMLClass.addClass("OldClass");
        UMLClass.renameClass("OldClass", "NewClass");
        assertTrue(UMLClass.classMap.containsKey("NewClass"), "Class should be renamed");
        assertFalse(UMLClass.classMap.containsKey("OldClass"), "Old class name should be removed from classMap");
    }

    // Test for Fields methods

    @Test
    public void testAddField() {
        UMLClass.addClass("TestClass1");
        fieldsManager.addField("TestClass1", "field1");
        assertTrue(UMLFields.classFields.get("TestClass1").containsKey("field1"), "Field should be added to class");
    }

    @Test
    public void testAddDuplicateField() {
        UMLClass.addClass("TestClass1");
        fieldsManager.addField("TestClass1", "field1");
        fieldsManager.addField("TestClass1", "field1");
        assertEquals(1, UMLFields.classFields.get("TestClass1").size(), "Duplicate field should not be added");
    }

    @Test
    public void testRemoveField() {
        UMLClass.addClass("TestClass1");
        fieldsManager.addField("TestClass1", "field1");
        fieldsManager.removeField("TestClass1", "field1");
        assertFalse(UMLFields.classFields.get("TestClass1").containsKey("field1"), "Field should be removed from class");
    }

    @Test
    public void testRenameField() {
        UMLClass.addClass("TestClass1");
        fieldsManager.addField("TestClass1", "oldField");
        fieldsManager.renameField("TestClass1", "oldField", "newField");
        assertTrue(UMLFields.classFields.get("TestClass1").containsKey("newField"), "Field should be renamed");
        assertFalse(UMLFields.classFields.get("TestClass1").containsKey("oldField"), "Old field name should not exist");
    }

    // Test for Methods methods

    @Test
    public void testAddMethod() {
        UMLClass.addClass("TestClass1");
        methodsManager.addMethod("TestClass1", "method1", Arrays.asList("int param1"));
        assertTrue(UMLMethods.classMethods.get("TestClass1").containsKey("method1"), "Method should be added to class");
    }

    @Test
    public void testAddDuplicateMethod() {
        UMLClass.addClass("TestClass1");
        methodsManager.addMethod("TestClass1", "method1", Arrays.asList("int param1"));
        methodsManager.addMethod("TestClass1", "method1", Arrays.asList("int param1"));
        assertEquals(1, UMLMethods.classMethods.get("TestClass1").size(), "Duplicate method should not be added");
    }

    @Test
    public void testRemoveMethod() {
        UMLClass.addClass("TestClass1");
        methodsManager.addMethod("TestClass1", "method1", Arrays.asList("int param1"));
        methodsManager.removeMethod("TestClass1", "method1");
        assertFalse(UMLMethods.classMethods.get("TestClass1").containsKey("method1"), "Method should be removed from class");
    }

    @Test
    public void testRenameMethod() {
        UMLClass.addClass("TestClass1");
        methodsManager.addMethod("TestClass1", "oldMethod", Arrays.asList("int param1"));
        methodsManager.renameMethod("TestClass1", "oldMethod", "newMethod");
        assertTrue(UMLMethods.classMethods.get("TestClass1").containsKey("newMethod"), "Method should be renamed");
        assertFalse(UMLMethods.classMethods.get("TestClass1").containsKey("oldMethod"), "Old method name should not exist");
    }

    @Test
    public void testAddParameter() {
        UMLClass.addClass("TestClass1");
        methodsManager.addMethod("TestClass1", "method1", new ArrayList<>(Arrays.asList("int param1")));
        methodsManager.addParameter("TestClass1", "method1", "String param2");
        List<Object> parameters = UMLMethods.classMethods.get("TestClass1").get("method1");
        assertTrue(parameters.contains("String param2"), "Parameter should be added to method");
    }


    @Test
    public void testRemoveParameter() {
        UMLClass.addClass("TestClass1");
        methodsManager.addMethod("TestClass1", "method1", new ArrayList<>(Arrays.asList("int param1")));
        methodsManager.addParameter("TestClass1", "method1", "String param2");
        methodsManager.removeParameter("TestClass1", "method1", "String param2");
        List<Object> parameters = UMLMethods.classMethods.get("TestClass1").get("method1");
        assertFalse(parameters.contains("String param2"), "Parameter should be removed from method");
    }


    @Test
    public void testChangeParameters() {
        UMLClass.addClass("TestClass1");
        methodsManager.addMethod("TestClass1", "method1", Arrays.asList("int param1"));
        methodsManager.changeParameters("TestClass1", "method1", Arrays.asList("String newParam"));
        List<Object> parameters = UMLMethods.classMethods.get("TestClass1").get("method1");
        assertEquals(1, parameters.size(), "Parameters should be replaced");
        assertTrue(parameters.contains("String newParam"), "New parameter should be set");

    }
}
