import java.io.*;

public class FileBasedOutputConfig implements OutputConfig {
    private final String outputFilePath;

    public FileBasedOutputConfig(String outputFilePath) {
        this.outputFilePath = outputFilePath;
    }

    @Override
    public String getUserId() {
        return outputFilePath;
    }

    public void write(String data, char delimiter) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath, true))) {
            writer.write(data);
            writer.write(delimiter);
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to output file: " + outputFilePath, e);
        }
    }
}