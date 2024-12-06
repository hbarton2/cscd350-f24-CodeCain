package codecain.GraphicalUserInterface.Model.RelationshipLines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;

public class PathNavigator {

    /**
     * the grid to navigate
     */
    private final LineGrid grid;

    /**
     * keeps track of where each node came from
     */
    private HashMap<GridCell, GridCell> cameFrom;

    /**
     * the priority queue, which
     */
    private Queue<GridCell> priorityQueue;

    public PathNavigator(LineGrid grid) {
        this.grid = grid;
        this.cameFrom = new HashMap<>();
    }

    public ArrayList<GridCell> findPath(GridCell start, GridCell end){
        HashMap<GridCell, Integer> gScore = new HashMap<>();
        HashMap<GridCell, Integer> fScore = new HashMap<>();



        return null;
    }

    /**
     * Calculates the Heuristic between two cells
     * this returns the Manhattan distance between the two cells
     * @param current current node
     * @param goal the end goal
     * @return the Manhattan distance
     */
    private int calculateHeuristic(GridCell current, GridCell goal){
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
