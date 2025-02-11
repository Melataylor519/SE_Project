package project.annotations;

/**
 * Represents a request to write data as part of data processing operations.
 * Implements the DataProcessingRequest interface.
 */
public class WriteDataRequest implements DataProcessingRequest {
	/** The data to be written. */
	private String data;
	
	/**
	 * Constructs a new WriteDataRequest with the specified data.
	 *
	 * @param data The data to be written
	 */
	public WriteDataRequest(String data) {
		this.data = data;
	}
	
	/**
	 * Returns the data to be written.
	 *
	 * @return The data string
	 */
	public String getData() {
		return data;
	}
}
