package datastorecomponents;

public class ReadResult {
    public enum Status {
        SUCCESS, FAILURE
    }

    private final Status status;
    private final int[] results;

    public ReadResult(Status status, int[] results) {
        this.status = status;
        this.results = results;
    }

    public Status getStatus() {
        return status;
    }

    public int[] getResults() {
        return results;
    }
}
