import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TestMultiUser {
    private ComputationCoordinator coordinator;

    @BeforeEach
    public void initializeComputeEngine() {
        ComputeEngine engine = new ComputeEngineImpl();
        DataStore dataStore = new InMemoryDataStore();
        coordinator = new MultiThreadedCoordinator(engine, dataStore);
    }

    @Test
    public void compareMultiAndSingleThreaded() throws IOException {
        int nThreads = 4;
        List<TestUser> testUsers = new ArrayList<>();
        for (int i = 0; i < nThreads; i++) {
            testUsers.add(new TestUser(coordinator));
        }

        String singleThreadFilePrefix = "testMultiUser.compareMultiAndSingleThreaded.test.singleThreadOut.tmp";
        for (int i = 0; i < nThreads; i++) {
            File singleThreadedOut = new File(singleThreadFilePrefix + i);
            singleThreadedOut.deleteOnExit();
            testUsers.get(i).run(singleThreadedOut.getCanonicalPath());
        }

        ExecutorService threadPool = Executors.newFixedThreadPool(nThreads);
        List<Future<?>> results = new ArrayList<>();
        String multiThreadFilePrefix = "testMultiUser.compareMultiAndSingleThreaded.test.multiThreadOut.tmp";
        for (int i = 0; i < nThreads; i++) {
            File multiThreadedOut = new File(multiThreadFilePrefix + i);
            multiThreadedOut.deleteOnExit();
            String multiThreadOutputPath = multiThreadedOut.getCanonicalPath();
            TestUser testUser = testUsers.get(i);
            results.add(threadPool.submit(() -> testUser.run(multiThreadOutputPath)));
        }

        results.forEach(future -> {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

        List<String> singleThreaded = loadAllOutput(singleThreadFilePrefix, nThreads);
        List<String> multiThreaded = loadAllOutput(multiThreadFilePrefix, nThreads);
        Assert.assertEquals(singleThreaded, multiThreaded);
    }

    private List<String> loadAllOutput(String prefix, int nThreads) throws IOException {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < nThreads; i++) {
            File multiThreadedOut = new File(prefix + i);
            result.addAll(Files.readAllLines(multiThreadedOut.toPath()));
        }
        return result;
    }
}
