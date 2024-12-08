package codecain.GraphicalUserInterface.Model.RelationshipLines;


import java.util.ArrayList;
import java.util.HashMap;

import codecain.BackendCode.Model.Relationship;
import codecain.GraphicalUserInterface.Controller.Controller;
import codecain.GraphicalUserInterface.View.ClassNode;

/**
 * this is a class to take relationships and turn them into paths.
 * Also maps relatioinships to the classNodes from the GUI
 * This class is useful for linking the paths to the relationships that they
 * represent, and also for linking the relationships to the
 * ClassNodes from the GUI. Allows mediation between the
 * grid paths and the ClassNodes
 */
public class RelationshipPathHolder {

    /**
     * Helper class to hold the source and destination nodes of the relationship line.
     */
    private static class RelBoxHolder {
        ClassNode source;
        ClassNode destination;

        RelBoxHolder(ClassNode source, ClassNode destination) {
            this.source = source;
            this.destination = destination;
        }

        ClassNode getSource() {
            return this.source;
        }

        ClassNode getDestination() {
            return this.destination;
        }
    }


    /**
     * finds the classboxes from the relationship?
     */
    private final HashMap<Relationship, RelBoxHolder> classBoxHolder;
    private final HashMap<Relationship, GridPath> paths;
    private final Controller controller;

    public RelationshipPathHolder(Controller controller){
        this.classBoxHolder = new HashMap<>();
        this.paths = new HashMap<>();
        this.controller = controller;
    }

    public ClassNode getSourceClassNode(Relationship r) {
        ClassNode src = this.classBoxHolder.get(r).getSource();
        if (src == null){

            throw new IllegalStateException("No such ClassNode exists for the source");
        }
        return src;
    }

    public ClassNode getDestinationClassNode(Relationship r) {
        ClassNode dest = this.classBoxHolder.get(r).getDestination();
        if (dest == null){

            throw new IllegalStateException("No such ClassNode exists for the destination");
        }
        return dest;
    }


    /**
     * must be called every time a relationship is added
     * @param relationship the relationship to add
     * @param path the path of the relationship
     */
    public void addRelationshipPath(Relationship relationship, GridPath path){
        paths.put(relationship, path);

    }

    public void addRelationshipHolder(Relationship relationship) {
        ClassNode sourceBox = controller.findClassNode(relationship.getSource());
        ClassNode destBox = controller.findClassNode(relationship.getDestination());
        RelBoxHolder h = new RelBoxHolder(sourceBox, destBox);
        classBoxHolder.put(relationship, h);
    }

    /**
     * must be called every time a relationship is deleted
     * @param relationship the relationship to remove
     * @return the removed class
     */
    public GridPath removeRelationshipPath(Relationship relationship){
        GridPath p = paths.get(relationship);
        paths.remove(relationship);
        classBoxHolder.remove(relationship);
        return p;
    }

    public ArrayList<GridPath> getPathsToArrayList(){
        return new ArrayList<>(this.paths.values());
    }



    public void clearHolder(){
        this.classBoxHolder.clear();
        this.paths.clear();
    }



    public GridPath getPath(Relationship relationship){
        return paths.get(relationship);
    }


}
