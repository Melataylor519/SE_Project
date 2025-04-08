package datastorecomponents;

/**
 * Simple implementation of InputConfig that provides a file path.
 */
public class SimpleInputConfig implements InputConfig {
    private final String filePath;

    /**
     * Creates a new SimpleInputConfig with the specified file path.
     *
     * @param filePath The path to the input file
     * @throws IllegalArgumentException if filePath is null or empty
     */
    public SimpleInputConfig(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }
        this.filePath = filePath;
    }

    @Override
    public String getFilePath() {
        return filePath;
    }

    @Override
    public String getInputData() {
        return null; // Not used in this implementation
    }
}