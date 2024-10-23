package codecain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class Relationship {

    /**
     * Set with 2 class names that will be in the relationship
     */
    private final Collection<String> classNames;

    /**
     * arraylist for every single relationship created.
     * This is a static list, so every relationship created only exists in this list
     */
    public static final ArrayList<Relationship> relationshipList = new ArrayList<Relationship>();

    /**
     * constructor for relationship class. This is private
     * and is only used by the addRelationship method
     * @param class1 name of first class to add
     * @param class2 name of second class to add
     */
    private Relationship(String class1, String class2){
        this.classNames = new HashSet<String>();
        classNames.add(class1);
        classNames.add(class2);
    }

    /**
     * deletes all relationships that contain the specified class
     */
    public static void removeAttachedRelationships(String className) /*throws IllegalArgumentException*/ {
        //if(!classes.classExists(className)) throw new Exception("class not found");
        relationshipList.removeIf(r -> r.classNames.contains(className));
    }

    /**
     * checks if the relationship already exists
     * @param class1 class to look for
     * @return true if there is a relationship with that class
     */
    public static boolean relationshipExists(String class1){
        for (Relationship r : relationshipList){
            if (r.classNames.contains(class1))
                return true;
        }
        return false;
    }

    /**
     * relationshipExists
     * checks if the relationship already exists
     * @param class1 first class
     * @param class2 second class
     * @return true if the relationship exists
     */
    public static boolean relationshipExists(String class1, String class2){
        for (Relationship r: relationshipList){
            if (
                    r.classNames.contains(class1)
                            && r.classNames.contains(class2)
            )
                return true;
        }
        return false;
    }

    /**
     * helper method to check if both classes exists and dont already have a relationship
     * @param class1 first class
     * @param class2 second class
     * @return true if entry is valid
     */
    private static boolean entryIsValid(String class1, String class2){
        return !relationshipExists(class1,class2)
                && UMLClass.classMap.containsKey(class1)
                && UMLClass.classMap.containsKey(class2);
    }

    /**
     * adds a relationship to the relationship map
     *
     * @param class1 the first class to add
     * @param class2 the second class to add
     * @return true if the input is valid, false if the input is invalid.
     */
    public static boolean addRelationship(String class1, String class2){

        if (relationshipExists(class1,class2)){
            System.out.println("relationship already exists");
            return false;
        }
        if (!UMLClass.classMap.containsKey(class1)){
            System.out.println(class1 + " is not an existing class");
            return false;
        }
        if (!UMLClass.classMap.containsKey(class2)){
            System.out.println(class2 + " is not an existing class");
            return false;
        }

        Relationship newRelationship = new Relationship(class1, class2);

        relationshipList.add(newRelationship);
        System.out.println("relationship between " + class1 + " and " + class2 + " added");
        return true;
    }

//
//    /**
//     * adds a relationship to the relationship map
//     * @param class1 the first class to add
//     * @param class2 the second class to add
//     */
//    public static void addRelationship(String class1, String class2) throws IllegalArgumentException {
//
//        if (relationshipExists(class1,class2)){
//            throw new IllegalArgumentException("class/relationship exists");
//        }
//        if (!Class.classMap.containsKey(class1)){
//            throw new IllegalArgumentException(class1 + " is not an existing class");
//        }
//        if (!Class.classMap.containsKey(class2)){
//            throw new IllegalArgumentException(class2 + " is not an existing class");
//        }
//
//        Relationship newRelationship = new Relationship(class1, class2);
//
//        relationshipList.add(newRelationship);
//    }

    /**
     * removes a relationship from the relationship array.
     * @param class1 the first class to add
     * @param class2 the second class to add
     * @return true if the relationship is removed, otherwise it returns false;
     */
    public static boolean removeRelationship(String class1, String class2){

        for (Relationship r : relationshipList){
            if (r.classNames.contains(class1)
                    && r.classNames.contains(class2))
            {
                relationshipList.remove(r);
                System.out.println("Relationship between " + class1 + " and " + class2 + " removed");
                return true;
            }
        }

        System.out.println("relationship not found");
        return false;
    }

//    /**
//     * removes a relationship from the relationship array.
//     * @param class1 the first class to add
//     * @param class2 the second class to add
//     */
//    public static void removeRelationship(String class1, String class2) throws IllegalArgumentException{
//
//        for (Relationship r : relationshipList){
//            if (r.classNames.contains(class1)
//                    && r.classNames.contains(class2))
//            {
//                relationshipList.remove(r);
//                return;
//            }
//        }
//
//        throw new IllegalArgumentException("relationship not found");
//
//    }

    /**
     * takes the list of relationships and returns it as a string
     * @return String - the list of relationships
     */
    public static String listToString() {
        if (relationshipList.isEmpty()) return "";

        StringBuilder s = new StringBuilder();

        for (Relationship r: relationshipList){
            String[] names = r.getClassNames();

            if (names.length < 2) System.out.println("There are no clases to print out");

            s.append(names[0]).append(" ------- ").append(names[1]).append("\n");
        }
        return s.toString();
    }

//    /**
//     * replaces the old class name with a new class name for every relationship where that class is used
//     * @param oldName name to replace
//     * @param newName name to replace with
//     */
//    public static void renameClassInRelationships(String oldName, String newName) throws IllegalArgumentException{
//        if (!relationshipExists(oldName)) throw new IllegalArgumentException("relationship does not exist");
//        for (Relationship r : relationshipList){
//            if (r.classNames.contains(oldName)){
//                r.classNames.remove(oldName);
//                r.classNames.add(newName);
//            }
//        }
//    }

    /**
     * this version of the method has return statements to handle the errors
     * replaces the old class name with a new class name for every relationship where that class is used
     * @param oldName name to replace
     * @param newName name to replace with
     * @return true if the class is renamed. Return false if the class to rename doesn't exist.
     */
    public static boolean renameClassInRelationships(String oldName, String newName){
        if (!relationshipExists(oldName)){
            System.out.println("relationship does not exist");
            return false;
        }
        for (Relationship r : relationshipList){
            if (r.classNames.contains(oldName)){
                r.classNames.remove(oldName);
                r.classNames.add(newName);
            }
        }
        return true;
    }

    /**
     * helper method to get the names of the classes in the relationship in an
     * array
     * @return String[]
     */
    private String[] getClassNames(){
        String[] names = new String[2];
        this.classNames.toArray(names);
        return names;
    }

}