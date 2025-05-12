package datastorecomponents;

public class ReadResultImp implements ReadResult {
    private final Status status;
    private final int[] results;

    public ReadResultImp(Status status, int[] results) {
        this.status = status;
        this.results = results;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public int[] getResults() {
        return results;
    }
}
