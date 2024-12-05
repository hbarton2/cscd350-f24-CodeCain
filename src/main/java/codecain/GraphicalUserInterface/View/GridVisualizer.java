package codecain.GraphicalUserInterface.View;

import codecain.GraphicalUserInterface.Model.RelationshipLines.GridCell;
import codecain.GraphicalUserInterface.Model.RelationshipLines.LineGrid;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class GridVisualizer {

    public class Dot extends Circle{

        public Dot(double x, double y){
            super();
            super.setCenterX(x);
            super.setCenterY(y);
            super.setRadius(16.0);
            super.setStroke(Color.BLACK);
            super.setFill(Color.BLACK);
        }

        public Dot(double x, double y, double radius){
            this(x,y);
            this.setRadius(radius);
        }

    }

    private boolean showSmallDots;
    private LineGrid grid;
    private Pane nodeContainer;
    private ArrayList<Dot> dots;

    public GridVisualizer(LineGrid grid, Pane nodeContainer){
        this.grid = grid;
        this.nodeContainer = nodeContainer;
        this.dots = new ArrayList<>();
        this.showSmallDots = false;
    }

    public Dot drawDot(double x, double y){
        Dot dot = new Dot(x, y);
        nodeContainer.getChildren().add(dot);
        return dot;
    }

    public Dot drawDot(double x, double y, double radius){
        Dot dot = new Dot(x, y, radius);
        nodeContainer.getChildren().add(dot);
        return dot;
    }

    public void updateGridVisualizer(){
        clearDots();
        int numRows = grid.getNumRows();
        int numCols = grid.getNumCols();
        GridCell currentCell = null;
        for (int row = 0; row < numRows; row++){
            for (int col = 0; col < numCols; col++){
                currentCell = grid.getCell(row, col);
                double x = grid.getXval(col);
                double y = grid.getYval(row);
                if (currentCell.isOccupied()){
                    dots.add(drawDot(x,y));
                }
                else if (showSmallDots){
                    dots.add(drawDot(x,y,2.0));
                }
            }
        }
    }

    private void clearDots(){
        for(Dot d : dots){
            nodeContainer.getChildren().remove(d);
        }
    }

    public void showSmallDots(){
        showSmallDots = true;
    }

    public void hideSmallDots(){
        showSmallDots = false;
    }

}