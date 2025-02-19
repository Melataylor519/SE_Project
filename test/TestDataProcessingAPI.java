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
        ReadResult mockReadResult = new ReadResult(ReadResult.Status.SUCCESS, Arrays.asList(1, 2, 3));
        when(mockAPI.read(any(InputConfig.class))).thenReturn(mockReadResult);

        WriteResult mockWriteResult = new WriteResult(WriteResult.WriteResultStatus.SUCCESS);
        when(mockAPI.appendSingleResult(any(OutputConfig.class), anyString(), eq(','))).thenReturn(mockWriteResult);

        prototype.prototype(mockAPI);

        verify(mockAPI).read(any(InputConfig.class));
        verify(mockAPI, times(mockReadResult.getResults().size()))
            .appendSingleResult(any(OutputConfig.class), anyString(), eq(','));
    }

    @Test
    public void testReadFailure_WithInvalidInput() {
        ReadResult mockReadResult = new ReadResult(ReadResult.Status.FAILURE, null);
        when(mockAPI.read(any(InputConfig.class))).thenReturn(mockReadResult);

        prototype.prototype(mockAPI);

        verify(mockAPI).read(any(InputConfig.class));
        verify(mockAPI, never()).appendSingleResult(any(OutputConfig.class), anyString(), anyChar());
    }

    @Test
    public void testWriteFailure_WhenAppendFails() {
        ReadResult mockReadResult = new ReadResult(ReadResult.Status.SUCCESS, Arrays.asList(1));
        when(mockAPI.read(any(InputConfig.class))).thenReturn(mockReadResult);

        WriteResult mockWriteResult = new WriteResult(WriteResult.WriteResultStatus.FAILURE);
        when(mockAPI.appendSingleResult(any(OutputConfig.class), anyString(), eq(','))).thenReturn(mockWriteResult);

        prototype.prototype(mockAPI);

        verify(mockAPI).read(any(InputConfig.class));
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
