/*
import codecain.BackendCode.Relationship;
import codecain.BackendCode.UMLClass;
import codecain.BackendCode.UMLClassInfo;
import codecain.BackendCode.UMLMethodInfo;
import codecain.CommandLineInterface.CLI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CLI_Tests {

    private CLI cli;

    @BeforeEach
    void setUp() {
        // Initialize CLI
        cli = new CLI();
        // Clear classMap and relationshipList for a clean test environment
        UMLClass.classMap.clear();
        Relationship.relationshipList.clear();
    }

    @Test
    void testAddClass() {
        cli.getCommandManager().parseAndExecute("add class Person");
        assertTrue(UMLClass.classMap.containsKey("Person"), "Class 'Person' should exist in the classMap.");
    }

    @Test
    void testDeleteClass() {
        cli.getCommandManager().parseAndExecute("add class Person");
        cli.getCommandManager().parseAndExecute("delete class Person");
        assertFalse(UMLClass.classMap.containsKey("Person"), "Class 'Person' should be removed from the classMap.");
    }

    @Test
    void testRenameClass() {
        cli.getCommandManager().parseAndExecute("add class Person");
        cli.getCommandManager().parseAndExecute("rename class Person Employee");
        assertFalse(UMLClass.classMap.containsKey("Person"), "Old class name 'Person' should not exist in the classMap.");
        assertTrue(UMLClass.classMap.containsKey("Employee"), "New class name 'Employee' should exist in the classMap.");
    }

    @Test
    void testAddField() {
        cli.getCommandManager().parseAndExecute("add class Person");
        cli.getCommandManager().parseAndExecute("add field Person String name");
        UMLClassInfo personInfo = UMLClass.classMap.get("Person");
        assertNotNull(personInfo);
        assertTrue(personInfo.getFields().stream().anyMatch(field -> field.getFieldName().equals("name") && field.getFieldType().equals("String")), "Field 'name' of type 'String' should be added to 'Person'.");
    }

    @Test
    void testDeleteField() {
        cli.getCommandManager().parseAndExecute("add class Person");
        cli.getCommandManager().parseAndExecute("add field Person String name");
        cli.getCommandManager().parseAndExecute("delete field Person name");
        UMLClassInfo personInfo = UMLClass.classMap.get("Person");
        assertNotNull(personInfo);
        assertTrue(personInfo.getFields().stream().noneMatch(field -> field.getFieldName().equals("name")), "Field 'name' should be removed from 'Person'.");
    }

    @Test
    void testRenameField() {
        cli.getCommandManager().parseAndExecute("add class Person");
        cli.getCommandManager().parseAndExecute("add field Person String name");
        cli.getCommandManager().parseAndExecute("rename field Person name int fullName");
        UMLClassInfo personInfo = UMLClass.classMap.get("Person");
        assertNotNull(personInfo);
        assertTrue(personInfo.getFields().stream().anyMatch(field -> field.getFieldName().equals("fullName") && field.getFieldType().equals("int")), "Field 'name' should be renamed to 'fullName' with type 'int' in 'Person'.");
    }

    @Test
    void testAddMethod() {
        cli.getCommandManager().parseAndExecute("add class Person");
        cli.getCommandManager().parseAndExecute("add method Person setName String name");
        UMLClassInfo personInfo = UMLClass.classMap.get("Person");
        assertNotNull(personInfo);
        assertTrue(personInfo.getMethods().stream().anyMatch(method -> method.getMethodName().equals("setName") && method.getParameters().size() == 1 && method.getParameters().get(0).getParameterName().equals("name") && method.getParameters().get(0).getParameterType().equals("String")), "Method 'setName' with parameter 'String name' should be added to 'Person'.");
    }

    @Test
    void testDeleteMethod() {
        cli.getCommandManager().parseAndExecute("add class Person");
        cli.getCommandManager().parseAndExecute("add method Person setName String name");
        cli.getCommandManager().parseAndExecute("delete method Person setName");
        UMLClassInfo personInfo = UMLClass.classMap.get("Person");
        assertNotNull(personInfo);
        assertTrue(personInfo.getMethods().stream().noneMatch(method -> method.getMethodName().equals("setName")), "Method 'setName' should be removed from 'Person'.");
    }

    @Test
    void testRenameMethod() {
        cli.getCommandManager().parseAndExecute("add class Person");
        cli.getCommandManager().parseAndExecute("add method Person setName String name");
        cli.getCommandManager().parseAndExecute("rename method Person setName updateName");
        UMLClassInfo personInfo = UMLClass.classMap.get("Person");
        assertNotNull(personInfo);
        assertTrue(personInfo.getMethods().stream().anyMatch(method -> method.getMethodName().equals("updateName")), "Method 'setName' should be renamed to 'updateName' in 'Person'.");
    }

    @Test
    void testAddParameter() {
        cli.getCommandManager().parseAndExecute("add class Person");
        cli.getCommandManager().parseAndExecute("add method Person setName String name");
        cli.getCommandManager().parseAndExecute("add parameter Person setName int age");
        UMLClassInfo personInfo = UMLClass.classMap.get("Person");
        UMLMethodInfo methodInfo = personInfo.getMethodByName("setName");
        assertNotNull(methodInfo);
        assertTrue(methodInfo.getParameters().stream().anyMatch(param -> param.getParameterName().equals("age") && param.getParameterType().equals("int")), "Parameter 'int age' should be added to method 'setName' in 'Person'.");
    }

    @Test
    void testDeleteParameter() {
        cli.getCommandManager().parseAndExecute("add class Person");
        cli.getCommandManager().parseAndExecute("add method Person setName String name");
        cli.getCommandManager().parseAndExecute("add parameter Person setName int age");
        cli.getCommandManager().parseAndExecute("delete parameter Person setName age");
        UMLClassInfo personInfo = UMLClass.classMap.get("Person");
        UMLMethodInfo methodInfo = personInfo.getMethodByName("setName");
        assertNotNull(methodInfo);
        assertTrue(methodInfo.getParameters().stream().noneMatch(param -> param.getParameterName().equals("age")), "Parameter 'age' should be removed from method 'setName' in 'Person'.");
    }

    @Test
    void testAddRelationship() {
        cli.getCommandManager().parseAndExecute("add class Person");
        cli.getCommandManager().parseAndExecute("add class Address");
        cli.getCommandManager().parseAndExecute("add relationship Person Address");
        assertTrue(Relationship.relationshipList.stream().anyMatch(rel -> rel.getClassNames().contains("Person") && rel.getClassNames().contains("Address")), "Relationship between 'Person' and 'Address' should be added.");
    }

    @Test
    void testDeleteRelationship() {
        cli.getCommandManager().parseAndExecute("add class Person");
        cli.getCommandManager().parseAndExecute("add class Address");
        cli.getCommandManager().parseAndExecute("add relationship Person Address");
        cli.getCommandManager().parseAndExecute("delete relationship Person Address");
        assertTrue(Relationship.relationshipList.stream().noneMatch(rel -> rel.getClassNames().contains("Person") && rel.getClassNames().contains("Address")), "Relationship between 'Person' and 'Address' should be removed.");
    }

    @Test
    void testListClasses() {
        cli.getCommandManager().parseAndExecute("add class Person");
        cli.getCommandManager().parseAndExecute("add class Address");
        assertTrue(UMLClass.classMap.containsKey("Person") && UMLClass.classMap.containsKey("Address"), "Classes 'Person' and 'Address' should exist in the classMap.");
    }

    @Test
    void testListRelationships() {
        cli.getCommandManager().parseAndExecute("add class Person");
        cli.getCommandManager().parseAndExecute("add class Address");
        cli.getCommandManager().parseAndExecute("add relationship Person Address");
        assertEquals(1, Relationship.relationshipList.size(), "There should be exactly one relationship in the relationshipList.");
    }

    @Test
    void testRenameSingleParameter() {
        cli.getCommandManager().parseAndExecute("add class Person");
        cli.getCommandManager().parseAndExecute("add method Person setName String name");
        cli.getCommandManager().parseAndExecute("add parameter Person setName int age");
        cli.getCommandManager().parseAndExecute("rename parameter Person setName age double years");

        UMLClassInfo personInfo = UMLClass.classMap.get("Person");
        UMLMethodInfo methodInfo = personInfo.getMethodByName("setName");

        assertNotNull(methodInfo, "Method 'setName' should exist in 'Person'.");
        assertTrue(methodInfo.getParameters().stream()
                        .anyMatch(param -> param.getParameterName().equals("years") && param.getParameterType().equals("double")),
                "Parameter 'age' should be renamed to 'years' with type 'double' in method 'setName' of 'Person'.");
    }

    @Test
    void testChangeAllParameters() {
        cli.getCommandManager().parseAndExecute("add class Person");
        cli.getCommandManager().parseAndExecute("add method Person setName String name int age");
        cli.getCommandManager().parseAndExecute("rename all_parameters Person setName double newName float newAge");

        UMLClassInfo personInfo = UMLClass.classMap.get("Person");
        UMLMethodInfo methodInfo = personInfo.getMethodByName("setName");

        assertNotNull(methodInfo, "Method 'setName' should exist in 'Person'.");
        assertEquals(2, methodInfo.getParameters().size(), "Method 'setName' should have exactly two parameters after replacement.");

        assertTrue(methodInfo.getParameters().stream()
                        .anyMatch(param -> param.getParameterName().equals("newName") && param.getParameterType().equals("double")),
                "Parameter should be 'newName' of type 'double' in method 'setName'.");
        assertTrue(methodInfo.getParameters().stream()
                        .anyMatch(param -> param.getParameterName().equals("newAge") && param.getParameterType().equals("float")),
                "Parameter should be 'newAge' of type 'float' in method 'setName'.");
    }
}
*/
