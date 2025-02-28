package project.annotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryDataStoreAPITest {

    private InMemoryDataStoreAPITest dataStore;

    @BeforeEach
    void setUp() {
        dataStore = new InMemoryDataStoreAPITest();
    }

    @Test
    void testRead() {
        List<Integer> inputData = List.of(1, 2, 3, 4);
        InMemoryInputConfig inputConfig = new InMemoryInputConfig(inputData);

        ReadResult result = dataStore.read(inputConfig);
        
        assertEquals(ReadResult.Status.SUCCESS, result.getStatus());
        assertEquals(inputData, result.getData());
    }

    @Test
    void testAppendSingleResult() {
        List<String> outputData = new ArrayList<>();
        InMemoryOutputConfig outputConfig = new InMemoryOutputConfig(outputData);

        WriteResult result = dataStore.appendSingleResult(outputConfig, "Test Result", ',');

        assertEquals(WriteResult.WriteResultStatus.SUCCESS, result.getStatus());
        assertTrue(outputData.contains("Test Result"));
    }

    @Test
    void testInvalidReadConfig() {
        InputConfig invalidConfig = new InputConfig() {}; 

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            dataStore.read(invalidConfig);
        });

        assertEquals("Unsupported InputConfig type", exception.getMessage());
    }

    @Test
    void testInvalidAppendConfig() {
        OutputConfig invalidConfig = new OutputConfig() {}; 

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            dataStore.appendSingleResult(invalidConfig, "Test Result", ',');
        });

        assertEquals("Unsupported OutputConfig type", exception.getMessage());
    }
}

