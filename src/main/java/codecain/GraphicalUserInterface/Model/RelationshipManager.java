package codecain.GraphicalUserInterface.Model;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import codecain.BackendCode.Model.Relationship;
import codecain.BackendCode.Model.RelationshipType;
import codecain.BackendCode.Model.UMLClass;
import codecain.GraphicalUserInterface.Controller.Controller;
import codecain.GraphicalUserInterface.Controller.RelationshipLines.GridManager;
import codecain.GraphicalUserInterface.View.ClassNode;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;


public class RelationshipManager {



    /**
     * The Controller instance responsible for managing user interactions and coordinating between 
     * the frontend GUI and the backend model of the UML editor.
     */
    private static Controller controller;



    /**
     * Sets the Controller to manage user interactions and the application state.
     *
     * @param ctrl the Controller instance.
     */
    public static void setController(Controller ctrl) {
        controller = ctrl;
    }



    /**
     * adds a relationship to the relationship hasmap and visualizes it on the canvas
     */
    public static void addRelationshipBtn() {

        TextInputDialog sourceDialog = new TextInputDialog();
        sourceDialog.setTitle("Source Input");
        sourceDialog.setHeaderText("Enter the source of the relationship");
        sourceDialog.setContentText("Source Class:");

        Optional<String> sourceResult = sourceDialog.showAndWait();
        if (sourceResult.isEmpty()) {
            showErrorDialog("Source input is required.");
            return;
        }
        String source = sourceResult.get();
        if (!UMLClass.classMap.containsKey(source)){
            showErrorDialog("Source class does not exist");
            return;
        }

        TextInputDialog destinationDialog = new TextInputDialog();
        destinationDialog.setTitle("Destination Input");
        destinationDialog.setHeaderText("Enter the destination of the relationship");
        destinationDialog.setContentText("Destination Class:");

        Optional<String> destinationResult = destinationDialog.showAndWait();
        if (destinationResult.isEmpty()) {
            showErrorDialog("Destination input is required.");
            return;
        }
        String destination = destinationResult.get();
        if (!UMLClass.classMap.containsKey(destination)){
            showErrorDialog("Destination class does not exist");
            return;
        }

        List<String> relationshipTypes = Arrays.asList("COMPOSITION", "AGGREGATION", "GENERALIZATION", "REALIZATION");
        ChoiceDialog<String> typeDialog = new ChoiceDialog<>("COMPOSITION", relationshipTypes);
        typeDialog.setTitle("Relationship Type");
        typeDialog.setHeaderText("Select the type of relationship");
        typeDialog.setContentText("Relationship Type:");

        Optional<String> typeResult = typeDialog.showAndWait();
        if (typeResult.isEmpty()) {
            showErrorDialog("Relationship type is required.");
            return;
        }
        String relationshipType = typeResult.get();
        RelationshipType type = RelationshipType.fromString(relationshipType);

    if (Relationship.addRelationship(source, destination, type)) {
        System.out.println("Relationship added successfully.");

        ClassNode sourceNode = controller.findClassNode(source);
        ClassNode destNode = controller.findClassNode(destination);
        GridManager.getInstance();
        GridManager.updateRelationshipPaths();

        } else {
            showErrorDialog("Failed to add relationship.");
        }
    }

    


    /**
     * removes a relationship from the relationship hashmap and the canvas
     */
    public static void removeRelationshipBtn() {

        TextInputDialog sourceDialog = new TextInputDialog();
        sourceDialog.setTitle("Source Input");
        sourceDialog.setHeaderText("Enter the source of the relationship");
        sourceDialog.setContentText("Source Class:");

        Optional<String> sourceResult = sourceDialog.showAndWait();
        if (sourceResult.isEmpty()) {
            showErrorDialog("Source input is required.");
            return;
        }
        String source = sourceResult.get();
        if (!UMLClass.classMap.containsKey(source)){
            showErrorDialog("Source class does not exist");
            return;
        }

        TextInputDialog destinationDialog = new TextInputDialog();
        destinationDialog.setTitle("Destination Input");
        destinationDialog.setHeaderText("Enter the destination of the relationship");
        destinationDialog.setContentText("Destination Class:");

        Optional<String> destinationResult = destinationDialog.showAndWait();
        if (destinationResult.isEmpty()) {
            showErrorDialog("Destination input is required.");
            return;
        }
        String destination = destinationResult.get();
        if (!UMLClass.classMap.containsKey(destination)){
            showErrorDialog("Destination class does not exist");
            return;
        }

  
        Relationship rel = Relationship.getRelationship(source, destination, null);
    
        if (rel == null) {
            showErrorDialog("The specified relationship does not exist.");
            return;
        }
    
        if (Relationship.removeRelationship(source, destination)) {
            System.out.println("Relationship removed successfully.");
            GridManager.updateRelationshipPaths();
            
        } else {
            showErrorDialog("Failed to remove relationship from backend.");
        }    
    }


    /**
     * shows an error window
     * @param s the error message to display
     */
    private static void showErrorDialog(String s) {

    }
}
