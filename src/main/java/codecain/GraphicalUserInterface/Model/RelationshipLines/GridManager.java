package codecain.GraphicalUserInterface.Model.RelationshipLines;

import codecain.GraphicalUserInterface.View.GridVisualizer;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;


/**
 * singleton class that sets a grid which can be accessed across the entire program.
 * Hopefully this isn't too confusing to use.
 * First, set the grid when first started, and go from there
 */
public class GridManager {

    private LineGrid grid;
    private static boolean visualizerSet = false;
    private static GridManager instance;
    private boolean updateScheduled = false;
    private long lastUpdateTime = 0;
    private final long updateInterval = 100_000_000;
    GridVisualizer visualizer;


    private GridManager() {}



    public static GridManager getInstance() {
        if (instance == null) {
            synchronized (GridManager.class) {
                if (instance == null) {
                    instance = new GridManager();
                }
            }
        }
        return instance;
    }

    /**
     * Set the grid for the GridManager instance.
     * @param grid the LineGrid to set
     * @return the provided LineGrid instance
     */
    public LineGrid setGrid(LineGrid grid) {
        if (this.grid != null) {
            throw new IllegalStateException("Grid has already been set!");
        }
        this.grid = grid;
        return grid;
    }

    /**
     * Get the grid managed by GridManager.
     * @return the LineGrid instance
     * @throws IllegalStateException if the grid is not set
     */
    public LineGrid getGrid() {
        if (grid == null) {
            throw new IllegalStateException("Grid has not been set!");
        }
        return grid;
    }

    /**
     * Adds listeners to a VBox node for position and size changes.
     * When any changes occur, the grid is updated accordingly.
     * @param classNode the VBox to add listeners to
     */
    public static void addClassListeners(VBox classNode) {
        if (classNode == null) return;

        classNode.layoutXProperty().addListener((observable, oldValue, newValue) -> instance.scheduleGridUpdate());
        classNode.layoutYProperty().addListener((observable, oldValue, newValue) -> instance.scheduleGridUpdate());
        classNode.prefWidthProperty().addListener((observable, oldValue, newValue) -> instance.scheduleGridUpdate());
        classNode.prefHeightProperty().addListener((observable, oldValue, newValue) -> instance.scheduleGridUpdate());
    }

    /**
     * Schedules a grid update if it hasn't been scheduled recently.
     */
    private void scheduleGridUpdate() {
        if (!updateScheduled) {
            updateScheduled = true;

            // Use an AnimationTimer to debounce updates
            new AnimationTimer() {
                @Override
                public void handle(long now) {
                    if (now - lastUpdateTime >= updateInterval) {
                        performGridUpdate();
                        lastUpdateTime = now;
                        updateScheduled = false;
                        stop();
                    }
                }
            }.start();
        }
    }

    /**
     * Perform the actual grid update.
     */
    private void performGridUpdate() {
        System.out.println("Updating grid...");
        getGrid().updateGrid();
        if (visualizer != null){
            visualizer.updateGridVisualizer();
        }
        printGrid();
    }

    public static void setVisualizer(){
        if (!visualizerSet) {
            instance.visualizer = new GridVisualizer(instance.grid, instance.grid.getNodeContainer());
            visualizerSet = true;
        }
        else return;
    }

    public static GridVisualizer getVisualizer(){
        return instance.visualizer;
    }

    private static void printGrid() {
        instance.getGrid().printGrid();
    }
}