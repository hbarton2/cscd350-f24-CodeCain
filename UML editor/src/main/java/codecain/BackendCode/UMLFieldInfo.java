package codecain.BackendCode;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The UMLFieldsInfo class represents a field in a UML class.
 * It includes the field name and the field type.
 */
public class UMLFieldInfo {

    /**
     * The name of the field.
     */
    private String fieldName;

    /**
     * The type of the field.
     */
    private String fieldType;

    /**
     * Constructs a UMLFieldsInfo object with default values for the field name and field type.
     * Required for serialization by Jackson.
     */
    public UMLFieldInfo() {
    }
    /**
     * Constructs a UMLFieldsInfo object with the specified field name and field type.
     *
     * @param fieldName the name of the field
     * @param fieldType the type of the field
     */
    @JsonCreator
    public UMLFieldInfo(@JsonProperty("fieldType") String fieldType,
                        @JsonProperty("fieldName") String fieldName) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
    }

    /**
     * Gets the name of the field.
     *
     * @return the name of the field
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Gets the type of the field.
     *
     * @return the type of the field
     */
    public String getFieldType() {
        return fieldType;
    }

    /**
     * Sets the name of the field.
     *
     * @param fieldName the new name of the field
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * Sets the type of the field.
     *
     * @param fieldType the new type of the field
     */
    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    /**
     * Returns a string representation of the UMLFieldsInfo object, showing the field name and type.
     *
     * @return a string in the format "fieldType fieldName"
     */
    @Override
    public String toString() {
        return fieldType + " " + fieldName;
    }
}
