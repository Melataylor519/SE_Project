package usercomputecomponents;

import projectannotations.NetworkAPIPrototype;
import datastorecomponents.DataProcessingAPI;
import datastorecomponents.InputConfig;
import datastorecomponents.FileInputConfig;
import datastorecomponents.OutputConfig;
import datastorecomponents.FileOutputConfig;
import datastorecomponents.ReadResult;
import datastorecomponents.WriteResult;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserComputeEnginePrototype implements UserComputeEngineAPI {

    private static final String[] DEFAULT_DELIMITERS = {",", ";", " "};
    private static final String TARGET = "localhost:50052";

    private final ExecutorService userThreadPool = Executors.newFixedThreadPool(10);

    @NetworkAPIPrototype
    @Override
    public void processData(DataProcessingAPI client, String inputSource, String outputSource, String[] delimiters) {
        userThreadPool.submit(() -> handleDataProcessing(client, inputSource, outputSource, delimiters));
    }

    private void handleDataProcessing(DataProcessingAPI client, String inputSource, String outputSource, String[] delimiters) {
        if (delimiters == null || delimiters.length == 0) {
            delimiters = DEFAULT_DELIMITERS;
        }

        String rawData = readData(client, inputSource);
        if (rawData == null || rawData.isEmpty()) {
            System.err.println("No data read. Skipping processing.");
            return;
        }

        // Parallel processing stage
        String processedData = processParallel(rawData, delimiters);

        writeData(client, outputSource, processedData);
    }

    public String readData(DataProcessingAPI client, String source) {
        System.out.println("Reading data from " + source);
        try {
            InputConfig input = new FileInputConfig(source);
            ReadResult result = client.read(input);

            if (result.getStatus() == ReadResult.Status.SUCCESS && result.getResults() != null) {
                return StreamSupport.stream(result.getResults().spliterator(), false)
                        .map(String::valueOf)
                        .collect(Collectors.joining(" "));
            } else {
                System.err.println("Failed to read data or file not found: " + source);
                return "";
            }
        } catch (Exception e) {
            System.err.println("Exception occurred during read operation:");
            e.printStackTrace();
            return "";
        }
    }

    public void writeData(DataProcessingAPI client, String destination, String data) {
        System.out.println("Writing to " + destination + ": " + data);
        OutputConfig output = new FileOutputConfig(destination);

        WriteResult result = client.appendSingleResult(output, data, ' ');
        if (result.getStatus() != WriteResult.WriteResultStatus.SUCCESS) {
            System.err.println("Failed to write data to " + destination);
        }
    }

    // Split data into lines and process in parallel
    private String processParallel(String data, String[] delimiters) {
        return List.of(data.split("\n")).parallelStream()
                .map(line -> process(line, delimiters))
                .collect(Collectors.joining(" "));
    }

    // Simple processor: replaces delimiters with space
    private String process(String data, String[] delimiters) {
        for (String delimiter : delimiters) {
            data = data.replace(delimiter, " ");
        }
        return data.trim();
    }

    public void shutdown() {
        userThreadPool.shutdown();
        try {
            if (!userThreadPool.awaitTermination(5, TimeUnit.SECONDS)) {
                userThreadPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            userThreadPool.shutdownNow();
        }
    }
}
