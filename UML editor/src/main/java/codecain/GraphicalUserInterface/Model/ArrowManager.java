package codecain.GraphicalUserInterface.Model;

import java.util.HashMap;
import java.util.Map;

import codecain.BackendCode.Model.Relationship;
import codecain.BackendCode.Model.RelationshipType;
import codecain.GraphicalUserInterface.View.ArrowDesigner;
import codecain.GraphicalUserInterface.View.ClassNode;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;

/**
 * Manages the functionality of graphical arrows representing relationships between UML classes.
 * Handles adding, updating, and removing arrows and ensures the GUI stays in sync with the data model.
 */
public class ArrowManager {
    private final Pane arrowContainer;
    private final Map<Relationship, Polygon> arrowMap;
    private final ArrowDesigner arrowDesigner;

    /**
     * Constructs an ArrowManager with the specified container and designer.
     *
     * @param arrowContainer the pane to which arrows will be added.
     */
    public ArrowManager(Pane arrowContainer) {
        this.arrowContainer = arrowContainer;
        this.arrowMap = new HashMap<>();
        this.arrowDesigner = new ArrowDesigner();
    }

    /**
     * Adds an arrow representing a relationship between two class nodes.
     */
    public void addArrow(Relationship relationship, ClassNode sourceNode, ClassNode destNode) {
        if (sourceNode == null || destNode == null) {
            System.out.println("Source or destination node not found.");
            return;
        }

        Polygon arrow = arrowDesigner.createArrow(
                calculateNodeCenterX(sourceNode), calculateNodeCenterY(sourceNode),
                calculateNodeCenterX(destNode), calculateNodeCenterY(destNode),
                relationship.getType()
        );

        arrowContainer.getChildren().add(arrow);
        arrow.toBack();
        arrowMap.put(relationship, arrow);

        updateArrowPosition(arrow, sourceNode, destNode, relationship.getType());
        sourceNode.layoutXProperty().addListener((obs, oldVal, newVal) -> updateArrowPosition(arrow, sourceNode, destNode, relationship.getType()));
        sourceNode.layoutYProperty().addListener((obs, oldVal, newVal) -> updateArrowPosition(arrow, sourceNode, destNode, relationship.getType()));
        destNode.layoutXProperty().addListener((obs, oldVal, newVal) -> updateArrowPosition(arrow, sourceNode, destNode, relationship.getType()));
        destNode.layoutYProperty().addListener((obs, oldVal, newVal) -> updateArrowPosition(arrow, sourceNode, destNode, relationship.getType()));
    }

    /**
     * Updates the position of an existing arrow.
     */
    public void updateArrowPosition(Polygon arrow, ClassNode sourceNode, ClassNode destNode, RelationshipType type) {
        Point2D startEdge = calculateEdgeIntersection(sourceNode, calculateNodeCenterX(destNode), calculateNodeCenterY(destNode));
        Point2D endEdge = calculateEdgeIntersection(destNode, calculateNodeCenterX(sourceNode), calculateNodeCenterY(sourceNode));

        arrow.getPoints().setAll(arrowDesigner.createArrow(
                startEdge.getX(), startEdge.getY(),
                endEdge.getX(), endEdge.getY(),
                type
        ).getPoints());
    }

    /**
     * Removes an arrow representing a relationship.
     */
    public void removeArrow(Relationship relationship) {
        Polygon arrow = arrowMap.get(relationship);
        if (arrow != null) {
            arrowContainer.getChildren().remove(arrow);
            arrowMap.remove(relationship);
        }
    }

    /**
     * Clears all arrows from the GUI and map.
     */
    public void clearArrows() {
        arrowMap.values().forEach(arrowContainer.getChildren()::remove);
        arrowMap.clear();
    }

    /**
     * Retrieves the arrow representing a given relationship.
     */
    public Polygon getArrow(Relationship relationship) {
        return arrowMap.get(relationship);
    }


/**
 * Calculates the intersection point between the edge of a class node and a line drawn from the node's center
 * to a target point.
 *
 * @param classNode          the class node whose edge intersection is being calculated.
 * @param targetXCoordinate  the x-coordinate of the target point.
 * @param targetYCoordinate  the y-coordinate of the target point.
 * @return the intersection point as a {@code Point2D}.
 */
private Point2D calculateEdgeIntersection(ClassNode classNode, double targetXCoordinate, double targetYCoordinate) {
    double nodeCenterX = calculateNodeCenterX(classNode);
    double nodeCenterY = calculateNodeCenterY(classNode);

    // Calculate the differences between the target point and the node's center
    double deltaX = targetXCoordinate - nodeCenterX;
    double deltaY = targetYCoordinate - nodeCenterY;

    // Calculate scaling factors for the x and y axes to normalize the line to the node's boundary
    double scaleX = (classNode.getWidth() / 2) / Math.abs(deltaX);
    double scaleY = (classNode.getHeight() / 2) / Math.abs(deltaY);

    // Use the smaller scaling factor to ensure the point lies on the edge of the node
    double scale = Math.min(scaleX, scaleY);

    // Calculate the intersection point by applying the scale to the deltas
    return new Point2D(nodeCenterX + scale * deltaX, nodeCenterY + scale * deltaY);
}


    /**
     * Calculates the x-coordinate of the center of a node.
     *
     * @param node the node whose center x-coordinate is being calculated.
     * @return the x-coordinate of the node's center.
     */
    private double calculateNodeCenterX(ClassNode node) {
        return node.getLayoutX() + node.getWidth() / 2;
    }

    /**
     * Calculates the y-coordinate of the center of a node.
     *
     * @param node the node whose center y-coordinate is being calculated.
     * @return the y-coordinate of the node's center.
     */
    private double calculateNodeCenterY(ClassNode node) {
        return node.getLayoutY() + node.getHeight() / 2;
    }

}
