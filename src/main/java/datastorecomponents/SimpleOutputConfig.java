package datastorecomponents;

/**
 * Simple implementation of OutputConfig that provides a file path
 * and basic output formatting.
 */
public class SimpleOutputConfig implements OutputConfig {
    private final String filePath;

    /**
     * Creates a new SimpleOutputConfig with the specified file path.
     *
     * @param filePath The path to the output file
     * @throws IllegalArgumentException if filePath is null or empty
     */
    public SimpleOutputConfig(String filePath) {
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
    public String formatOutput(String result) {
        if (result == null) {
            return "";
        }
        return result.trim();
    }
}