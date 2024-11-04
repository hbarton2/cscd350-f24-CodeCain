package codecain;

import java.util.List;

/**
 * The UMLMethods class provides functionality to manage methods in a UML class.
 * This includes adding, removing, renaming methods, and managing their parameters(adding, removing, and renaming).
 */
public class UMLMethods {

    /**
     * Adds a method to a class.
     *
     * @param className   The name of the class to which the method is added
     * @param methodName  The name of the method being added
     * @param parameters  A list of UMLParameterInfo objects for the method
     */
    public void addMethod(String className, String methodName, List<UMLParameterInfo> parameters) {
        if (isInputInvalid(className, methodName)) return;
        UMLClassInfo classInfo = getClassInfo(className);
        if (classInfo == null) return;

        UMLMethodInfo existingMethod = classInfo.getMethodByName(methodName);
        if (existingMethod != null && existingMethod.getParameters().equals(parameters)) {
            System.out.println("Action Canceled: Method " + methodName + " with " + parameters + " already exists in class " + className);
        } else {
            classInfo.getMethods().add(new UMLMethodInfo(methodName, parameters));
            System.out.println("Method " + methodName + " with " + parameters + " added to class " + className);
        }
    }


    /**
     * Removes a method from a class.
     *
     * @param className  The name of the class
     * @param methodName The name of the method to remove
     */
    public void removeMethod(String className, String methodName) {
        UMLClassInfo classInfo = getClassInfo(className);
        if (classInfo == null) return;
        UMLMethodInfo method = classInfo.getMethodByName(methodName);
        if (method == null) {
            System.out.println("Action Canceled: Method " + methodName + " does not exist in class " + className);
        } else {
            classInfo.getMethods().remove(method);
            System.out.println("Method " + methodName + " removed from class " + className);
        }
    }

    /**
     * Renames a method in a class.
     *
     * @param className     The name of the class containing the method
     * @param oldMethodName The current name of the method
     * @param newMethodName The new name for the method
     */
    public void renameMethod(String className, String oldMethodName, String newMethodName) {
        UMLClassInfo classInfo = getClassInfo(className);
        if (classInfo == null) return;
        UMLMethodInfo method = classInfo.getMethodByName(oldMethodName);
        if (method == null) {
            System.out.println("Action Canceled: Method " + oldMethodName + " does not exist in class " + className);
        } else if (classInfo.getMethodByName(newMethodName) != null) {
            System.out.println("Action Canceled: Method " + newMethodName + " already exists in class " + className);
            // Needs Fixed to work with overloaded methods
        } else {
            method.setMethodName(newMethodName);
            System.out.println("Method " + oldMethodName + " renamed to " + newMethodName + " in class " + className);
        }
    }

    /**
     * Adds a parameter to a method.
     *
     * @param className     The name of the class
     * @param methodName    The name of the method to add a parameter to
     * @param parameterType The type of the parameter to add
     * @param parameterName The name of the parameter to add
     */
    public void addParameter(String className, String methodName, String parameterType, String parameterName) {
        UMLClassInfo classInfo = getClassInfo(className);
        if (classInfo == null) return;
        UMLMethodInfo method = classInfo.getMethodByName(methodName);
        if (method == null) {
            System.out.println("Action Canceled: Method " + methodName + " does not exist in class " + className);
            return;
        }
        UMLParameterInfo newParameter = new UMLParameterInfo(parameterType, parameterName);
        if (!method.getParameters().contains(newParameter)) {
            method.addParameter(newParameter);
            System.out.println("Parameter " + newParameter + " added to method " + methodName + " in class " + className);
        } else {
            System.out.println("Action Canceled: Parameter " + newParameter + " already exists in method " + methodName);
        }
    }

    /**
     * Removes a parameter from a method based on the parameter name.
     *
     * @param className  The name of the class
     * @param methodName The name of the method to remove a parameter from
     * @param parameterName The name of the parameter to remove
     */
    public void removeParameter(String className, String methodName, String parameterName) {
        UMLClassInfo classInfo = getClassInfo(className);
        if (classInfo == null) return;
        UMLMethodInfo method = classInfo.getMethodByName(methodName);
        if (method == null) {
            System.out.println("Action Canceled: Method " + methodName + " does not exist in class " + className);
            return;
        }
        UMLParameterInfo parameterToRemove = getParameterByName(method, parameterName);
        if (parameterToRemove != null) {
            method.removeParameter(parameterToRemove);
            System.out.println("Parameter '" + parameterName + "' removed from method '" + methodName + "' in class '" + className + "'.");
        } else {
            System.out.println("Action Canceled: Parameter '" + parameterName + "' does not exist in method '" + methodName + "'.");
        }
    }


    /**
     * Changes all parameters of a method by replacing the current list with a new list of parameters.
     *
     * @param className   The name of the class
     * @param methodName  The name of the method to change parameters for
     * @param newParameters The new list of UMLParameterInfo objects to set as the parameters of the method
     */
    public void changeAllParameters(String className, String methodName, List<UMLParameterInfo> newParameters) {
        UMLClassInfo classInfo = getClassInfo(className);
        if (classInfo == null) return;
        UMLMethodInfo method = getMethodInfo(classInfo, methodName);
        if (method == null) return;
        method.getParameters().clear();
        newParameters.forEach(param -> addParameter(className, methodName, param.getParameterType(), param.getParameterName()));
        System.out.println("All parameters replaced for method " + methodName + " in class " + className);
    }

    /**
     * Changes a single parameter in a method, identified by the old parameter name, with a new parameter type and name.
     *
     * @param className        The name of the class
     * @param methodName       The name of the method to change the parameter in
     * @param oldParameterName The name of the existing parameter to replace
     * @param newParameterType The type of the new parameter
     * @param newParameterName The name of the new parameter
     */
    public void changeSingleParameter(String className, String methodName, String oldParameterName, String newParameterType, String newParameterName) {
        UMLClassInfo classInfo = getClassInfo(className);
        if (classInfo == null) return;
        UMLMethodInfo method = classInfo.getMethodByName(methodName);
        if (method == null) {
            System.out.println("Action Canceled: Method " + methodName + " does not exist in class " + className);
            return;
        }
        if (renameParameter(method, oldParameterName, newParameterType, newParameterName)) {
            System.out.println("Parameter " + oldParameterName + " updated to " + newParameterType + " " + newParameterName + " in method " + methodName + " of class " + className);
        } else {
            System.out.println("Action Canceled: Parameter " + oldParameterName + " does not exist in method " + methodName);
        }
    }

    /**
     * Helper method to update a parameter's name and type within a UMLMethodInfo.
     *
     * @param method           The UMLMethodInfo object
     * @param oldParameterName The current name of the parameter
     * @param newParameterType The new type for the parameter
     * @param newParameterName The new name for the parameter
     * @return true if the parameter was found and updated, false otherwise
     */
    private boolean renameParameter(UMLMethodInfo method, String oldParameterName, String newParameterType, String newParameterName) {
        for (UMLParameterInfo param : method.getParameters()) {
            if (param.getParameterName().equals(oldParameterName)) {
                param.setParameterType(newParameterType);
                param.setParameterName(newParameterName);
                return true;
            }
        }
        return false;
    }

    /**
     * Helper method to check if className and methodName are not blank.
     *
     * @param className  The name of the class
     * @param methodName The name of the method
     * @return true if both values are valid, false otherwise
     */
    private boolean isInputInvalid(String className, String methodName) {
        if (className == null || className.isBlank()) {
            System.out.println("Action Canceled: Input Class Name is Blank");
            return true;
        }
        if (methodName == null || methodName.isBlank()) {
            System.out.println("Action Canceled: Input Method Name is Blank");
            return true;
        }
        return false;
    }

    /**
     * Helper method to retrieve UMLClassInfo for a class.
     * Prints an error message if the class does not exist.
     *
     * @param className The name of the class.
     * @return The UMLClassInfo object if the class exists; null otherwise.
     */
    private UMLClassInfo getClassInfo(String className) {
        UMLClassInfo classInfo = UMLClass.classMap.get(className);
        if (classInfo == null) {
            System.out.println("Action Canceled: Class " + className + " does not exist");
        }
        return classInfo;
    }

    /**
     * Helper method to retrieve a method from a class if it exists.
     * Prints an error message if the method does not exist.
     *
     * @param classInfo  The UMLClassInfo object representing the class.
     * @param methodName The name of the method.
     * @return The UMLMethodInfo object if the method exists; null otherwise.
     */
    private UMLMethodInfo getMethodInfo(UMLClassInfo classInfo, String methodName) {
        UMLMethodInfo method = classInfo.getMethodByName(methodName);
        if (method == null) {
            System.out.println("Action Canceled: Method " + methodName + " does not exist in class " + classInfo.getClassName());
        }
        return method;
    }

    /**
     * Retrieves a parameter by its name from a method.
     *
     * @param method the UMLMethodInfo object representing the method
     * @param parameterName the name of the parameter to find
     * @return the UMLParameterInfo object representing the parameter, or null if not found
     */
    private UMLParameterInfo getParameterByName(UMLMethodInfo method, String parameterName) {
        for (UMLParameterInfo parameter : method.getParameters()) {
            if (parameter.getParameterName().equals(parameterName)) {
                return parameter;
            }
        }
        return null;
    }
}
