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

        // Step 1: Read input data from storage
        InputConfig inputConfig = new FileInputConfig(inputSource);
        ReadResult readResult = dataStorage.read(inputConfig);

        if (readResult.getStatus() != ReadResult.Status.SUCCESS) {
            return "Error: Failed to read input data. Status: " + readResult.getStatus();
        }

        Iterable<Integer> loadedData = readResult.getResults();

        // Step 2: Optimized string construction using StringBuilder
        StringBuilder inputDataBuilder = new StringBuilder();
        for (int num : loadedData) {
            inputDataBuilder.append(num).append(",");
        }

        // Remove trailing comma if data exists
        if (inputDataBuilder.length() > 0) {
            inputDataBuilder.setLength(inputDataBuilder.length() - 1);
        }
        String inputDataString = inputDataBuilder.toString();

        if (inputDataString.isEmpty()) {
            return "Error: No valid input data.";
        }

        // Step 3: Prepare compute output config
        OutputConfig computeOutputConfig = new FileOutputConfig(outputSource);

        // Step 4: Run computation
        ComputeRequest request = new ComputeRequest(new FileInputConfig(inputDataString), computeOutputConfig, ',');
        ComputeResponse response = computeSystem.compute(request);

        if (!response.getStatus().isSuccess()) {
            return "Computation failed: " + response.getFailureMessage();
        }

        // Step 5: Write the result
        WriteResult writeResult = dataStorage.appendSingleResult(computeOutputConfig, response.getResult(), ',');
        if (writeResult.getStatus() != WriteResult.WriteResultStatus.SUCCESS) {
            return "Error: Failed to write output data. Status: " + writeResult.getStatus();
        }

        return "Computation successful. Result: " + response.getResult();
    }
}
