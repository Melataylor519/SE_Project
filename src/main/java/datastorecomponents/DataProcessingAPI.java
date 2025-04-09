package main.java.datastorecomponents;

import main.java.projectannotations.ProcessAPI;

@ProcessAPI
public interface DataProcessingAPI {
    ReadResult read(InputConfig input);
    WriteResult appendSingleResult(OutputConfig output, String result, char delimiter);
}
