package codecain.GraphicalUserInterface.View;

import codecain.BackendCode.Model.Relationship;
import codecain.BackendCode.Model.RelationshipType;
import codecain.GraphicalUserInterface.Controller.RelationshipLines.GridPath;
import codecain.GraphicalUserInterface.Controller.RelationshipLines.GridCell;
import codecain.GraphicalUserInterface.Controller.RelationshipLines.LineGrid;
import codecain.GraphicalUserInterface.Controller.RelationshipLines.RelationshipPathHolder;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;

import java.util.ArrayList;
import java.util.Iterator;

public class LineDrawer {

    private Pane nodeContainer;
    private ArrayList<Polyline> lines;
    private LineGrid grid;



    /**
     * constructor takes in a grid object and adds a container
     * from that object
     * @param grid
     */
    public LineDrawer(LineGrid grid){
        this.grid = grid;
        this.nodeContainer = grid.getNodeContainer();
    }


    /**
     * draws a line from the specified path
     * @param path the path  to draw
     */
    public Polyline drawLineFromPath(GridPath path, boolean isDashed){
        if (path == null || path.getCells() == null) {
            throw new IllegalArgumentException("path or its cells cannot be null");
        }
        Polyline line = new Polyline();
        for (GridCell cell : path.getCells()){
            double x = grid.getXcoord(cell);
            double y = grid.getYcoord(cell);
            line.getPoints().addAll(x, y);
        }
        line.setStroke(Color.BLACK);
        line.setStrokeWidth(3.0);
        if (isDashed){
            line.getStrokeDashArray().addAll(10.0, 10.0);
        }

        nodeContainer.getChildren().add(line);
        return line;
    }

    private void drawLinesFromPaths(RelationshipPathHolder holder){
        for (Relationship r:Relationship.relationshipList){
            boolean isDashed = r.getType().equals(RelationshipType.REALIZATION);
            drawLineFromPath(holder.getPath(r), isDashed).toBack();
        }
    }


    public void redrawLines(RelationshipPathHolder holder) {
        Iterator<Node> iterator = nodeContainer.getChildren().iterator();
        while (iterator.hasNext()) {
            Node n = iterator.next();
            if (n instanceof Polyline) {
                iterator.remove();
            }
        }
        drawLinesFromPaths(holder);
    }

    /**
     * lines should be cleared after every refresh
     */
    public void clearLines(){
        lines.clear();
    }



}
