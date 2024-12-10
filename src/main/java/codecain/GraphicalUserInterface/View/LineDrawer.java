package codecain.GraphicalUserInterface.View;

import java.util.ArrayList;
import java.util.Iterator;

import codecain.BackendCode.Model.Relationship;
import codecain.BackendCode.Model.RelationshipType;
import codecain.GraphicalUserInterface.Controller.RelationshipLines.GridCell;
import codecain.GraphicalUserInterface.Controller.RelationshipLines.GridPath;
import codecain.GraphicalUserInterface.Controller.RelationshipLines.LineGrid;
import codecain.GraphicalUserInterface.Controller.RelationshipLines.RelationshipPathHolder;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;

public class LineDrawer {

    private Pane nodeContainer;
    private ArrayList<Polyline> lines;
    private LineGrid grid;

    /**
     * constructor takes in a grid object and adds a container
     * from that object
     * 
     * @param grid
     */
    public LineDrawer(LineGrid grid) {
        this.grid = grid;
        this.nodeContainer = grid.getNodeContainer();
    }

    /**
     * Draws a line from the specified path
     * 
     * @param path the path to draw
     * @param type the relationship type
     */
    public Polyline drawLineFromPath(GridPath path, RelationshipType type) {
        if (path == null || path.getCells() == null) {
            throw new IllegalArgumentException("path or its cells cannot be null");
        }

        Polyline line = new Polyline();
        for (GridCell cell : path.getCells()) {
            double x = grid.getXcoord(cell);
            double y = grid.getYcoord(cell);
            line.getPoints().addAll(x, y);
        }

        // Set stroke color based on relationship type
        switch (type) {
            case GENERALIZATION:
                line.setStroke(Color.BLUE);
                //addArrowhead(line, Color.BLUE);
                break;
            case AGGREGATION:
                line.setStroke(Color.ORANGE);
                //addDiamond(line, Color.ORANGE);
                break;
            case COMPOSITION:
                line.setStroke(Color.PURPLE);
                //addFilledDiamond(line, Color.PURPLE);
                break;
            case REALIZATION:
                line.setStroke(Color.RED);
                line.getStrokeDashArray().addAll(10.0, 10.0); // Dashed for realization
                //addArrowhead(line, Color.RED);
                break;
            default:
                throw new IllegalArgumentException("Unsupported relationship type: " + type);
        }

        line.setStrokeWidth(3.0);
        nodeContainer.getChildren().add(line);
        return line;
    }

    private void addArrowhead(Polyline line, Color color) {
        double[] points = getLastSegment(line);
        double startX = points[0];
        double startY = points[1];
        double endX = points[2];
        double endY = points[3];

        // Calculate arrowhead points
        double angle = Math.atan2(endY - startY, endX - startX) - Math.PI / 2;
        double arrowLength = 15;
        double arrowWidth = 7;

        double x1 = endX + arrowLength * Math.cos(angle - Math.PI / 6);
        double y1 = endY + arrowLength * Math.sin(angle - Math.PI / 6);
        double x2 = endX + arrowLength * Math.cos(angle + Math.PI / 6);
        double y2 = endY + arrowLength * Math.sin(angle + Math.PI / 6);

        Polygon arrowhead = new Polygon(endX, endY, x1, y1, x2, y2);
        arrowhead.setFill(color);
        nodeContainer.getChildren().add(arrowhead);
    }

    private void addDiamond(Polyline line, Color color) {
        double[] points = getLastSegment(line);
        double startX = points[0];
        double startY = points[1];
        double endX = points[2];
        double endY = points[3];

        // Calculate the midpoint of the line
        double midX = (startX + endX) / 2;
        double midY = (startY + endY) / 2;

        // Calculate the vector for the direction of the line
        double dx = endX - startX;
        double dy = endY - startY;

        // Normalize the direction vector
        double length = Math.sqrt(dx * dx + dy * dy);
        double unitX = dx / length;
        double unitY = dy / length;

        // Perpendicular vector for the diamond shape
        double perpX = -unitY;
        double perpY = unitX;

        // Dimensions of the diamond
        double diamondWidth = 10;
        double diamondHeight = 15;

        // Calculate diamond points
        double tipX = midX + unitX * diamondHeight;
        double tipY = midY + unitY * diamondHeight;

        double leftX = midX + perpX * diamondWidth;
        double leftY = midY + perpY * diamondWidth;

        double rightX = midX - perpX * diamondWidth;
        double rightY = midY - perpY * diamondWidth;

        double baseX = midX - unitX * diamondHeight;
        double baseY = midY - unitY * diamondHeight;

        // Create the diamond as a filled polygon
        Polygon diamond = new Polygon(
                tipX, tipY,
                leftX, leftY,
                baseX, baseY,
                rightX, rightY);
        diamond.setFill(color);
        nodeContainer.getChildren().add(diamond);
    }

    private void addFilledDiamond(Polyline line, Color color) {
        double[] points = getLastSegment(line);
        double startX = points[0];
        double startY = points[1];
        double endX = points[2];
        double endY = points[3];

        // Calculate diamond points
        double midX = (startX + endX) / 2;
        double midY = (startY + endY) / 2;
        double diamondWidth = 10;
        double diamondHeight = 10;

        double x1 = midX + diamondWidth;
        double y1 = midY;
        double x2 = midX;
        double y2 = midY - diamondHeight;
        double x3 = midX - diamondWidth;
        double y3 = midY;
        double x4 = midX;
        double y4 = midY + diamondHeight;

        Polygon diamond = new Polygon(midX, midY, x1, y1, x2, y2, x3, y3, x4, y4);
        diamond.setFill(color); // Filled diamond
        nodeContainer.getChildren().add(diamond);
    }

    private double[] getLastSegment(Polyline line) {
        int size = line.getPoints().size();
        if (size < 4) {
            throw new IllegalArgumentException("Polyline must have at least two points.");
        }

        double startX = line.getPoints().get(size - 4);
        double startY = line.getPoints().get(size - 3);
        double endX = line.getPoints().get(size - 2);
        double endY = line.getPoints().get(size - 1);

        return new double[] { startX, startY, endX, endY };
    }

    /**
     * Draws lines for all paths in the holder based on their relationship types.
     * 
     * @param holder the RelationshipPathHolder containing paths and relationships
     */
    private void drawLinesFromPaths(RelationshipPathHolder holder) {
        for (Relationship r : Relationship.relationshipList) {
            RelationshipType type = r.getType();
            drawLineFromPath(holder.getPath(r), type).toBack();
        }
    }

//    public void redrawLines(RelationshipPathHolder holder) {
//        // Remove all Polyline and Polygon nodes (lines and markers)
//        Iterator<Node> iterator = nodeContainer.getChildren().iterator();
//        while (iterator.hasNext()) {
//            Node n = iterator.next();
//            if (n instanceof Polyline || n instanceof Polygon) {
//                iterator.remove();
//            }
//        }
//        // Redraw all lines from paths
//        drawLinesFromPaths(holder);
//    }

    public void redrawLines(RelationshipPathHolder holder) {
        // Remove all Polyline and Polygon nodes (lines and markers)
        Iterator<Node> iterator = nodeContainer.getChildren().iterator();
        while (iterator.hasNext()) {
            Node n = iterator.next();
            if (n instanceof Polyline) {
                iterator.remove();
            }
        }
        // Redraw all lines from paths
        drawLinesFromPaths(holder);
    }

    /**
     * lines should be cleared after every refresh
     */
    public void clearLines() {
        lines.clear();
    }

}
