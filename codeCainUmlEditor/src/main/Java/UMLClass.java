import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a UML class and provides methods for managing a collection of classes.
 * Each class is identified by its name, and all classes are stored in a shared static map.
 */
public class UMLClass {
    private String className;

    /**
     * Constructs a new Class with the specified name.
     *
     * @param className the name of the class
     */
    public UMLClass(String className) {
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
    public static Map<String, UMLClass> classMap = new HashMap<>();

    /**
     * Adds a class with the specified name to the classMap.
     * If the class already exists, an error message is printed.
     *
     * @param className the name of the class to be added
     */
    public static void addClass(String className) {
        if (classMap.containsKey(className)) {
            System.out.println("Error: Class " + className + " already exists");
        } else {
            classMap.put(className, new UMLClass(className));
            UMLMethods.classMethods.put(className, new HashMap<>());
            UMLFields.classFields.put(className, new HashMap<>());
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
        if (!classMap.containsKey(className)) {
            System.out.println("Error: Class " + className + " does not exist");
        } else {
            classMap.remove(className);
            UMLMethods.classMethods.remove(className);
            UMLFields.classFields.remove(className);
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
        if (!classMap.containsKey(oldClassName)) {
            System.out.println("Error: Class " + oldClassName + " does not exist");
        } else if (classMap.containsKey(newClassName)) {
            System.out.println("Error: Class " + newClassName + " already exists");
        } else {
            UMLClass classObj = classMap.remove(oldClassName);
            classObj.className = newClassName;
            classMap.put(newClassName, classObj);
            Map<String, List<String>> methods = UMLMethods.classMethods.remove(oldClassName);
            UMLMethods.classMethods.put(newClassName, methods);
            Map<String, String> fields = UMLFields.classFields.remove(oldClassName);
            UMLFields.classFields.put(newClassName, fields);
            System.out.println("Class " + oldClassName + " renamed to " + newClassName);
        }
    }
}
