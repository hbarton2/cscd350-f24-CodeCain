import codecain.GraphicalUserInterface.Controller.Controller;
import codecain.GraphicalUserInterface.Controller.RelationshipLines.GridCell;
import codecain.GraphicalUserInterface.Controller.RelationshipLines.GridManager;
import codecain.GraphicalUserInterface.Controller.RelationshipLines.LineGrid;
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
        GridManager.getInstance().setGrid(new LineGrid(50.0,2000.0,2000.0, new Pane()), new Controller());
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


//    @Test
//    void occupyPath1Test(){
//        GridPath testPath = getTestPath1();
//        gridManager.getUpdater().occupyPathCells(testPath);
//        System.out.println("Test Path 1 ----");
//        assertTrue(gridManager.getGrid().checkOccupied(0,0), "test cell at 0,0 wasn't occupied. occupyPathCells failed");
//        System.out.println("cell from testPath: 0,0 is occupied successfully");
//        assertTrue(gridManager.getGrid().checkOccupied(0,1), "test cell at 0,1 wasn't occupied. occupyPathCells failed");
//        System.out.println("cell from testPath: 0,1 is occupied successfully");
//        assertTrue(gridManager.getGrid().checkOccupied(0,2), "test cell at 0,2 wasn't occupied. occupyPathCells failed");
//        System.out.println("cell from testPath: 0,2 is occupied successfully");
//        assertTrue(gridManager.getGrid().checkOccupied(0,3), "test cell at 0,3 wasn't occupied. occupyPathCells failed");
//        System.out.println("cell from testPath: 0,3 is occupied successfully");
//        assertTrue(gridManager.getGrid().checkOccupied(0,4), "test cell at 0,4 wasn't occupied. occupyPathCells failed");
//        System.out.println("cell from testPath: 0,4 is occupied successfully");
//        System.out.println();
//    }
//
//    private GridPath getTestPath1(){
//        GridPath testPath = new GridPath();
//        testPath.addCell(gridManager.getGrid().getCell(0,0));
//        testPath.addCell(gridManager.getGrid().getCell(0,1));
//        testPath.addCell(gridManager.getGrid().getCell(0,2));
//        testPath.addCell(gridManager.getGrid().getCell(0,3));
//        testPath.addCell(gridManager.getGrid().getCell(0,4));
//        return testPath;
//    }
//
//    @Test
//    void occupyPathTest2(){
//        GridPath testPath = getTestPath2();
//        gridManager.getUpdater().occupyPathCells(testPath);
//        System.out.println("Test Path 2 ----");
//        assertTrue(gridManager.getGrid().checkOccupied(1,0), "test cell at 0,0 wasn't occupied. occupyPathCells failed");
//        System.out.println("cell from testPath: 1,0 is occupied successfully");
//        assertTrue(gridManager.getGrid().checkOccupied(1,1), "test cell at 0,1 wasn't occupied. occupyPathCells failed");
//        System.out.println("cell from testPath: 1,1 is occupied successfully");
//        assertTrue(gridManager.getGrid().checkOccupied(2,1), "test cell at 0,2 wasn't occupied. occupyPathCells failed");
//        System.out.println("cell from testPath: 2,1 is occupied successfully");
//        assertTrue(gridManager.getGrid().checkOccupied(2,2), "test cell at 0,3 wasn't occupied. occupyPathCells failed");
//        System.out.println("cell from testPath: 2,2 is occupied successfully");
//        assertTrue(gridManager.getGrid().checkOccupied(3,2), "test cell at 0,4 wasn't occupied. occupyPathCells failed");
//        System.out.println("cell from testPath: 3,2 is occupied successfully");
//        System.out.println();
//
//    }
//
//    private GridPath getTestPath2(){
//        GridPath testPath = new GridPath();
//        testPath.addCell(gridManager.getGrid().getCell(1,0));
//        testPath.addCell(gridManager.getGrid().getCell(1,1));
//        testPath.addCell(gridManager.getGrid().getCell(2,1));
//        testPath.addCell(gridManager.getGrid().getCell(2,2));
//        testPath.addCell(gridManager.getGrid().getCell(3,2));
//        return testPath;
//    }
//
//    @Test
//    void addPathTest(){
//        GridPath path1 = getTestPath1();
//        GridPath path2 = getTestPath2();
//        gridManager.getGrid().addPath(path1);
//        gridManager.getGrid().addPath(path2);
//        assertFalse(gridManager.getGrid().getPaths().isEmpty(), "addPath failed(paths size = 0)");
//        assertFalse(gridManager.getGrid().getPaths().size() != 2, "addPath failed(paths size = 0)");
//    }
//
//    @Test
//    void testPerformGridUpdate(){
//        GridPath path1 = getTestPath1();
//        gridManager.getGrid().addPath(path1);
//        gridManager.getUpdater().performGridUpdate();
//        assertTrue(gridManager.getGrid().checkOccupied(0,0), "test cell at 0,0 wasn't occupied. occupyPathCells failed");
//        System.out.println("cell from testPath: 0,0 is occupied successfully");
//        assertTrue(gridManager.getGrid().checkOccupied(0,1), "test cell at 0,1 wasn't occupied. occupyPathCells failed");
//        System.out.println("cell from testPath: 0,1 is occupied successfully");
//        assertTrue(gridManager.getGrid().checkOccupied(0,2), "test cell at 0,2 wasn't occupied. occupyPathCells failed");
//        System.out.println("cell from testPath: 0,2 is occupied successfully");
//        assertTrue(gridManager.getGrid().checkOccupied(0,3), "test cell at 0,3 wasn't occupied. occupyPathCells failed");
//        System.out.println("cell from testPath: 0,3 is occupied successfully");
//        assertTrue(gridManager.getGrid().checkOccupied(0,4), "test cell at 0,4 wasn't occupied. occupyPathCells failed");
//        System.out.println("cell from testPath: 0,4 is occupied successfully");
//        System.out.println();
//    }

}
