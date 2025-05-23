package projectannotations;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import datastore.DataProcessingGrpc;
import datastore.DatastoreProto;
import datastorecomponents.DataProcessingAPI;
import datastorecomponents.FileInputConfig;
import datastorecomponents.FileOutputConfig;
import datastorecomponents.InputConfig;
import datastorecomponents.OutputConfig;
import datastorecomponents.ReadResult;
import datastorecomponents.ReadResultImp;
import datastorecomponents.WriteResult;
import datastorecomponents.WriteResultImp;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class DataStoreClient implements DataProcessingAPI {
    private final DataProcessingGrpc.DataProcessingBlockingStub blockingStub;

    public DataStoreClient(ManagedChannel channel) {
        blockingStub = DataProcessingGrpc.newBlockingStub(channel);
    }

    public static DataStoreClient connect(String target) {
        ManagedChannel channel = ManagedChannelBuilder.forTarget(target)
                .usePlaintext()
                .build();
        return new DataStoreClient(channel);
    }

    @Override
    public ReadResult read(InputConfig input) {
        try {
            // Convert custom InputConfig to proto InputConfig
            DatastoreProto.InputConfig protoInput = DatastoreProto.InputConfig.newBuilder()
                    .setFilePath(input.getFilePath())
                    .build();

            // Call gRPC method
            DatastoreProto.ReadResult protoResult = blockingStub.read(protoInput);

            // Convert proto result to custom ReadResult
            List<Integer> results = new ArrayList<>(protoResult.getDataList());

            ReadResult.Status status = protoResult.getStatus() == DatastoreProto.ReadResult.Status.SUCCESS
                    ? ReadResult.Status.SUCCESS
                    : ReadResult.Status.FAILURE;

            return new ReadResultImp(status, results);

        } catch (Exception e) {
            e.printStackTrace();
            return new ReadResultImp(ReadResult.Status.FAILURE, null);
        }
    }

    @Override
    public WriteResult appendSingleResult(OutputConfig output, String result, char delimiter) {
        try {
            // Convert custom OutputConfig to proto OutputConfig
            DatastoreProto.OutputConfig protoOutput = DatastoreProto.OutputConfig.newBuilder()
                    .setFilePath(output.getFilePath())
                    .build();

            // Create proto AppendRequest
            DatastoreProto.AppendRequest protoRequest = DatastoreProto.AppendRequest.newBuilder()
                    .setOutput(protoOutput)
                    .setResult(result)
                    .setDelimiter(String.valueOf(delimiter))
                    .build();

            // Call gRPC method
            DatastoreProto.WriteResult protoResult = blockingStub.appendSingleResult(protoRequest);

            WriteResult.WriteResultStatus status = protoResult.getStatus() == DatastoreProto.WriteResult.WriteResultStatus.SUCCESS
                    ? WriteResult.WriteResultStatus.SUCCESS
                    : WriteResult.WriteResultStatus.FAILURE;

            return new WriteResultImp(status);

        } catch (Exception e) {
            e.printStackTrace();
            return new WriteResultImp(WriteResult.WriteResultStatus.FAILURE);
        }
    }

    public static void main(String[] args) throws Exception {
        String target = "localhost:50052";  

        ManagedChannel channel = Grpc.newChannelBuilder(target, InsecureChannelCredentials.create())
                .build();
        try {
            DataStoreClient client = new DataStoreClient(channel);

            // Create a temporary file to test with content "1;2;3;4;5"
            String tempFilePath = "testfile.txt";
            File tempFile = new File(tempFilePath);
            tempFile.createNewFile();
            tempFile.deleteOnExit();
            Files.write(tempFile.toPath(), "1;2;3;4;5".getBytes(StandardCharsets.UTF_8));
            System.out.println("Temporary file created at: " + tempFile.toString());
            // Example read
            InputConfig input = new FileInputConfig(tempFile.toString());
            ReadResult readResult = client.read(input);
            System.out.println("Read Status: " + readResult.getStatus());
            if (readResult.getResults() != null) {
                System.out.println("Data: " + readResult.getResults());
            }

            // Example append
            OutputConfig output = new FileOutputConfig(tempFile.toString());
            WriteResult writeResult = client.appendSingleResult(output, "123", ';');
            System.out.println("Write Status: " + writeResult.getStatus());



        } finally {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }
}
