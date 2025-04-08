import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import projectannotations.MultiThreadedNetworkAPI;
import usercomputecomponents.UserComputeEngineAPI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestMultiUser {
    @Mock
    private UserComputeEngineAPI coordinator;

    @TempDir
    Path tempDir;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void compareMultiAndSingleThreaded() throws Exception {
        int numThreads = 4;
        List<String> singleThreadedResults = new ArrayList<>();
        List<String> multiThreadedResults = new ArrayList<>();

        // Setup mock behavior with proper file paths
        doNothing().when(coordinator).processData(anyString(), anyString(), any(String[].class));

        // Run single-threaded with proper paths
        for (int i = 0; i < numThreads; i++) {
            Path inputPath = tempDir.resolve("input" + i + ".txt");
            Path outputPath = tempDir.resolve("output" + i + ".txt");
            coordinator.processData(inputPath.toString(), outputPath.toString(), new String[] { "," });
            singleThreadedResults.add("test result");
        }

        // Run multi-threaded using MultiThreadedNetworkAPI with proper paths
        List<Runnable> tasks = new ArrayList<>();
        for (int i = 0; i < numThreads; i++) {
            final int index = i;
            tasks.add(() -> {
                Path inputPath = tempDir.resolve("input" + index + ".txt");
                Path outputPath = tempDir.resolve("output" + index + ".txt");
                coordinator.processData(inputPath.toString(), outputPath.toString(), new String[] { "," });
                multiThreadedResults.add("test result");
            });
        }

        List<Future<?>> results = MultiThreadedNetworkAPI.runMultiThreaded(tasks);

        // Wait for all threads to finish
        for (Future<?> future : results) {
            future.get();
        }

        // Verify results
        assertEquals(singleThreadedResults.size(), multiThreadedResults.size(),
                "Should have same number of results");
        assertEquals(singleThreadedResults, multiThreadedResults,
                "Single-threaded and multi-threaded results should be equal");
        verify(coordinator, times(numThreads * 2)).processData(anyString(), anyString(), any(String[].class));
    }

    @Test
    public void testMultiUserComputation() {
        int numUsers = 3;

        // Setup mock behavior with proper paths
        doNothing().when(coordinator).processData(anyString(), anyString(), any(String[].class));

        // Test single-threaded computation
        for (int i = 0; i < numUsers; i++) {
            Path inputPath = tempDir.resolve("input" + i + ".txt");
            Path outputPath = tempDir.resolve("output" + i + ".txt");
            coordinator.processData(inputPath.toString(), outputPath.toString(), new String[] { "," });
        }

        // Test multi-threaded computation
        for (int i = 0; i < numUsers; i++) {
            Path inputPath = tempDir.resolve("input" + i + ".txt");
            Path outputPath = tempDir.resolve("output" + i + ".txt");
            coordinator.processData(inputPath.toString(), outputPath.toString(), new String[] { "," });
        }

        // Verify mock was called the expected number of times
        verify(coordinator, times(numUsers * 2)).processData(anyString(), anyString(), any(String[].class));
    }
}