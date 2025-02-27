// This file is to handle reading from and writing to files based on the provided configurations.
package project.annotations;

// FileDataProcessing.java
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileDataProcessing implements DataProcessingAPI {

    @Override
    public ReadResult read(InputConfig input) {
        if (!(input instanceof FileInputConfig)) {
            return new ReadResult(ReadResult.Status.FAILURE, null);
        }

        String filePath = input.getFilePath();
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            return new ReadResult(ReadResult.Status.SUCCESS, lines);
        } catch (IOException e) {
            e.printStackTrace();
            return new ReadResult(ReadResult.Status.FAILURE, null);
        }
    }

    @Override
    public WriteResult appendSingleResult(OutputConfig output, String result, char delimiter) {
        if (!(output instanceof FileOutputConfig)) {
            return new WriteResult(WriteResult.Status.FAILURE);
        }

        String filePath = output.getFilePath();
        try {
            Files.write(Paths.get(filePath), (result + delimiter).getBytes(), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
            return new WriteResult(WriteResult.Status.SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            return new WriteResult(WriteResult.Status.FAILURE);
        }
    }
}
