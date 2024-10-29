package codecain;

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
    public static Map<Object, UMLClassInfo> classMap = new HashMap<>();

    /**
     * Adds a new class to the classMap if it does not already exist.
     *
     * @param className the name of the class to be added
     */
    public static void addClass(Object className) {
        if (className == null || className.toString().isBlank()) {
            System.out.println("Canceled: Inputted Class Name is Blank");
            return;
        }
        if (classMap.containsKey(className)) {
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
    public static void removeClass(Object className) {
        if (className == null || className.toString().isBlank()) {
            System.out.println("Canceled: Inputted Class Name is Blank");
            return;
        }
        if (!classMap.containsKey(className)) {
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
    public static void renameClass(Object oldClassName, Object newClassName) {
        if (oldClassName == null || oldClassName.toString().isBlank()) {
            System.out.println("Canceled: Inputted Old Class Name is Blank");
            return;
        }
        if (newClassName == null || newClassName.toString().isBlank()) {
            System.out.println("Canceled: Inputted New Class Name is Blank");
            return;
        }
        if (!classMap.containsKey(oldClassName)) {
            System.out.println("Class " + oldClassName + " does not exist");
        } else if (classMap.containsKey(newClassName)) {
            System.out.println("Class " + newClassName + " already exists");
        } else {
            UMLClassInfo classInfo = classMap.remove(oldClassName);
            classInfo.setClassName(newClassName);
            classMap.put(newClassName, classInfo);
            System.out.println("Class " + oldClassName + " renamed to " + newClassName);
        }
    }
}
