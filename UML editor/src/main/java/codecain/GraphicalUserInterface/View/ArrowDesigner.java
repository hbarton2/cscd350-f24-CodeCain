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
     * TODO: Fix the Diamond being a little bit off
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

        Polygon arrowShape = new Polygon();

        // Line from start to base of arrow
        double basePointX = endX - ARROW_LENGTH * Math.cos(angleToEndPoint);
        double basePointY = endY - ARROW_LENGTH * Math.sin(angleToEndPoint);
        arrowShape.getPoints().addAll(startX, startY, basePointX, basePointY);

        if (type == RelationshipType.COMPOSITION || type == RelationshipType.AGGREGATION) {
            // Calculate diamond points relative to the base of the arrow
            double rightPointX = basePointX - ARROW_WIDTH * Math.cos(angleToEndPoint - Math.PI / 2);
            double rightPointY = basePointY - ARROW_WIDTH * Math.sin(angleToEndPoint - Math.PI / 2);
            double leftPointX = basePointX + ARROW_WIDTH * Math.cos(angleToEndPoint - Math.PI / 2);
            double leftPointY = basePointY + ARROW_WIDTH * Math.sin(angleToEndPoint - Math.PI / 2);
            double topPointX = endX;
            double topPointY = endY;
            double bottomPointX = basePointX - ARROW_LENGTH * Math.cos(angleToEndPoint);
            double bottomPointY = basePointY - ARROW_LENGTH * Math.sin(angleToEndPoint);
    
            
            arrowShape.getPoints().addAll(
                rightPointX, rightPointY,   
                topPointX, topPointY,    
                leftPointX, leftPointY, 
                bottomPointX, bottomPointY
            );
        }    
        

        if (type == RelationshipType.GENERALIZATION || type == RelationshipType.REALIZATION) {
            double leftWingX = basePointX + ARROW_WIDTH * Math.sin(angleToEndPoint);
            double leftWingY = basePointY - ARROW_WIDTH * Math.cos(angleToEndPoint);
            double rightWingX = basePointX - ARROW_WIDTH * Math.sin(angleToEndPoint);
            double rightWingY = basePointY + ARROW_WIDTH * Math.cos(angleToEndPoint);

            arrowShape.getPoints().addAll(
                leftWingX, leftWingY,
                endX, endY,
                rightWingX, rightWingY,
                basePointX, basePointY
            );
        }

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
                arrow.setStroke(Color.BLACK);
            }
            case REALIZATION -> {
                arrow.setFill(Color.TRANSPARENT);
                arrow.setStroke(Color.BLACK);
                arrow.getStrokeDashArray().addAll(10.0, 10.0); // Dashed line
            }
            case COMPOSITION -> {
                arrow.setFill(Color.BLACK);
                arrow.setStroke(Color.BLACK);
            }
            case AGGREGATION -> {
                arrow.setFill(Color.TRANSPARENT);
                arrow.setStroke(Color.BLACK);
            }
        }
        arrow.setStrokeWidth(2);
    }
}
