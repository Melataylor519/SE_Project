package project.annotation;

class ReadResultImp implements ReadResult {
	private final Status status;
	private final Iterable<Integer> results; 
		
	/**
	 * Constructor for ReadResultImp.
	 * @param status the status of the read operation
	 * @param results the results from the read operation
	 */
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
}
