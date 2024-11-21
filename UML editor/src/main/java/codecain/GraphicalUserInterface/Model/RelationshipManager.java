package codecain.GraphicalUserInterface.Model;

import codecain.BackendCode.Model.Relationship;
import codecain.BackendCode.Model.UMLClass;
import codecain.GraphicalUserInterface.View.ClassNode;
import codecain.BackendCode.Model.RelationshipType;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceDialog;

import javafx.scene.control.TextInputDialog;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class RelationshipManager {

    /**
     * adds a relationship to the relationship hasmap
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

        Relationship.addRelationship(source,destination, type);

    }


    /**
     * removes a relationship
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

        Relationship.removeRelationship(source,destination);
    }


    /**
     * shows an error window
     * @param s the error message to display
     */
    private static void showErrorDialog(String s) {

    }
}
