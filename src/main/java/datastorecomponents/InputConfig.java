package datastorecomponents;

/**
 * Interface for configuring input data sources.
 * Implementations should provide the path to the input file.
 */
public interface InputConfig {
	/**
	 * Gets the file path for the input data.
	 * 
	 * @return The path to the input file
	 */
	String getFilePath();
}
