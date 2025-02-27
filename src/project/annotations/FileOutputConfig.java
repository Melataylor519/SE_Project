package project.annotations;

public class FileOutputConfig implements OutputConfig {
    private final String filePath;

    public FileOutputConfig(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String getFilePath() {
        return filePath;
    }

    
    public String formatOutput(String input) {
        return input;
    }
}
