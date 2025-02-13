//Data Processing Prototype 
package project.annotations;

/**
 * Prototype implementation of the DataProcessingAPI interface.
 * Handles reading and writing data across different storage types.
 */
public class DataProcessingPrototype implements DataProcessingAPI {

	/**
	 * Reads data from the specified source.
	 *
	 * @param sourceType the type of storage to read from
	 * @param inputSource the source location identifier
	 * @return ReadDataResponse containing the read data
	 */
	@Override
	public ReadDataResponse readData(StorageType sourceType, String inputSource) {
		// If sourceType = FILE_SYSTEM, then inputSource = " /data.input.txt"
		// If sourceType = DATABASE, then inputSource = "user_table"
		String data = "Data from " + sourceType + " at " + inputSource;
		return new ReadDataResponse(data);
	}

	/**
	 * Writes data to the specified target.
	 *
	 * @param targetType the type of storage to write to
	 * @param outputSource the target location identifier
	 * @param request the data processing request containing the data to write
	 */
	@Override
	public void writeData(StorageType targetType, String outputSource, DataProcessingRequest request) {
		if (request instanceof WriteDataRequest) {
			WriteDataRequest writeRequest = (WriteDataRequest) request;
			System.out.println("Writing data to " + targetType + " at " + outputSource + ": " + writeRequest.getData());
		}
	}
}
