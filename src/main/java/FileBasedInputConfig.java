import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class FileBasedInputConfig implements InputConfig {
    private final List<Integer> values;

    public FileBasedInputConfig(String filePath) {
        try {
            String content = Files.readString(Path.of(filePath));
            this.values = Arrays.stream(content.split(","))
                                 .map(String::trim)
                                 .map(Integer::parseInt)
                                 .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input file: " + filePath, e);
        }
    }

    @Override
    public int getValue() {
        // Only return the first value for single-job compute
        return values.isEmpty() ? 0 : values.get(0);
    }

    public List<Integer> getAllValues() {
        return values;
    }
}