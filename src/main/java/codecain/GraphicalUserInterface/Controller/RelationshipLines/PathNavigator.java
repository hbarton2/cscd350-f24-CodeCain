package codecain.GraphicalUserInterface.Controller.RelationshipLines;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * generates a path around occupied nodes
 */
public class PathNavigator {

    /**
     * the grid to navigate
     */
    private final LineGrid grid;

    /**
     * keeps track of where each node came from
     */
    private HashMap<GridCell, GridCell> cameFrom;

    public PathNavigator(LineGrid grid) {
        this.grid = grid;
        this.cameFrom = new HashMap<>();
    }



    /**
     * calculates a path from start cell to goal cell
     * @param start the cell to start from
     * @param goal the cell to reach
     * @return list of ordered grid cells to traverse the path
     */
    private GridPath findPath(GridCell start, GridCell goal) {
        // fScore is the total estimated cost, gScore is the actual cost from start to current (number of steps from start to current)
        HashMap<GridCell, Integer> gScore = new HashMap<>();
        HashMap<GridCell, Integer> fScore = new HashMap<>();
        cameFrom = new HashMap<>(); // To reconstruct the path

        GridPriorityQueue open = new GridPriorityQueue();
        Set<GridCell> closedList = new HashSet<>();

        // Initialize start node
        gScore.put(start, 0); // Cost from start to start is 0
        fScore.put(start, calculateHeuristic(start, goal)); // f = g + h for start node
        open.push(start, fScore.get(start));

        while (!open.isEmpty()) {
            // Step 1: Get the node with the lowest f score
            GridCell current = open.pop();
            //System.out.println("node added at " + current.getCol() + ", " + current.getRow());


            // Step 2: If the current node is the goal, reconstruct the path
            if (current == goal || grid.getNeighbors(current).contains(goal)) {
                GridPath g = new GridPath(reconstructPath(current));
                g.setEndPoints(start,goal);
                System.out.println(g.toString());
                //grid.addPath(g);
                return g;
            }


            // Step 3: Move the current node to the closed list
            closedList.add(current);

            // Step 4: Process each neighbor
            for (GridCell neighbor : grid.getWalkableNeighbors(current)) {
                if (closedList.contains(neighbor)) {
                    continue; // Skip if already processed
                }

                // Step 5: Calculate the tentative g score for the neighbor
                int tentativeG = gScore.get(current) + 1; // Assuming uniform cost (1 step)

                // If the neighbor isn't in the open list or we found a better path to it
                if (!gScore.containsKey(neighbor) || tentativeG < gScore.get(neighbor)) {
                    // Update the gScore, fScore, and cameFrom
                    gScore.put(neighbor, tentativeG);
                    fScore.put(neighbor, tentativeG + calculateHeuristic(neighbor, goal));
                    cameFrom.put(neighbor, current);

                    // If the neighbor isn't in the open list, add it
                    if (!open.contains(neighbor)) {
                        open.push(neighbor, fScore.get(neighbor));
                    }
                }
            }
        }
        //System.out.println("no path found");

        // Return an empty path if no path was found
        return new GridPath();
    }


    /**
     * finds a path from one node to the other
     * @param coveredCellsStart
     * @param coveredCellsGoal
     * @param startCenter
     * @param goalCenter
     * @return
     */
    public GridPath findPathFromCells(ArrayList<GridCell> coveredCellsStart,
                                      ArrayList<GridCell> coveredCellsGoal, GridCell startCenter, GridCell goalCenter){
        boolean printStuff = true;
        GridPriorityQueue availableStartCells;
        GridPriorityQueue availableGoalCells;

        //add all start cells to a priority queue ordered by their cost
        availableStartCells = loadAvailableStartingCells(coveredCellsStart,goalCenter);

        //findPath until one works
        GridPath path = null;
        GridPath bestPath = null;
        int bestPathSize = 1000;

        while (!availableStartCells.isEmpty()) {
            //checks every start cell and end cell
            GridCell start = availableStartCells.pop();
            availableGoalCells = loadAvailableGoalCells(coveredCellsGoal,start);
            while(!availableGoalCells.isEmpty()){
                path = findPath(start, availableGoalCells.pop());
                if (path != null) {
                    return path;
                }
            }
        }

        //returns empty path if no path was found

        return new GridPath();
    }

    /**
     * helper method for findPathFromCells
     * finds all available starting cells and adds them to a GridPriorityQueue based on their
     * heuristic cost (estimated distance)
     * @param coveredCellsStart arralylist with all the cells covered by the ClassNode
     * @param goalCenter the center cell of the destination node
     * @return priority queue of all available grid cells
     */
    private GridPriorityQueue loadAvailableStartingCells(ArrayList<GridCell> coveredCellsStart, GridCell goalCenter){
        boolean printStuff = true;

        GridPriorityQueue availableStartCells = new GridPriorityQueue();

        for ( GridCell cell: coveredCellsStart){
            if (!grid.getWalkableNeighbors(cell).isEmpty()){
                availableStartCells.push(cell, calculateHeuristic(cell,goalCenter));
                if (printStuff == true) System.out.println("available start cell found: " + cell.toString());
            }
        }

        //if (printStuff) System.out.println(availableStartCells.toString());

        return availableStartCells;
    }

    /**
     * helper method for findPathFromCells
     * finds all available starting cells and adds them to a GridPriorityQueue based on their
     * heuristic cost (estimated distance)
     * @param coveredCellsGoal arralylist with all the cells covered by the ClassNode
     * @param startingCell the center cell of the destination node
     * @return priority queue of all available grid cells
     */
    private GridPriorityQueue loadAvailableGoalCells(ArrayList<GridCell> coveredCellsGoal, GridCell startingCell){
        boolean printStuff = true;

        GridPriorityQueue availableStartCells = new GridPriorityQueue();

        for ( GridCell cell: coveredCellsGoal){
            if (!grid.getWalkableNeighbors(cell).isEmpty()){
                availableStartCells.push(cell, calculateHeuristic(cell,startingCell));
                //if (printStuff == true) System.out.println("available start cell found: " + cell.toString());
            }
        }

        //if (printStuff) System.out.println(availableStartCells.toString());

        return availableStartCells;
    }


    /**
     * reconstructs the path from the goal cell
     * @param goal the goal of the path to reconstruct from
     * @return a new path reconstructed from the cameFrom HashMap
     */
    private ArrayList<GridCell> reconstructPath(GridCell goal) {
        ArrayList<GridCell> path = new ArrayList<>();
        GridCell node = goal;

        while (cameFrom.containsKey(node)) {
            path.add(node);
            node = cameFrom.get(node);
        }

        Collections.reverse(path); // Reverse to get the path from start to goal
        return path;
    }


    /**
     * Calculates the Heuristic between two cells
     * this returns the distance between the two cells
     * @param current current node
     * @param goal the end goal
     * @return the distance between current and goal
     */
    private int calculateHeuristic(GridCell current, GridCell goal) {
        int dx = Math.abs(current.getCol() - goal.getCol());
        int dy = Math.abs(current.getRow() - goal.getRow());
        int heuristic = dx + dy; // Manhattan distance

        // Add a penalty for direction changes
        if (cameFrom.containsKey(current)) {
            GridCell previous = cameFrom.get(current);
            int currentDirection = calculateDirection(previous, current);
            int goalDirection = calculateDirection(current, goal);
            if (currentDirection != goalDirection) {
                if (currentDirection >= 4 && currentDirection <= 7) {
                    heuristic += 20; // Higher penalty for diagonal direction changes
                } else {
                    heuristic += 10; // Lower penalty for non-diagonal direction changes
                }
            }
        }

        return heuristic;
    }

     /**
     * Calculates the direction between two cells.
     * 
     * @param from the starting cell
     * @param to the destination cell
     * @return an integer representing the direction:
     *         0 - Down, 1 - Up, 2 - Right, 3 - Left,
     *         4 - Down-Right, 5 - Up-Right, 6 - Down-Left, 7 - Up-Left,
     *         -1 - Same cell
     */
    private int calculateDirection(GridCell from, GridCell to) {
        int dx = to.getCol() - from.getCol();
        int dy = to.getRow() - from.getRow();
        if (dx == 0 && dy > 0) return 0; // Down
        if (dx == 0 && dy < 0) return 1; // Up
        if (dx > 0 && dy == 0) return 2; // Right
        if (dx < 0 && dy == 0) return 3; // Left
        if (dx > 0 && dy > 0) return 4; // Down-Right
        if (dx > 0 && dy < 0) return 5; // Up-Right
        if (dx < 0 && dy > 0) return 6; // Down-Left
        if (dx < 0 && dy < 0) return 7; // Up-Left
        return -1; // Same cell
    }

}
