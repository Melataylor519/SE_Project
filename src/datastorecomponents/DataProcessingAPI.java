package datastorecomponents;

import projectannotations.ProcessAPI;

@ProcessAPI
public interface DataProcessingAPI {
    ReadResult read(InputConfig input);
    WriteResult appendSingleResult(OutputConfig output, String result, char delimiter);
}
