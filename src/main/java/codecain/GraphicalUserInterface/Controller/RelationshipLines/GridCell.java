package codecain.GraphicalUserInterface.Controller.RelationshipLines;

public class GridCell{
    boolean occupied;
    double cost;
    int row;
    int col;


    public GridCell(boolean occupied, double cost) {
        this.occupied = occupied;
        this.cost = cost;
        this.row = 0;
        this.col = 0;
    }

    public GridCell(boolean occupied, double cost, int row, int col) {
        this.occupied = occupied;
        this.cost = cost;
        this.row = row;
        this.col = col;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public String toString(){
        return "col: " + col + " row: " + row;
    }


}