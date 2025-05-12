package datastorecomponents;

public interface ReadResult {
    // Define the enum inside the interface
    public static enum Status {
        SUCCESS,
        FAILURE;
    }

    // Change the return type to int[]
    public int[] getResults();

    public Status getStatus();
}
