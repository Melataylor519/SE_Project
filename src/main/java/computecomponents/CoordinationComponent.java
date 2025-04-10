package main.java.computecomponents;

import main.java.usercomputecomponents.UserComputeEngineAPI;
import main.java.datastorecomponents.DataProcessingAPI;
import main.java.datastorecomponents.ReadResult;
import main.java.datastorecomponents.WriteResult;
import main.java.datastorecomponents.InputConfig; 
import main.java.datastorecomponents.OutputConfig; 

public class CoordinationComponent {
    //private final UserComputeEngineAPI userComputeEngine;
    private final DataProcessingAPI dataStorage;
    private final ComputeSystem computeSystem;

    public CoordinationComponent(UserComputeEngineAPI userComputeEngine, DataProcessingAPI dataStorage) {
    	if (userComputeEngine == null) {
            throw new IllegalArgumentException("UserComputeEngineAPI cannot be null");
        }
        if (dataStorage == null) {
            throw new IllegalArgumentException("DataProcessingAPI cannot be null");
        }
    	
    	//this.userComputeEngine = userComputeEngine;
        this.dataStorage = dataStorage;
        this.computeSystem = new ComputeSystemImpl(dataStorage, userComputeEngine);
    }

    public String handleComputation(String inputSource, String outputSource) {
      


    	if (inputSource == null || inputSource.trim().isEmpty()) {
            return "Error: inputSource cannot be null or empty.";
        }

    	
    	

      // Read input data from storage
    	// create initial InputConfig


    	InputConfig inputConfig = new DefaultInputConfig(""); 

    	// read data
    	ReadResult readResult = dataStorage.read(inputConfig);
    	if (readResult.getStatus() == ReadResult.Status.SUCCESS) {
    	    Iterable<Integer> loadedData = readResult.getResults(); // get Iterable<Integer>
    	    StringBuilder inputData = new StringBuilder();
    	    
    	    for (int num : loadedData) {

    	        inputData.append(num).append(",");  // Convert integers to strings separated by commas (,)


    	    }
    	    
    	    // delete last comma
    	    String inputDataString = inputData.length() > 0 ? inputData.substring(0, inputData.length() - 1) : "";

    	    if (inputDataString.isEmpty()) {
    	        return "Error: No valid input data.";
    	    }

    	    // Set it up correctly using `DefaultInputConfig`
    	    inputConfig = new DefaultInputConfig(inputDataString);
    	} else {
    	    return "Error: Failed to read input data.";
    	}


        // Check for valid input
        if (inputConfig.getInputData() == null) {
            return "Error: Failed to read input data.";
        }

        // Compute result
        OutputConfig computeOutputConfig = new OutputConfig() {
            @Override
            public String getFilePath() {
                return "";
            }
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
        OutputConfig annotationOutputConfig = new OutputConfig() {
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
