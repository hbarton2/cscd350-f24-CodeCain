package codecain.GraphicalUserInterface.Model.RelationshipLines;

import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;

public class AStarPath {
    private ArrayList<GridCell> cells;

    public AStarPath(Collection<GridCell> cells){
        this.cells = (ArrayList<GridCell>) cells;
    }

    public AStarPath(){
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
}
