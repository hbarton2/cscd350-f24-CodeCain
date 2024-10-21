package codecain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a UML class and provides methods for managing a collection of classes.
 * Each class is identified by its name, and all classes are stored in a shared static map.
 */
public class Class {
    private String className;

    /**
     * Constructs a new Class with the specified name.
     *
     * @param className the name of the class
     */
    public Class(String className) {
        this.className = className;
    }

    /**
     * Returns the name of the class.
     *
     * @return the class name
     */
    public String getClassName() {
        return this.className;
    }

    /**
     * A static map that stores all classes by their name.
     * The key is the class name, and the value is the corresponding Class object.
     */
    public static Map<String, Class> classMap = new HashMap<>();

    /**
     * Adds a class with the specified name to the classMap.
     * If the class already exists, an error message is printed.
     *
     * @param className the name of the class to be added
     */
    public static void addClass(String className) {
        if (className.isBlank()) {
            System.out.println("Canceled: Inputted Class Name is Blank");
            return;
        }
        if (classMap.containsKey(className)) {
            System.out.println("Class " + className + " already exists");
        } else {
            classMap.put(className, new Class(className));
            Methods.classMethods.put(className, new HashMap<>());
            Fields.classFields.put(className, new HashMap<>());
            System.out.println("Class " + className + " added");
        }
    }

    /**
     * Removes the class with the specified name from the classMap.
     * If the class does not exist, an error message is printed.
     *
     * @param className the name of the class to be removed
     */
    public static void removeClass(String className) {
        if (className.isBlank()) {
            System.out.println("Canceled: Inputted Class Name is Blank");
            return;
        }
        if (!classMap.containsKey(className)) {
            System.out.println("Class " + className + " does not exist");
        } else {
            classMap.remove(className);
            Methods.classMethods.remove(className);
            Fields.classFields.remove(className);
            System.out.println("Class " + className + " removed");
        }
    }

    /**
     * Renames a class by changing its key in the classMap.
     * If the old class name does not exist or the new class name already exists, an error message is printed.
     *
     * @param oldClassName the current name of the class to be renamed
     * @param newClassName the new name for the class
     */
    public static void renameClass(String oldClassName, String newClassName) {
        if (oldClassName.isBlank()) {
            System.out.println("Canceled: Inputted Old Class Name is Blank");
            return;
        }
        if (newClassName.isBlank()) {
            System.out.println("Canceled: Inputted New Class Name is Blank");
            return;
        }
        if (!classMap.containsKey(oldClassName)) {
            System.out.println("Class " + oldClassName + " does not exist");
        } else if (classMap.containsKey(newClassName)) {
            System.out.println("Class " + newClassName + " already exists");
        } else {
            Class classObj = classMap.remove(oldClassName);
            classObj.className = newClassName;
            classMap.put(newClassName, classObj);
            Map<String, List<String>> methods = Methods.classMethods.remove(oldClassName);
            Methods.classMethods.put(newClassName, methods);
            Map<String, String> fields = Fields.classFields.remove(oldClassName);
            Fields.classFields.put(newClassName, fields);
            System.out.println("Class " + oldClassName + " renamed to " + newClassName);
        }
    }
}