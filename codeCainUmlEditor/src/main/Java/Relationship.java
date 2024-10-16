import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class Relationship {

    /**
     * Set with 2 class names that will be in the relationship
     */
    private Collection<String> classNames;


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
    public static void removeAttachedRelationships(String className /*, Classes classes*/) /*throws Exception*/ {
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
     * adds a relationship to the relationship map
     * @param class1 the first class to add
     * @param class2 the second class to add
     * @throws Exception if a class entered doesn't exist
     */
    public static void addRelationship(String class1, String class2) throws Exception {
        boolean isValid = !relationshipExists(class1,class2);
                            //&& classes.classExists(class1)
                            //&& classes.classExists(class2);

        if(!isValid) throw new Exception("invalid entry");

        Relationship newRelationship = new Relationship(class1, class2);

        relationshipList.add(newRelationship);
    }



    /**
     * adds a relationship to the relationship map
     * @param class1 the first class to add
     * @param class2 the second class to add
     * @throws Exception if a class entered doesn't exist
     */
    public static void removeRelationship(String class1, String class2) throws Exception {

        for (Relationship r : relationshipList){
            if (r.classNames.contains(class1)
                && r.classNames.contains(class2))
            {
                relationshipList.remove(r);
                return;
            }
        }
        throw new Exception("relationship not found");
    }


    /**
     * takes the list of relationships and returns it as a string
     * @return String - the list of relationships
     */
    public static String listToString() throws Exception {
        if (relationshipList.isEmpty()) return "";
        StringBuilder s = new StringBuilder();
        for (Relationship r: relationshipList){
            String[] names = r.getClassNames();

            if (names.length < 2) throw new Exception("no classes found");

            s.append(names[0]).append(" ------- ").append(names[1]).append("\n");
        }
        return s.toString();
    }


    /**
     * renames a class in a relationship
     * @param oldName name to replace
     * @param newName name to replace with
     */
    public static void renameClassInRelationships(String oldName, String newName) throws Exception {
        if (!relationshipExists(oldName)) throw new Exception("relationship does not exist");
        for (Relationship r : relationshipList){
            if (r.classNames.contains(oldName)){
                r.classNames.remove(oldName);
                r.classNames.add(newName);
            }
        }
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