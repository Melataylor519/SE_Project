package projectannotations;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import datastore.DataProcessingGrpc;
import datastore.DatastoreProto;
import datastorecomponents.DataProcessingImp;
import datastorecomponents.FileInputConfig;
import datastorecomponents.FileOutputConfig;
import datastorecomponents.InputConfig;
import datastorecomponents.OutputConfig;
import datastorecomponents.ReadResult;
import datastorecomponents.WriteResult;
import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.Server;
import io.grpc.protobuf.services.ProtoReflectionService;
import io.grpc.stub.StreamObserver;

public class DataStoreServer extends DataProcessingGrpc.DataProcessingImplBase {
    private Server server;

    private void start() throws IOException {
        int port = 50052;

        server = Grpc.newServerBuilderForPort(port, InsecureServerCredentials.create())
                .addService(this)
                .addService(ProtoReflectionService.newInstance())
                .build()
                .start();

        System.out.println("Server started on, listening on port " + port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("*** Shutting down gRPC server since JVM is shutting down ***");
            try {
                if (server != null) {
                    server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
                }
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
            System.err.println("*** Server shut down ***");
        }));
    }

    @Override
    public void read(DatastoreProto.InputConfig request, StreamObserver<DatastoreProto.ReadResult> responseObserver) {
        try {
            // Convert proto InputConfig to custom InputConfig
            InputConfig inputConfig = new FileInputConfig(request.getFilePath());

            // Process the read operation
            ReadResult readResult = new DataProcessingImp().read(inputConfig);

            // Convert custom ReadResult to proto ReadResult
            DatastoreProto.ReadResult.Builder protoResultBuilder = DatastoreProto.ReadResult.newBuilder()
                    .setStatus(readResult.getStatus() == ReadResult.Status.SUCCESS
                            ? DatastoreProto.ReadResult.Status.SUCCESS
                            : DatastoreProto.ReadResult.Status.FAILURE)
                    .addAllData(readResult.getResults());

            // Send response
            responseObserver.onNext(protoResultBuilder.build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            e.printStackTrace();
            // Send failure response
            DatastoreProto.ReadResult.Builder protoResultBuilder = DatastoreProto.ReadResult.newBuilder()
                    .setStatus(DatastoreProto.ReadResult.Status.FAILURE);
            responseObserver.onNext(protoResultBuilder.build());
            responseObserver.onCompleted();
        }
    }

    @Override
    public void appendSingleResult(DatastoreProto.AppendRequest request, StreamObserver<DatastoreProto.WriteResult> responseObserver) {
        try {
            // Convert proto OutputConfig to custom OutputConfig
            OutputConfig outputConfig = new FileOutputConfig(request.getOutput().getFilePath());

            // Process the append operation
            WriteResult writeResult = new DataProcessingImp().appendSingleResult(outputConfig, request.getResult(), request.getDelimiter().charAt(0));

            // Convert custom WriteResult to proto WriteResult
            DatastoreProto.WriteResult.Builder protoResultBuilder = DatastoreProto.WriteResult.newBuilder()
                    .setStatus(writeResult.getStatus() == WriteResult.WriteResultStatus.SUCCESS
                            ? DatastoreProto.WriteResult.WriteResultStatus.SUCCESS
                            : DatastoreProto.WriteResult.WriteResultStatus.FAILURE);

            // Send response
            responseObserver.onNext(protoResultBuilder.build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            e.printStackTrace();
            // Send failure response
            DatastoreProto.WriteResult.Builder protoResultBuilder = DatastoreProto.WriteResult.newBuilder()
                    .setStatus(DatastoreProto.WriteResult.WriteResultStatus.FAILURE);
            responseObserver.onNext(protoResultBuilder.build());
            responseObserver.onCompleted();
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws Exception {
        DataStoreServer server = new DataStoreServer();
        server.start();
        server.blockUntilShutdown();
    }
}

