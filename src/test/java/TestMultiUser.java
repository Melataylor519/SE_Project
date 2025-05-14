import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import projectannotations.MultiThreadedNetworkAPI;
import usercomputecomponents.UserComputeEngineAPI;
import usercomputecomponents.UserComputeEnginePrototype;

public class TestMultiUser {
    private UserComputeEngineAPI coordinator;
    private final String inputFilePrefix = "testMultiUser.compareMultiAndSingleThreaded.test.input.tmp";
    private final String singleThreadFilePrefix = "testMultiUser.compareMultiAndSingleThreaded.test.singleThreadOut.tmp";
    private final String multiThreadFilePrefix = "testMultiUser.compareMultiAndSingleThreaded.test.multiThreadOut.tmp";

    @BeforeEach
    public void initializeComputeEngine() {
        coordinator = new UserComputeEnginePrototype();
    }

    @AfterEach
    public void cleanup() throws IOException {
        for (int i = 0; i < 4; i++) {
            Files.deleteIfExists(new File(inputFilePrefix + i).toPath());
            Files.deleteIfExists(new File(singleThreadFilePrefix + i).toPath());
            Files.deleteIfExists(new File(multiThreadFilePrefix + i).toPath());
        }
    }
    
    @Test
    public void compareMultiAndSingleThreaded() throws Exception {
        int numThreads = 4;
        List<TestUser> testUsers = new ArrayList<>();
        char delimiter = ';';

        // Create input files and populate them with data
        for (int i = 0; i < numThreads; i++) {
            File inputFile = new File(inputFilePrefix + i);
            inputFile.createNewFile();
            inputFile.deleteOnExit();

            try (FileWriter writer = new FileWriter(inputFile)) {
                writer.write("Sample data for thread " + i + "\n");
            }

            testUsers.add(new TestUser(coordinator, inputFile.getCanonicalPath(), delimiter));
        }

        // Run single-threaded
        for (int i = 0; i < numThreads; i++) {
            File singleThreadedOut = new File(singleThreadFilePrefix + i);
            singleThreadedOut.createNewFile();
            singleThreadedOut.deleteOnExit();
            testUsers.get(i).run(singleThreadedOut.getCanonicalPath());
        }

        // Run multi-threaded
        List<Runnable> tasks = new ArrayList<>();
        for (int i = 0; i < numThreads; i++) {
            File multiThreadedOut = new File(multiThreadFilePrefix + i);
            multiThreadedOut.createNewFile();
            multiThreadedOut.deleteOnExit();
            final String outputPath = multiThreadedOut.getCanonicalPath();
            final int threadIndex = i;
            tasks.add(() -> {
                try {
                    testUsers.get(threadIndex).run(outputPath);
                } catch (Exception e) {
                    System.err.println("Error in thread " + threadIndex + " execution: " + e.getMessage());
                    e.printStackTrace();
                }
            });
        }

        List<Future<?>> results = MultiThreadedNetworkAPI.runMultiThreaded(tasks);
        List<Exception> exceptions = new ArrayList<>();
        results.forEach(future -> {
            try {
                future.get();
            } catch (Exception e) {
                exceptions.add(e);
                System.err.println("Error in thread execution: " + e.getMessage());
                e.printStackTrace();
            }
        });

        if (!exceptions.isEmpty()) {
            Assert.fail("Errors occurred in multi-threaded execution: " + exceptions);
        }

        // Validate outputs
        List<String> singleThreaded = loadAllOutput(singleThreadFilePrefix, numThreads);
        List<String> multiThreaded = loadAllOutput(multiThreadFilePrefix, numThreads);
        Assert.assertEquals(singleThreaded, multiThreaded);
    }

    private List<String> loadAllOutput(String prefix, int numThreads) throws IOException {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < numThreads; i++) {
            File multiThreadedOut = new File(prefix + i);
            result.addAll(Files.readAllLines(multiThreadedOut.toPath()));
        }
        return result;
    }
}
