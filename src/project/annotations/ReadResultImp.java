package project.annotations;

import java.util.List;

class ReadResultImp implements ReadResult {
	private Status status;
	private Iterable<Integer> results;
	private List<String> processedData;
		
	/**
	 * Constructor for ReadResultImp.
	 * @param status the status of the read operation
	 * @param results the results from the read operation
	 */
	public ReadResultImp(Status status, Iterable<Integer> results,List<String> processedData) {
		this.status = status;
		this.results = results;
		this.processedData = processedData;
	}
	
	public ReadResultImp(List<String> processedData) {
        this.processedData = processedData;
    }
	
	@Override	
	public Status getStatus() {
		return status;
	}	
	
	@Override
	public Iterable<Integer> getResults() {
		return results;
	}
	
	public List<String> getProcessedData() {
	    return processedData;
	}
	
}
