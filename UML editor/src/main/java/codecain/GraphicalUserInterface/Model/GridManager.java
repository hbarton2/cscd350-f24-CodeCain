package codecain.GraphicalUserInterface.Model;

import javafx.scene.layout.VBox;

public class GridManager {

    private LineGrid grid;

    private static GridManager instance;

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

        instance.getGrid().updateGrid();

        classNode.layoutXProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Class node moved. New X: " + newValue);
            instance.getGrid().updateGrid();
            printGrid();
        });

        classNode.layoutYProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Class node moved. New Y: " + newValue);
            instance.getGrid().updateGrid();
            printGrid();

        });

        // Listener for size changes (Width and Height)
        classNode.prefWidthProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Class node width changed. New Width: " + newValue);
            instance.getGrid().updateGrid();
            printGrid();

        });

        classNode.prefHeightProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Class node height changed. New Height: " + newValue);
            instance.getGrid().updateGrid();
            printGrid();
        });
    }

    private static void printGrid(){
        instance.getGrid().printGrid();
    }
}
