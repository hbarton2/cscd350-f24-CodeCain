package codecain.GraphicalUserInterface.Model.RelationshipLines;

import java.util.ArrayList;
import java.util.Collection;

public class RelationshipPath {
    private ArrayList<GridCell> cells;

    public RelationshipPath(Collection<GridCell> cells){
        this.cells = (ArrayList<GridCell>) cells;
    }

    public RelationshipPath(){
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
