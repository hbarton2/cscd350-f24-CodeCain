import java.util.HashSet;

public class Classes {
    private HashSet<String> classes;

    public Classes() {
        classes = new HashSet<>();
    }

    /**
     * Adds a new class to the UML diagram and updates relationships and fields.
     *
     * @param className the name of the class to add
     */
    public void addClass(String className) {
        if (classExists(className)) {
            System.out.println("The class " + className + " already exists");
        } else {
            classes.add(className);
            System.out.println("Class " + className + " was added");
        }
    }

    /**
     * Removes a class from the UML diagram and updates relationships and fields.
     *
     * @param className the name of the class to remove
     */
    public void removeClass(String className) {
        if (classExists(className)) {
            try {
                Relationship.removeRelationship(className, className);
            } catch (Exception e) {

            }

            Fields.removeFieldsForClass(className);
            Methods.removeMethodsForClass(className);

            classes.remove(className);
            System.out.println("Class " + className + " removed.");
        } else {
            System.out.println("Error: Class " + className + " does not exist.");
        }
    }

    /**
     * Renames an existing class in the UML diagram and updates references.
     *
     * @param oldName the current name of the class
     * @param newName the new name for the class
     */
    public void renameClass(String oldName, String newName) {
        if (classExists(oldName) && !classExists(newName)) {
            Relationship.renameClassInRelationships(oldName, newName);
            Fields.renameFieldForClass(oldName, newName);
            Methods.renameMethodForClass(oldName, newName);

            classes.remove(oldName);
            classes.add(newName);
            System.out.println("Class " + oldName + " renamed to " + newName + ".");
        } else {
            System.out.println("Error: Cannot rename class " + oldName + " to " + newName + ".");
        }
    }

    /**
     * Checks if a class exists in the UML diagram.
     *
     * @param className the name of the class to check
     * @return true if the class exists, false otherwise
     */
    private boolean classExists(String className) {
        return classes.contains(className);
    }

    /**
     * Displays all classes currently in the UML diagram.
     */
    public void displayClasses() {
        System.out.println("Current Classes: " + classes);
    }
}
