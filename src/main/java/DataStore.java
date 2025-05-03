import projectannotations.ProcessAPI;

@ProcessAPI
public interface DataStore {
    DataStoreReadResult read(InputConfig input);
    WriteResult appendSingleResult(OutputConfig output, String result, char delimiter);
}
