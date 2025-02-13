package project.annotations;

/**
 * Interface defining data processing operations for reading and writing data.
 */
public interface DataProcessingAPI {
	
	/**
	 * Reads data from the specified input source.
	 *
	 * @param sourceType the type of storage to read from
	 * @param inputSource the source location to read from
	 * @return DataProcessingResponse containing the read data
	 */
	@ProcessAPIPrototype(description = "Read data from the specified input source: ")
	// Since the method returns data, so I wrap it inside a DataProcessingResponse 
	DataProcessingResponse readData(StorageType sourceType, String inputSource); 
	
	/**
	 * Writes data to the specified output source.
	 *
	 * @param targetType the type of storage to write to
	 * @param outputSource the target location to write to
	 * @param request the data processing request containing data to write
	 */
	@ProcessAPIPrototype(description = "Write data to the specified output source: ")
	void writeData(StorageType targetType, String outputSource, DataProcessingRequest request);
	
}

