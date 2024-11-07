package codecain;

public class Result {
    private final Status status;
    private final String message;

    public Result(Status status, String message) {
        this.status = status;
        this.message = message;
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return status + ": " + message;
    }
}
