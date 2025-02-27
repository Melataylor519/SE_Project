package computecomponents;

import main.java.com.assignment2.api.UserComputeEngineAPI;
import project.annotations.InputConfig;
import project.annotations.OutputConfig;

public class ComputeRequest {
	
	private static final char DEFAULT_DELIMITER = ';';

	private final InputConfig inputConfig;
	private final OutputConfig outputConfig;
	private final char delimiter;
	
	public ComputeRequest(InputConfig inputConfig, OutputConfig outputConfig) {
		this(inputConfig, outputConfig, DEFAULT_DELIMITER); // default delimiter: ;
	}
	
	public ComputeRequest(InputConfig inputConfig, OutputConfig outputConfig, char delimiter) {
		this.inputConfig = inputConfig;
		this.outputConfig = outputConfig;
		this.delimiter = delimiter;
	}
	
	public char getDelimiter() {
		return delimiter;
	}

	public InputConfig getInputConfig() {
		return inputConfig;
	}

	public OutputConfig getOutputConfig() {
		return outputConfig;
	}

    @Override
    public String toString() {
        return "ComputeRequest [inputConfig=" + inputConfig + ", outputConfig=" + outputConfig + ", delimiter="
                + delimiter + "]";
    }

}
