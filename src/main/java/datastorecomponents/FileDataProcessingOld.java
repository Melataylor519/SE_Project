package datastorecomponents;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;

public class FileDataProcessingOld implements DataProcessingAPI {

    @Override
    public ReadResult read(InputConfig input) {
        if (!(input instanceof FileInputConfig)) {
            return new ReadResultImp(ReadResult.Status.FAILURE, null);
        }

        String filePath = input.getFilePath();
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }

        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            List<Integer> data = lines.stream()
                                      .map(Integer::parseInt)
                                      .collect(Collectors.toList());
            return new ReadResultImp(ReadResult.Status.SUCCESS, data);
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            return new ReadResultImp(ReadResult.Status.FAILURE, null);
        }
    }

    // @Override
    // public ReadResult read(InputConfig input) {
    //     if (!(input instanceof FileInputConfig)) {
    //         return new ReadResultImp(ReadResult.Status.FAILURE, null);
    //     }

    //     String filePath = input.getFilePath();
    //     if (filePath == null || filePath.trim().isEmpty()) {
    //         throw new IllegalArgumentException("File path cannot be null or empty");
    //     }

    //     try (var reader = Files.newBufferedReader(Paths.get(filePath))) {
    //         List<Integer> data = reader.lines()
    //                                 .map(String::trim)
    //                                 .filter(line -> !line.isEmpty())
    //                                 .mapToInt(Integer::parseInt)
    //                                 .boxed()
    //                                 .collect(Collectors.toList());

    //         return new ReadResultImp(ReadResult.Status.SUCCESS, data);
    //     } catch (IOException | NumberFormatException e) {
    //         e.printStackTrace();
    //         return new ReadResultImp(ReadResult.Status.FAILURE, null);
    //     }
    // }


    @Override
    public WriteResult appendSingleResult(OutputConfig output, String result, char delimiter) {
        if (!(output instanceof FileOutputConfig)) {
            return new WriteResultImp(WriteResult.WriteResultStatus.FAILURE);
        }

        String filePath = output.getFilePath();
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }

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
