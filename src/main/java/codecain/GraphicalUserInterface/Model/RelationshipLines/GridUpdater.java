package codecain.GraphicalUserInterface.Model.RelationshipLines;

import codecain.GraphicalUserInterface.View.GridVisualizer;
import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.HashMap;

public class GridUpdater {

    /**
     * the grid to update
     */
    private LineGrid grid;

    /**
     * pane which contains class nodes, which we will be updating from
     */
    private Pane nodeContainer;

    private GridVisualizer visualizer;

    private boolean updateScheduled = false;

    /**
     * keeps track of the last update time
     */
    private long lastUpdateTime = 0;

    /**
     * update interval (every 100 milliseconds)
     */
    private final long updateInterval = 100_000_000;

    private HashMap<VBox, ArrayList<GridCell>> coveredCells;

    /**
     * animation timer to keep track of the last grid updates
     * runs for 100 milliseconds
     */
    private final AnimationTimer gridUpdateTimer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            if (now - lastUpdateTime >= updateInterval) {
                performGridUpdate();
                lastUpdateTime = now;
                updateScheduled = false;
                stop();
            }
        }
    };

    /**
     * constructor for the updater. This class keeps track of stuff in the grid
     * and updates it
     * @param grid the grid to update
     */
    public GridUpdater(LineGrid grid){
        this.visualizer = null;
        this.grid = grid;
        this.coveredCells = new HashMap<>();
        this.nodeContainer = grid.getNodeContainer();

    }

    public void setVisualizer(GridVisualizer visualizer){
        this.visualizer = visualizer;
    }

    /**
     * Adds listeners to a VBox node for position and size changes.
     * When any changes occur, the grid is updated accordingly.
     * @param classNode the VBox to add listeners to
     */
    public void addClassListeners(VBox classNode) {
        if (classNode == null) return;
        classNode.layoutXProperty().addListener((observable, oldValue, newValue) -> scheduleGridUpdate());
        classNode.layoutYProperty().addListener((observable, oldValue, newValue) -> scheduleGridUpdate());
        classNode.prefWidthProperty().addListener((observable, oldValue, newValue) -> scheduleGridUpdate());
        classNode.prefHeightProperty().addListener((observable, oldValue, newValue) -> scheduleGridUpdate());
    }


    /**
     * sifts through all elements of the container and looks for the ones that are VBoxes,
     * then updates the grid with which ones are occupied
     */
    public void updateGridBoxes(){
        //grid.generateGrid();
        for (Node node : nodeContainer.getChildren()) {
            if (node instanceof VBox) {
                System.out.println("class at " +node.getLayoutX() + " , " + + node.getLayoutY());
                updateOccupiedClassBoxCells((VBox) node);
            }
        }
        //updatePaths();
    }

    /**
     * schedules a grid update whenever the classes are moved or modified
     */
    private void scheduleGridUpdate() {
        if (!updateScheduled) {
            System.out.println("updateScheduled");
            updateScheduled = true;
            gridUpdateTimer.start();
        }
    }


    /**
     * helper method to update a single classBox's nodes
     * @param classNode the VBox representing the current classNode
     */
    public void updateOccupiedClassBoxCells(VBox classNode){

        int numRows = grid.getNumRows();
        int numCols = grid.getNumCols();
        double nodeWidth = classNode.getWidth();
        double nodeHeight = classNode.getHeight();
        double nodeX = classNode.getLayoutX();
        double nodeY = classNode.getLayoutY();
        int rowStart = grid.getRow(nodeY) + 1;
        int rowEnd = grid.getRow(nodeY + nodeHeight) + 1;
        int colStart = grid.getCol(nodeX) + 1;
        int colEnd = grid.getCol(nodeX + nodeWidth) + 1;

        if (this.coveredCells.containsKey(classNode)){
            coveredCells.remove(classNode);
        }
        ArrayList<GridCell> newCoveredCells = new ArrayList<>();
        for (int row = rowStart; row < rowEnd; row++){
            for (int col = colStart; col < colEnd; col++){
                if (row >= 0 && row < numRows && col >= 0 && col < numCols) {
                    GridCell cell = grid.getCell(row,col);
                    cell.occupied = true;
                    newCoveredCells.add(cell);
                }
            }
        }
        if (newCoveredCells.size() > Math.abs((rowStart - rowEnd) * (colStart - colEnd))){
            throw new IllegalStateException("error updating covered cells list");
        }
        getCoveredCells().put(classNode, newCoveredCells);
        //System.out.printf("\nOccupied at 100,100: %c\n", grid.checkOccupied(100.0,100.0)? 'T' : 'F');
    }


    public HashMap<VBox, ArrayList<GridCell>> getCoveredCells(){
        return this.coveredCells;
    }

    /**
     * Perform the actual grid update.
     */
    public void performGridUpdate() {
        System.out.println("Updating grid...");
        grid.clearGrid();
        updateGridBoxes();
        updatePaths();
        if (visualizer != null){
            visualizer.updateGridVisualizer();
        }
        //grid.printGrid();
    }

    /**
     * occupies the cells in the grid path list
     */
    private void updatePaths(){
        for (GridPath path : grid.getPaths()){
            System.out.println("Processing path: " + path);
            occupyPathCells(path);
        }
    }

    public void occupyPathCells(GridPath path){
        for (GridCell current : path.getCells()){
            System.out.println("Updating cell: " + current.toString());
            current.occupied = true;
        }
    }

        public void cleanUp(){
        for (VBox box : coveredCells.keySet()){
            if(!nodeContainer.getChildren().contains(box)){
                coveredCells.remove(box);
            }
        }
    }

    public GridCell getStartingCell(VBox startingNode, VBox goalNode){

        int[] goalPoint = findCenter(goalNode);
        GridCell goal = grid.getCell(goalPoint[0],goalPoint[1]);
        int minDist = 20000000;
        GridCell start = null;

        for (GridCell cell : this.coveredCells.get(startingNode)){
            int dist = (int) grid.calculateHeuristic(cell, goal);
            if (!grid.getWalkableNeighbors(cell).isEmpty() && dist < minDist){
                minDist = dist;
                start=cell;
            }
        }
        if (start == null){
            System.out.println("no walkable starting points found!");
        }
        return start;
    }

    public GridPath navigatePath(VBox start, VBox goal){
        GridCell startingCell = getStartingCell(start,goal);
        GridCell goalCell = getStartingCell(goal, start);
        PathNavigator navigator = new PathNavigator(grid);
        GridPath p = navigator.findPath(startingCell,goalCell);
        grid.getPaths().add(p);
        return p;
    }

    private int[] findCenter(VBox classNode){
        double nodeWidth = classNode.getWidth();
        double nodeHeight = classNode.getHeight();
        double nodeX = classNode.getLayoutX();
        double nodeY = classNode.getLayoutY();
        int rowStart = grid.getRow(nodeY) + 1;
        int rowEnd = grid.getRow(nodeY + nodeHeight) + 1;
        int colStart = grid.getCol(nodeX) + 1;
        int colEnd = grid.getCol(nodeX + nodeWidth) + 1;
        int col = colStart + Math.abs((int)((colStart - colEnd)/2));
        int row = rowStart + Math.abs((int)((rowStart - rowEnd)/2));
        return new int[]{col, row};
    }

}
