package datastorecomponents;

public class SimpleInputConfig implements InputConfig {
    private final String filePath;

    public SimpleInputConfig(String filePath) {
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