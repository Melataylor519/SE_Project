import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyChar;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import datastorecomponents.DataProcessingAPI;
import datastorecomponents.DataProcessingImp;
import datastorecomponents.DataProcessingPrototype;
import datastorecomponents.InputConfig;
import datastorecomponents.OutputConfig;
import datastorecomponents.ReadResult;
import datastorecomponents.WriteResult;
import datastorecomponents.ReadResultImp;
import datastorecomponents.WriteResultImp;

public class TestDataProcessingAPI {
    
    @Mock
    private DataProcessingAPI mockAPI;

    @Mock
    private InputConfig mockInputConfig;

    @Mock
    private OutputConfig mockOutputConfig;

    private DataProcessingPrototype prototype;
    private DataProcessingImp realAPI;
    private DataProcessingAPI api;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        prototype = new DataProcessingPrototype();
        realAPI = new DataProcessingImp(); 
    }

    @Test
    public void testReadSuccess() {
        List<Integer> mockData = Arrays.asList(1, 2, 3);
        ReadResult mockReadResult = new ReadResultImp(ReadResult.Status.SUCCESS, mockData);
        when(mockAPI.read(any(InputConfig.class))).thenReturn(mockReadResult);

        WriteResult mockWriteResult = new WriteResultImp(WriteResult.WriteResultStatus.SUCCESS);
        when(mockAPI.appendSingleResult(any(OutputConfig.class), anyString(), eq(','))).thenReturn(mockWriteResult);

        prototype.prototype(mockAPI);
        
        verify(mockAPI).read(any(InputConfig.class));

        for (Integer value : mockData) {
            verify(mockAPI).appendSingleResult(any(OutputConfig.class), eq(String.valueOf(value)), eq(','));
        }
        verify(mockAPI, times(mockData.size())).appendSingleResult(any(OutputConfig.class), anyString(), eq(','));
    }

    @Test
    public void testReadFailure_WithInvalidInput() {
        ReadResult mockReadResult = new ReadResultImp(ReadResult.Status.FAILURE, null);
        when(mockAPI.read(any(InputConfig.class))).thenReturn(mockReadResult);

        prototype.prototype(mockAPI);

        verify(mockAPI).read(any(InputConfig.class));
        verify(mockAPI, never()).appendSingleResult(any(OutputConfig.class), anyString(), anyChar());
    }

    @Test
    public void testWriteFailure_WhenAppendFails() {
        ReadResult mockReadResult = new ReadResultImp(ReadResult.Status.SUCCESS, Arrays.asList(1));
        when(mockAPI.read(any(InputConfig.class))).thenReturn(mockReadResult);

        WriteResult mockWriteResult = new WriteResultImp(WriteResult.WriteResultStatus.FAILURE);
        when(mockAPI.appendSingleResult(any(OutputConfig.class), anyString(), eq(','))).thenReturn(mockWriteResult);

        prototype.prototype(mockAPI);

        verify(mockAPI).read(any(InputConfig.class));
        verify(mockAPI).appendSingleResult(any(OutputConfig.class), anyString(), eq(','));
    }

    @Test
    public void testRealImplementation_ReadReturnsValidResult() {
        InputConfig inputConfig = new InputConfig() {

			@Override
			public String getInputData() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getFilePath() {
				// TODO Auto-generated method stub
				return null;
			}
			};  
        ReadResult result = realAPI.read(inputConfig);

        assertNotNull("Read result should not be null", result);
        assertNull("Initial implementation should return null results", result.getResults());
    }

    @Test
    public void testRealImplementation_WriteReturnsValidResult() {
        OutputConfig outputConfig = new OutputConfig() {

			@Override
			public String getFilePath() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String formatOutput(String result) {
				// TODO Auto-generated method stub
				return null;
			}
			};  
        String testData = "test";
        char delimiter = ',';

        WriteResult result = realAPI.appendSingleResult(outputConfig, testData, delimiter);

        assertNotNull("Write result should not be null", result);
    }

    @Test
    public void testSimple_ReadReturnsNonNullResult() {
        DataProcessingAPI api = new DataProcessingImp();
        ReadResult result = api.read(null);

        assertNotNull("Read result should not be null", result);
    }
}
