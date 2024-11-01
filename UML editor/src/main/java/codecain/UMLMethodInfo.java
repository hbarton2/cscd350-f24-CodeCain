package codecain;

import java.util.ArrayList;
import java.util.List;

/**
 * The UMLMethodInfo class represents a method in a UML class.
 * It stores the method name and a list of parameters.
 */
public class UMLMethodInfo {

    /**
     * The name of the method.
     */
    private String methodName;

    /**
     * The list of parameters for the method.
     */
    private List<UMLParameterInfo> parameters;

    /**
     * Constructs a UMLMethodInfo object with the specified method name and parameters.
     *
     * @param methodName the name of the method
     * @param parameters the list of parameters for the method (can be null)
     */
    public UMLMethodInfo(String methodName, List<UMLParameterInfo> parameters) {
        this.methodName = methodName;
        if (parameters != null) {
            this.parameters = new ArrayList<>(parameters);
        } else {
            this.parameters = new ArrayList<>();
        }
    }

    /**
     * Gets the name of the method.
     *
     * @return the name of the method
     */
    public String getMethodName() {
        return this.methodName;
    }

    /**
     * Sets the name of the method.
     *
     * @param methodName the new name of the method
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    /**
     * Gets the list of parameters for the method.
     *
     * @return the list of parameters
     */
    public List<UMLParameterInfo> getParameters() {
        return this.parameters;
    }

    /**
     * Adds a parameter to the method.
     *
     * @param parameter the parameter to be added
     */
    public void addParameter(UMLParameterInfo parameter) {
        this.parameters.add(parameter);
    }

    /**
     * Removes a parameter from the method.
     *
     * @param parameter the parameter to be removed
     */
    public void removeParameter(UMLParameterInfo parameter) {
        this.parameters.remove(parameter);
    }

    /**
     * Renames an existing parameter by replacing the old name with the new name.
     *
     * @param oldParameter the current name of the parameter
     * @param newParameter the new name for the parameter
     */
    public void changeParameter(UMLParameterInfo oldParameter, UMLParameterInfo newParameter) {
        int index = this.parameters.indexOf(oldParameter);
        if (index != -1) {
            this.parameters.set(index, newParameter);
        }
    }

    /**
     * Returns a string representation of the method with parameters in the format "name(type name, ...)".
     *
     * @return formatted method as "name(type name, ...)"
     */
    @Override
    public String toString() {
        StringBuilder parameterList = new StringBuilder();
        for (int i = 0; i < parameters.size(); i++) {
            parameterList.append(parameters.get(i).toString());
            if (i < parameters.size() - 1) {
                parameterList.append(", ");
            }
        }
        return methodName + "(" + parameterList + ")";
    }
}
