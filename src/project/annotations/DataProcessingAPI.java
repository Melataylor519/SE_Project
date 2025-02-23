package project.annotations;

@ProcessAPI
public interface DataProcessingAPI {
    ReadResult read(computecomponents.InputConfig inputConfig);
    WriteResult appendSingleResult(OutputConfig output, String result, char delimiter);
}

	

