package codecain.GraphicalUserInterface.Model;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * grids must interact with classes and arrows
 * arrows talk to arrows, occupy cells
 * starting destination isn't part of the occupied
 * make comments inside github issues
 */

public class LineGrid {

    public class GridCell{
        boolean occupied;
        double cost;

        public GridCell(boolean occupied, double cost) {
            this.occupied = occupied;
            this.cost = cost;
        }
    }

    private final double cellWidth;
    private GridCell[][] grid;
    private boolean isGenerated;
    private int numRows;
    private int numCols;
    private final Pane nodeContainer;

    /**
     * Constructor for GridManager
     * @param cellWidth the width of each cell
     * @param nodeContainer the pane containing the classNodes
     */
    public LineGrid(double cellWidth, Pane nodeContainer){
        this.nodeContainer = nodeContainer;
        isGenerated = false;
        this.cellWidth = cellWidth;
        initGrid();
    }

    /**
     * generates a grid. Should only be called in the constructor
     */
    private void initGrid(){
        if (isGenerated){
            return;
        }
        isGenerated = true;

        int screenWidth = 2000;
        int screenHeight = 2000;

        numRows = (int) (screenWidth / cellWidth);
        numCols = (int) (screenHeight / cellWidth);
        generateGrid();
    }

    private void generateGrid(){
        grid = new GridCell[numRows][numCols];
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                grid[row][col] = new GridCell(false, 1.0); // Default state
            }
        }
    }

    /**
     * prints out an ascii representation of the grid. May be used for testing?
     */
    public void printGrid(){
        for (int row = 0; row < numRows; row++){
            String newLine = "";
            for (int col = 0; col < numCols; col++){
                newLine += "[";
                newLine += grid[row][col].occupied? "T":"F";
                newLine += "]";
            }
            System.out.println(newLine);
        }
    }

    /**
     * sifts through all elements of the container and looks for the ones that are VBoxes,
     * then updates the grid with which ones are occupied
     */
    public void updateGrid(){
        generateGrid();
        for (Node node : nodeContainer.getChildren()) {
            if (node instanceof VBox) {
                updateClassBox((VBox) node);
            }
        }
    }

    /**
     * helper method to update a single classBox's nodes
     * @param classNode the VBox representing the current classNode
     */
    private void updateClassBox(VBox classNode){
        double screenWidth = nodeContainer.getWidth();
        double screenHeight = nodeContainer.getHeight();
        double nodeWidth = classNode.getWidth();
        double nodeHeight = classNode.getHeight();
        double nodeX = classNode.getLayoutX();
        double nodeY = classNode.getLayoutY();
        int rowStart = getRow(nodeY);
        int rowEnd = getRow(nodeY + nodeHeight);
        int colStart = getCol(nodeY);
        int colEnd = getCol(nodeY + nodeHeight);
        for (int row = rowStart; row < rowEnd; row++){
            for (int col = colStart; col < colEnd; col++){
                if (row >= 0 && row < numRows && col >= 0 && col < numCols) {
                    grid[row][col].occupied = true;
                }
            }
        }
    }

    /**
     * checks if the cell is occupied or not
     * @param layoutX the x coordinate in the container to check
     * @param layoutY the y coordinate in the container to check
     * @return true if the cell is occupied
     */
    public boolean checkOccupied(double layoutX, double layoutY){
        int row = getRow(layoutX);
        int col = getCol(layoutY);
        return grid[row][col].occupied;
    }

    /**
     * makes a cell occupied
     * @param layoutX the x value in the pane
     * @param layoutY the y value in the pane
     */
    public void occupyCell(double layoutX, double layoutY){
        int row = getRow(layoutX);
        int col = getCol(layoutY);
        grid[row][col].occupied = true;
    }

    /**
     * unoccupied a cell in the grid
     * @param layoutX the x value in the pane
     * @param layoutY the y value in the pane
     */
    public void unoccupyCell(double layoutX, double layoutY){
        int row = getRow(layoutX);
        int col = getCol(layoutY);
        grid[row][col].occupied = false;
    }

    /**
     * returns the row of the coordinate x value
     * @param x the coordinate x value
     * @return the index for the x value
     */
    private int getRow(double x) {
        int row = (int)(x / cellWidth);
        return Math.max(0, Math.min(row, numRows - 1));
    }

    /**
     * returns the column from the coordinate y value
     * @param y the coordinate y value
     * @return the index of the column of the coordinate y value
     */
    private int getCol(double y) {
        int col = (int)(y / cellWidth);
        return Math.max(0, Math.min(col, numCols - 1));
    }
}
