package codecain.GraphicalUserInterface.Model;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

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
    private double screenWidth;
    private double screenHeight;



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
        try {
            initGrid();
        } catch (Exception error) {
            System.out.println("Error during grid initialization: " + error.getMessage());
            throw new RuntimeException("Grid initialization failed", error);
        }
        drawTestDot(100.0,100.0, nodeContainer);
    }

    public double getCellWidth(){
        return this.cellWidth;
    }

    /**
     * draws a dot to mark a certain point for testing
     * @param x x value of the dot
     * @param y y value of the dot
     * @param pane the pane to draw the dot on
     */
    private void drawTestDot(double x, double y, Pane pane){
        Circle dot = new Circle();
        dot.setCenterX(x);
        dot.setCenterY(y);
        dot.setRadius(3.0);
        dot.setFill(Color.BLACK);
        pane.getChildren().add(dot);
        dot.toFront();
    }

    /**
     * generates a grid. Should only be called in the constructor
     */
    private void initGrid() throws Exception {
        this.numRows = (int) (screenWidth / cellWidth);
        this.numCols = (int) (screenHeight / cellWidth);
        checkConstructorErrors();
        if (isGenerated){
            return;
        }
        isGenerated = true;
        generateGrid();
    }

    /**
     * validates the dimensions of the screen to make sure everything is created properly
     */
    private void checkConstructorErrors() throws Exception {
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
    private void generateGrid(){
        grid = new GridCell[numRows][numCols];
        System.out.println("New grid created: numCols: " + numCols + ", numRows: " + numRows);
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
        double nodeWidth = classNode.getWidth();
        double nodeHeight = classNode.getHeight();
        double nodeX = classNode.getLayoutX();
        double nodeY = classNode.getLayoutY();
        int rowStart = getRow(nodeY);
        int rowEnd = getRow(nodeY + nodeHeight);
        int colStart = getCol(nodeX);
        int colEnd = getCol(nodeX + nodeWidth);
        for (int row = rowStart; row < rowEnd; row++){
            for (int col = colStart; col < colEnd; col++){
                if (row >= 0 && row < numRows && col >= 0 && col < numCols) {
                    grid[row][col].occupied = true;
                }
            }
        }
        System.out.printf("\nOccupied at 100,100: %c\n", checkOccupied(100.0,100.0)? 'T' : 'F');
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
    private int getRow(double y) {
        int row = (int)(y / cellWidth);
        return Math.max(0, Math.min(row, numRows - 1));
    }

    /**
     * returns the column from the coordinate y value
     * @param y the coordinate y value
     * @return the index of the column of the coordinate y value
     */
    private int getCol(double x) {
        int col = (int)(x / cellWidth);
        return Math.max(0, Math.min(col, numCols - 1));
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
}
