import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryDataStore implements DataStore {
    @Override
    public DataStoreReadResult read(InputConfig input) {
        if (!(input instanceof InMemoryInputConfig inMemoryInput)) {
            throw new IllegalArgumentException("Unsupported InputConfig type: "
                + input.getClass().getSimpleName());
        }
        List<Integer> inputData = inMemoryInput.getInputs();
        return new DataStoreReadResult(DataStoreReadResult.Status.SUCCESS, inputData);
        
    }

    @Override
    public WriteResult appendSingleResult(OutputConfig output, String result, char delimiter) {
    	if (!(output instanceof InMemoryOutputConfig)) {
            throw new IllegalArgumentException("Unsupported OutputConfig type: "
                + output.getClass().getSimpleName());
        }
    
        if (result == null) {
            return new SimpleWriteResult(WriteResult.WriteResultStatus.FAILURE);
        }
    
        InMemoryOutputConfig memoryOutput = (InMemoryOutputConfig) output;
        memoryOutput.getOutputMutable().add(result);
        return new SimpleWriteResult(WriteResult.WriteResultStatus.SUCCESS);
    }
}
