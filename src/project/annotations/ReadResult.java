package project.annotations;

import java.util.List;

public interface ReadResult {
	public static enum Status {
	    SUCCESS,
	    FAILURE;
	}

	public Iterable<Integer> getResults();
	    
	public Status getStatus();

	public List<Integer> getData();


}
