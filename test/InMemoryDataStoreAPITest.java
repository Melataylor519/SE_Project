package project.annotations;

import java.util.List;

public class InMemoryDataStoreAPITest implements DataProcessingAPI {

	@Override
	public ReadResult read(InputConfig input) {
		if (!(input instanceof InMemoryInputConfig)) {
            		throw new IllegalArgumentException("Unsupported InputConfig type");
        	}

        	InMemoryInputConfig inMemoryInput = (InMemoryInputConfig) input;
        	List<Integer> inputData = inMemoryInput.getInput();

        	return new ReadResultImp(ReadResult.Status.SUCCESS, inputData);
	}

	@Override
	public WriteResult appendSingleResult(OutputConfig output, String result, char delimiter) {
		if (!(output instanceof InMemoryOutputConfig)) {
			throw new IllegalArgumentException("Unsupported OutputConfig type");
	     	}
		 
		InMemoryOutputConfig inMemoryOutput = (InMemoryOutputConfig) output;
		inMemoryOutput.getOutput().add(result);

	     	return new WriteResultImp(WriteResult.WriteResultStatus.SUCCESS);
	}

}
