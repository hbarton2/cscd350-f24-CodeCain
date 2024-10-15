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
    private Map<String, Map<String, String>> classFields = new HashMap<>();

    /**
     * Adds a field to a class.
     *
     * @param className the name of the class to which the field is added
     * @param fieldName the name of the field being added
     */
    public void addField(String className, String fieldName) {
        if (validateInputs(className, fieldName)) return;
        if (classExists(className)) return;

        classFields.putIfAbsent(className, new HashMap<>());
        Map<String, String> fields = classFields.get(className);

        if (fields.containsKey(fieldName)) {
            System.out.println("Error: Field " + fieldName + " already exists in class " + className);
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
        if (validateInputs(className, fieldName)) return;
        if (classExists(className)) return;
        if (fieldExists(className, fieldName)) return;

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
        if (validateInputs(className, oldFieldName) || newFieldName == null) {
            System.out.println("Error: Class Name or Old/New Field Name is Null");
            return;
        }
        if (classExists(className)) return;
        if (fieldExists(className, oldFieldName)) return;

        Map<String, String> fields = classFields.get(className);
        if (fields.containsKey(newFieldName)) {
            System.out.println("Error: Field " + newFieldName + " already exists in class " + className);
        } else {
            fields.put(newFieldName, fields.remove(oldFieldName));
            System.out.println("Field " + oldFieldName + " renamed to " + newFieldName + " in class " + className);
        }
    }


    /**
     * Helper method to validate input.
     *
     * @param className the name of the class
     * @param fieldName the name of the field
     * @return false if both className and fieldName are not null, true otherwise
     */
    private boolean validateInputs(String className, String fieldName) {
        if (className == null || fieldName == null) {
            System.out.println("Error: Class Name or Field Name is Null");
            return true;
        }
        return false;
    }

    /**
     * Helper method to check if a class exists in the map.
     *
     * @param className the name of the class
     * @return false if the class exists, true otherwise
     */
    private boolean classExists(String className) {
        if (!classFields.containsKey(className)) {
            System.out.println("Error: Class " + className + " does not exist");
            return true;
        }
        return false;
    }

    /**
     * Helper method to check if a field exists in a class.
     *
     * @param className the name of the class
     * @param fieldName the name of the field
     * @return false if the field exists, true otherwise
     */
    private boolean fieldExists(String className, String fieldName) {
        Map<String, String> fields = classFields.get(className);
        if (!fields.containsKey(fieldName)) {
            System.out.println("Error: Field " + fieldName + " does not exist in class " + className);
            return true;
        }
        return false;
    }
}
