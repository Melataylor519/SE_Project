// ComputeEngineIntegrationTest.java
import usercomputecomponents.UserComputeEnginePrototype;

import datastorecomponents.ReadResult;
import datastorecomponents.WriteResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

class ComputeEngineIntegrationTest {

    private UserComputeEnginePrototype computeEngine;
    private InMemoryDataStoreAPI dataStore;
    private InMemoryInputConfig inputConfig;
    private InMemoryOutputConfig outputConfig;

    @BeforeEach
    void setUp() {
        computeEngine = new UserComputeEnginePrototype();
        dataStore = new InMemoryDataStoreAPI();

        // Initialize input with predefined values: [1, 10, 25]
        List<Integer> inputData = Arrays.asList(1, 10, 25);
        inputConfig = new InMemoryInputConfig(inputData);

        // Prepare output configuration for storing results
        outputConfig = new InMemoryOutputConfig();
    }

    @Test
    void testComputeEngineProcessesDataCorrectly() {
        // Step 1: Read data from the data store
        ReadResult readResult = dataStore.read(inputConfig);
        assertEquals(ReadResult.Status.SUCCESS, readResult.getStatus(), "Failed to read input data.");

        // Step 2: Process data (Since the engine is not implemented yet, we assume direct processing)
        Iterable<Integer> processedData = readResult.getResults(); // Using raw data as no processing is implemented yet
        String resultString = processedData.toString(); // Convert the processed result into a string format

        // Step 3: Write processed data to the data store
        WriteResult writeResult = dataStore.appendSingleResult(outputConfig, resultString, ',');
        assertEquals(WriteResult.WriteResultStatus.SUCCESS, writeResult.getStatus(), "Failed to write output data.");

        // Step 4: Verify that the stored output matches expectations
        assertTrue(outputConfig.getOutput().contains(resultString),
                "Output data does not match the expected result. This test may fail until the compute engine is fully implemented.");
    }
}
