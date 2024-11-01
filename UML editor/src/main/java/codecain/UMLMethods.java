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
     * @param parameters  A list of parameters for the method
     */
    public void addMethod(String className, String methodName, List<String> parameters) {
        if (isInputInvalid(className, methodName)) return;
        UMLClassInfo classInfo = getClassInfo(className);
        if (classInfo == null) return;
        UMLMethodInfo existingMethod = classInfo.getMethodByName(methodName);
        if (existingMethod != null && existingMethod.getParameters().equals(parameters)) {
            System.out.println("Action Canceled: Method " + methodName + " with " + parameters + " already exists in class " + className);
        } else {
            UMLMethodInfo newMethod = new UMLMethodInfo(methodName, parameters);
            classInfo.getMethods().add(newMethod);
            System.out.println("Method " + methodName + " with" + parameters + " added to class " + className);
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
     * @param className   The name of the class
     * @param methodName  The name of the method to add a parameter to
     * @param parameter   The new parameter to add
     */
    public void addParameter(String className, String methodName, String parameter) {
        UMLClassInfo classInfo = getClassInfo(className);
        if (classInfo == null) return;
        UMLMethodInfo method = classInfo.getMethodByName(methodName);
        if (method == null) {
            System.out.println("Action Canceled: Method " + methodName + " does not exist in class " + className);
            return;
        }
        if (!method.getParameters().contains(parameter)) {
            method.addParameter(parameter);
            System.out.println("Parameter " + parameter + " added to method " + methodName + " in class " + className);
        } else {
            System.out.println("Action Canceled: Parameter " + parameter + " already exists in method " + methodName);
        }
    }

    /**
     * Removes a parameter from a method.
     *
     * @param className  The name of the class
     * @param methodName The name of the method to remove a parameter from
     * @param parameter  The parameter to remove
     */
    public void removeParameter(String className, String methodName, String parameter) {
        UMLClassInfo classInfo = getClassInfo(className);
        if (classInfo == null) return;
        UMLMethodInfo method = classInfo.getMethodByName(methodName);
        if (method == null) {
            System.out.println("Action Canceled: Method " + methodName + " does not exist in class " + className);
            return;
        }
        if (method.getParameters().contains(parameter)) {
            method.removeParameter(parameter);
            System.out.println("Parameter " + parameter + " removed from method " + methodName + " in class " + className);
        } else {
            System.out.println("Action Canceled: Parameter " + parameter + " does not exist in method " + methodName);
        }
    }

    /**
     * Changes all parameters of an existing method.
     *
     * @param className     The name of the class
     * @param methodName    The name of the method to change parameters for
     * @param newParameters The new list of parameters
     */
    public void changeParameter(String className, String methodName, String oldParameterName, String newParameterName) {
        UMLClassInfo classInfo = getClassInfo(className);
        if (classInfo == null) return;
        UMLMethodInfo method = classInfo.getMethodByName(methodName);
        if (method == null) {
            System.out.println("Action Canceled: Method " + methodName + " does not exist in class " + className);
            return;
        }
        method.changeParameter(oldParameterName, newParameterName);
        System.out.println("Parameter " + oldParameterName + " renamed to " + newParameterName + " in method " + methodName + " of class " + className);
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
     * Helper method to get UMLClassInfo for a class.
     *
     * @param className The name of the class
     * @return the UMLClassInfo object, or null if class does not exist
     */
    private UMLClassInfo getClassInfo(String className) {
        UMLClassInfo classInfo = UMLClass.classMap.get(className);
        if (classInfo == null) {
            System.out.println("Action Canceled: Class " + className + " does not exist");
        }
        return classInfo;
    }
}
