import java.io.File;

import FileBasedInputConfig;

public class TestUser {

	// TODO 3: change the type of this variable to the name you're using for your
	// @NetworkAPI interface; also update the parameter passed to the constructor
	private final ComputationCoordinator coordinator;

	public TestUser(ComputationCoordinator coordinator) {
		this.coordinator = coordinator;
	}

	public void run(String outputPath) {
		char delimiter = ';';
		String inputPath = "test" + File.separatorChar + "testInputFile.test";

		// TODO 4: Call the appropriate method(s) on the coordinator to get it to
		// run the compute job specified by inputPath, outputPath, and delimiter

		InputConfig inputConfig = new FileBasedInputConfig(inputPath); // you'll implement this
		OutputConfig outputConfig = new FileBasedOutputConfig(outputPath); // you'll implement this

		ComputeRequest request = new ComputeRequest(inputConfig, outputConfig, delimiter);
		coordinator.compute(request);
	}

}
