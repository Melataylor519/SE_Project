package computecomponents;

import datastorecomponents.DataProcessingAPI;
import datastorecomponents.FileInputConfig;
import datastorecomponents.FileOutputConfig;
import datastorecomponents.InputConfig;
import datastorecomponents.OutputConfig;
import datastorecomponents.ReadResult;
import datastorecomponents.WriteResult;
import usercomputecomponents.UserComputeEngineAPI; 

public class CoordinationComponent implements CoordinationAPI {

    private final DataProcessingAPI dataStorage;
    private final ComputeSystem computeSystem;

    public CoordinationComponent(UserComputeEngineAPI userComputeEngine, DataProcessingAPI dataStorage) {
        if (userComputeEngine == null) {
            throw new IllegalArgumentException("UserComputeEngineAPI cannot be null");
        }
        if (dataStorage == null) {
            throw new IllegalArgumentException("DataProcessingAPI cannot be null");
        }

        this.dataStorage = dataStorage;
        this.computeSystem = new ComputeSystemImpl(dataStorage, userComputeEngine);
    }

    public String handleComputation(String inputSource, String outputSource) {
        if (inputSource == null || inputSource.trim().isEmpty()) {
            return "Error: inputSource cannot be null or empty.";
        }

        // Read input data from storage
        InputConfig inputConfig = new FileInputConfig(inputSource); // initial config
        ReadResult readResult = dataStorage.read(inputConfig);

        if (readResult.getStatus() == ReadResult.Status.SUCCESS) {
            Iterable<Integer> loadedData = readResult.getResults();

            // Optimized String construction using StringBuilder 
            StringBuilder inputDataBuilder = new StringBuilder();
            boolean first = true;
            for (int num : loadedData) {
                if (!first) {
                    inputDataBuilder.append(",");
                    inputDataBuilder.append(num);
                    first = false;
                }
            }
            String inputDataString = inputDataBuilder.toString();

            if (inputDataString.isEmpty()) {
                return "Error: No valid input data.";
            }

            inputConfig = new FileInputConfig(inputDataString);
        } else {
            return "Error: Failed to read input data.";
        }

        // Prepare compute output config
        OutputConfig computeOutputConfig = new FileOutputConfig(outputSource);

        // Run computation
        ComputeRequest request = new ComputeRequest(inputConfig, computeOutputConfig, ',');
        ComputeResponse response = computeSystem.compute(request);

        if (!response.getStatus().isSuccess()) {
            return "Computation failed: " + response.getFailureMessage();
        }

        // Step 4: Write the result
        OutputConfig annotationOutputConfig = new FileOutputConfig(System.getProperty("user.dir")); // Current directory
        annotationOutputConfig = new FileOutputConfig(outputSource); // Use the passed output source

        WriteResult writeResult = dataStorage.appendSingleResult(annotationOutputConfig, response.getResult(), ',');

        if (writeResult.getStatus() != WriteResult.WriteResultStatus.SUCCESS) {
            return "Error: Failed to write output data.";
        }

        return "Computation successful. Result: " + response.getResult();
    }
}
