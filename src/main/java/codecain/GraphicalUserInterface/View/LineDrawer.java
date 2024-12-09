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
import javafx.scene.shape.Polyline;

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
     * Draws a line from the specified path
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
                break;
            case AGGREGATION:
                line.setStroke(Color.ORANGE);
                break;
            case COMPOSITION:
                line.setStroke(Color.PURPLE);
                break;
            case REALIZATION:
                line.setStroke(Color.RED);
                line.getStrokeDashArray().addAll(10.0, 10.0); // Dashed for realization
                break;
            default:
                throw new IllegalArgumentException("Unsupported relationship type: " + type);
        }

        line.setStrokeWidth(3.0);
        nodeContainer.getChildren().add(line);
        return line;
    }

    /**
     * Draws lines for all paths in the holder based on their relationship types.
     * @param holder the RelationshipPathHolder containing paths and relationships
     */
    private void drawLinesFromPaths(RelationshipPathHolder holder) {
        for (Relationship r : Relationship.relationshipList) {
            RelationshipType type = r.getType();
            drawLineFromPath(holder.getPath(r), type).toBack();
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
