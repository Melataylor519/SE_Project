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
    // Use UserComputeEngineAPI for the coordinator
    private UserComputeEngineAPI coordinator;

    @BeforeEach
    public void initializeComputeEngine() {
        // Initialize UserComputeEngineAPI
        coordinator = new UserComputeEnginePrototype();
    } 

    @Test
    public void compareMultiAndSingleThreaded() throws Exception {
        int numThreads = 4;
        List<TestUser> testUsers = new ArrayList<>();
        for (int i = 0; i < numThreads; i++) {
            testUsers.add(new TestUser(coordinator));
        }

        // Run single-threaded
        String singleThreadFilePrefix = "testMultiUser.compareMultiAndSingleThreaded.test.singleThreadOut.tmp";
        for (int i = 0; i < numThreads; i++) {
            File singleThreadedOut = new File(singleThreadFilePrefix + i);
            singleThreadedOut.createNewFile();
            singleThreadedOut.deleteOnExit();
            testUsers.get(i).run(singleThreadedOut.getCanonicalPath());
        }

        // Run multi-threaded using MultiThreadedNetworkAPI
        List<Runnable> tasks = new ArrayList<>();
        String multiThreadFilePrefix = "testMultiUser.compareMultiAndSingleThreaded.test.multiThreadOut.tmp";
        for (int i = 0; i < numThreads; i++) {
            File multiThreadedOut = new File(multiThreadFilePrefix + i);
            multiThreadedOut.createNewFile();        
            multiThreadedOut.deleteOnExit();
            String multiThreadOutputPath = multiThreadedOut.getCanonicalPath();
            TestUser testUser = testUsers.get(i);
            
            tasks.add(() -> {
                testUser.run(multiThreadOutputPath);
            });
        }

        List<Future<?>> results = MultiThreadedNetworkAPI.runMultiThreaded(tasks);

        // Wait for all threads to finish
        results.forEach(future -> {
			try {
				future.get();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
        
        // Check that the output is the same for multi-threaded and single-threaded
        List<String> singleThreaded = loadAllOutput(singleThreadFilePrefix, numThreads);
        List<String> multiThreaded = loadAllOutput(multiThreadFilePrefix, numThreads);
        Assert.assertEquals(singleThreaded, multiThreaded);
    }

    private List<String> loadAllOutput(String prefix, int numThreads) throws IOException {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < numThreads; i++) {
            File multiThreadedOut =
                    new File(prefix + i);
            result.addAll(Files.readAllLines(multiThreadedOut.toPath()));
        }
        return result;
    }
}
