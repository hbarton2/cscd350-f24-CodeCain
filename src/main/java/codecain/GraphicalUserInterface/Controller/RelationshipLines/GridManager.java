package codecain.GraphicalUserInterface.Controller.RelationshipLines;

import codecain.GraphicalUserInterface.Controller.Controller;
import codecain.GraphicalUserInterface.View.ClassNode;
import codecain.GraphicalUserInterface.View.GridVisualizer;
import codecain.GraphicalUserInterface.View.LineDrawer;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 * singleton class that sets a grid which can be accessed across the entire program.
 * Hopefully this isn't too confusing to use.
 * First, set the grid when first started, and go from there
 *
 * I think the grid should be controlled from here, not sure though
 */
public class GridManager {

    private static boolean visualizerSet = false;
    private static GridManager instance;

    private LineGrid grid;
    private GridVisualizer visualizer;
    private LineDrawer lineDrawer;
    private GridUpdater updater;
    private PathNavigator pathNavigator;
    private RelationshipPathHolder holder;


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

    public static void clearGridManager(){
        instance = new GridManager();
    }

    /**
     * Set the grid for the GridManager instance, also sets lineDrawer
     * @param grid the LineGrid to set
     * @return the provided LineGrid instance
     */
    public LineGrid setGrid(LineGrid grid, Controller controller) {
        if (this.grid != null) {
            throw new IllegalStateException("Grid has already been set!");
        }
        this.grid = grid;
        this.lineDrawer = new LineDrawer(grid);
        this.pathNavigator = new PathNavigator(this.grid);
        this.holder = new RelationshipPathHolder(controller);
        this.updater = new GridUpdater(this.grid,holder, this.lineDrawer);

        return grid;
    }

    public void resetGrid() {
        this.grid = null;
        this.visualizer = null;
        this.lineDrawer = null;
        this.updater = null;
        this.pathNavigator = null;
        this.holder = null;
    }

    private void checkGrid(){
        if (grid == null) {
            throw new IllegalStateException("Grid has not been set!");
        }
    }

    /**
     * setter for the LineDrawer object
     */
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
        checkGrid();
        return grid;
    }

    /**
     * Adds listeners to a ClassNode node for position and size changes.
     * When any changes occur, the grid is updated accordingly.
     * @param classNode the ClassNode to add listeners to
     */
    public static void addClassListeners(ClassNode classNode) {
        instance.checkGrid();
        instance.updater.addClassListeners(classNode);
    }

    public static void loadGridManager(LineGrid grid, Controller controller, Pane container){
        instance = new GridManager();
        instance.setGrid(grid,controller);
        for ( Node n :container.getChildren()){
            if (n instanceof ClassNode){
                addClassListeners((ClassNode) n);
            }
        }
        updateRelationshipPaths();
    }


    /**
     * sets the visualizer
     */
    public static void setVisualizer(){
        instance.checkGrid();
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

//    /**
//     * prints an ascii representation of the grid
//     */
//    private static void printGrid() {
//        instance.checkGrid();
//        instance.getGrid().printGrid();
//    }

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

    public static void updateRelationshipPaths(){
        instance.getUpdater().performGridUpdate();
    }


    public GridUpdater getUpdater(){
        return this.updater;
    }
}
