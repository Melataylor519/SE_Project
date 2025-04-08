package datastorecomponents;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TestDataProcessingAPI {

    @Mock
    private DataProcessingAPI mockAPI;

    private DataProcessingPrototype prototype;
    private DataProcessingImp validator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        prototype = new DataProcessingPrototype();
        validator = new DataProcessingImp(mockAPI);
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
        ReadResult mockReadResult = new ReadResultImp(ReadResult.Status.FAILURE, Collections.emptyList());
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
    public void testValidatorReadValidation() {
        InputConfig validConfig = new SimpleInputConfig("/test/path.txt");
        ReadResult expectedResult = new ReadResultImp(ReadResult.Status.SUCCESS, Collections.emptyList());
        when(mockAPI.read(validConfig)).thenReturn(expectedResult);

        ReadResult result = validator.read(validConfig);

        assertEquals(ReadResult.Status.FAILURE, result.getStatus(), "Should fail for non-existent file");
        assertFalse(result.getResults().iterator().hasNext(), "Should have no results for failure");
    }

    @Test
    public void testValidatorWriteValidation() {
        OutputConfig validConfig = new SimpleOutputConfig("/test/path.txt");
        String testData = "test";
        char delimiter = ',';

        WriteResult result = validator.appendSingleResult(validConfig, testData, delimiter);

        assertEquals(WriteResult.WriteResultStatus.FAILURE, result.getStatus(),
                "Should fail for non-existent directory");
    }
}
