package codecain.BackendCode;

import java.util.Objects;

/**
 * The UMLParameterInfo class represents a parameter in a UML class.
 * It includes the parameter name and the parameter type.
 */
public class UMLParameterInfo {

    /**
     * The name of the parameter.
     */
    private String parameterName;

    /**
     * The type of the parameter.
     */
    private String parameterType;

    /**
     * No-argument constructor for Jackson deserialization.
     */
    public UMLParameterInfo() {}

    /**
     * Constructs a UMLParameterInfo object with the specified parameter type and parameter name.
     *
     * @param parameterType the type of the parameter
     * @param parameterName the name of the parameter
     */
    public UMLParameterInfo(String parameterType, String parameterName) {
        this.parameterType = parameterType;
        this.parameterName = parameterName;
    }

    /**
     * Gets the name of the parameter.
     *
     * @return the name of the parameter
     */
    public String getParameterName() {
        return parameterName;
    }

    /**
     * Sets the name of the parameter.
     *
     * @param parameterName the new name of the parameter
     */
    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    /**
     * Gets the type of the parameter.
     *
     * @return the type of the parameter
     */
    public String getParameterType() {
        return parameterType;
    }

    /**
     * Sets the type of the parameter.
     *
     * @param parameterType the new type of the parameter
     */
    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    /**
     * Returns a string representation of the parameter in the format "type name".
     *
     * @return a formatted string representing the parameter as "type name"
     */
    @Override
    public String toString() {
        return parameterType + " " + parameterName;
    }

    /**
     * Compares this UMLParameterInfo object to another object for equality.
     * Two UMLParameterInfo objects are considered equal if they have the same parameter type and name.
     *
     * @param o the object to compare with this UMLParameterInfo
     * @return true if the specified object is equal to this UMLParameterInfo; false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UMLParameterInfo that = (UMLParameterInfo) o;
        return parameterType.equals(that.parameterType) && parameterName.equals(that.parameterName);
    }

    /**
     * Returns the hash code value for this UMLParameterInfo object based on the parameter type and name.
     *
     * @return the hash code value for this UMLParameterInfo
     */
    @Override
    public int hashCode() {
        return Objects.hash(parameterType, parameterName);
    }
}
