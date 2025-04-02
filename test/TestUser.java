package test;

import java.io.File;
import src.usercomputecomponents.UserComputeEngineAPI;

public class TestUser {
	
	// TODO 3: change the type of this variable to the name you're using for your
	// @NetworkAPI interface; also update the parameter passed to the constructor
	private final UserComputeEngineAPI coordinator;

	public TestUser(UserComputeEngineAPI coordinator) {
		this.coordinator = coordinator;
	}

	public void run(String outputPath) {
		char delimiter = ';';
		String inputPath = "test" + File.separatorChar + "testInputFile.test";
		
		// TODO 4: Call the appropriate method(s) on the coordinator to get it to 
		// run the compute job specified by inputPath, outputPath, and delimiter
		// processData() receives delimiter as a String array, so convert it to an array
	    	String[] delimiters = {String.valueOf(delimiter)};

	    	// data process
	    	coordinator.processData(inputPath, outputPath, delimiters);
	}
}
