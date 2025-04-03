package datastorecomponents;

import java.nio.file.Files;
import java.nio.file.Paths;

import datastorecomponents.ReadResult.Status;
import datastorecomponents.WriteResult.WriteResultStatus;

// Implementation for Data Store API
public class DataProcessingImp implements DataProcessingAPI {
    private final DataProcessingAPI dataProcessAPI;

    // Constructor may be used 
    public DataProcessingImp(DataProcessingAPI dataProcessAPI) {
        this.dataProcessAPI = dataProcessAPI;
    }

    @Override
    public ReadResult read(InputConfig input) {
        try {
            if (input == null || input.getFilePath() == null || input.getFilePath().isEmpty()) {
                throw new IllegalArgumentException("InputConfig or file path cannot be null or empty");
            }

            // Validate that the file exists and is readable
            if (!Files.exists(Paths.get(input.getFilePath())) || !Files.isReadable(Paths.get(input.getFilePath()))) {
                throw new IllegalArgumentException("File does not exist or is not readable: " + input.getFilePath());
            }
            return dataProcessAPI.read(input);
            
        } catch (IllegalArgumentException e) {
            // Handle known exceptions
            e.printStackTrace();
            return new ReadResultImp(ReadResult.Status.FAILURE, null);
            
        } catch (Exception e) {
            // Handle unexpected exceptions
            e.printStackTrace();
            return new ReadResultImp(ReadResult.Status.FAILURE, null);
        }
    }

    @Override
    public WriteResult appendSingleResult(OutputConfig output, String result, char delimiter) {
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

            try {
                return dataProcessAPI.appendSingleResult(output, result, delimiter);
            } catch (Exception e) {
                e.printStackTrace();
                return new WriteResultImp(WriteResult.WriteResultStatus.FAILURE);
            }
    }
}
