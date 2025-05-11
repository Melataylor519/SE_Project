package projectannotations;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import networkapi.NetworkAPIGrpc;
import networkapi.NetworkAPIProto;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

import usercomputecomponents.UserComputeEngineAPI;
import usercomputecomponents.UserComputeEnginePrototype;
import datastorecomponents.DataProcessingAPI;
import datastorecomponents.DataStoreClient;


import java.io.IOException;

public class NetworkAPIServer {

    static class NetworkAPIImpl extends NetworkAPIGrpc.NetworkAPIImplBase implements GrpcNetworkAPI {

        @Override
        public void processRequest(NetworkAPIProto.RequestMessage request,
                StreamObserver<NetworkAPIProto.ResponseMessage> responseObserver) {

            try {
                // 1. Setup compute engine and datastore client
                UserComputeEngineAPI engine = new UserComputeEnginePrototype();
                DataProcessingAPI client = DataStoreClient.connect("localhost:50051"); 

                // 2. Prepare input/output file paths
                String inputPath = "temp_input.txt";
                String outputPath = "temp_output.txt";

                // Only create the output file if it doesn't already exist
                Path outputFile = Paths.get(outputPath);
                if (!Files.exists(outputFile)) {
                    Files.createFile(outputFile);
                }

                // 3. Write request data to input file
                java.nio.file.Files.write(java.nio.file.Paths.get(inputPath), request.getRequestData().getBytes());

                // 4. Call computation engine
                engine.processData(client, inputPath, outputPath, new String[]{","});

                // 5. Read result from output file
                String result = java.nio.file.Files.readString(java.nio.file.Paths.get(outputPath)).trim();

                // 6. Return result to client
                NetworkAPIProto.ResponseMessage response = NetworkAPIProto.ResponseMessage.newBuilder()
                        .setResponseData(result)
                        .build();

                responseObserver.onNext(response);
                responseObserver.onCompleted();

            } catch (Exception e) {
                e.printStackTrace();
                responseObserver.onError(e);
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 50051;
        Server server = ServerBuilder.forPort(port)
                .addService(new NetworkAPIImpl())
                .build();

        server.start();
        System.out.println("gRPC Server is running on port " + port);
        server.awaitTermination();
    }
}
