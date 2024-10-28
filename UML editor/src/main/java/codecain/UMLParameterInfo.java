package codecain;

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
     * Constructs a UMLParameterInfo object with the specified field name and parameter type.
     *
     * @param parameterName the name of the parameter
     * @param parameterType the type of the parameter
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
     * Gets the type of the parameter.
     *
     * @return the type of the parameter
     */
    public String getParameterType() {
        return parameterType;
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
     * Sets the type of the parameter.
     *
     * @param parameterType the new type of the parameter
     */
    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    /**
     * Returns a string representation of the UMLParameterInfo object, showing the parameter name and type.
     *
     * @return a string in the format "parameterType : parameterName"
     */
    @Override
    public String toString() {
        return parameterType + " : " + parameterName;
    }
}

