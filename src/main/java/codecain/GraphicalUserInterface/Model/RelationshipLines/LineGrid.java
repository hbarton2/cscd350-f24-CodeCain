package codecain.GraphicalUserInterface.Model.RelationshipLines;

import java.util.ArrayList;

import javafx.scene.layout.Pane;


/**
 * grids must interact with classes and arrows
 * arrows talk to arrows, occupy cells
 * starting destination isn't part of the occupied
 * make comments inside github issues
 */

public class LineGrid {

    private boolean showText = false;

    /**
     * the width of the cell
     */
    private final double cellWidth;

    /**
     * matrix containing the cells in the grid
     */
    private GridCell[][] gridCells;

    /**
     * tells if the grid has been generated
     */
    private boolean isGenerated;

    /**
     * number of rows in the grid
     */
    private int numRows;

    /**
     * number of columns in the grid
     */
    private int numCols;

    /**
     * pane containing the class nodes and everything
     */
    private final Pane nodeContainer;

    /**
     * the width of the screen
     */
    private final double screenWidth;

    /**
     * the height of the screen
     */
    private final double screenHeight;

    private ArrayList<GridPath> paths;


    /**
     * Constructor for GridManager
     * @param cellWidth the width of each cell
     * @param nodeContainer the pane containing the classNodes
     */
    public LineGrid(double cellWidth, double screenWidth, double screenHeight, Pane nodeContainer) {
        this.nodeContainer = nodeContainer;
        isGenerated = false;
        this.cellWidth = cellWidth;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.paths = new ArrayList<>();
        //this.coveredCells = new HashMap<>();
        try {
            initGrid();
        } catch (Exception error) {
            if (showText)System.out.println("Error during grid initialization: " + error.getMessage());
            throw new RuntimeException("Grid initialization failed", error);
        }
    }

    /**
     * getter for the cell width
     * @return
     */
    public double getCellWidth(){
        return this.cellWidth;
    }

    /**
     * helper method for the constructor
     * generates a grid. Should only be called in the constructor
     */
    private void initGrid() throws Exception {
        this.numRows = (int) (screenWidth / cellWidth);
        this.numCols = (int) (screenHeight / cellWidth);
        checkErrors();
        if (isGenerated){
            return;
        }
        isGenerated = true;
        generateGrid();
    }

    /**
     * clears the grid
     */
    public void clearGrid(){
        for (int row = 0; row < numRows; row++){
            for (int col = 0; col < numCols; col++){
                gridCells[row][col].occupied = false;
            }
        }
    }

    /**
     * validates the dimensions of the screen to make sure everything is created properly
     */
    private void checkErrors() throws Exception {
        if (screenHeight == 0 || screenWidth == 0){
            String errorMsg = "";
            if (screenHeight == 0){
                errorMsg += "screenHeight = 0! ";
            }
            if (screenWidth == 0) {
                errorMsg += "screenWidth = 0!";
            }
            throw new Exception(errorMsg + " Screen width and Height must be set!");
        }

        if (numRows == 0 || numCols == 0){
            throw new Exception("Grid cannot have a width or height of 0");
        }
    }

    /**
     * helper method to generate the grid
     */
    public void generateGrid(){
        gridCells = new GridCell[numRows][numCols];
        if (showText) System.out.println("New grid created: numCols: " + numCols + ", numRows: " + numRows);
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                gridCells[row][col] = new GridCell(false, 1.0, row, col);
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
                newLine += gridCells[row][col].occupied? "T":"F";
                newLine += "]";
            }
            System.out.println(newLine);
        }
    }

    /**
     *
     * @param path
     */
    public void updateOccupiedPathCells(GridPath path){
        for(GridCell cell : path.getCells()){
            cell.setOccupied(true);
        }
    }


    /**
     * checks if the cell is occupied or not
     * @param col the x coordinate in the container to check
     * @param row the y coordinate in the container to check
     * @return true if the cell is occupied
     */
    public boolean checkOccupied(int row, int col){
        //int row = getRow(layoutX);
        //int col = getCol(layoutY);
        return gridCells[row][col].occupied;
    }

    /**
     * makes a cell occupied
     * @param layoutX the x value in the pane
     * @param layoutY the y value in the pane
     */
    public void occupyCell(double layoutX, double layoutY){
        int row = getRow(layoutX);
        int col = getCol(layoutY);
        gridCells[row][col].occupied = true;
    }

    /**
     * unoccupied a cell in the grid
     * @param layoutX the x value in the pane
     * @param layoutY the y value in the pane
     */
    public void unoccupyCell(double layoutX, double layoutY){
        int row = getRow(layoutX);
        int col = getCol(layoutY);
        gridCells[row][col].occupied = false;
    }

    /**
     * returns the row of the coordinate x value
     * @param y the coordinate x value
     * @return the index for the x value
     */
    int getRow(double y) {
        int row = (int)(y / cellWidth);
        return Math.max(0, Math.min(row, numRows - 1));
    }

    /**
     * returns the column index from the coordinate y value
     * @param x the coordinate y value
     * @return the index of the column of the coordinate y value
     */
    int getCol(double x) {
        int col = (int)(x / cellWidth);
        return Math.max(0, Math.min(col, numCols - 1));
    }

    /**
     * returns an arraylist of grid coordinates
     * @param row the row of the cell
     * @param col the column of the cell
     * @return list of neighboring points
     */
    public ArrayList<int[]> getNeighbors(int row, int col){
        ArrayList<int[]> neighbors = new ArrayList<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : directions){
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            if (newRow >= 0 && newRow < numRows && newCol >= 0 && newCol < numCols){
                neighbors.add(new int[]{newRow, newCol});
            }
        }
        return neighbors;
    }

    public ArrayList<GridCell> getNeighbors(GridCell gridCell){
        ArrayList<GridCell> neighbors = new ArrayList<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : directions){
            int newRow = gridCell.row + dir[0];
            int newCol = gridCell.col + dir[1];
            if (newRow >= 0 && newRow < numRows && newCol >= 0 && newCol < numCols){
                neighbors.add(gridCells[newRow][newCol]);
            }
        }
        return neighbors;
    }


    public double getScreenHeight() {
        return screenHeight;
    }

    public double getScreenWidth() {
        return screenWidth;
    }

    public int getNumCols() {
        return numCols;
    }

    public int getNumRows() {
        return numRows;
    }

    public Pane getNodeContainer() {
        return nodeContainer;
    }


    /**
     * Returns the x-coordinate of the specified column in the grid.
     *
     * @param col the column index
     * @return the x-coordinate corresponding to the column
     */
    public double getXcoord(int col) {
        return col * cellWidth;
    }

    /**
     * Returns the x-coordinate of the specified GridCell.
     *
     * @param cell the GridCell whose x-coordinate is to be found
     * @return the x-coordinate corresponding to the GridCell
     */
    public double getXcoord(GridCell cell) {
        return getXcoord(cell.getCol());
    }

    /**
     * Returns the y-coordinate of the specified row in the grid.
     *
     * @param row the row index
     * @return the y-coordinate corresponding to the row
     */
    public double getYcoord(int row) {
        return row * cellWidth;
    }

    /**
     * Returns the y-coordinate of the specified GridCell.
     *
     * @param cell the GridCell whose y-coordinate is to be found
     * @return the y-coordinate corresponding to the GridCell
     */
    public double getYcoord(GridCell cell) {
        return getYcoord(cell.getRow());
    }


    /**
     * tests to see if a cell is walkable or not
     * @param row the row of the cell
     * @param col the column of the cell
     * @return true if the cell isn't occupied
     */
    public boolean isWalkable(int row, int col) {
        if (row >= 0 && row < numRows && col >= 0 && col < numCols) {
            return !gridCells[row][col].isOccupied();
        }
        return false;
    }

    /**
     * gets the cell from the given row and column
     * @param row the row of the cell
     * @param col the row of the column
     * @return the cell in the grid, or null if the cell is out of bounds
     */
    public GridCell getCell(int row, int col){
        if (row >= 0 && row < numRows && col >= 0 && col < numCols) {
            return gridCells[row][col];
        }
        return null;
    }

    /**
     * gets the walkable neighbors
     * @param current current cell
     * @return list of walkable neighbors
     */
    public ArrayList<GridCell> getWalkableNeighbors(GridCell current) {
        ArrayList<GridCell> neighbors = getNeighbors(current);
        neighbors.removeIf(neighbor -> !isWalkable(neighbor.row, neighbor.col));
        return neighbors;
    }


}
