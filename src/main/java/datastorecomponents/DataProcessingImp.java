package datastorecomponents;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import datastorecomponents.ReadResult.Status;
import datastorecomponents.WriteResult.WriteResultStatus;

public class DataProcessingImp implements DataProcessingAPI {

    public DataProcessingImp() {

    }
  
    @Override
    public ReadResult read(InputConfig inputConfig) {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(inputConfig.getSource()))) {
            Stream<String> lines = reader.lines();
            int[] data = lines.mapToInt(Integer::parseInt).toArray();
            return new ReadResult(ReadResult.Status.SUCCESS, data);
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            return new ReadResult(ReadResult.Status.FAILURE, null);
        }
    }

    @Override
    public WriteResult appendSingleResult(OutputConfig output, String result, char delimiter) {
        try {
            if (output == null || output.getFilePath() == null || output.getFilePath().isEmpty()) {
                throw new IllegalArgumentException("OutputConfig or file path cannot be null or empty");
            }
            // Validate that the file is writable
            if (!Files.exists(Paths.get(output.getFilePath())) || !Files.isWritable(Paths.get(output.getFilePath()))) {
                throw new IllegalArgumentException("File does not exist or is not writable: " + output.getFilePath());
            }

            if (result == null) {
                throw new IllegalArgumentException("Result cannot be null");
            }
            String formattedResult = result + delimiter;
            Files.write(Paths.get(output.getFilePath()), formattedResult.getBytes(StandardCharsets.UTF_8), 
                         java.nio.file.StandardOpenOption.APPEND);

            return new WriteResultImp(WriteResult.WriteResultStatus.SUCCESS);
            
        } catch (IllegalArgumentException e) {
            // Handle known exceptions
            e.printStackTrace();
            return new WriteResultImp(WriteResult.WriteResultStatus.FAILURE);
            
        } catch (Exception e) {
            // Handle unexpected exceptions
            e.printStackTrace();
            return new WriteResultImp(WriteResult.WriteResultStatus.FAILURE);
        }
    }
}
