import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class manages methods within classes in a UML diagram.
 * Each class can have multiple methods, each with a list of parameters.
 */
public class Methods {

    /**
     * Stores methods for each class.
     * The key is the class name and the value is a map of method names to their parameters.
     */
    public static Map<String, Map<String, List<String>>> classMethods = new HashMap<>();

    /**
     * Adds a method to a class.
     *
     * @param className the name of the class to which the method is added
     * @param methodName the name of the method being added
     * @param parameters a list of parameters for the method
     */
    public void addMethod(String className, String methodName, List<String> parameters) {
        if (isClassNameOrMethodNameInvalid(className, methodName)) { return; }
        if (!doesClassExists(className)) {return;}
        Map<String, List<String>> methods = classMethods.get(className);
        if (methods.containsKey(methodName)) {
            System.out.println("Action Canceled: Method " + methodName + " already exists in class " + className);
        } else {
            methods.put(methodName, parameters);
            System.out.println("Method " + methodName + " added to class " + className);
        }
    }

    /**
     * Removes a method from a class.
     *
     * @param className the name of the class from which the method is removed
     * @param methodName the name of the method to be removed
     */
    public void removeMethod(String className, String methodName) {
        if (isClassNameOrMethodNameInvalid(className, methodName)) return;
        if (!doesMethodExist(className, methodName)) return;
        classMethods.get(className).remove(methodName);
        System.out.println("Method " + methodName + " removed from class " + className);
    }

    /**
     * Renames an existing method in a class.
     *
     * @param className the name of the class containing the method
     * @param oldMethodName the current name of the method
     * @param newMethodName the new name for the method
     */
    public void renameMethod(String className, String oldMethodName, String newMethodName) {
        if (className.isBlank() || oldMethodName.isBlank() || newMethodName.isBlank()) {
            System.out.println("Action Canceled: One or More Inputs (Class Name, Old Method Name, New Method Name) are empty");
            return;
        }
        if (!doesClassExists(className)) {return;}
        Map<String, List<String>> methods = classMethods.get(className);
        if (methods.containsKey(newMethodName)) {
            System.out.println("Action Canceled: Method " + newMethodName + " already exists in class " + className);
        } else {
            methods.put(newMethodName, methods.remove(oldMethodName));
            System.out.println("Method " + oldMethodName + " renamed to " + newMethodName + " in class " + className);
        }
    }

    /**
     * Adds a parameter to an existing method.
     *
     * @param className the name of the class
     * @param methodName the name of the method to add a parameter to
     * @param parameter the parameter to add (e.g., "int param1")
     */
    public void addParameter(String className, String methodName, String parameter) {
        if (isClassNameOrMethodNameOrParameterInvalid(className, methodName, parameter)) return;
        if (!doesMethodExist(className, methodName)) return;
        List<String> parameters = classMethods.get(className).get(methodName);
        if (!parameters.contains(parameter)) {
            parameters.add(parameter);
            System.out.println("Parameter " + parameter + " added to method " + methodName + " in class " + className);
        } else {
            System.out.println("Action Canceled:Parameter " + parameter + " already exists in method " + methodName);
        }
    }

    /**
     * Removes a parameter from an existing method.
     *
     * @param className the name of the class
     * @param methodName the name of the method to remove the parameter from
     * @param parameter the parameter to remove
     */
    public void removeParameter(String className, String methodName, String parameter) {
        if (isClassNameOrMethodNameOrParameterInvalid(className, methodName, parameter)) return;
        if (!doesMethodExist(className, methodName)) return;
        List<String> parameters = classMethods.get(className).get(methodName);
        if (!parameters.contains(parameter)) {
            System.out.println("Action Canceled: Parameter " + parameter + " does not exist in method " + methodName);
        } else {
            parameters.remove(parameter);
            System.out.println("Parameter " + parameter + " removed from method " + methodName + " in class " + className);
        }
    }

    /**
     * Replaces the parameters of an existing method with a new list.
     *
     * @param className the name of the class
     * @param methodName the name of the method to replace parameters for
     * @param newParameters the new list of parameters
     */
    public void changeParameters(String className, String methodName, List<String> newParameters) {
        if (isClassNameOrMethodNameOrParameterInvalid(className, methodName, String.valueOf(newParameters))) return;
        if (!doesClassExists(className)) {return;}

        Map<String, List<String>> methods = classMethods.get(className);
        if (!methods.containsKey(methodName)) {
            System.out.println("Action Canceled: Method " + methodName + " does not exist in class " + className);
            return;
        }
        methods.put(methodName, newParameters);
        System.out.println("Parameters in method " + methodName + " in class " + className + " have been updated");

    }

    /**
     * Helper method to check if a class exists in the map.
     *
     * @param className the name of the class
     * @return true if the class exists, false otherwise
     */
    public boolean doesClassExists(String className) {
        if (!Class.classMap.containsKey(className)) {
            System.out.println("Action Canceled: Class " + className + " does not exist");
            return false;
        }
        return true;
    }

    /**
     * Helper method to check if className and methodName are not blank.
     *
     * @param className the name of the class
     * @param methodName the name of the method or parameter
     * @return true if both values are valid, false otherwise
     */
    private boolean isClassNameOrMethodNameInvalid(String className, String methodName) {
        if (className.isBlank()) {
            System.out.println("Canceled: Input Class Name is Blank");
            return true;
        }
        if (methodName.isBlank()) {
            System.out.println("Canceled: Input Method Name is Blank");
            return true;
        }
        return false;
    }

    /**
     * Helper method to check if className and methodName/parameter are not blank.
     *
     * @param className the name of the class
     * @param methodName the name of the method or parameter
     * @return true if all values are valid, false otherwise
     */
    private boolean isClassNameOrMethodNameOrParameterInvalid(String className, String methodName, String parameter) {
        isClassNameOrMethodNameInvalid(className, methodName);
        if (parameter.isBlank()) {
            System.out.println("Canceled: Inputted Parameter is Blank");
            return true;
        }
        return false;
    }

    /**
     * Retrieves the methods map for a class.
     * Checks if the class exists in the map before returning the methods.
     *
     * @param className the name of the class
     * @return the methods map for the class, or null if the class does not exist
     */
    private Map<String, List<String>> getMethodsForClass(String className) {
        if (doesClassExists(className)) {
            return null;
        }
        return classMethods.get(className);
    }

    /**
     * Checks if a method exists within a class.
     *
     * @param className the name of the class
     * @param methodName the name of the method
     * @return true if the method exists, false otherwise
     */
    private boolean doesMethodExist(String className, String methodName) {
        if (!classMethods.get(className).containsKey(methodName)) {
            System.out.println("Method " + methodName + " does not exist in class " + className);
            return false;
        }
        return true;
    }

}