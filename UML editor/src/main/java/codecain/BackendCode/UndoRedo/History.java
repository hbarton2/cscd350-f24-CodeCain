package codecain.BackendCode.UndoRedo;

import java.util.*;
import codecain.BackendCode.UndoRedo.Command;

public class History {
    private List<Pair> history = new ArrayList<Pair>();
    private int size = 0;

    private class Pair {
        Memento memento;
        Command command;

        public Pair(Memento m, Command c) {
            memento = m;
            command = c;
        }
        private Command getCommand() {
            return command;
        }
        private Memento getMemento() {
            return memento;
        }
    }

    public void Push(Command c, Memento m) {
        if (size != history.size() && size > 0) {
            history = history.subList(0, size - 1);
        }
        history.add(new Pair(m, c));
        size = history.size();
    }

    public boolean redo(){
        Pair pair = getRedo();
        if (pair == null) {
            return false;
        }
        System.out.println("Redoing: " + pair.getCommand().getName());
        pair.getMemento().restore();
        pair.getCommand().execute();
        return true;
    }

    private Pair getRedo(){
        if (size == history.size()) {
            return null;
        }
        size = Math.min(size, history.size() + 1);
        return history.get(size - 1);
    }

    public boolean undo(){
        Pair pair = getUndo();
        if (pair == null) {
            return false;
        }
        System.out.println("Undoing: " + pair.getCommand().getName());
        pair.getMemento().restore();
        return true;
    }

    private Pair getUndo(){
        if (size == history.size()) {
            return null;
        }
        size = Math.min(size, history.size() - 1);
        return history.get(size);
    }

}
