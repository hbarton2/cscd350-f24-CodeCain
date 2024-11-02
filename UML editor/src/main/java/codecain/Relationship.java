package codecain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class Relationship {

    /**
     * the type of relationship
     */
    private RelationshipType type;

    /**
     * Set with 2 class names that will be in the relationship
     */
    private Collection<String> classNames;  // Removed final for deserialization

    /**
     * this is a string that is the name of the source class.
     * Source as in 'source to destination' - where the relationship arrow points to.
     */
    private String source;

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
        this.source = "";
    }

    /**
     * Constructor for relationship class. This is private
     * and is only used by the addRelationship method
     * @param class1 name of first class to add
     * @param class2 name of second class to add
     */
    private Relationship(String class1, String class2, RelationshipType type) {
        this.classNames = new HashSet<>();
        this.type = type;
        this.classNames.add(class1);
        this.classNames.add(class2);
        this.source = class1;
        relationshipList.add(this);
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
     * Checks if the relationship already exists with the specified class
     * @param class1 class to look for
     * @return true if there is a relationship with that class
     */
    public static boolean relationshipHasClass(String class1) {
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
     * Checks if the relationship already exists between two classes that has the specified type
     * @param class1 first class
     * @param class2 second class
     * @return true if the relationship exists
     */
    public static boolean relationshipExists(String class1, String class2, RelationshipType type){
        for (Relationship r : relationshipList) {
            if (r.classNames.contains(class1) && r.classNames.contains(class2) && r.type.equals(type)) {
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
    public static boolean addRelationship(String class1, String class2, RelationshipType type) {

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

        Relationship newRelationship = new Relationship(class1, class2, type);
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
                s.append(names[0]).append(r.type.getArrowString())
                        .append(names[1]).append(" ").append(r.type).append("\n");
            }
        }
        return s.toString();
    }

    /**
     * Helper method to get the names of the classes in the relationship in an array
     * this method puts the source string in the first address in the array
     * so that when you print the relationship list, the strings are in order.
     * @return String[]
     */
    public String[] getClassNamesAsArray() {
        String[] names = new String[2];
        this.classNames.toArray(names);
        if(!names[0].equals(source)){
            String t = names[0];
            names[0] = names[1];
            names[1] = t;
        }

        return names;
    }

    /**
     * setter for the source of the relationship
     * @return String - the key for the relationship name
     */
    public String getSource(){
        return this.source;
    }


    /**
     * getter for the source of the relationship
     * @param source the first class that the relationship goes from
     */
    public void setSource(String source){
        if (!this.classNames.contains(source)){
            throw new IllegalArgumentException("Class must be inside the relationship");
        }
        this.source = source;
    }

    /**
     * Getter for relationship type
     * @return - the type of the relationship
     */
    public RelationshipType getType(){
        return this.type;
    }

    /**
     * finds the relationship with the specified type and returns it
     * as an object
     * @param class1 the name of the first class
     * @param class2 the name of the second class
     * @param type the relationship type
     * @return the relationship with specified classes and type
     * @throws IllegalArgumentException if the class doesn't exist
     */
    public static Relationship getRelationship(String class1, String class2, RelationshipType type){
        for (Relationship r : relationshipList){
            if (r.type.equals(type) && r.classNames.contains(class1) && r.classNames.contains(class2)){
                return r;
            }
        }
        throw new IllegalArgumentException("this class does not exist");
    }


    public ArrayList<Relationship> getAttachedRelationships(String className){
        ArrayList<Relationship> relationships = new ArrayList<>();
        for (Relationship r : relationshipList){
            if (r.getClassNames().contains(className)){
                relationships.add(r);
            }
        }
        return relationships;
    }


}
