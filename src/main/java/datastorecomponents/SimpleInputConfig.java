package datastorecomponents;

public class SimpleInputConfig implements InputConfig {
    private final String filePath;

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
        return null; 
    }
}
