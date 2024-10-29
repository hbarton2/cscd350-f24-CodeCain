import static org.junit.jupiter.api.Assertions.*;

import codecain.UMLFields;
import codecain.UMLMethods;
import codecain.UMLClass;
import codecain.UMLClassInfo;
import codecain.UMLMethodInfo;
import codecain.UMLFieldInfo;
import codecain.UMLParameterInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

public class UMLClassTest {

    UMLFields fieldsManager;
    UMLMethods methodsManager;

    @BeforeEach
    public void setUp() {
        fieldsManager = new UMLFields();
        methodsManager = new UMLMethods();
        UMLClass.classMap = new java.util.HashMap<>();
    }

    @Test
    public void testAddField() {
        UMLClass.addClass("TestClass1");
        fieldsManager.addField("TestClass1", "field1", "String");
        UMLClassInfo classInfo = UMLClass.classMap.get("TestClass1");
        assertEquals(1, classInfo.getFields().size(), "Field should be added to the class");
    }

    @Test
    public void testAddDuplicateField() {
        UMLClass.addClass("TestClass1");
        fieldsManager.addField("TestClass1", "field1", "String");
        fieldsManager.addField("TestClass1", "field1", "String");
        UMLClassInfo classInfo = UMLClass.classMap.get("TestClass1");
        assertEquals(1, classInfo.getFields().size(), "Duplicate field should not be added");
    }

    @Test
    public void testRemoveField() {
        UMLClass.addClass("TestClass1");
        fieldsManager.addField("TestClass1", "field1", "String");
        fieldsManager.removeField("TestClass1", "field1", "String");
        UMLClassInfo classInfo = UMLClass.classMap.get("TestClass1");
        assertEquals(0, classInfo.getFields().size(), "Field should be removed from the class");
    }

    @Test
    public void testRemoveNonExistentField() {
        UMLClass.addClass("TestClass1");
        fieldsManager.removeField("TestClass1", "nonExistentField", "String");
        UMLClassInfo classInfo = UMLClass.classMap.get("TestClass1");
        assertEquals(0, classInfo.getFields().size(), "No field should be removed if it does not exist");
    }

    @Test
    public void testRenameField() {
        UMLClass.addClass("TestClass1");
        fieldsManager.addField("TestClass1", "oldField", "String");
        fieldsManager.renameField("TestClass1", "oldField", "String", "newField", "int");

        UMLClassInfo classInfo = UMLClass.classMap.get("TestClass1");

        UMLFieldInfo renamedField = classInfo.getFields().stream()
                .filter(f -> f.getFieldName().equals("newField") && f.getFieldType().equals("int"))
                .findFirst()
                .orElse(null);

        assertNotNull(renamedField, "Field should be renamed to newField with type int");
    }

    @Test
    public void testRenameNonExistentField() {
        UMLClass.addClass("TestClass1");
        fieldsManager.renameField("TestClass1", "nonExistentField", "String", "newField", "int");
        UMLClassInfo classInfo = UMLClass.classMap.get("TestClass1");
        assertNull(classInfo.getFields().stream().filter(f -> f.getFieldName().equals("newField")).findFirst().orElse(null), "Non-existent field should not be renamed");
    }

    @Test
    public void testAddMethod() {
        UMLClass.addClass("TestClass1");
        methodsManager.addMethod("TestClass1", "method1", List.of(new UMLParameterInfo("int", "param1")));
        UMLClassInfo classInfo = UMLClass.classMap.get("TestClass1");
        UMLMethodInfo method = classInfo.getMethodByName("method1");
        assertNotNull(method, "Method should be added to the class");
    }

    @Test
    public void testAddDuplicateMethod() {
        UMLClass.addClass("TestClass1");
        methodsManager.addMethod("TestClass1", "method1", List.of(new UMLParameterInfo("int", "param1")));
        methodsManager.addMethod("TestClass1", "method1", List.of(new UMLParameterInfo("int", "param1")));
        UMLClassInfo classInfo = UMLClass.classMap.get("TestClass1");
        assertEquals(1, classInfo.getMethods().size(), "Duplicate method should not be added");
    }

    @Test
    public void testRemoveMethod() {
        UMLClass.addClass("TestClass1");
        methodsManager.addMethod("TestClass1", "method1", List.of(new UMLParameterInfo("int", "param1")));
        methodsManager.removeMethod("TestClass1", "method1");
        UMLClassInfo classInfo = UMLClass.classMap.get("TestClass1");
        UMLMethodInfo method = classInfo.getMethodByName("method1");
        assertNull(method, "Method should be removed from the class");
    }

    @Test
    public void testRenameMethod() {
        UMLClass.addClass("TestClass1");
        methodsManager.addMethod("TestClass1", "oldMethod", List.of(new UMLParameterInfo("int", "param1")));
        methodsManager.renameMethod("TestClass1", "oldMethod", "newMethod");
        UMLClassInfo classInfo = UMLClass.classMap.get("TestClass1");
        UMLMethodInfo method = classInfo.getMethodByName("newMethod");
        assertNotNull(method, "Method should be renamed");
        assertNull(classInfo.getMethodByName("oldMethod"), "Old method name should no longer exist");
    }

    @Test
    public void testAddParameter() {
        UMLClass.addClass("TestClass1");
        methodsManager.addMethod("TestClass1", "method1", List.of(new UMLParameterInfo("int", "param1")));
        methodsManager.addParameter("TestClass1", "method1", new UMLParameterInfo("String", "param2"));
        UMLClassInfo classInfo = UMLClass.classMap.get("TestClass1");
        UMLMethodInfo method = classInfo.getMethodByName("method1");
        assertTrue(method.getParameters().stream().map(UMLParameterInfo::toString).collect(Collectors.toList()).contains("String : param2"), "Parameter should be added to method");
    }

    @Test
    public void testRemoveParameter() {
        UMLClass.addClass("TestClass1");
        methodsManager.addMethod("TestClass1", "method1", List.of(new UMLParameterInfo("int", "param1"), new UMLParameterInfo("String", "param2")));
        methodsManager.removeParameter("TestClass1", "method1", new UMLParameterInfo("String", "param2"));
        UMLClassInfo classInfo = UMLClass.classMap.get("TestClass1");
        UMLMethodInfo method = classInfo.getMethodByName("method1");
        assertFalse(method.getParameters().stream().map(UMLParameterInfo::toString).collect(Collectors.toList()).contains("String : param2"), "Parameter should be removed from method");
        assertTrue(method.getParameters().stream().map(UMLParameterInfo::toString).collect(Collectors.toList()).contains("int : param1"), "Parameter should not be removed from method");
    }

    @Test
    public void testChangeParameter() {
        UMLClass.addClass("TestClass1");
        methodsManager.addMethod("TestClass1", "method1", List.of(new UMLParameterInfo("int", "param1")));
        UMLParameterInfo oldParam = new UMLParameterInfo("int", "param1");
        UMLParameterInfo newParam = new UMLParameterInfo("String", "param1");
        methodsManager.changeParameter("TestClass1", "method1", oldParam, newParam);
        UMLClassInfo classInfo = UMLClass.classMap.get("TestClass1");
        UMLMethodInfo method = classInfo.getMethodByName("method1");
        assertEquals(1, method.getParameters().size(), "Parameters should be updated");
        assertTrue(method.getParameters().stream().map(UMLParameterInfo::toString).collect(Collectors.toList()).contains("String : param1"), "Parameter should be changed to new type");
    }

    @Test
    public void testAddClass() {
        UMLClass.addClass("TestClass1");
        assertTrue(UMLClass.classMap.containsKey("TestClass1"), "Class should be added to the map");
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
        assertFalse(UMLClass.classMap.containsKey("TestClass1"), "Class should be removed from the map");
    }

    @Test
    public void testRenameClass() {
        UMLClass.addClass("TestClass1");
        UMLClass.renameClass("TestClass1", "RenamedClass");
        assertFalse(UMLClass.classMap.containsKey("TestClass1"), "Old class name should be removed");
        assertTrue(UMLClass.classMap.containsKey("RenamedClass"), "Class should be renamed");
    }

    @Test
    public void testAddMethodWithMultipleParameters() {
        UMLClass.addClass("TestClass1");
        methodsManager.addMethod("TestClass1", "method1", List.of(
                new UMLParameterInfo("int", "param1"),
                new UMLParameterInfo("String", "param2"),
                new UMLParameterInfo("boolean", "param3")
        ));

        UMLClassInfo classInfo = UMLClass.classMap.get("TestClass1");
        UMLMethodInfo method = classInfo.getMethodByName("method1");

        assertNotNull(method, "Method should be added to the class");
        assertEquals(3, method.getParameters().size(), "Method should have 3 parameters");
        assertTrue(method.getParameters().stream().map(UMLParameterInfo::toString).collect(Collectors.toList()).containsAll(List.of(
                "int : param1",
                "String : param2",
                "boolean : param3"
        )), "Method should contain all the specified parameters");
    }
}
