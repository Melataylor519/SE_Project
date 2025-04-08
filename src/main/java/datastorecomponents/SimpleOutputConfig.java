package datastorecomponents;

public class SimpleOutputConfig implements OutputConfig {
    private final String filePath;

    public SimpleOutputConfig(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String getFilePath() {
        return filePath;
    }

    @Override
    public String formatOutput(String result) {
        return result; // Simple implementation that returns the result as is
    }
}