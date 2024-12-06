package codecain.GraphicalUserInterface.Model.RelationshipLines;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * grids must interact with classes and arrows
 * arrows talk to arrows, occupy cells
 * starting destination isn't part of the occupied
 * make comments inside github issues
 */

public class LineGrid {

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
            System.out.println("Error during grid initialization: " + error.getMessage());
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
        System.out.println("New grid created: numCols: " + numCols + ", numRows: " + numRows);
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

//    /**
//     * sifts through all elements of the container and looks for the ones that are VBoxes,
//     * then updates the grid with which ones are occupied
//     */
//    public void updateGrid(){
//        generateGrid();
//        for (Node node : nodeContainer.getChildren()) {
//            if (node instanceof VBox) {
//                System.out.println("class at " +node.getLayoutX() + " , " + + node.getLayoutY());
//                updateOccupiedClassBoxCells((VBox) node);
//            }
//        }
//    }

//    /**
//     * helper method to update a single classBox's nodes
//     * @param classNode the VBox representing the current classNode
//     */
//    public void updateOccupiedClassBoxCells(VBox classNode){
//
//        double nodeWidth = classNode.getWidth();
//        double nodeHeight = classNode.getHeight();
//        double nodeX = classNode.getLayoutX();
//        double nodeY = classNode.getLayoutY();
//        int rowStart = getRow(nodeY) + 1;
//        int rowEnd = getRow(nodeY + nodeHeight) + 1;
//        int colStart = getCol(nodeX) + 1;
//        int colEnd = getCol(nodeX + nodeWidth) + 1;
//
//        if (this.coveredCells.containsKey(classNode)){
//            coveredCells.remove(classNode);
//        }
//        ArrayList<GridCell> newCoveredCells = new ArrayList<>();
//        for (int row = rowStart; row < rowEnd; row++){
//            for (int col = colStart; col < colEnd; col++){
//                if (row >= 0 && row < numRows && col >= 0 && col < numCols) {
//                    gridCells[row][col].occupied = true;
//                    newCoveredCells.add(gridCells[row][col]);
//                }
//            }
//        }
//        if (newCoveredCells.size() > Math.abs((rowStart - rowEnd) * (colStart - colEnd))){
//            throw new IllegalStateException("error updating covered cells list");
//        }
//        getCoveredCells().put(classNode, newCoveredCells);
//        System.out.printf("\nOccupied at 100,100: %c\n", checkOccupied(100.0,100.0)? 'T' : 'F');
//    }

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
     * @param layoutX the x coordinate in the container to check
     * @param layoutY the y coordinate in the container to check
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
     * returns the column from the coordinate y value
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

    public double getXcoord(int col){
        return col*cellWidth;
    }


    public double getXcoord(GridCell cell){
        return getXcoord(cell.getCol());
    }

    public double getYcoord(int row){
        return row*cellWidth;
    }

    public double getYcoord(GridCell cell){
        return getYcoord(cell.getRow());
    }


    /**
     * calculates the cost of the path?
     * @param start starting cell
     * @param goal ending cell
     * @return the cost
     */
    public double calculateHeuristic(GridCell start, GridCell goal) {
        return Math.abs(start.row - goal.row) + Math.abs(start.col - goal.col);
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

    /**
     *
     * @return
     */
    public ArrayList<GridPath> getPaths(){
        return this.paths;
    }

    /**
     * adds a path to the list
     * @param path the path to add
     */
    public void addPath(GridPath path){
        this.paths.add(path);
    }

////    public HashMap<VBox, ArrayList<GridCell>> getCoveredCells(){
////        return this.coveredCells;
////    }
//
////    public void cleanUp(){
////        for (VBox box : coveredCells.keySet()){
////            if(!nodeContainer.getChildren().contains(box)){
////                coveredCells.remove(box);
////            }
////        }
////    }
//
////    public GridCell getStartingCell(VBox startingNode, VBox goalNode){
////
////        int[] goalPoint = findCenter(goalNode);
////        GridCell goal = this.getCell(goalPoint[0],goalPoint[1]);
////        int minDist = 20000000;
////        GridCell start = null;
////
////        for (GridCell cell : this.coveredCells.get(startingNode)){
////            int dist = (int) calculateHeuristic(cell, goal);
////            if (!getWalkableNeighbors(cell).isEmpty() && dist < minDist){
////                minDist = dist;
////                start=cell;
////            }
////        }
////        if (start == null){
////            System.out.println("no walkable starting points found!");
////        }
////        return start;
////    }
//
////    public GridPath navigatePath(VBox start, VBox goal){
////        GridCell startingCell = getStartingCell(start,goal);
////        GridCell goalCell = getStartingCell(goal, start);
////        PathNavigator navigator = new PathNavigator(this);
////        GridPath p = navigator.findPath(startingCell,goalCell);
////        paths.add(p);
////        return p;
////    }
//
//    private int[] findCenter(VBox classNode){
//        double nodeWidth = classNode.getWidth();
//        double nodeHeight = classNode.getHeight();
//        double nodeX = classNode.getLayoutX();
//        double nodeY = classNode.getLayoutY();
//        int rowStart = getRow(nodeY) + 1;
//        int rowEnd = getRow(nodeY + nodeHeight) + 1;
//        int colStart = getCol(nodeX) + 1;
//        int colEnd = getCol(nodeX + nodeWidth) + 1;
//        int col = colStart + Math.abs((int)((colStart - colEnd)/2));
//        int row = rowStart + Math.abs((int)((rowStart - rowEnd)/2));
//        return new int[]{col, row};
//    }



}
