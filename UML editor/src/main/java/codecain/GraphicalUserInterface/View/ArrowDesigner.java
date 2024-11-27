package codecain.GraphicalUserInterface.View;

import codecain.BackendCode.Model.RelationshipType;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

/**
 * Responsible for designing and styling arrows in the UML diagram.
 */
public class ArrowDesigner {


    /**
     * Represents the length of the arrowhead.
     * This constant is used to define the size of the arrowhead.
     */
    private static final double ARROW_LENGTH = 15;

    /**
     * Represents the width of the arrowhead.
     * This constant is used to define the width of the arrowhead.
     */
    private static final double ARROW_WIDTH = 7;

    /**
     * Creates a polygon representing an arrow between two points.
     *
     * @param startX the x-coordinate of the start point.
     * @param startY the y-coordinate of the start point.
     * @param endX   the x-coordinate of the end point.
     * @param endY   the y-coordinate of the end point.
     * @param type   the relationship type.
     * @return the created polygon representing the arrow.
     */
    public Polygon createArrow(double startX, double startY, double endX, double endY, RelationshipType type) {
        double angleToEndPoint = Math.atan2(endY - startY, endX - startX);

        // Define base line coordinates
        double startPointX = startX;
        double startPointY = startY;
        double basePointX = endX - ARROW_LENGTH * Math.cos(angleToEndPoint);
        double basePointY = endY - ARROW_LENGTH * Math.sin(angleToEndPoint);

        // Define arrowhead coordinates
        double arrowTipX = endX;
        double arrowTipY = endY;
        double leftWingX = basePointX + ARROW_WIDTH * Math.sin(angleToEndPoint);
        double leftWingY = basePointY - ARROW_WIDTH * Math.cos(angleToEndPoint);
        double rightWingX = basePointX - ARROW_WIDTH * Math.sin(angleToEndPoint);
        double rightWingY = basePointY + ARROW_WIDTH * Math.cos(angleToEndPoint);

        // Create polygon with points
        Polygon arrowShape = new Polygon();
        arrowShape.getPoints().addAll(
            startPointX, startPointY, // Start of the line
            basePointX, basePointY,   // Base of the arrowhead
            leftWingX, leftWingY,     // Left wing of the arrowhead
            arrowTipX, arrowTipY,     // Tip of the arrowhead
            rightWingX, rightWingY,   // Right wing of the arrowhead
            basePointX, basePointY    // Back to the base
        );

        styleArrow(arrowShape, type);
        return arrowShape;
    }

    /**
     * Styles an arrow based on the relationship type.
     *
     * @param arrow the arrow to style.
     * @param type  the relationship type.
     */
    private void styleArrow(Polygon arrow, RelationshipType type) {
        switch (type) {
            case GENERALIZATION -> {
                arrow.setFill(Color.TRANSPARENT);
                arrow.setStroke(Color.BLACK);            }
            case REALIZATION -> {
                arrow.setFill(Color.TRANSPARENT);
                arrow.setStroke(Color.BLACK);            }
            case COMPOSITION -> {
                arrow.setFill(Color.BLACK);
                arrow.setStroke(Color.BLACK);
            }
            case AGGREGATION -> {
                arrow.setFill(Color.TRANSPARENT);
                arrow.setStroke(Color.BLACK);
            }
            default -> {
                arrow.setFill(Color.TRANSPARENT);
                arrow.setStroke(Color.BLACK);
            }
        }
        arrow.setStrokeWidth(2);
    }
}
