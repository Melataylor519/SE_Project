import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CoordinatorTest {
    @Test
    public void smokeTest() {
        try {
            DataStore dataStore = mock(DataStore.class);
            ComputeEngine computeEngine = mock(ComputeEngine.class);

            when(dataStore.read(any(InputConfig.class)))
                .thenReturn(new DataStoreReadResult(DataStoreReadResult.Status.SUCCESS, new ArrayList<Integer>()));

            ComputationCoordinator coord = new CoordinatorImpl(computeEngine, dataStore);

            ComputeRequest mockRequest = mock(ComputeRequest.class);
            when(mockRequest.getInputConfig()).thenReturn(mock(InputConfig.class));
            when(mockRequest.getOutputConfig()).thenReturn(mock(OutputConfig.class));
            when(mockRequest.getDelimiter()).thenReturn(',');

            ComputeResult result = coord.compute(mockRequest);

            Assertions.assertEquals(result.getStatus(), ComputeResult.ComputeResultStatus.SUCCESS);
        } catch (Exception e) {
            Assertions.fail("Exception occurred: " + e.getMessage());
        }
    }
}