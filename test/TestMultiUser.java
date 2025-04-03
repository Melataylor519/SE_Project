package test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import src.projectannotations.MultiThreadedNetworkAPI;
import src.computecomponents.ComputeRequest;
import src.computecomponents.ComputeResponse;
import src.datastorecomponents.InputConfig;
import src.datastorecomponents.OutputConfig;

public class TestMultiUser {
    // Use MultiThreadedNetworkAPI to simulate requests
    private MultiThreadedNetworkAPI coordinator;

    @BeforeEach
    public void initializeComputeEngine() {
        // Initialize MultiThreadedNetworkAPI
        coordinator = new MultiThreadedNetworkAPI();
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
            File singleThreadedOut =
                    new File(singleThreadFilePrefix + i);
            singleThreadedOut.deleteOnExit();
            testUsers.get(i).run(singleThreadedOut.getCanonicalPath());
        }

        // Run multi-threaded using MultiThreadedNetworkAPI
        ExecutorService threadPool = java.util.concurrent.Executors.newCachedThreadPool();
        List<Future<?>> results = new ArrayList<>();
        String multiThreadFilePrefix = "testMultiUser.compareMultiAndSingleThreaded.test.multiThreadOut.tmp";
        for (int i = 0; i < numThreads; i++) {
            File multiThreadedOut =
                    new File(multiThreadFilePrefix + i);
            multiThreadedOut.deleteOnExit();
            String multiThreadOutputPath = multiThreadedOut.getCanonicalPath();
            TestUser testUser = testUsers.get(i);
            
            // Creating mock InputConfig and OutputConfig for each user
            InputConfig inputConfig = new InputConfig() {
                @Override
                public String getInputData() {
                    return "Mock Input Data for User " + i;
                }

                @Override
                public String getFilePath() {
                    return "mock/input/path" + i;
                }
            };
            
            OutputConfig outputConfig = new OutputConfig() {
                @Override
                public String getFilePath() {
                    return "mock/output/path" + i;
                }

                @Override
                public String formatOutput(String result) {
                    return "Formatted Output: " + result;
                }
            };

            // Create the ComputeRequest using mock configurations
            ComputeRequest request = new ComputeRequest(inputConfig, outputConfig);
            
            // Submit the request to the multi-threaded API
            results.add(threadPool.submit(() -> {
                Future<ComputeResponse> futureResponse = MultiThreadedNetworkAPI.processRequest(request);
                try {
                    ComputeResponse response = futureResponse.get(); // Wait for the response
                    // Simulate the user’s computation output
                    testUser.run(multiThreadOutputPath); // Use the response if necessary to customize output
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));
        }

        // Wait for all threads to finish
        for (Future<?> future : results) {
            future.get();
        }

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
