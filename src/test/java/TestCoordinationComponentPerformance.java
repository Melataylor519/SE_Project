import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import computecomponents.CoordinationComponent;
import computecomponents.CoordinationComponentOld;
import datastorecomponents.DataProcessingAPI;
import datastorecomponents.InputConfig;
import datastorecomponents.OutputConfig;
import datastorecomponents.ReadResult;
import datastorecomponents.ReadResultImp;
import datastorecomponents.WriteResult;
import datastorecomponents.WriteResultImp;
import usercomputecomponents.UserComputeEngineAPI;

public class TestCoordinationComponentPerformance {

    private static final int REPEAT = 5;
    private static final int TEST_DATA_SIZE = 5_000_000;

    private static DataProcessingAPI mockDataStorage;
    private static UserComputeEngineAPI mockUserComputeEngine;
    private static Iterable<Integer> testData;

    @BeforeAll
    public static void setUp() {
        List<Integer> data = new ArrayList<>();
        for (int i = 0; i < TEST_DATA_SIZE; i++) {
            data.add(i);
        }
        testData = data;

        mockDataStorage = Mockito.mock(DataProcessingAPI.class);
        mockUserComputeEngine = Mockito.mock(UserComputeEngineAPI.class);

        Mockito.when(mockDataStorage.read(Mockito.any(InputConfig.class)))
                .thenReturn(new ReadResultImp(ReadResult.Status.SUCCESS, testData));

        Mockito.when(mockDataStorage.appendSingleResult(
                Mockito.any(OutputConfig.class),
                Mockito.anyString(),
                Mockito.anyChar()))
                .thenReturn(new WriteResultImp(WriteResult.WriteResultStatus.SUCCESS));
    }

    @Test
    public void testPerformanceImprovement() {
        CoordinationComponentOld oldComponent =
                new CoordinationComponentOld(mockUserComputeEngine, mockDataStorage);
        CoordinationComponent newComponent =
                new CoordinationComponent(mockUserComputeEngine, mockDataStorage);

        long totalOld = 0;
        long totalNew = 0;

        for (int i = 0; i < REPEAT; i++) {
            long startOld = System.nanoTime();
            oldComponent.handleComputation("input", "output");
            long endOld = System.nanoTime();

            long startNew = System.nanoTime();
            newComponent.handleComputation("input", "output");
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

