package codecain;

import java.util.HashMap;
import java.util.Map;

/**
 * This class manages fields within classes in a UML diagram.
 * Each class can have multiple fields with unique names.
 */
public class Fields {

    /**
     * Stores fields for each class.
     * The key is the class name, and the value is a map of field names to field values.
     */
    public static Map<String, Map<String, String>> classFields = new HashMap<>();

    /**
     * Adds a field to a class.
     *
     * @param className the name of the class to which the field is added
     * @param fieldName the name of the field being added
     */
    public void addField(String className, String fieldName) {
        if (isInputInvalid(className, fieldName)) return;
        if (!doesClassExists(className)) return;
        Map<String, String> fields = classFields.get(className);
        if (fields.containsKey(fieldName)) {
            System.out.println("Action Canceled: Field " + fieldName + " already exists in class " + className);
        } else {
            fields.put(fieldName, "");
            System.out.println("Field " + fieldName + " added to class " + className);
        }
    }

    /**
     * Removes a field from a class.
     *
     * @param className the name of the class from which the field is removed
     * @param fieldName the name of the field to be removed
     */
    public void removeField(String className, String fieldName) {
        if (isInputInvalid(className, fieldName)) return;
        if (!doesClassExists(className)) return;
        if (!doesFieldExist(className, fieldName)) return;
        classFields.get(className).remove(fieldName);
        System.out.println("Field " + fieldName + " removed from class " + className);
    }

    /**
     * Renames an existing field in a class.
     *
     * @param className the name of the class containing the field
     * @param oldFieldName the current name of the field
     * @param newFieldName the new name for the field
     */
    public void renameField(String className, String oldFieldName, String newFieldName) {
        if (className.isBlank() || oldFieldName.isBlank() || newFieldName.isBlank()) {
            System.out.println("Action Canceled: One or More Inputs (Class Name, Old Field Name, New Field Name) are empty");
            return;
        }
        if (!doesClassExists(className)) return;
        if (!doesFieldExist(className, oldFieldName)) return;
        Map<String, String> fields = classFields.get(className);
        if (fields.containsKey(newFieldName)) {
            System.out.println("Action Canceled: Field " + newFieldName + " already exists in class " + className);
        } else {
            fields.put(newFieldName, fields.remove(oldFieldName));
            System.out.println("Field " + oldFieldName + " renamed to " + newFieldName + " in class " + className);
        }
    }


    /**
     * Helper method to validate Class and Field.
     *
     * @param className the name of the class
     * @param fieldName the name of the field
     * @return true if both className and fieldName are blank, false otherwise
     */
    private boolean isInputInvalid(String className, String fieldName) {
        if (className.isBlank()) {
            System.out.println("Canceled: Input Class Name is Blank");
            return true;
        }
        if (fieldName.isBlank()) {
            System.out.println("Canceled: Input Field Name is Blank");
            return true;
        }
        return false;
    }


    /**
     * Helper method to check if a class exists in the map.
     *
     * @param className the name of the class
     * @return true if the class exists, false otherwise
     */
    public boolean doesClassExists(String className) {
        if (!Class.classMap.containsKey(className)) {
            System.out.println("Action Canceled: Class " + className + " does not exist");
            return false;
        }
        return true;
    }

    /**
     * Helper method to check if a field exists in a class.
     *
     * @param className the name of the class
     * @param fieldName the name of the field
     * @return true if the field exists, false otherwise
     */
    private boolean doesFieldExist(String className, String fieldName) {
        if (!classFields.get(className).containsKey(fieldName)) {
            System.out.println("Field " + fieldName + " does not exist in class " + className);
            return false;
        }
        return true;
    }
}