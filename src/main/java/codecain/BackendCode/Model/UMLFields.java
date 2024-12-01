package codecain.BackendCode.Model;

/**
 * The UMLFields class provides functionality to manage fields in a UML class.
 * This includes adding, removing, and renaming fields within a class.
 */
public class UMLFields {

    /**
     * Adds a field to a class.
     *
     * @param className the name of the class to which the field is added
     * @param fieldName the name of the field being added
     * @param fieldType the type of the field being added
     */
    public void addField(String className, String fieldType, String fieldName) {
        if (isInputInvalid(className, fieldName)) return;
        if (fieldType == null || fieldType.isBlank()) {
            System.out.println("Action Canceled: Field type is invalid");
            return;
        }
        UMLClassInfo classInfo = getClassInfo(className);
        if (classInfo == null) return;
        if (doesFieldExist(classInfo, fieldName)) {

            System.out.println("Action Canceled: Field of type " + fieldType + " " + fieldName + " already exists in class " + className);
            return;
        }
        classInfo.getFields().add(new UMLFieldInfo(fieldType, fieldName));
        System.out.println("Field of type " + fieldType + " " + fieldName + " added to class " + className);
    }

    /**
     * Removes a field from a class.
     *
     * @param className the name of the class from which the field is removed
     * @param fieldName the name of the field to be removed
     */
    public void removeField(String className, String fieldName) {
        if (isInputInvalid(className, fieldName)) return;
        UMLClassInfo classInfo = getClassInfo(className);
        if (classInfo == null) return;
        UMLFieldInfo field = getFieldByName(classInfo, fieldName);
        if (field == null) {
            System.out.println("Action Canceled: Field " + fieldName + " does not exist in class " + className);
            return;
        }
        classInfo.getFields().remove(field);
        System.out.println("Field " + fieldName + " removed from class " + className);
    }

    /**
     * Renames an existing field in a class and optionally changes its type.
     *
     * @param className the name of the class containing the field
     * @param oldFieldName the current name of the field
     * @param newFieldName the new name for the field
     * @param newFieldType the new type for the field
     */
    public void renameField(String className, String oldFieldName, String newFieldType, String newFieldName) {
        if (isInputInvalid(className, oldFieldName) || isInputInvalid(className, newFieldName)) {
            return;
        }
        UMLClassInfo classInfo = getClassInfo(className);
        if (classInfo == null) return;
        UMLFieldInfo field = getFieldByName(classInfo, oldFieldName);
        if (field == null) {
            System.out.println("Action Canceled: Field " + oldFieldName + " does not exist in class " + className);
            return;
        }
        if (!oldFieldName.equals(newFieldName) && getFieldByName(classInfo, newFieldName) != null) {
            System.out.println("Action Canceled: Field " + newFieldName + " already exists in class " + className);
            return;
        }
        field.setFieldName(newFieldName);
        field.setFieldType(newFieldType);
        System.out.println("Field " + oldFieldName + " renamed to " + newFieldName + " with type " + newFieldType + " in class " + className);
    }


    /**
     * Helper method to validate className and fieldName.
     *
     * @param className the name of the class
     * @param fieldName the name of the field
     * @return true if any of the inputs are invalid, false otherwise
     */
    private boolean isInputInvalid(String className, String fieldName) {
        if (className == null || className.isBlank()) {
            System.out.println("Action Canceled: Class name is invalid");
            return true;
        }
        if (fieldName == null || fieldName.isBlank()) {
            System.out.println("Action Canceled: Field name is invalid");
            return true;
        }
        return false;
    }


    /**
     * Helper method to get UMLClassInfo for a class.
     *
     * @param className the name of the class
     * @return the UMLClassInfo object, or null if class does not exist
     */
    private UMLClassInfo getClassInfo(String className) {
        UMLClassInfo classInfo = UMLClass.classMap.get(className);
        if (classInfo == null) {
            System.out.println("Action Canceled: Class " + className + " does not exist");
        }
        return classInfo;
    }

    /**
     * Helper method to check if a field with the given type and name exists in the class.
     *
     * @param classInfo the UMLClassInfo object for the class
     * @param fieldName the name of the field
     * @return true if the field exists, false otherwise
     */
    public boolean doesFieldExist(UMLClassInfo classInfo, String fieldName) {
        for (UMLFieldInfo field : classInfo.getFields()) {
            if (field.getFieldName().equals(fieldName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves a field by its name from the class.
     *
     * @param classInfo the UMLClassInfo object for the class
     * @param fieldName the name of the field
     * @return the UMLFieldInfo object representing the field, or null if not found
     */
    private UMLFieldInfo getFieldByName(UMLClassInfo classInfo, String fieldName) {
        for (UMLFieldInfo field : classInfo.getFields()) {
            if (field.getFieldName().equals(fieldName)) {
                return field;
            }
        }
        return null;
    }
}
