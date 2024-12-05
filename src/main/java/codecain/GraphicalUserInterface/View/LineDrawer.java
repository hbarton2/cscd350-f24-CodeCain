package codecain.GraphicalUserInterface.View;

import codecain.GraphicalUserInterface.Model.RelationshipLines.AStarPath;
import codecain.GraphicalUserInterface.Model.RelationshipLines.GridCell;
import codecain.GraphicalUserInterface.Model.RelationshipLines.LineGrid;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;

import java.util.ArrayList;

public class LineDrawer {

    private Pane nodeContainer;
    private ArrayList<Polyline> lines;
    private LineGrid grid;

    public LineDrawer(LineGrid grid){
        this.grid = grid;
        this.nodeContainer = grid.getNodeContainer();
    }

    public void drawTestLine(){
        AStarPath path = new AStarPath(getTestCells());
        drawLineFromPath(path);

        String cellList = "";
        for (GridCell c: path.getCells()){
            cellList += "cell | row: " + c.getRow() + ", column: " + c.getCol();
        }
        System.out.println(cellList);
    }
    
    private ArrayList<GridCell> getTestCells(){
        ArrayList<GridCell> cells = new ArrayList<>();
        cells.add(grid.getCell(0,0));
        cells.add(grid.getCell(0,1));
        cells.add(grid.getCell(1,1));
        cells.add(grid.getCell(1,2));
        cells.add(grid.getCell(1,3));
        cells.add(grid.getCell(1,5));
        return cells;
    }


    /**
     * draws a line from the specified path
     * @param path the path  to draw
     */
    public Polyline drawLineFromPath(AStarPath path){
        if (path == null || path.getCells() == null) {
            throw new IllegalArgumentException("path or its cells cannot be null");
        }
        Polyline line = new Polyline();
        for (GridCell cell : path.getCells()){
            double x = grid.getXval(cell);
            double y = grid.getYval(cell);
            line.getPoints().addAll(x, y);
        }
        line.setStroke(Color.BLACK);
        line.setStrokeWidth(3.0);

        nodeContainer.getChildren().add(line);
        return line;
    }



}
