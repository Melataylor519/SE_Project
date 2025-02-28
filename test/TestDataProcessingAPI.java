package project.annotations;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

public class TestDataProcessingAPI {
    
    @Mock
    private DataProcessingAPI mockAPI;

    @Mock
    private InputConfig mockInputConfig;

    @Mock
    private OutputConfig mockOutputConfig;

    private DataProcessingPrototype prototype;
    private DataProcessingAPIImp realAPI;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        prototype = new DataProcessingPrototype();
        realAPI = new DataProcessingAPIImp(new DataProcessingAPI()); 
    }
    @Test
    public void testReadSuccess() {
        List<Integer> mockData = List.of(1, 2, 3); 
        ReadResult mockReadResult = new ReadResultImp(ReadResult.Status.SUCCESS, mockData);
        when(mockAPI.read(any(InputConfig.class))).thenReturn(mockReadResult);

        WriteResult mockWriteResult = new WriteResultImp(WriteResult.WriteResultStatus.SUCCESS);
        when(mockAPI.appendSingleResult(any(OutputConfig.class), anyString(), eq(',')))
        .thenAnswer(invocation -> new WriteResultImp(WriteResult.WriteResultStatus.SUCCESS));

        // Execute the method under test
        prototype.execute(mockAPI); 

        // Verify read() is called once
        verify(mockAPI).read(any(InputConfig.class));

        // Verify appendSingleResult() for each value in mockData
        for (Integer value : mockData) {
            verify(mockAPI).appendSingleResult(any(OutputConfig.class), eq(String.valueOf(value)), eq(','));
        }    
    }

    @Test
    public void testReadFailure_WithInvalidInput() {
        ReadResult mockReadResult = new ReadResult(ReadResult.Status.FAILURE, null);
        when(mockAPI.read(any(InputConfig.class))).thenReturn(mockReadResult);

        prototype.execute(mockAPI);

        verify(mockAPI).read(any(InputConfig.class));
        verify(mockAPI, never()).appendSingleResult(any(OutputConfig.class), anyString(), anyChar());
    }

    @Test
    public void testWriteFailureWhenAppendFails() {
        // Mock a successful read
        List<Integer> mockData = List.of(1);
        ReadResult mockReadResult = new ReadResultImp(ReadResult.Status.SUCCESS, mockData);
        when(mockAPI.read(any(InputConfig.class))).thenReturn(mockReadResult);

        // Mock a failed append operation
        WriteResult mockWriteResult = new WriteResultImp(WriteResult.WriteResultStatus.FAILURE);
        when(mockAPI.appendSingleResult(any(OutputConfig.class), anyString(), eq(',')))
        .thenReturn(mockWriteResult);

        // Execute the method 
        prototype.execute(mockAPI); 

        // Verify read operation was called
        verify(mockAPI).read(any(InputConfig.class));

        // Verify appendSingleResult was attempted and failed
        verify(mockAPI).appendSingleResult(any(OutputConfig.class), anyString(), eq(','));
    }


    @Test
    public void testRealImplementation_ReadReturnsValidResult() {
        InputConfig inputConfig = new InputConfig() {};  
        ReadResult result = realAPI.read(inputConfig);

        assertNotNull("Read result should not be null", result);
        assertNull("Initial implementation should return null results", result.getResults());
    }

    @Test
    public void testRealImplementation_WriteReturnsValidResult() {
        OutputConfig outputConfig = new OutputConfig() {};  
        String testData = "test";
        char delimiter = ',';

        WriteResult result = realAPI.appendSingleResult(outputConfig, testData, delimiter);

        assertNotNull("Write result should not be null", result);
    }

    @Test
    public void testSimple_ReadReturnsNonNullResult() {
        DataProcessingAPI api = new DataProcessingAPIImp(null);
        ReadResult result = api.read(null);

        assertNotNull(result);
    }
}
