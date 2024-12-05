import codecain.GraphicalUserInterface.Model.RelationshipLines.GridCell;
import codecain.GraphicalUserInterface.Model.RelationshipLines.GridManager;
import codecain.GraphicalUserInterface.Model.RelationshipLines.LineGrid;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AStarTests {

    static GridManager gridManager;

    @BeforeAll
    static void setUp(){
        gridManager = GridManager.getInstance();
        GridManager.getInstance().setGrid(new LineGrid(50.0,2000.0,2000.0, new Pane()));
    }

    @AfterEach
    void tearDown(){
        gridManager.getGrid().clearGrid();
    }

    /**
     * makes sure to test that the getNeighbors method works
     */
    @Test
    void getNeighborsTest(){
        ArrayList<GridCell> neighbors = gridManager.getGrid().getNeighbors(gridManager.getGrid().getCell(20,20));
        GridCell cellNeighbor = gridManager.getGrid().getCell(21,20);
        assertTrue(neighbors.contains(cellNeighbor), "neighbor cell wrong");
        cellNeighbor = gridManager.getGrid().getCell(20,21);
        assertTrue(neighbors.contains(cellNeighbor), "neighbor cell wrong");
        cellNeighbor = gridManager.getGrid().getCell(20,19);
        assertTrue(neighbors.contains(cellNeighbor), "neighbor cell wrong");
        cellNeighbor = gridManager.getGrid().getCell(21,20);
        assertTrue(neighbors.contains(cellNeighbor), "neighbor cell wrong");
        cellNeighbor = gridManager.getGrid().getCell(19,20);
        assertTrue(neighbors.contains(cellNeighbor), "neighbor cell wrong");
        cellNeighbor = gridManager.getGrid().getCell(19,19);
        assertFalse(neighbors.contains(cellNeighbor), "neighbor cell wrong");
        cellNeighbor = gridManager.getGrid().getCell(20,18);
        assertFalse(neighbors.contains(cellNeighbor), "neighbor cell wrong");
    }

    @Test
    void getWalkableNeighborsTest(){
        ArrayList<GridCell> neighbors = gridManager.getGrid().getWalkableNeighbors(gridManager.getGrid().getCell(20,20));
        GridCell cellNeighbor = gridManager.getGrid().getCell(21,20);
        assertTrue(neighbors.contains(cellNeighbor), "neighbor cell wrong");
        cellNeighbor = gridManager.getGrid().getCell(20,21);
        assertTrue(neighbors.contains(cellNeighbor), "neighbor cell wrong");
        cellNeighbor = gridManager.getGrid().getCell(20,19);
        assertTrue(neighbors.contains(cellNeighbor), "neighbor cell wrong");
        cellNeighbor = gridManager.getGrid().getCell(21,20);
        assertTrue(neighbors.contains(cellNeighbor), "neighbor cell wrong");
        cellNeighbor = gridManager.getGrid().getCell(19,20);
        assertTrue(neighbors.contains(cellNeighbor), "neighbor cell wrong");
        cellNeighbor = gridManager.getGrid().getCell(19,19);
        assertFalse(neighbors.contains(cellNeighbor), "neighbor cell wrong");
        cellNeighbor = gridManager.getGrid().getCell(20,18);
        assertFalse(neighbors.contains(cellNeighbor), "neighbor cell wrong");
    }

    @Test
    void getWalkableNeighborsTest1(){
        gridManager.getGrid().getCell(21,20).setOccupied(true);
        ArrayList<GridCell> neighbors = gridManager.getGrid().getWalkableNeighbors(gridManager.getGrid().getCell(20,20));
        GridCell cellNeighbor = gridManager.getGrid().getCell(21,20);
        assertFalse(neighbors.contains(cellNeighbor), "neighbor cell not walkable");
        cellNeighbor = gridManager.getGrid().getCell(19,20);
        assertTrue(neighbors.contains(cellNeighbor));
    }


}
