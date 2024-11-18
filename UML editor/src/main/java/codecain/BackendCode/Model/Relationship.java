package codecain.BackendCode.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class Relationship {

    /**
     * Set with 2 class names that will be in the relationship
     */
    private Collection<String> classNames;  // Removed final for deserialization

    /**
     * ArrayList for every single relationship created.
     * This is a static list, so every relationship created only exists in this list
     */
    public static ArrayList<Relationship> relationshipList = new ArrayList<>();

    /**
     * Default constructor needed for Jackson deserialization
     */
    public Relationship() {
        this.classNames = new HashSet<>();
    }

    /**
     * Constructor for relationship class. This is private
     * and is only used by the addRelationship method
     * @param class1 name of first class to add
     * @param class2 name of second class to add
     */
    private Relationship(String class1, String class2) {
        this.classNames = new HashSet<>();
        this.classNames.add(class1);
        this.classNames.add(class2);
    }

    // Getter for Jackson serialization/deserialization
    public Collection<String> getClassNames() {
        return classNames;
    }

    // Setter for Jackson deserialization
    public void setClassNames(Collection<String> classNames) {
        this.classNames = new HashSet<>(classNames);  // Ensure it's a HashSet
    }

    /**
     * Deletes all relationships that contain the specified class
     */
    public static void removeAttachedRelationships(String className) {
        relationshipList.removeIf(r -> r.classNames.contains(className));
    }

    /**
     * Checks if the relationship already exists
     * @param class1 class to look for
     * @return true if there is a relationship with that class
     */
    public static boolean relationshipExists(String class1) {
        for (Relationship r : relationshipList) {
            if (r.classNames.contains(class1)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the relationship already exists between two classes
     * @param class1 first class
     * @param class2 second class
     * @return true if the relationship exists
     */
    public static boolean relationshipExists(String class1, String class2) {
        for (Relationship r : relationshipList) {
            if (r.classNames.contains(class1) && r.classNames.contains(class2)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a relationship to the relationship map
     *
     * @param class1 the first class to add
     * @param class2 the second class to add
     * @return true if the input is valid, false if the input is invalid.
     */
    public static boolean addRelationship(String class1, String class2) {

        if (relationshipExists(class1, class2)) {
            System.out.println("Relationship already exists");
            return false;
        }
        if (!UMLClass.classMap.containsKey(class1)) {
            System.out.println(class1 + " is not an existing class");
            return false;
        }
        if (!UMLClass.classMap.containsKey(class2)) {
            System.out.println(class2 + " is not an existing class");
            return false;
        }

        Relationship newRelationship = new Relationship(class1, class2);
        relationshipList.add(newRelationship);
        System.out.println("Relationship between " + class1 + " and " + class2 + " added");
        return true;
    }

    /**
     * Removes a relationship from the relationship list
     * @param class1 the first class to add
     * @param class2 the second class to add
     * @return true if the relationship is removed, otherwise it returns false;
     */
    public static boolean removeRelationship(String class1, String class2) {
        for (Relationship r : relationshipList) {
            if (r.classNames.contains(class1) && r.classNames.contains(class2)) {
                relationshipList.remove(r);
                System.out.println("Relationship between " + class1 + " and " + class2 + " removed");
                return true;
            }
        }
        System.out.println("Relationship not found");
        return false;
    }

    /**
     * Converts the list of relationships into a string representation
     * @return String - the list of relationships
     */
    public static String listToString() {
        if (relationshipList.isEmpty()) return "";

        StringBuilder s = new StringBuilder();

        for (Relationship r : relationshipList) {
            String[] names = r.getClassNamesAsArray();
            if (names.length < 2) {
                System.out.println("There are no classes to print out");
            } else {
                s.append(names[0]).append(" ------- ").append(names[1]).append("\n");
            }
        }
        return s.toString();
    }

    /**
     * Helper method to get the names of the classes in the relationship in an array
     * @return String[]
     */
    private String[] getClassNamesAsArray() {
        String[] names = new String[2];
        this.classNames.toArray(names);
        return names;
    }

}
