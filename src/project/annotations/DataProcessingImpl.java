package project.annotations;

import java.util.Arrays;

import computecomponents.InputConfig;
import computecomponents.OutputConfig;

/**
 * Implementation of DataProcessingAPI interface.
 */
public class DataProcessingImpl implements DataProcessingAPI {
    private final DataProcessingAPI dataProcessAPI; // Field to store API instance 

    /**
     * Constructor for DataProcessingImpl.
     * @param dataProcessAPI The API instance to be used
     */
    public DataProcessingImpl(DataProcessingAPI dataProcessAPI) {
        this.dataProcessAPI = dataProcessAPI;
    }

    @Override
    public ReadResult read(InputConfig input) {
        
        String inputData = input.getInputData();  // get data from InputConfig
        
        if (inputData == null || inputData.isEmpty()) {
            // fail: data is empty or missing
            return new ReadResultImp(ReadResult.Status.FAILURE, null);
        }
        
        // Process the data and return a successful result (for example, returning a list of integers)
        Iterable<Integer> processedData = processInputData(inputData);  // example of processing input data
        
        return new ReadResultImp(ReadResult.Status.SUCCESS, processedData);
    }

    public WriteResult appendSingleResult(OutputConfig output, String result, char delimiter) {
        // add result to OutputConfig
        if (output == null || result == null) {
            return new WriteResultImp(WriteResult.WriteResultStatus.FAILURE);  // fail
        }
        
        // save result : OutputConfig format
        String formattedResult = output.formatOutput(result);  // use formatOutput of OutputConfig
        boolean writeSuccess = writeResultToStorage(formattedResult, delimiter);  // save logic
        
        if (writeSuccess) {
            return new WriteResultImp(WriteResult.WriteResultStatus.SUCCESS);  // success
        }
        
        return new WriteResultImp(WriteResult.WriteResultStatus.FAILURE);  // fail
    }

    // Helper method to process input data (example)
    private Iterable<Integer> processInputData(String inputData) {
        // Logic for processing input data as numbers (actual logic implemented here)
        return Arrays.asList(1, 2, 3);  // temporary return value
    }

    // Helper method to simulate writing the result (example)
    private boolean writeResultToStorage(String result, char delimiter) {
        // Implement actual save logic (e.g. save to a file or DB)
        System.out.println("Result saved: " + result);
        return true;  // assume success
    }

	@Override
	public WriteResult appendSingleResult(project.annotations.OutputConfig output, String result, char delimiter) {
		
		return null;
	}
}

