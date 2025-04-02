package test;

import java.util.List;
import src.datastorecomponents.DataProcessingAPI;
import src.datastorecomponents.InputConfig;
import src.datastorecomponents.OutputConfig;
import src.datastorecomponents.ReadResult;
import src.datastorecomponents.ReadResultImp;
import src.datastorecomponents.WriteResult;
import src.datastorecomponents.WriteResultImp;

public class InMemoryDataStoreAPI implements DataProcessingAPI {
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
