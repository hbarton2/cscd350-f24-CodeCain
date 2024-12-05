package codecain.GraphicalUserInterface.Model.RelationshipLines;

import codecain.GraphicalUserInterface.View.GridVisualizer;
import codecain.GraphicalUserInterface.View.LineDrawer;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import javax.sound.sampled.Line;


/**
 * singleton class that sets a grid which can be accessed across the entire program.
 * Hopefully this isn't too confusing to use.
 * First, set the grid when first started, and go from there
 */
public class GridManager {

    private static boolean visualizerSet = false;
    private static GridManager instance;

    private LineGrid grid;
    private GridVisualizer visualizer;
    private LineDrawer lineDrawer;
    private GridUpdater updater;
    private GridManager() {}

    /**
     * returns only instance allowed of the grid manager
     * @return instance
     */
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
     * Set the grid for the GridManager instance, also sets lineDrawer
     * @param grid the LineGrid to set
     * @return the provided LineGrid instance
     */
    public LineGrid setGrid(LineGrid grid) {
        if (this.grid != null) {
            throw new IllegalStateException("Grid has already been set!");
        }
        this.grid = grid;
        this.lineDrawer = new LineDrawer(grid);
        this.updater = new GridUpdater(this.grid);
        return grid;
    }

    public void setLineDrawer(){
        if (grid != null && lineDrawer == null){
            this.lineDrawer = new LineDrawer(this.grid);
        }
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
        instance.updater.addClassListeners(classNode);
    }


    /**
     * sets the visualizer
     */
    public static void setVisualizer(){
        if (!visualizerSet) {
            instance.visualizer = new GridVisualizer(instance.grid, instance.grid.getNodeContainer());
            instance.updater.setVisualizer(instance.visualizer);
            visualizerSet = true;
        }
        else return;
    }

    /**
     * getter for the visualizer
     * @return the grid visualizer
     */
    public static GridVisualizer getVisualizer(){
        return instance.visualizer;
    }

    /**
     * prints an ascii representation of the grid
     */
    private static void printGrid() {
        instance.getGrid().printGrid();
    }

    /**
     * getter for the line drawer object
     * @return line drawer from the instance
     */
    public static LineDrawer getLineDrawer(){
        if (instance != null){
            return instance.lineDrawer;
        }
        else{
            throw new RuntimeException("set grid please");
        }
    }
}
