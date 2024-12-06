package codecain.GraphicalUserInterface.Model.RelationshipLines;

import java.util.*;

public class GridPriorityQueue {

    private static class PriorityQueueEntry{
        GridCell cell;

        int totalCost;

        public PriorityQueueEntry(GridCell cell, int totalCost) {
            this.cell = cell;
            this.totalCost = totalCost;
        }


        public GridCell getCell (){
            return cell;
        }
    }
    private PriorityQueue<PriorityQueueEntry> queue;
    private HashMap<GridCell, PriorityQueueEntry> entryMap;

    public GridPriorityQueue(){
        this.queue = new PriorityQueue<>(Comparator.comparingInt(PriorityQueueEntry -> PriorityQueueEntry.totalCost));
        entryMap = new HashMap<>();
    }


    public void push(GridCell cell, int totalCost) {
        if (entryMap.containsKey(cell)){
            PriorityQueueEntry existingEntry = entryMap.get(cell);
            if (totalCost < existingEntry.totalCost){
                removeEntry(existingEntry);
                PriorityQueueEntry newEntry = new PriorityQueueEntry(cell, totalCost);
                queue.add(newEntry);
                entryMap.put(cell, newEntry);
            }
        }
        else{
            PriorityQueueEntry newEntry = new PriorityQueueEntry(cell, totalCost);
            queue.add(newEntry);
            entryMap.put(cell, newEntry);
        }
    }

    private void removeEntry(PriorityQueueEntry entry){
        queue.remove(entry);
        entryMap.remove(entry.cell);
    }


    /**
     * removes the cell from the top of the queue, and returns it
     * @return the cell from the top of the queue
     */
    public GridCell pop() {
        while (!queue.isEmpty()){
            PriorityQueueEntry currentEntry = queue.poll();
            if (entryMap.containsKey(currentEntry.cell) && entryMap.get(currentEntry.cell) == currentEntry){
                entryMap.remove(currentEntry.cell);
                return currentEntry.cell;
            }
        }
        throw new NoSuchElementException("Queue is empty");
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }



}
