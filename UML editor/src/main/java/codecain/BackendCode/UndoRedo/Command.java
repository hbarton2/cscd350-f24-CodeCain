package codecain.BackendCode.UndoRedo;

public interface Command {
    String getName();
    void execute();
}
