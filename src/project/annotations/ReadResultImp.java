package project.annotations;

import java.util.List;

public class ReadResultImp implements ReadResult {
	private final Status status;
	private final Iterable<Integer> results; 
	
	public ReadResultImp(Status status, Iterable<Integer> results) {
		this.status = status;
		this.results = results;
	}
	
	@Override	
	public Status getStatus() {
		return status;
	}	
	
	@Override
	public Iterable<Integer> getResults() {
		return results;
	}

	@Override
	public List<Integer> getData() {
		// TODO Auto-generated method stub
		return null;
	}
}
