package codecain.GraphicalUserInterface.Model.RelationshipLines;

import java.util.*;

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
    public GridPath findPath(GridCell start, GridCell goal) {
        // fScore is the total estimated cost, gScore is the actual cost from start to current
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
            System.out.println("node added at " + current.getCol() + ", " + current.getRow());


            // Step 2: If the current node is the goal, reconstruct the path
            if (current == goal || grid.getNeighbors(current).contains(goal)) {
                GridPath g = new GridPath(reconstructPath(current));
                System.out.println(g.toString());
                grid.addPath(g);
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
        System.out.println("no path found");

        // Return an empty path if no path was found
        return new GridPath();
    }

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
    public int calculateHeuristic(GridCell current, GridCell goal){
        return Math.abs(current.col - goal.col) + Math.abs(current.row - goal.row);
    }


    /**
     * returns the cost to move from one cell to the neighboring cell
     * @param neighbor
     * @return
     */
    private int calculateMovementCost(GridCell neighbor){
        return (int) neighbor.cost;
    }

    /**
     * validates the movement from the current node to it's neighbor
     * @param current
     * @param neighbor
     * @return
     */
    private boolean checkHeuristicConsistency(GridCell current, GridCell neighbor, GridCell goal){
        return calculateHeuristic(current, goal) <= calculateHeuristic(neighbor, goal) + calculateMovementCost(neighbor);
    }

    /**
     *
     * @param actualCost the total number of steps from the starting cell to the
     *                   cell in question
     * @param estimatedCost the heuristic from the current node to the ending goal
     * @return the total cost f(n) = g(n) + h(n)
     */
    private int calculateTotalCost(int actualCost, int estimatedCost){
        return actualCost + estimatedCost;
    }




}
