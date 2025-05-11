package datastorecomponents;

import projectannotations.StorageAPI;

@StorageAPI
public interface DataProcessingAPI {
    ReadResult read(InputConfig input);
    WriteResult appendSingleResult(OutputConfig output, String result, char delimiter);
}
