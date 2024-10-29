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
    private List<String> parameters;

    /**
     * Constructs a UMLMethodInfo object with the specified method name and parameters.
     *
     * @param methodName the name of the method
     * @param parameters the list of parameters for the method (can be null)
     */
    public UMLMethodInfo(String methodName, List<String> parameters) {
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
    public List<String> getParameters() {
        return this.parameters;
    }

    /**
     * Adds a parameter to the method.
     *
     * @param parameter the parameter to be added
     */
    public void addParameter(String parameter) {
        this.parameters.add(parameter);
    }

    /**
     * Removes a parameter from the method.
     *
     * @param parameter the parameter to be removed
     */
    public void removeParameter(String parameter) {
        this.parameters.remove(parameter);
    }

    /**
     * Renames an existing parameter by replacing the old name with the new name.
     *
     * @param oldName the current name of the parameter
     * @param newName the new name for the parameter
     */
    public void changeParameter(String oldName, String newName) {
        int index = this.parameters.indexOf(oldName);
        if (index != -1) {
            this.parameters.set(index, newName);
        }
    }

    /**
     * Returns a string representation of the UMLMethodInfo object,
     * including the method name and its parameters in a formatted string.
     *
     * @return a string in the format "methodName(param1, param2, ...)"
     */
    @Override
    public String toString() {
        return methodName + "(" + String.join(", ", parameters) + ")";
    }
}
