import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.*;

import computecomponents.CoordinationComponent;
import computecomponents.CoordinationComponentOld;
import datastorecomponents.*;

import usercomputecomponents.UserComputeEnginePrototype;

public class TestCoordinationComponentPerformance {

    private static final int REPEAT = 30;
    private static final int TEST_DATA_SIZE = 5_000_000;

    private static Path inputFilePath;
    private static Path outputFilePathOld;
    private static Path outputFilePathNew;

    @BeforeAll
    public static void setUp() throws IOException {
        // Generate large test input data
        String inputData = IntStream.range(0, TEST_DATA_SIZE)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(";"));

        inputFilePath = Files.createTempFile("test_input", ".txt");
        Files.writeString(inputFilePath, inputData, StandardOpenOption.TRUNCATE_EXISTING);

        outputFilePathOld = Files.createTempFile("test_output_old", ".txt");
        Files.writeString(outputFilePathOld, "", StandardOpenOption.TRUNCATE_EXISTING);

        outputFilePathNew = Files.createTempFile("test_output_new", ".txt");
        Files.writeString(outputFilePathNew, "", StandardOpenOption.TRUNCATE_EXISTING);
    }

    @AfterAll
    public static void tearDown() throws IOException {
        Files.deleteIfExists(inputFilePath);
        Files.deleteIfExists(outputFilePathOld);
        Files.deleteIfExists(outputFilePathNew);
    }

    @Test
    public void testPerformanceImprovement() throws IOException {
        DataProcessingAPI dataStoreOld = new DataProcessingImp();
        DataProcessingAPI dataStoreNew = new DataProcessingImp();
        UserComputeEnginePrototype userComputeEngine = new UserComputeEnginePrototype();

        CoordinationComponentOld oldComponent =
                new CoordinationComponentOld(userComputeEngine, dataStoreOld);
        CoordinationComponent newComponent =
                new CoordinationComponent(userComputeEngine, dataStoreNew);

        long totalOld = 0;
        long totalNew = 0;

        for (int i = 0; i < REPEAT; i++) {
            // Reset output files
            Files.writeString(outputFilePathOld, "", StandardOpenOption.TRUNCATE_EXISTING);
            Files.writeString(outputFilePathNew, "", StandardOpenOption.TRUNCATE_EXISTING);

            String inputPath = inputFilePath.toString();
            String outputPathOld = outputFilePathOld.toString();
            String outputPathNew = outputFilePathNew.toString();

            long startOld = System.nanoTime();
            oldComponent.handleComputation(inputPath, outputPathOld);
            long endOld = System.nanoTime();

            long startNew = System.nanoTime();
            newComponent.handleComputation(inputPath, outputPathNew);
            long endNew = System.nanoTime();

            long timeOld = (endOld - startOld) / 1_000_000;
            long timeNew = (endNew - startNew) / 1_000_000;

            totalOld += timeOld;
            totalNew += timeNew;

            System.out.println("Run " + (i + 1) + ": Old = " + timeOld + " ms, New = " + timeNew + " ms, Δ = " +
                    String.format("%.2f", percentImprovement(timeOld, timeNew)) + "%");
        }

        long avgOld = totalOld / REPEAT;
        long avgNew = totalNew / REPEAT;
        double improvement = percentImprovement(avgOld, avgNew);

        System.out.println("\n========== Improvement Result ==========");
        System.out.println("Average Old Time: " + avgOld + " ms");
        System.out.println("Average New Time: " + avgNew + " ms");
        System.out.println("Improvement: " + String.format("%.2f", improvement) + "%");

        Assertions.assertTrue(improvement >= 10,
                "Expected at least 10% performance improvement, but got: " + improvement + "%");
    }

    private double percentImprovement(long oldTime, long newTime) {
        return ((double) (oldTime - newTime) / oldTime) * 100;
    }
}
