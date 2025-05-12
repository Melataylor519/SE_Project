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
        if (userComputeEngine == null || dataStorage == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }
        this.dataStorage = dataStorage;
        this.computeSystem = new ComputeSystemImpl(dataStorage, userComputeEngine);
    }

    public String handleComputation(String inputSource, String outputSource) {
        if (inputSource == null || inputSource.trim().isEmpty()) {
            return "Error: inputSource cannot be null or empty.";
        }

        InputConfig inputConfig = new FileInputConfig(inputSource);
        ReadResult readResult = dataStorage.read(inputConfig);

        if (readResult.getStatus() == ReadResult.Status.SUCCESS) {
            int[] loadedData = readResult.getResults(); // Changed from Iterable<Integer> to int[]
            StringBuilder inputData = new StringBuilder();

            for (int num : loadedData) {
                inputData.append(num).append(","); // Process int[] directly
            }

            String inputDataString = inputData.length() > 0
                ? inputData.substring(0, inputData.length() - 1)
                : "";

            if (inputDataString.isEmpty()) {
                return "Error: No valid input data.";
            }

            inputConfig = new FileInputConfig(inputDataString);
        } else {
            return "Error: Failed to read input data.";
        }

        OutputConfig computeOutputConfig = new FileOutputConfig(outputSource);
        ComputeRequest request = new ComputeRequest(inputConfig, computeOutputConfig, ',');
        ComputeResponse response = computeSystem.compute(request);

        if (!response.getStatus().isSuccess()) {
            return "Computation failed: " + response.getFailureMessage();
        }

        WriteResult writeResult = dataStorage.appendSingleResult(computeOutputConfig, response.getResult(), ',');
        if (writeResult.getStatus() != WriteResult.WriteResultStatus.SUCCESS) {
            return "Error: Failed to write output data.";
        }

        return "Computation successful. Result: " + response.getResult();
    }
}

