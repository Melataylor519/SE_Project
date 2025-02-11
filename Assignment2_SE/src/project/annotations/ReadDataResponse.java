package project.annotations;

/**
 * Response class for reading data that implements DataProcessingResponse interface.
 */
public class ReadDataResponse implements DataProcessingResponse {
	private final String data;
	
	/**
	 * Constructs a ReadDataResponse with the specified data.
	 * @param data The data to be stored
	 */
	public ReadDataResponse(String data) {
		this.data = data;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getData() {
		return data;
	}
}

