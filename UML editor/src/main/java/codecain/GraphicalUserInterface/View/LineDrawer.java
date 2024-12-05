package codecain.GraphicalUserInterface.View;

import codecain.GraphicalUserInterface.Model.RelationshipLines.AStarPath;
import codecain.GraphicalUserInterface.Model.RelationshipLines.GridCell;
import codecain.GraphicalUserInterface.Model.RelationshipLines.GridManager;
import codecain.GraphicalUserInterface.Model.RelationshipLines.LineGrid;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;

import java.util.ArrayList;

public class LineDrawer {

    private Pane nodeContainer;
    private ArrayList<Polyline> lines;
    private LineGrid grid;

    public LineDrawer(LineGrid grid, Pane nodeContainer){
        this.nodeContainer = nodeContainer;
        this.grid = grid;
    }

    public void drawLineFromPath(AStarPath path){
        Polyline line = new Polyline();
        for (GridCell cell : path.getCells()){
            double x = grid.getXval(cell);
            double y = grid.getYval(cell);
            line.getPoints().addAll(x, y);
        }
        line.setStrokeWidth(2.0);
        line.setFill(Color.BLACK);
        nodeContainer.getChildren().add(line);
    }

}
