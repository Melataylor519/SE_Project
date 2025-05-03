public class DataStoreReadResult {
    public enum Status { SUCCESS, FAILURE; }
    private final Status status;
    private final Iterable<Integer> results;

    public DataStoreReadResult(Status status, Iterable<Integer> results) {
        this.status = status;
        this.results = results;
    }

    public Status getStatus() {
        return status;
    }

    public Iterable<Integer> getResults() {
        return results;
    }
}
