package datastorecomponents;

/**
 * Interface for configuring output data destinations.
 * Implementations should provide the path to the output file
 * and handle formatting of the output data.
 */
public interface OutputConfig {
	/**
	 * Gets the file path for the output data.
	 * 
	 * @return The path to the output file
	 */
	String getFilePath();

	/**
	 * Formats the output data according to the implementation's requirements.
	 * 
	 * @param result The data to be formatted
	 * @return The formatted data
	 */
	String formatOutput(String result);
}
