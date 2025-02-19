package project.annotations;

@ProcessAPI
public interface DataProcessingAPI {
    ReadResult read(InputConfig input);
    WriteResult appendSingleResult(OutputConfig output, String result, char delimiter);
}

	

