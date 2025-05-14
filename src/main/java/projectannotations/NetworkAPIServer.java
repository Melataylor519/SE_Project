package projectannotations;

import java.io.IOException;
import java.util.stream.Collectors;

import datastore.DataProcessingGrpc;
import datastore.DatastoreProto;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import networkapi.NetworkAPIGrpc;
import networkapi.NetworkAPIProto;

public class NetworkAPIServer extends NetworkAPIGrpc.NetworkAPIImplBase {
    private final DataProcessingGrpc.DataProcessingBlockingStub dataStoreStub;

    public NetworkAPIServer(String dataStoreHost, int dataStorePort) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(dataStoreHost, dataStorePort)
                .usePlaintext()
                .build();
        this.dataStoreStub = DataProcessingGrpc.newBlockingStub(channel);
    }

    @Override
    public void processRequest(NetworkAPIProto.RequestMessage request, StreamObserver<NetworkAPIProto.ResponseMessage> responseObserver) {
        try {
            // Forward the request to DataStoreServer
            DatastoreProto.InputConfig inputConfig = DatastoreProto.InputConfig.newBuilder()
                    .setFilePath(request.getRequestData())
                    .build();

            DatastoreProto.ReadResult readResult = dataStoreStub.read(inputConfig);
            if (readResult.getStatus() == DatastoreProto.ReadResult.Status.SUCCESS) {
                if (readResult.getDataList() == null || readResult.getDataList().isEmpty()) {
                    responseObserver.onError(new RuntimeException("Data list is empty or null in DataStoreServer response."));
                    return;
                }
            
                // Build the response
                NetworkAPIProto.ResponseMessage response = NetworkAPIProto.ResponseMessage.newBuilder()
                    .setResponseData(readResult.getDataList().stream()
                            .map(String::valueOf) // Convert Integer to String
                            .collect(Collectors.joining(","))) // Join with a comma
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            } else {
                responseObserver.onError(new RuntimeException("Failed to process request in DataStoreServer."));
            }
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 50051; // Ensure this port does not conflict with DataStoreServer
        String dataStoreHost = "localhost";
        int dataStorePort = 50052; // Port of the DataStoreServer

        NetworkAPIServer server = new NetworkAPIServer(dataStoreHost, dataStorePort);
        Server grpcServer = ServerBuilder.forPort(port)
                .addService(server)
                .build()
                .start();

        System.out.println("NetworkAPIServer started, listening on port " + port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("*** Shutting down gRPC server since JVM is shutting down ***");
            grpcServer.shutdown();
            System.err.println("*** Server shut down ***");
        }));

        grpcServer.awaitTermination();
    }
}
