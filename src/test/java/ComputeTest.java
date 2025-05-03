import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ComputeTest {
    @Test
    public void smokeTestCompute() {
        ComputeEngine engine = new ComputeEngineImpl();
        Assertions.assertEquals("1", engine.compute(1));
    }

    @Test
    public void testInvalidInput() {
        ComputeEngine engine = new ComputeEngineImpl();
        Assertions.assertTrue(engine.compute(0).startsWith("ERROR"));
        Assertions.assertTrue(engine.compute(-100).startsWith("ERROR"));
    }
}