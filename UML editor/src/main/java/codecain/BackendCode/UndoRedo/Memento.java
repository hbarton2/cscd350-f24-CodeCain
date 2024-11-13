package codecain.BackendCode.UndoRedo;
public class Memento {
    private final String state;  // Change type based on what state is stored.

    public Memento(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}
