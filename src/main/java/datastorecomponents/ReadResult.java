package main.java.datastorecomponents;

public interface ReadResult {
	public static enum Status {
	    SUCCESS,
	    FAILURE;
	}

	public Iterable<Integer> getResults();
	    
	public Status getStatus();

}
