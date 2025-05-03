import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class DataStoreTest {

    @Test
    public void smokeTestRead() throws Exception {
        InputConfig inputConfig = Mockito.mock(InputConfig.class);
        DataStore dataStore = new InMemoryDataStore();
        Assertions.assertEquals(
            DataStoreReadResult.Status.SUCCESS,
            dataStore.read(inputConfig).getStatus()
        );
    }

    @Test
    public void smokeTestWrite() throws Exception {
        OutputConfig outputConfig = Mockito.mock(OutputConfig.class);
        Mockito.when(outputConfig.getUserId()).thenReturn("testUser");
        DataStore dataStore = new InMemoryDataStore();
        Assertions.assertEquals(
            WriteResult.WriteResultStatus.SUCCESS,
            dataStore.appendSingleResult(outputConfig, "result", ';').getStatus()
        );
    }
}
