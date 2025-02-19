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


