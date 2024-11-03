package codecain;

import javax.swing.*;
import java.util.HashMap;

public class ClassPanel extends JPanel {
    private static Integer numClasses;
    private final Integer classNumber;

    public HashMap<Relationship, Integer> attachedRelationshipIndices;

    public ClassPanel(){
        attachedRelationshipIndices = new HashMap<>();
        if (numClasses == null){
            numClasses = 1;
        }
        else{
            numClasses = numClasses + 1;
        }
        classNumber = numClasses;
        System.out.println("Class " + classNumber.toString() + " added");
    }


    public static void init(){
        numClasses = 0;
    }


    public void addRelationshipPoint(Relationship r){
        attachedRelationshipIndices.put(r, getLowestValue());
        System.out.println("relationship added to class " + classNumber.toString());
    }


    public void removeRelationshipPoint(Relationship r){
        attachedRelationshipIndices.remove(r);
        System.out.println("relationship removed from class " + classNumber.toString());
    }


    private Integer getLowestValue(){
        int i = 0;
        for (; attachedRelationshipIndices.containsValue(i); i++);
        return i;
    }

}
