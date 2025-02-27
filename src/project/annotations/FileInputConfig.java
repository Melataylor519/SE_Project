public class FileInputConfig implements InputConfig {
    private final String filePath;

    public FileInputConfig(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String getFilePath() {
        return filePath;
    }
}
