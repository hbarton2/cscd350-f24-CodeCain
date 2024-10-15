package Relationships;

import java.util.ArrayList;

public class Relationship {

    /**
     * name of class1
     */
    private String class1;

    /**
     * name of class2
     */
    private String class2;

    /**
     * arraylist for every relationship
     */
    public static final ArrayList<Relationship> relationshipList = new ArrayList<Relationship>();


    /**
     * constructor for relationship class
     * @param class1 name of first class to add
     * @param class2 name of second class to add
     */
    public Relationship(String class1, String class2){
        this.class1 = class1;
        this.class2 = class2;
    }

    /**
     * private method to validate the classes entered
     * @param class1 name of first class to search for
     * @param class2 name of second class to search for
     * @return true if both classes are in the list
     */
    public static boolean searchClasses(String class1, String class2) throws Exception {
        boolean isClass1Here = false;
        boolean isClass2Here = false;

        if (Class.classList.isEmpty() || Class.classList.size() < 2) throw new Exception("list is too small");

        for(String c : Class.classList){
            if(c.equals(class1)){
                isClass1Here = true;
            }
            if(c.equals(class2)){
                isClass2Here = true;
            }
        }
        return isClass1Here && isClass2Here;
    }


    /**
     * finds the index to the arraylist for the given relationship
     * @param class1 name of class1
     * @param class2 name of class2
     * @return int index of the relationship
     * @throws Exception if the relationship doesn't exist
     */
    protected static int findRelationshipIndex(String class1, String class2) throws Exception {
        if (!searchClasses(class1,class2)){
            throw new Exception("invalid entry");
        }

        for(int index = 0; index < relationshipList.size(); index++){
            Relationship r = relationshipList.get(index);

            boolean relationshipExists = (r.getClass1().equals(class1)
                    && r.getClass2().equals(class2)) ||
                    (r.getClass2().equals(class1) &&r.getClass1().equals(class1));
            if (relationshipExists){
                return index;
            }
        }

        throw new Exception("relationship doesn't exist");
    }


    /**
     * getter for class1
     * @return class1 name
     */
    public String getClass2(){
        return this.class2;
    }

    /**
     * getter for class2
     * @return class2 name
     */
    public String getClass1(){
        return this.class1;
    }

    /**
     * adds a relationship to the relationship map
     * @param class1 the first class to add
     * @param class2 the second class to add
     * @throws Exception if a class entered doesn't exist
     */
    public static void addRelationship(String class1, String class2) throws Exception {
        boolean isValid = searchClasses(class1, class2);

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
        boolean isValid = searchClasses(class1, class2);

        if(!isValid) throw new Exception("invalid entry");

        relationshipList.remove(findRelationshipIndex(class1,class2));
    }

    /**
     * takes the list of relationships and returns it as a string
     * @return String - the list of relationships
     */
    public static String listToString() {
        if (relationshipList.isEmpty()) return "";
        StringBuilder s = new StringBuilder();
        for (Relationship r: relationshipList){
            s.append(r.class1).append(" <------> ").append(r.class2).append("\n");
        }
        return s.toString();
    }
}
