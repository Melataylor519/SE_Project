package project.annotations;

public class DataProcessingPrototype implements DataProcessingAPI {

	@Override
	public ReadResult read(InputConfig input) {
		return null;
	}

	@Override
	public WriteResult appendSingleResult(OutputConfig output, String result, char delimiter) {
		return null;
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
