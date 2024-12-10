package codecain.GraphicalUserInterface.Controller.RelationshipLines;

import java.util.ArrayList;
import java.util.HashMap;

import codecain.BackendCode.Model.Relationship;
import codecain.GraphicalUserInterface.View.ClassNode;
import codecain.GraphicalUserInterface.View.GridVisualizer;
import codecain.GraphicalUserInterface.View.LineDrawer;
import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.layout.Pane;


public class GridUpdater {

    /**
     * disable to disable debug messages
     */
    private boolean showText = false;

    /**
     * the grid to update
     */
    private LineGrid grid;

    /**
     * pane which contains class nodes, which we will be updating from
     */
    private Pane nodeContainer;


    /**
     * visualizer for testing
     */
    private GridVisualizer visualizer;

    /**
     *
     */
    private boolean updateScheduled = false;

    /**
     * keeps track of the last update time
     */
    private long lastUpdateTime = 0;

    /**
     * drawer for lines
     */
    private LineDrawer lineDrawer;


    /**
     * update interval (every 100 milliseconds)
     */
    private final long updateInterval = 100_000_000;

    private RelationshipPathHolder pathHolder;

    private HashMap<ClassNode, ArrayList<GridCell>> coveredCells;

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
    public GridUpdater(LineGrid grid, RelationshipPathHolder pathHolder, LineDrawer lineDrawer){
        this.visualizer = null;
        this.grid = grid;
        this.coveredCells = new HashMap<>();
        this.nodeContainer = grid.getNodeContainer();
        this.pathHolder = pathHolder;
        this.lineDrawer = lineDrawer;
    }

    public void setVisualizer(GridVisualizer visualizer){
        this.visualizer = visualizer;
    }

    /**
     * Adds listeners to a ClassNode node for position and size changes.
     * When any changes occur, the grid is updated accordingly.
     * @param classNode the ClassNode to add listeners to
     */
    public void addClassListeners(ClassNode classNode) {
        if (classNode == null) return;
        classNode.layoutXProperty().addListener((observable, oldValue, newValue) -> scheduleGridUpdate());
        classNode.layoutYProperty().addListener((observable, oldValue, newValue) -> scheduleGridUpdate());
        classNode.prefWidthProperty().addListener((observable, oldValue, newValue) -> scheduleGridUpdate());
        classNode.prefHeightProperty().addListener((observable, oldValue, newValue) -> scheduleGridUpdate());
    }




    /**
     * schedules a grid update whenever the classes are moved or modified
     */
    private void scheduleGridUpdate() {
        if (!updateScheduled) {
            if (showText) System.out.println("updateScheduled");
            updateScheduled = true;
            gridUpdateTimer.start();
        }
    }


    /**
     * Helper method to update a single ClassNode's occupied cells.
     * @param classNode the ClassNode representing the current classNode
     */
    private void updateOccupiedClassBoxCells(ClassNode classNode) {
        int numRows = grid.getNumRows();
        int numCols = grid.getNumCols();
        double nodeWidth = classNode.getWidth();
        double nodeHeight = classNode.getHeight();
        double nodeX = classNode.getLayoutX();
        double nodeY = classNode.getLayoutY();
        int rowStart = grid.getRow(nodeY) +1;
        int rowEnd = grid.getRow(nodeY + nodeHeight) + 1;
        int colStart = grid.getCol(nodeX) +1;
        int colEnd = grid.getCol(nodeX + nodeWidth) + 1;

        if (this.coveredCells.containsKey(classNode)) {
            coveredCells.remove(classNode);
        }
        ArrayList<GridCell> newCoveredCells = new ArrayList<>();
        for (int row = rowStart; row < rowEnd; row++) {
            for (int col = colStart; col < colEnd; col++) {
                if (row >= 0 && row < numRows && col >= 0 && col < numCols) {
                    GridCell cell = grid.getCell(row, col);
                    cell.occupied = true;
                    newCoveredCells.add(cell);
                }
            }
        }
        if (newCoveredCells.size() > Math.abs((rowStart - rowEnd) * (colStart - colEnd))) {
            throw new IllegalStateException("error updating covered cells list");
        }

        getCoveredCells().put(classNode, newCoveredCells);
    }


    public HashMap<ClassNode, ArrayList<GridCell>> getCoveredCells(){
        return this.coveredCells;
    }

    /**
     * Perform the actual grid update.
     * this is called 100 ms after a class node is moved
     * or when a relationship is created or removed,
     * or when a class is created or removed.
     */
    public void performGridUpdate() {
        if (showText) System.out.println("Updating grid...");
        grid.clearGrid();
        //updateGridBoxes();
        Relationship.removeInvalidRelationships();

        lineDrawer.redrawLines(updateRelationshipPaths(0));


        if (visualizer != null){
            visualizer.updateGridVisualizer();
        }
        //grid.printGrid();
    }


    /**
     * sifts through all elements of the container and looks for the ones that are VBoxes,
     * then updates the grid with which ones are occupied
     */
    private void updateGridBoxes(){
        //grid.generateGrid();
        for (Node node : nodeContainer.getChildren()) {
            if (node instanceof ClassNode) {
                System.out.println("class at " +node.getLayoutX() + " , " + + node.getLayoutY());
                updateOccupiedClassBoxCells((ClassNode) node);
            }
        }
        //updatePaths();
    }

    /**
     * helper method for PerformGridUpdate
     * recalculates paths on update
     * @return list of paths
     */
    private RelationshipPathHolder updateRelationshipPaths(int zero){

        //this recursively calls the function again if the path fails, shifting the order of the relationship list


        pathHolder.clearHolder();
        grid.clearGrid();
        updateGridBoxes();

        for (Relationship r : Relationship.relationshipList){
            if (r.getSource() == null || r.getDestination() == null){
                throw new NullPointerException("Invalid relationship");
            }
            GridPath newPath = createPathFromRelationship(r);
            occupyPathCells(newPath);
            pathHolder.addRelationshipPath(r, newPath);

            if (newPath.size() == 0 && zero < Relationship.relationshipList.size()){
                moveToFront(Relationship.relationshipList, r);
                return updateRelationshipPaths(zero+1);
            }
        }
        return pathHolder;
    }

    public <T> void moveToFront(ArrayList<T> list, T element) {
        if (list == null || element == null) return;
        int index = list.indexOf(element);
        if (index != -1) {
            list.remove(index);
            list.add(0, element);
        }
    }

    /**
     * Helper method - creates a path from an existing relationship
     * @param relationship
     * @return null if no path is found, otherwise returns a path
     */
    private GridPath createPathFromRelationship(Relationship relationship){
        Relationship.removeInvalidRelationships();
        pathHolder.addRelationshipHolder(relationship);
        if (!Relationship.relationshipExists(relationship.getSource(),relationship.getDestination())){
            throw new IllegalStateException("relationship doesn't exist");
        }
        return navigatePath((ClassNode) pathHolder.getSourceClassNode(relationship), (ClassNode) pathHolder.getDestinationClassNode(relationship));
    }


    /**
     * occupies all cells under the specified path
     * @param path the path to occupy
     */
    public void occupyPathCells(GridPath path){
        for (GridCell current : path.getCells()){
            if (showText) System.out.println("Updating cell: " + current.toString());
            current.occupied = true;
        }
    }


    /**
     * Navigates the grid from one node to the next.
     * @param start starting node
     * @param goal goal node
     * @return null if no path found, otherwise returns a path
     */
    private GridPath navigatePath(ClassNode start, ClassNode goal) {
        PathNavigator navigator = new PathNavigator(grid);
        GridPath p = navigator.findPathFromCells(coveredCells.get(start), coveredCells.get(goal),
                findCenter(start), findCenter(goal));
        if (p.size() == 0) {
            System.out.println("Empty path between " + start + " and " + goal);
            return p;
        }
        return p;
    }

    /**
     * Finds the center cell of a ClassNode.
     * @param classNode the ClassNode
     * @return the central GridCell
     */
    private GridCell findCenter(ClassNode classNode) {
        double nodeWidth = classNode.getWidth();
        double nodeHeight = classNode.getHeight();
        double nodeX = classNode.getLayoutX();
        double nodeY = classNode.getLayoutY();
        int rowStart = grid.getRow(nodeY) + 1;
        int rowEnd = grid.getRow(nodeY + nodeHeight) + 1;
        int colStart = grid.getCol(nodeX) + 1;
        int colEnd = grid.getCol(nodeX + nodeWidth) + 1;
        int col = colStart + Math.abs((int) ((colStart - colEnd) / 2));
        int row = rowStart + Math.abs((int) ((rowStart - rowEnd) / 2));
        System.out.println("Center found at: col:" + col + ", row: " + row);
        return grid.getCell(row, col);
    }

}
