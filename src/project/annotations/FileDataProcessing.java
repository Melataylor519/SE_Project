// This file is to handle reading from and writing to files based on the provided configurations.
package project.annotations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import java.nio.file.StandardOpenOption;
import java.util.stream.Collectors;

public class FileDataProcessing implements DataProcessingAPI {

    @Override
    public ReadResult read(InputConfig input) {
        if (!(input instanceof FileInputConfig)) {
            return new ReadResultImp(ReadResult.Status.FAILURE, null);
        }

        String filePath = input.getFilePath();
        try {
            // Read all lines from the file as strings.
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            // Convert each line (String) to an Integer.
            List<Integer> data = lines.stream()
                                      .map(Integer::parseInt)
                                      .collect(Collectors.toList());
            return new ReadResultImp(ReadResult.Status.SUCCESS, data);
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
        try {
            Files.write(Paths.get(filePath),
                        (result + delimiter).getBytes(),
                        StandardOpenOption.APPEND,
                        StandardOpenOption.CREATE);
            return new WriteResultImp(WriteResult.WriteResultStatus.SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            return new WriteResultImp(WriteResult.WriteResultStatus.FAILURE);
        }
    }
}
