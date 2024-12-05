package codecain.GraphicalUserInterface.Model.RelationshipLines;

import codecain.GraphicalUserInterface.View.GridVisualizer;
import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class GridUpdater {

    /**
     * the grid
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
    public void updateGrid(){
        grid.generateGrid();
        for (Node node : nodeContainer.getChildren()) {
            if (node instanceof VBox) {
                System.out.println("class at " +node.getLayoutX() + " , " + + node.getLayoutY());
                grid.updateOccupiedCells((VBox) node);
            }
        }
    }

    /**
     * schedules a grid update whenever the classes are moved or modified
     */
    private void scheduleGridUpdate() {
        if (!updateScheduled) {
            updateScheduled = true;
            gridUpdateTimer.start();
        }
    }

    /**
     * Perform the actual grid update.
     */
    private void performGridUpdate() {
        System.out.println("Updating grid...");
        grid.updateGrid();
        if (visualizer != null){
            visualizer.updateGridVisualizer();
        }
        grid.printGrid();
    }

}
