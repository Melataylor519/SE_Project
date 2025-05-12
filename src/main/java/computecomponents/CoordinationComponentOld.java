package computecomponents;

import datastorecomponents.DataProcessingAPI;
import datastorecomponents.FileInputConfig;
import datastorecomponents.FileOutputConfig;
import datastorecomponents.InputConfig;
import datastorecomponents.OutputConfig;
import datastorecomponents.ReadResult;
import datastorecomponents.WriteResult;
import usercomputecomponents.UserComputeEngineAPI;

public class CoordinationComponentOld implements CoordinationAPI {

    private final DataProcessingAPI dataStorage;
    private final ComputeSystem computeSystem;

    public CoordinationComponentOld(UserComputeEngineAPI userComputeEngine, DataProcessingAPI dataStorage) {
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

        // Step 1: Read input data from storage
        InputConfig inputConfig = new FileInputConfig(inputSource);
        ReadResult readResult = dataStorage.read(inputConfig);

        if (readResult.getStatus() == ReadResult.Status.SUCCESS) {
            int[] loadedData = readResult.getResults();
            if (loadedData == null || loadedData.length == 0) {
                return "Error: No valid input data.";
            }

            StringBuilder inputData = new StringBuilder();
            for (int num : loadedData) {
                inputData.append(num).append(",");
            }

            // Remove the last comma
            String inputDataString = inputData.substring(0, inputData.length() - 1);

            // Create a new InputConfig with the processed input data
            inputConfig = new FileInputConfig(inputDataString);
        } else {
            return "Error: Failed to read input data.";
        }

        // Check for valid input
        if (inputConfig.getInputData() == null || inputConfig.getInputData().isEmpty()) {
            return "Error: Failed to read input data.";
        }

        // Step 2: Compute result
        OutputConfig computeOutputConfig = new FileOutputConfig(outputSource) {
            @Override
            public String formatOutput(String result) {
                return result;
            }
        };

        ComputeRequest request = new ComputeRequest(inputConfig, computeOutputConfig, ',');
        ComputeResponse response = computeSystem.compute(request);

        if (!response.getStatus().isSuccess()) {
            return "Computation failed: " + response.getFailureMessage();
        }

        // Step 3: Write result to storage
        WriteResult writeResult = dataStorage.appendSingleResult(computeOutputConfig, response.getResult(), ',');

        if (writeResult.getStatus() != WriteResult.WriteResultStatus.SUCCESS) {
            return "Error: Failed to write output data.";
        }

        return "Computation successful. Result: " + response.getResult();
    }
}
