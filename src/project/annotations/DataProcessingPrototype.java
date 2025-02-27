package project.annotations;

import java.util.Arrays;

import computecomponents.InputConfig;  


public class DataProcessingPrototype implements DataProcessingAPI {

    @Override
    public ReadResult read(InputConfig input) {
        // 'read' implement
        String inputData = input.getInputData();
        if (inputData == null || inputData.isEmpty()) {
            return new ReadResultImp(ReadResult.Status.FAILURE, null);
        }
        
        // process data, return result
        Iterable<Integer> processedData = processInputData(inputData);
        return new ReadResultImp(ReadResult.Status.SUCCESS, processedData);
    }

    @Override
    public WriteResult appendSingleResult(project.annotations.OutputConfig output, String result, char delimiter) {
        if (output == null || result == null) {
            return new WriteResultImp(WriteResult.WriteResultStatus.FAILURE);
        }
        
        // save result format: OutputConfig
        String formattedResult = output.formatOutput(result);  // use formatOutput of OutputConfig
        boolean writeSuccess = writeResultToStorage(formattedResult, delimiter);
        
        if (writeSuccess) {
            return new WriteResultImp(WriteResult.WriteResultStatus.SUCCESS);
        }
        
        return new WriteResultImp(WriteResult.WriteResultStatus.FAILURE);
    }

    // Helper method to process input data (example)
    private Iterable<Integer> processInputData(String inputData) {
        // Logic for processing input data as numbers (actual logic implemented here)
        return Arrays.asList(1, 2, 3);  // Temporary return value
    }

    // Helper method to simulate writing the result (example)
    private boolean writeResultToStorage(String result, char delimiter) {
        // Implement actual save logic (e.g. save to a file or DB)
        System.out.println("Result saved: " + result);
        return true;  // Assuming it was saved successfully
    }

    public void prototype(DataProcessingAPI apiCall) {
        InputConfig inputConfig = new InputConfig() {
            @Override
            public String getInputData() {
                return "Sample input data";  // return example data
            }
        };

        
        project.annotations.OutputConfig outputConfig = new project.annotations.OutputConfig() {
            @Override
            public String formatOutput(String result) {
                return "Formatted: " + result;
            }
        };

        ReadResult dataStoreReadResult = apiCall.read(inputConfig);

        if (dataStoreReadResult.getStatus() == ReadResult.Status.SUCCESS) {
            Iterable<Integer> loadedData = dataStoreReadResult.getResults();

            for (int i : loadedData) {
                String result = "" + i;
                WriteResult writeResult = apiCall.appendSingleResult(outputConfig, result, ',');

                if (writeResult.getStatus() != WriteResult.WriteResultStatus.SUCCESS) {
                    System.out.println("Fail. Please try again.");
                }
            }
        }
    }
}



	
	public void prototype(DataProcessingAPI apiCall) {
		InputConfig inputConfig = new InputConfig(){
			@Override
    			public String getFilePath() {
        			return "";  // replace with the actual file path
    			}
		}; 
		OutputConfig outputConfig = null;

		ReadResult dataStoreReadResult = apiCall.read(inputConfig);

		if (dataStoreReadResult.getStatus() == ReadResult.Status.SUCCESS) {
			Iterable<Integer> loadedData = dataStoreReadResult.getResults();

			for (int i : loadedData) {
				String result = "" + i;
				WriteResult writeResult = apiCall.appendSingleResult(outputConfig, result, ',');

				if (writeResult.getStatus() != WriteResult.WriteResultStatus.SUCCESS) {
					System.out.println("Fail. Please try again.");
				}
			}
		}
	}
}
