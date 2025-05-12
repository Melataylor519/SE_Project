import static org.junit.jupiter.api.Assertions.assertEquals;

import computecomponents.CoordinationComponent;
import datastorecomponents.DataProcessingAPI;
import datastorecomponents.ReadResult;
import datastorecomponents.WriteResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class TestCoordinationComponent {

    private CoordinationComponent coordinationComponent;
    private DataProcessingAPI mockDataStorage;

    @BeforeEach
    public void setUp() {
        mockDataStorage = Mockito.mock(DataProcessingAPI.class);
        coordinationComponent = new CoordinationComponent(Mockito.mock(UserComputeEngineAPI.class), mockDataStorage);
    }

    @Test
    public void testHandleComputationSuccess() {
        // Mock the read result with valid primitive data
        int[] mockData = {1, 2, 3, 4};
        Mockito.when(mockDataStorage.read(Mockito.any()))
            .thenReturn(new ReadResult(ReadResult.Status.SUCCESS, mockData));

        // Mock the write result as successful
        Mockito.when(mockDataStorage.appendSingleResult(Mockito.any(), Mockito.anyString(), Mockito.anyChar()))
            .thenReturn(new WriteResult(WriteResult.WriteResultStatus.SUCCESS));

        String result = coordinationComponent.handleComputation("input.txt", "output.txt");
        assertEquals("Computation successful. Result: [mocked-result]", result);
    }

    @Test
    public void testHandleComputationNoInputData() {
        // Mock an empty primitive array
        Mockito.when(mockDataStorage.read(Mockito.any()))
            .thenReturn(new ReadResult(ReadResult.Status.SUCCESS, new int[0]));

        String result = coordinationComponent.handleComputation("input.txt", "output.txt");
        assertEquals("Error: No valid input data.", result);
    }
}
