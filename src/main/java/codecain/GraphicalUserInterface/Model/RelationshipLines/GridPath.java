package codecain.GraphicalUserInterface.Model.RelationshipLines;

import java.util.ArrayList;
import java.util.Collection;

public class GridPath {
    private ArrayList<GridCell> cells;

    public GridPath(Collection<GridCell> cells){
        this.cells = (ArrayList<GridCell>) cells;
    }

    public GridPath(){
        this.cells = new ArrayList<>();
    }

    public void addCell(GridCell cell){
        this.cells.add(cell);
    }

    public GridCell removeCell(GridCell cell){
        this.cells.remove(cell);
        return cell;
    }

    public ArrayList<GridCell> getCells(){
        return cells;
    }

    public String toString(){
        String s = "path: ";
        for (GridCell cell : cells){
            String cellString = "x: " + cell.getCol() + ", y: " + cell.getRow() + " ; ";
            s+=cellString;
        }
        return s;
    }



}
