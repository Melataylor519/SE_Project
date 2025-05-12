import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import projectannotations.MultiThreadedNetworkAPI;
import usercomputecomponents.UserComputeEngineAPI;
import usercomputecomponents.UserComputeEnginePrototype;

public class TestMultiUser {
    private UserComputeEngineAPI coordinator;

    @BeforeEach
    public void initializeComputeEngine() {
        coordinator = new UserComputeEnginePrototype();
    }

    @Test
    public void compareMultiAndSingleThreaded() throws Exception {
        int numThreads = 4;
        List<TestUser> testUsers = new ArrayList<>();
        for (int i = 0; i < numThreads; i++) {
            testUsers.add(new TestUser(coordinator));
        }

        // Fix input wiring
        String inputFilePath = "input/test_input.txt";
        Files.writeString(new File(inputFilePath).toPath(), "1;2;3;4");

        // Run single-threaded
        String singleThreadFilePrefix = "output/singleThreadOutput";
        for (int i = 0; i < numThreads; i++) {
            String outputPath = singleThreadFilePrefix + i + ".txt";
            testUsers.get(i).run(inputFilePath, outputPath);
        }

        // Run multi-threaded using MultiThreadedNetworkAPI
        List<Runnable> tasks = new ArrayList<>();
        String multiThreadFilePrefix = "output/multiThreadOutput";
        for (int i = 0; i < numThreads; i++) {
            String outputPath = multiThreadFilePrefix + i + ".txt";
            TestUser testUser = testUsers.get(i);
            tasks.add(() -> testUser.run(inputFilePath, outputPath));
        }

        List<Future<?>> results = MultiThreadedNetworkAPI.runMultiThreaded(tasks);
        for (Future<?> future : results) {
            future.get();
        }

        // Verify outputs
        List<String> singleThreaded = loadAllOutput(singleThreadFilePrefix, numThreads);
        List<String> multiThreaded = loadAllOutput(multiThreadFilePrefix, numThreads);
        Assert.assertEquals(singleThreaded, multiThreaded);
    }

    private List<String> loadAllOutput(String prefix, int numThreads) throws IOException {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < numThreads; i++) {
            result.addAll(Files.readAllLines(new File(prefix + i + ".txt").toPath()));
        }
        return result;
    }
}
