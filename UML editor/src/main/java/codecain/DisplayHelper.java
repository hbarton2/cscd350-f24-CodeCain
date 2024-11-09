package codecain;

/**
 * Helper class providing display messages for various UML diagram operations.
 */
public class DisplayHelper {

    /**
     * Provides a help message listing available commands for UML diagram operations.
     *
     * @return a multi-line string describing the commands and usage examples
     */
    public static String showHelp() {
        return """
            Available commands:

            Class Operations:
            1. add class 'name'                  - Adds a new class with a unique name.
            2. delete class 'name'               - Deletes the class with the specified name.
            3. rename class 'oldName' 'newName'  - Renames the class from 'oldName' to 'newName'.

            Relationship Operations:
            1. add relationship 'source' 'destination'   - Adds a relationship between 'source' and 'destination' classes.
            2. delete relationship 'source' 'destination' - Deletes the relationship between 'source' and 'destination'.

            Field Operations:
            1. add field 'className' 'fieldType' 'fieldName'  - Adds a unique field to the specified class.
            2. delete field 'className' 'fieldType' 'fieldName' - Removes a field from the specified class.
            3. rename field 'className' 'oldFieldName' 'newFieldName' 'newFieldType' - Renames a field and its type in the specified class.

            Method Operations:
            1. add method 'className' 'methodName' 'parameters' - Adds a unique method to the specified class.
            2. delete method 'className' 'methodName' - Removes the method from the specified class.
            3. rename method 'className' 'oldMethodName' 'newMethodName' - Renames a method in the specified class.

            Parameter Operations:
            1. add parameter 'className' 'methodName' 'parameterType' 'parameterName' - Adds a parameter to a method.
            2. delete parameter 'className' 'methodName' 'parameterName' - Removes a parameter from a method.
            3. rename parameter 'className' 'methodName' 'oldParameterName' 'newParameterType' 'newParameterName' - Renames a single parameter in a method, updating both its name and type.
            4. rename all_parameters 'className' 'methodName' 'parameterType1' 'parameterName1' ... - Replaces all parameters in a method with new ones.

            Save/Load Operations:
            1. save                                - Saves the current state of the project.
            2. load                                - Loads the project state from a file.

            Listing Operations:
            1. list classes                        - Lists all the classes in the project.
            2. list class 'className'              - (doesn't work) Lists the contents (fields and methods) of the specified class.
            3. list relationships                  - Lists all the relationships between classes.
            4. list AllClassInfo                   - Lists all the classes with their fields and methods.

            Other Commands:
            1. help                                - Shows this help message.
            2. exit                                - Exits the application.

            Examples:
            - add class Person
            - add relationship Person Address

            - add field Person String name
            - rename field Person name exampleName int

            - add method Person setName String name
            - add parameter Person setName int age

            - delete parameter Person setName age
            - rename parameter Person setName age double years
            - rename all_parameters Person setName double newName float newAge

            - delete class Person
            - rename class Person Employee
            - list classes
            - list relationships
            - help
            - exit

            """;
    }

    /**
     * Returns a message confirming the addition of a parameter to a method in a class.
     *
     * @param parameterName the name of the parameter added
     * @param methodName the method to which the parameter is added
     * @param className the class containing the method
     * @return a confirmation message of parameter addition
     */
    public static String parameterAdded(String parameterName, String methodName, String className) {
        return "Parameter '" + parameterName + "' added to method '" + methodName + "' in class '" + className + "'.";
    }

    /**
     * Returns a message confirming the removal of a parameter from a method in a class.
     *
     * @param parameterName the name of the parameter removed
     * @param methodName the method from which the parameter is removed
     * @param className the class containing the method
     * @return a confirmation message of parameter removal
     */
    public static String parameterRemoved(String parameterName, String methodName, String className) {
        return "Parameter '" + parameterName + "' removed from method '" + methodName + "' in class '" + className + "'.";
    }

    /**
     * Returns a message confirming the renaming of a parameter in a method of a class.
     *
     * @param oldParameterName the old name of the parameter
     * @param newParameterName the new name of the parameter
     * @param methodName the method containing the parameter
     * @param className the class containing the method
     * @return a confirmation message of parameter renaming
     */
    public static String parameterRenamed(String oldParameterName, String newParameterName, String methodName, String className) {
        return "Parameter '" + oldParameterName + "' renamed to '" + newParameterName + "' in method '" + methodName + "' of class '" + className + "'.";
    }

    /**
     * Returns a message confirming that all parameters in a method have been replaced.
     *
     * @param methodName the method in which parameters were replaced
     * @param className the class containing the method
     * @return a confirmation message of parameter replacement
     */
    public static String allParametersChanged(String methodName, String className) {
        return "All parameters replaced in method '" + methodName + "' of class '" + className + "'.";
    }

    /**
     * Returns a message confirming the addition of a class.
     *
     * @param className the name of the added class
     * @return a confirmation message of class addition
     */
    public static String classAdded(String className) {
        return "Class '" + className + "' added.";
    }

    /**
     * Returns a message confirming the removal of a class and its relationships.
     *
     * @param className the name of the removed class
     * @return a confirmation message of class removal
     */
    public static String classRemoved(String className) {
        return "Class '" + className + "' and its attached relationships removed.";
    }

    /**
     * Returns a message confirming the addition of a relationship between two classes.
     *
     * @param class1 the source class of the relationship
     * @param class2 the destination class of the relationship
     * @return a confirmation message of relationship addition
     */
    public static String relationshipAdded(String class1, String class2) {
        return "Relationship between '" + class1 + "' and '" + class2 + "' added.";
    }

    /**
     * Returns a message confirming the removal of a relationship between two classes.
     *
     * @param class1 the source class of the relationship
     * @param class2 the destination class of the relationship
     * @return a confirmation message of relationship removal
     */
    public static String relationshipRemoved(String class1, String class2) {
        return "Relationship between '" + class1 + "' and '" + class2 + "' removed.";
    }

    /**
     * Returns a message confirming the addition of a field to a class.
     *
     * @param fieldName the name of the field added
     * @param fieldType the type of the field
     * @param className the class to which the field was added
     * @return a confirmation message of field addition
     */
    public static String fieldAdded(String fieldName, String fieldType, String className) {
        return "Field '" + fieldName + "' of type '" + fieldType + "' added to class '" + className + "'.";
    }

    /**
     * Returns a message confirming the removal of a field from a class.
     *
     * @param fieldName the name of the field removed
     * @param className the class from which the field was removed
     * @return a confirmation message of field removal
     */
    public static String fieldRemoved(String fieldName, String className) {
        return "Field '" + fieldName + "' removed from class '" + className + "'.";
    }

    /**
     * Returns a message confirming the addition of a method to a class.
     *
     * @param methodName the name of the method added
     * @param className the class to which the method was added
     * @return a confirmation message of method addition
     */
    public static String methodAdded(String methodName, String className) {
        return "Method '" + methodName + "' added to class '" + className + "'.";
    }

    /**
     * Returns a message confirming the removal of a method from a class.
     *
     * @param methodName the name of the method removed
     * @param className the class from which the method was removed
     * @return a confirmation message of method removal
     */
    public static String methodRemoved(String methodName, String className) {
        return "Method '" + methodName + "' removed from class '" + className + "'.";
    }

    /**
     * Returns a message confirming the renaming of a class.
     *
     * @param oldName the original name of the class
     * @param newName the new name for the class
     * @return a confirmation message of class renaming
     */
    public static String classRenamed(String oldName, String newName) {
        return "Class '" + oldName + "' renamed to '" + newName + "'.";
    }

    /**
     * Returns a message confirming the renaming of a field in a class.
     *
     * @param oldFieldName the original name of the field
     * @param newFieldName the new name for the field
     * @param className the class containing the field
     * @return a confirmation message of field renaming
     */
    public static String fieldRenamed(String oldFieldName, String newFieldName, String className) {
        return "Field '" + oldFieldName + "' renamed to '" + newFieldName + "' in class '" + className + "'.";
    }

    /**
     * Returns a message confirming the renaming of a method in a class.
     *
     * @param oldMethodName the original name of the method
     * @param newMethodName the new name for the method
     * @param className the class containing the method
     * @return a confirmation message of method renaming
     */
    public static String methodRenamed(String oldMethodName, String newMethodName, String className) {
        return "Method '" + oldMethodName + "' renamed to '" + newMethodName + "' in class '" + className + "'.";
    }

    /**
     * Returns a message indicating an unknown action in a command.
     *
     * @param action the unknown action type
     * @return an error message for the unknown action
     */
    public static String unknownAction(String action) {
        return "Unknown action: " + action;
    }

    /**
     * Returns a message indicating an unknown type for add operations.
     *
     * @param type the unknown add type
     * @return an error message for the unknown add type
     */
    public static String unknownAddType(String type) {
        return "Unknown add type: " + type + ". Use 'help' for available commands.";
    }

    /**
     * Returns a message indicating an unknown type for delete operations.
     *
     * @param type the unknown delete type
     * @return an error message for the unknown delete type
     */
    public static String unknownDeleteType(String type) {
        return "Unknown delete type: " + type + ". Use 'help' for available commands.";
    }

    /**
     * Returns a message indicating insufficient parameters for a command.
     *
     * @return an error message for insufficient parameters
     */
    public static String insufficientParameters() {
        return "Insufficient parameters. Type 'help' for more info.";
    }
}
