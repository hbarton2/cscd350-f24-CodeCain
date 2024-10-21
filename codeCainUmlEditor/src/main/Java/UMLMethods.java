
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class manages methods within classes in a UML diagram.
 * Each class can have multiple methods, each with a list of parameters.
 */
public class UMLMethods {

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
        if (validateInputs(className, methodName)) { return; }
        if (classExists(className)) {return;}
        Map<String, List<String>> methods = classMethods.get(className);
        if (methods.containsKey(methodName)) {
            System.out.println("Error: Method " + methodName + " already exists in class " + className);
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
        if (validateInputs(className, methodName)) return;
        if (methodExists(className, methodName)) return;
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
        if (validateInputs(className, oldMethodName) || newMethodName == null) { return; } //Redo this
        if (classExists(className)) {return;}
        Map<String, List<String>> methods = classMethods.get(className);
        if (methods.containsKey(newMethodName)) {
            System.out.println("Error: Method " + newMethodName + " already exists in class " + className);
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
        if (validateInputs(className, methodName) || parameter == null) return;
        if (methodExists(className, methodName)) return;

        classMethods.get(className).get(methodName).add(parameter);
        System.out.println("Parameter " + parameter + " added to method " + methodName + " in class " + className);
    }

    /**
     * Removes a parameter from an existing method.
     *
     * @param className the name of the class
     * @param methodName the name of the method to remove the parameter from
     * @param parameter the parameter to remove
     */
    public void removeParameter(String className, String methodName, String parameter) {
        if (validateInputs(className, methodName) || parameter == null) return;
        if (methodExists(className, methodName)) return;
        List<String> parameters = classMethods.get(className).get(methodName);
        if (!parameters.contains(parameter)) {
            System.out.println("Error: Parameter " + parameter + " does not exist in method " + methodName);
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
        if (validateInputs(className, methodName) || newParameters == null) { return; }
        if (classExists(className)) {return;}

        Map<String, List<String>> methods = classMethods.get(className);
        if (!methods.containsKey(methodName)) {
            System.out.println("Error: Method " + methodName + " does not exist in class " + className);
            return;
        }
        methods.put(methodName, newParameters);
        System.out.println("Parameters in method " + methodName + " in class " + className + " have been updated");

    }

    /**
     * Helper method to check if a class exists in the map.
     *
     * @param className the name of the class
     * @return false if the class exists, true otherwise
     */
    public boolean classExists(String className) {
        if (!UMLClass.classMap.containsKey(className)) {
            System.out.println("Error: Class " + className + " does not exist");
            return true;
        }
        return false;
    }

    /**
     * Helper method to check if className and methodName/parameter are not null.
     *
     * @param className the name of the class
     * @param methodName the name of the method or parameter
     * @return true if both values are valid, false otherwise
     */
    private boolean validateInputs(String className, String methodName) {
        if (className == null || methodName == null) {
            System.out.println("Error: Class Name or Method Name is null");
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
        if (classExists(className)) {
            return null;
        }
        return classMethods.get(className);
    }

    /**
     * Checks if a method exists within a class.
     *
     * @param className the name of the class
     * @param methodName the name of the method
     * @return false if the method exists, true otherwise
     */
    private boolean methodExists(String className, String methodName) {
        Map<String, List<String>> methods = getMethodsForClass(className);
        if (methods == null || !methods.containsKey(methodName)) {
            System.out.println("Error: Method " + methodName + " does not exist in class " + className);
            return true;
        }
        return false;
    }

}