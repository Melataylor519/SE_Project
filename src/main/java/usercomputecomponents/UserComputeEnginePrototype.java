package usercomputecomponents;

import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;

import projectannotations.NetworkAPIPrototype;
import datastorecomponents.DataStoreClient;
import datastorecomponents.InputConfig;
import datastorecomponents.FileInputConfig;
import datastorecomponents.OutputConfig;
import datastorecomponents.FileOutputConfig;
import datastorecomponents.ReadResult;
import datastorecomponents.WriteResult;

import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import datastorecomponents.DataProcessingAPI;

public class UserComputeEnginePrototype implements UserComputeEngineAPI {

    private static final String[] DEFAULT_DELIMITERS = {",", ";", " "};
    private static final String TARGET = "localhost:50051";

    @NetworkAPIPrototype
    @Override
    public void processData(DataProcessingAPI client, String inputSource, String outputSource, String[] delimiters) {
        if (delimiters == null || delimiters.length == 0) {
            delimiters = DEFAULT_DELIMITERS;
        }

        String rawData = readData(client, inputSource);
        if (rawData == null || rawData.isEmpty()) {
            System.err.println("No data read. Skipping processing.");
            return;
        }

        String processedData = process(rawData, delimiters);
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

    private String process(String data, String[] delimiters) {
        // If delimiters are not provided, use default delimiters
        if (delimiters == null || delimiters.length == 0) {
            delimiters = DEFAULT_DELIMITERS;
        }

        // Replace each delimiter with a standard delimiter (like a single space)
        for (String delimiter : delimiters) {
            data = data.replace(delimiter, " ");
        }

        return data.trim();
    }
}
