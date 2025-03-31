import java.io.File;


public class TestUser {
	
	private final UserComputeEngineAPI coordinator;

	public TestUser(UserComputeEngineAPI coordinator) {
		this.coordinator = coordinator;
	}

	public void run(String outputPath) {
		char delimiter = ';';
		String inputPath = "test" + File.separatorChar + "testInputFile.test";

	    // processData() receives delimiter as a String array, so convert it to an array
	    String[] delimiters = {String.valueOf(delimiter)};

	    // data process
	    coordinator.processData(inputPath, outputPath, delimiters);
	}

}
