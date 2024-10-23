package codecain;

import java.util.HashMap;
import java.util.Map;

/**
 * This class manages fields within classes in a UML diagram.
 * Each class can have multiple fields with unique names.
 */
public class UMLFields {

    /**
     * Stores fields for each class.
     * The key is the class name, and the value is a map of field names to field values.
     */
    public static Map<Object, Map<Object, Object>> classFields = new HashMap<>();

    /**
     * Adds a field to a class.
     *
     * @param className the name of the class to which the field is added
     * @param fieldName the name of the field being added
     */
    public void addField(Object className, Object fieldName) {
        if (isInputInvalid(className, fieldName)) return;
        if (!doesClassExists(className)) return;
        Map<Object, Object> fields = classFields.get(className);
        if (fields.containsKey(fieldName)) {
            System.out.println("Action Canceled: Field " + fieldName + " already exists in class " + className);
        } else {
            fields.put(fieldName, new Object());
            System.out.println("Field " + fieldName + " added to class " + className);
        }
    }

    /**
     * Removes a field from a class.
     *
     * @param className the name of the class from which the field is removed
     * @param fieldName the name of the field to be removed
     */
    public void removeField(Object className, Object fieldName) {
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
    public void renameField(Object className, Object oldFieldName, Object newFieldName) {
        if (isInputInvalid(className, oldFieldName) || isInputInvalid(className, newFieldName)) return;
        if (!doesClassExists(className)) return;
        if (!doesFieldExist(className, oldFieldName)) return;
        Map<Object, Object> fields = classFields.get(className);
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
    private boolean isInputInvalid(Object className, Object fieldName) {
        if (className == null || className.toString().isBlank()) {
            System.out.println("Canceled: Input Class Name is Blank");
            return true;
        }
        if (fieldName == null || fieldName.toString().isBlank()) {
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
    public boolean doesClassExists(Object className) {
        if (!UMLClass.classMap.containsKey(className)) {
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
    private boolean doesFieldExist(Object className, Object fieldName) {
        if (!doesClassExists(className)) return false;
        Map<Object, Object> fields = classFields.get(className);
        if (fields == null || !fields.containsKey(fieldName)) {
            System.out.println("Field " + fieldName + " does not exist in class " + className);
            return false;
        }
        return true;
    }


}