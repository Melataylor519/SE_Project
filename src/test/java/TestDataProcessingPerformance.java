import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import datastorecomponents.FileInputConfig;
import datastorecomponents.InputConfig;
import datastorecomponents.FileDataProcessing;
import datastorecomponents.FileDataProcessingOld;
import datastorecomponents.ReadResult; 
import datastorecomponents.ReadResultImp;
import datastorecomponents.ReadResult.Status;

public class TestDataProcessingPerformance {

    private static final String TEST_FILE_PATH = "TestInputFile.test";

    @BeforeAll
    public static void generateLargeTestFile() throws IOException {
        if (!Files.exists(Paths.get(TEST_FILE_PATH))) {
            List<String> lines = new ArrayList<>();
            for (int i = 0; i < 100_000; i++) {
                lines.add(String.valueOf(i));
            }
            Files.write(Paths.get(TEST_FILE_PATH), lines);
        }
    }

    private static InputConfig createInputConfig() {
        return new FileInputConfig(TEST_FILE_PATH);
    }

    @Test
    public void testCompareOldAndNewPerformance() {
        InputConfig input = createInputConfig();

        // Simulate "old" version (you can rename or copy your old class)
        FileDataProcessingOld oldVersion = new FileDataProcessingOld();

        // Optimized version with streaming
        FileDataProcessing newVersion = new FileDataProcessing();

        // Warm-up both
        oldVersion.read(input);
        newVersion.read(input);

        try {
            generateLargeTestFile();
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to generate large test file");
        } finally {
            // Time old version
            long startOld = System.nanoTime();
            ReadResult oldResult = oldVersion.read(input);
            long endOld = System.nanoTime();

            // Time new version
            long startNew = System.nanoTime();
            ReadResult newResult = newVersion.read(input);
            long endNew = System.nanoTime();

            // Validate data consistency
            assertEquals(oldResult.getStatus(), ReadResult.Status.SUCCESS);
            assertEquals(newResult.getStatus(), ReadResult.Status.SUCCESS);
            // assertEquals(oldResult.getData(), newResult.getData());

            // Compare times
            long oldTime = endOld - startOld;
            long newTime = endNew - startNew;

            double improvement = ((double)(oldTime - newTime) / oldTime) * 100;

            System.out.printf("Old: %d ns, New: %d ns, Improvement: %.2f%%\n", oldTime, newTime, improvement);
        }
    }
}
