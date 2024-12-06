package codecain.GraphicalUserInterface.View;

import codecain.GraphicalUserInterface.Model.RelationshipLines.GridPath;
import codecain.GraphicalUserInterface.Model.RelationshipLines.GridCell;
import codecain.GraphicalUserInterface.Model.RelationshipLines.LineGrid;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;

import java.util.ArrayList;

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
    public Polyline drawLineFromPath(GridPath path){
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

        nodeContainer.getChildren().add(line);
        return line;
    }

    /**
     * lines should be cleared after every refresh
     */
    public void clearLines(){
        lines.clear();
    }



}
