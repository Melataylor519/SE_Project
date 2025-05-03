import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryDataStore implements DataStore {
    private final Map<String, List<String>> storage = new HashMap<>();

    @Override
    public DataStoreReadResult read(InputConfig input) {
        return new DataStoreReadResult(DataStoreReadResult.Status.SUCCESS, List.of(input.getValue()));
    }

    @Override
    public WriteResult appendSingleResult(OutputConfig output, String result, char delimiter) {
    	if (output == null || result == null) {
    	    return new SimpleWriteResult(WriteResult.WriteResultStatus.FAILURE);
    	}
    	storage.computeIfAbsent(output.getUserId(), k -> new ArrayList<>()).add(result);
        return new SimpleWriteResult(WriteResult.WriteResultStatus.SUCCESS);
    }
}
