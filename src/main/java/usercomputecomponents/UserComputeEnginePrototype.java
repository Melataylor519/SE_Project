package usercomputecomponents;

import projectannotations.NetworkAPIPrototype;

public class UserComputeEnginePrototype implements UserComputeEngineAPI {

    private static final String[] DEFAULT_DELIMITERS = {",", ";", " "};
    private static final String TARGET = "localhost:50051";

    @NetworkAPIPrototype
    @Override
    public void processData(String inputSource, String outputSource, String[] delimiters) {
        // Use default delimiters if none are provided
        if (delimiters == null || delimiters.length == 0) {
            delimiters = DEFAULT_DELIMITERS;
        }

        ManagedChannel channel = Grpc.newChannelBuilder(TARGET, InsecureChannelCredentials.create()).build();
        DataStoreClient client = new DataStoreClient(channel);

        try {
            // Read data from datastore via gRPC
            String rawData = readData(client, inputSource);

            // Process data
            String processedData = process(rawData, delimiters);

            // Write processed data to datastore via gRPC
            writeData(client, outputSource, processedData);

        } finally {
            try {
                channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public String readData(DataStoreClient client, String source) {
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

    public void writeData(DataStoreClient client, String destination, String data) {
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
