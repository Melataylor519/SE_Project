// This file is to handle reading from and writing to files based on the provided configurations.
package project.annotations;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;

public class FileDataProcessing implements DataProcessingAPI {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public ReadResult read(InputConfig input) {
        if (!(input instanceof FileInputConfig)) {
            return new ReadResultImp(ReadResult.Status.FAILURE, null);
        }

        String filePath = input.getFilePath();
        File file = new File(filePath);

        // Check file permissions
        if (!file.canRead()) {
            System.err.println("Read permission denied for file: " + filePath);
            return new ReadResultImp(ReadResult.Status.FAILURE, null);
        }
        try {
            // Determine file type by extension
            if (filePath.endsWith(".json")) {
                // Read json file
                List<Integer> data = objectMapper.readValue(file, List.class);
                return new ReadResultImp(ReadResult.Status.SUCCESS, data);
            } else {
                // Read text file
                List<String> lines = Files.readAllLines(Paths.get(filePath));
                List<Integer> data = lines.stream()
                                          .map(Integer::parseInt)
                                          .toList();
                return new ReadResultImp(ReadResult.Status.SUCCESS, data);
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            return new ReadResultImp(ReadResult.Status.FAILURE, null);
        }
    }

    @Override
    public WriteResult appendSingleResult(OutputConfig output, String result, char delimiter) {
        if (!(output instanceof FileOutputConfig)) {
            return new WriteResultImp(WriteResult.WriteResultStatus.FAILURE);
        }

        String filePath = output.getFilePath();
        File file = new File(filePath);

        // Check file permissions
        if (file.exists() && !file.canWrite()) {
            System.err.println("Write permission denied for file: " + filePath);
            return new WriteResultImp(WriteResult.WriteResultStatus.FAILURE);
        }
        try {
            // Determine file type by extension
            if (filePath.endsWith(".json")) {
                // Read existing JSON data
                List<Integer> data;
                if (file.exists()) {
                    data = objectMapper.readValue(file, List.class);
                } else {
                    data = new ArrayList<>();
                }
                // Append new result
                data.add(Integer.parseInt(result));
                // Write new data back to json file
                objectMapper.writeValue(file, data);
            } else {
                // Append result to text file
                Files.write(Paths.get(filePath), (result + delimiter).getBytes(), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
            }
            return new WriteResultImp(WriteResult.WriteResultStatus.SUCCESS);
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            return new WriteResultImp(WriteResult.WriteResultStatus.FAILURE);
        }
    }
}
