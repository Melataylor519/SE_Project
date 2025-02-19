package project.annotations;

/**
 * Implementation of DataProcessingAPI interface.
 */
public class DataProcessingImp implements DataProcessingAPI {
	private final DataProcessingAPI dataProcessAPI;
	// Field to store API instance 
	
	/**
	 * Constructor for DataProcessingImp.
	 * @param dataProcessAPI The API instance to be used
	 */
	public DataProcessingImp(DataProcessingAPI dataProcessAPI) {
		this.dataProcessAPI = dataProcessAPI;
	}
	
	@Override
	public ReadResult read(InputConfig input) {
		return new ReadResultImp(ReadResult.Status.FAILURE, null);
	}

	@Override
	public WriteResult appendSingleResult(OutputConfig output, String result, char delimiter) {
		return new WriteResultImp(WriteResult.WriteResultStatus.FAILURE);
	}
} 

/**
 * Implementation of ReadResult Interface.
 */
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

/**
 * Implementation of WriteResult Interface.
 */
class WriteResultImp implements WriteResult {
	private final WriteResultStatus status; 
	
	/**
	 * Constructor for WriteResultImp.
	 * @param status the status of the write operation
	 */
	public WriteResultImp(WriteResultStatus status) {
		this.status = status;
	}
	
	@Override
	public WriteResultStatus getStatus() {
		return status;
	}
}

