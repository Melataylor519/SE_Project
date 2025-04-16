package datastorecomponents;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import datastorecomponents.ReadResult.Status;
import datastorecomponents.WriteResult.WriteResultStatus;

public class DataProcessingImp implements DataProcessingAPI {
    private final DataProcessingAPI dataProcessAPI;

    public DataProcessingImp(){
        this(null);
    }
 
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
            

            String content = new String(Files.readAllBytes(Paths.get(input.getFilePath())), StandardCharsets.UTF_8);
            String[] tokens = content.split(";");


            List<Integer> results = new ArrayList<>();
            for (String token : tokens) {
                try {
                    results.add(Integer.parseInt(token.trim()));
                } catch (NumberFormatException e) {
                    System.err.println("Skipping invalid integer: " + token);
                }
            }
            
            return new ReadResultImp(ReadResult.Status.SUCCESS, results);
            
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
