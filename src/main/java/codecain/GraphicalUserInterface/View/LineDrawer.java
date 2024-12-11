package codecain.GraphicalUserInterface.View;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import codecain.BackendCode.Model.Relationship;
import codecain.BackendCode.Model.RelationshipType;
import codecain.GraphicalUserInterface.Controller.RelationshipLines.GridCell;
import codecain.GraphicalUserInterface.Controller.RelationshipLines.GridPath;
import codecain.GraphicalUserInterface.Controller.RelationshipLines.LineGrid;
import codecain.GraphicalUserInterface.Controller.RelationshipLines.RelationshipPathHolder;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;

public class LineDrawer {

    private class pointHolder{
        Point2D start;
        Point2D goal;

        pointHolder(Point2D start, Point2D end){
            this.goal = end;
            this.start = start;
        }

    }

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
    public Polyline drawLineFromPath(GridPath path, RelationshipType type, ClassNode goalNode) {
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
                addArrowhead(findArrowGoalPoint(path,type,goalNode), Color.BLUE);
                break;
            case AGGREGATION:
                line.setStroke(Color.ORANGE);
                addDiamond(findDiamondGoalPoint(path,type,goalNode,15.0), Color.ORANGE);
                break;
            case COMPOSITION:
                line.setStroke(Color.PURPLE);
                addFilledDiamond(findDiamondGoalPoint(path,type,goalNode, 15.0), Color.PURPLE);
                break;
            case REALIZATION:
                line.setStroke(Color.RED);
                line.getStrokeDashArray().addAll(10.0, 10.0); // Dashed for realization
                addArrowhead(findArrowGoalPoint(path,type,goalNode), Color.RED);
                break;
            default:
                throw new IllegalArgumentException("Unsupported relationship type: " + type);
        }

        line.setStrokeWidth(3.0);
        nodeContainer.getChildren().add(line);
        return line;
    }

    private void addArrowhead(pointHolder points, Color color) {
        double startX = points.start.getX();
        double startY = points.start.getY();
        double endX = points.goal.getX();
        double endY = points.goal.getY();

        double end2X;
        double end2Y;

        // Calculate arrowhead points
        double angle = Math.atan2(endY - startY, endX - startX) - Math.PI;
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



    private void addDiamond(pointHolder points, Color color) {
        double startX = points.start.getX();
        double startY = points.start.getY();
        double endX = points.goal.getX();
        double endY = points.goal.getY();

        // Calculate the midpoint of the line
        double midX = endX;
        double midY = endY;

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
        diamond.setStrokeWidth(3.0);
        diamond.setStroke(color);
        diamond.setFill(Color.WHITE);
        nodeContainer.getChildren().add(diamond);
    }


    private void addFilledDiamond(pointHolder points, Color color) {

        double startX = points.start.getX();
        double startY = points.start.getY();
        double endX = points.goal.getX();
        double endY = points.goal.getY();

        // Calculate the midpoint of the line
        double midX = endX;
        double midY = endY;

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
            drawLineFromPath(holder.getPath(r), type, holder.getDestinationClassNode(r)).toBack();
        }
    }


    public void redrawLines(RelationshipPathHolder holder) {
        // Remove all Polyline and Polygon nodes (lines and markers)
        Iterator<Node> iterator = nodeContainer.getChildren().iterator();
        while (iterator.hasNext()) {
            Node n = iterator.next();
            if (n instanceof Polyline || n instanceof Polygon) {
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


    public pointHolder findArrowGoalPoint(GridPath path, RelationshipType type, ClassNode node){
        //ClassNode node = pathHolder.getDestinationClassNode(r);
        if (path.getCells().isEmpty()){
            return new pointHolder(new Point2D(0.0,0.0) , new Point2D(0.0,0.0));
        }
        double xLowerBound = node.getLayoutX();
        double xUpperBoumd = xLowerBound + node.getWidth();
        double yLowerBound = node.getLayoutY() + node.getHeight();
        double yUpperBound = node.getLayoutY();
        double x = 10.0;
        double y = 10.0;

        int direction = 0;
        GridCell destinationCell1 = path.getCells().getLast();
        GridCell destinationCell2 = path.getCells().get(path.size() - 2);

        double colX1 = destinationCell1.getCol() * grid.getCellWidth();
        double colX2 = destinationCell2.getCol() * grid.getCellWidth();
        double rowY1 = destinationCell1.getRow() * grid.getCellWidth();
        double rowY2 = destinationCell2.getRow() * grid.getCellWidth();

        Point2D start = new Point2D(colX2,rowY2);

        //if upper bound y is in between the two cells
        if (destinationCell1.getCol() == destinationCell2.getCol()){
            x = destinationCell1.getCol() *grid.getCellWidth();
            // if is in between
            if (isInbetween(rowY1,rowY2,yUpperBound)){
                y= yUpperBound;
            }
            else y = yLowerBound;
        }
        else if (destinationCell1.getRow() == destinationCell2.getRow()){
            y = destinationCell2.getRow() * grid.getCellWidth();
            if (isInbetween(colX1,colX2,xUpperBoumd)){
                x = xUpperBoumd;
            }
            else x = xLowerBound;
        }

        //arrowPoints.add(new Point2D(x,y));
        Point2D end = new Point2D(x,y);
        return new pointHolder(start, end);
    }


    public pointHolder findDiamondGoalPoint(GridPath path, RelationshipType type, ClassNode node, double diamondLength){
        //ClassNode node = pathHolder.getDestinationClassNode(r);
        if (path.getCells().isEmpty()){
            return new pointHolder(new Point2D(0.0,0.0) , new Point2D(0.0,0.0));
        }
        double xLowerBound = node.getLayoutX();
        double xUpperBoumd = xLowerBound + node.getWidth();
        double yLowerBound = node.getLayoutY() + node.getHeight();
        double yUpperBound = node.getLayoutY();
        double x = 10.0;
        double y = 10.0;

        int direction = 0;
        GridCell destinationCell1 = path.getCells().getLast();
        GridCell destinationCell2 = path.getCells().get(path.size() - 2);

        double colX1 = destinationCell1.getCol() * grid.getCellWidth();
        double colX2 = destinationCell2.getCol() * grid.getCellWidth();
        double rowY1 = destinationCell1.getRow() * grid.getCellWidth();
        double rowY2 = destinationCell2.getRow() * grid.getCellWidth();

        Point2D start = new Point2D(colX2,rowY2);

        //if upper bound y is in between the two cells
        if (destinationCell1.getCol() == destinationCell2.getCol()){
            x = destinationCell1.getCol() *grid.getCellWidth();
            // if is in between
            if (isInbetween(rowY1,rowY2,yUpperBound)){
                y= yUpperBound - (diamondLength);
            }
            else y = yLowerBound + (diamondLength);
        }
        else if (destinationCell1.getRow() == destinationCell2.getRow()){
            y = destinationCell2.getRow() * grid.getCellWidth();
            if (isInbetween(colX1,colX2,xUpperBoumd)){
                x = xUpperBoumd + (diamondLength);
            }
            else x = xLowerBound - (diamondLength);
        }

        //arrowPoints.add(new Point2D(x,y));
        Point2D end = new Point2D(x,y);
        return new pointHolder(start, end);
    }

//    public HashSet<Point2D> getArrowPoints(){
//        return (HashSet<Point2D>) this.arrowPoints;
//    }

    private boolean isInbetween(double v1, double v2, double c){
        if (v1 < v2){
            return c >= v1 && c <= v2;
        }
        return c >= v2 && c <= v1;
    }


}
