package computecomponents;

import main.java.com.assignment2.api.UserComputeEngineAPI;
import project.annotations.DataProcessingAPI;
import project.annotations.ReadResult;
import project.annotations.WriteResult;



/**
 * Coordination component for managing computation requests.
 */
public class CoordinationComponent {

    private final DataProcessingAPI dataStorage;
    private final ComputeSystem computeSystem;

    public CoordinationComponent(UserComputeEngineAPI userComputeEngine, DataProcessingAPI dataStorage) {

        this.dataStorage = dataStorage;
        this.computeSystem = new ComputeSystemImpl();
    }

    public String handleComputation(String inputSource, String outputSource) {
        // Read input data from storage

    	InputConfig inputConfig = new DefaultInputConfig(""); 

    	// read data
    	ReadResult readResult = dataStorage.read(inputConfig);
    	if (readResult.getStatus() == ReadResult.Status.SUCCESS) {
    	    Iterable<Integer> loadedData = readResult.getResults(); // get Iterable<Integer>
    	    StringBuilder inputData = new StringBuilder();
    	    
    	    for (int num : loadedData) {
    	        inputData.append(num).append(",");  // Convert integers to strings separated by commas (,)
    	    }
    	    
    	    // remove last comma
    	    String inputDataString = inputData.length() > 0 ? inputData.substring(0, inputData.length() - 1) : "";

    	    if (inputDataString.isEmpty()) {
    	        return "Error: No valid input data.";
    	    }

    	    inputConfig = new DefaultInputConfig(inputDataString);
    	} else {
    	    return "Error: Failed to read input data.";
    	}


        // Check for valid input
        if (inputConfig.getInputData() == null) {
            return "Error: Failed to read input data.";
        }

        // Compute result
        computecomponents.OutputConfig computeOutputConfig = new computecomponents.OutputConfig() {
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

        // Write result to storage
        project.annotations.OutputConfig annotationOutputConfig = new project.annotations.OutputConfig() {
            @Override
            public String formatOutput(String result) {
                return result;
            }
            @Override
            public String getFilePath() {
                return System.getProperty("user.dir"); // return current dir
            }

        };
        
        WriteResult writeResult = dataStorage.appendSingleResult(annotationOutputConfig, response.getResult(), ',');

        if (writeResult.getStatus() != WriteResult.WriteResultStatus.SUCCESS) {
            return "Error: Failed to write output data.";
        }

        return "Computation successful. Result: " + response.getResult();
    }
}
