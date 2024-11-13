package codecain.BackendCode;

import java.util.HashMap;
import java.util.Map;

/**
 * The UMLClass class provides static methods to manage UML classes.
 * It includes functionality to add, remove, and rename classes.
 * All class information is stored in a static map where the key is the class name
 * and the value is the UMLClassInfo object containing details about the class.
 */
public class UMLClass {

    /**
     * A static map storing all classes, where the key is the class name and the value is the UMLClassInfo object.
     */
    public static Map<String, UMLClassInfo> classMap = new HashMap<>();

    /**
     * Adds a new class to the classMap if it does not already exist.
     *
     * @param className the name of the class to be added
     */
    public static void addClass(String className) {

        // new logic for addClass. Old logic was incorrect.
        // boolean result = false;
        // if (className == null || className.isBlank()) {
        //     System.out.println("Canceled: Inputted Class Name is Blank");
        // } else if (exists(className)) {
        //     System.out.println("Class " + className + " already exists");
        // } else {
        //     classMap.put(className, new UMLClassInfo(className));
        //     System.out.println("Class " + className + " added");
        //     result = true;
        // }

        // Old logic for addClass

        if (className == null || className.isBlank()) {
            System.out.println("Canceled: Inputted Class Name is Blank");
            return;
        }
        if (exists(className)) {
            System.out.println("Class " + className + " already exists");
        } else {
            classMap.put(className, new UMLClassInfo(className));
            System.out.println("Class " + className + " added");
        }
    }

    /**
     * Removes a class from the classMap if it exists.
     *
     * @param className the name of the class to be removed
     */
    public static void removeClass(String className) {
        if (className == null || className.isBlank()) {
            System.out.println("Canceled: Inputted Class Name is Blank");
            return;
        }
        if (!exists(className)) {
            System.out.println("Class " + className + " does not exist");
        } else {
            classMap.remove(className);
            System.out.println("Class " + className + " removed");
        }
    }

    /**
     * Renames an existing class by replacing its old name with a new name.
     *
     * @param oldClassName the current name of the class
     * @param newClassName the new name for the class
     */
    public static void renameClass(String oldClassName, String newClassName) {
        if (oldClassName == null || oldClassName.isBlank()) {
            System.out.println("Canceled: Inputted Old Class Name is Blank");
            return;
        }
        if (newClassName == null || newClassName.isBlank()) {
            System.out.println("Canceled: Inputted New Class Name is Blank");
            return;
        }
        if (!exists(oldClassName)) {
            System.out.println("Class " + oldClassName + " does not exist");
        } else if (exists(newClassName)) {
            System.out.println("Class " + newClassName + " already exists");
        } else {
            UMLClassInfo classInfo = classMap.remove(oldClassName);
            classInfo.setClassName(newClassName);
            classMap.put(newClassName, classInfo);
            System.out.println("Class " + oldClassName + " renamed to " + newClassName);
        }
    }

    /**
     * Lists all classes and their details (fields and methods) in a formatted string.
     * If no classes are present, returns "No classes to display."
     *
     * @return a formatted string of all classes, fields, and methods
     */
    public static String listAllClassesInfo() {
        if (classMap.isEmpty()) {
            return "No classes to display.";
        }
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, UMLClassInfo> entry : classMap.entrySet()) {
            UMLClassInfo classInfo = entry.getValue();
            result.append("Class: ").append(classInfo.getClassName()).append("\n");
            result.append("  Fields:\n");
            for (UMLFieldInfo field : classInfo.getFields()) {
                result.append("    - ").append(field).append("\n");
            }
            result.append("  Methods:\n");
            for (UMLMethodInfo method : classInfo.getMethods()) {
                result.append("    - ").append(method).append("\n");
            }
            result.append("\n");
        }
        return result.toString();
    }

    /**
     * Checks if a class with the given name exists in the classMap.
     *
     * @param className the name of the class to check for existence
     * @return true if the class exists, false otherwise
     */
    public static boolean exists(String className) {
        return classMap.containsKey(className);
    }

    /**
     * Checks if a class with the given name exists in the classMap.
     *
     * @param className the name of the class to check for existence
     * @return the name if the class exists, null otherwise
     */
    public static UMLClassInfo getClassInfo(String className) {
        return classMap.get(className);
    }
}
