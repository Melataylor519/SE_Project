import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;


import datastorecomponents.DataProcessingAPI;
import datastorecomponents.DataProcessingImp;
import usercomputecomponents.UserComputeEnginePrototype;
import computecomponents.CoordinationComponent;


public class TestCoordinationComponent {

    private static Path inputFilePath;
    private static Path outputFilePath;
    
	@Test
	public void testCorrectnessOfOutput() throws IOException {
	    DataProcessingAPI dataStore = new DataProcessingImp();
	    UserComputeEnginePrototype userComputeEngine = new UserComputeEnginePrototype();
	    CoordinationComponent component = new CoordinationComponent(userComputeEngine, dataStore);

	    String input = "1;2;3;4;5";
	    Files.writeString(inputFilePath, input, StandardOpenOption.TRUNCATE_EXISTING);

	    String outputPath = outputFilePath.toString();
	    component.handleComputation(inputFilePath.toString(), outputPath);

	    String result = Files.readString(outputFilePath).trim();
	    Assertions.assertEquals("1 2 3 4 5", result);
	}

	@Test
	public void testEmptyInputFile() throws IOException {
	    DataProcessingAPI dataStore = new DataProcessingImp();
	    UserComputeEnginePrototype userComputeEngine = new UserComputeEnginePrototype();
	    CoordinationComponent component = new CoordinationComponent(userComputeEngine, dataStore);

	    Files.writeString(inputFilePath, "", StandardOpenOption.TRUNCATE_EXISTING);

	    component.handleComputation(inputFilePath.toString(), outputFilePath.toString());

	    String output = Files.readString(outputFilePath).trim();
	    Assertions.assertTrue(output.isEmpty(), "Output should be empty for empty input");
	}

	@Test
	public void testInvalidInputPath() {
	    DataProcessingAPI dataStore = new DataProcessingImp();
	    UserComputeEnginePrototype userComputeEngine = new UserComputeEnginePrototype();
	    CoordinationComponent component = new CoordinationComponent(userComputeEngine, dataStore);

	    String invalidInputPath = "non_existent_file.txt";

	    Assertions.assertDoesNotThrow(() ->
	        component.handleComputation(invalidInputPath, outputFilePath.toString())
	    );
	}

}
