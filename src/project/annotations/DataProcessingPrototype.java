package project.annotations;

import java.util.List;
import java.util.Arrays;

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
		InputConfig inputConfig = new InputConfig() {
			@Override
			public List<Integer> getInputData(){
				return Arrays.asList(1,2,3,4,5);
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

