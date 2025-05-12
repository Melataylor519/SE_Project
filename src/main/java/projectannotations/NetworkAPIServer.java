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
    static class NetworkAPIImpl extends NetworkAPIGrpc.NetworkAPIImplBase {
        @Override
        public void processRequest(NetworkAPIProto.RequestMessage request,
                                   StreamObserver<NetworkAPIProto.ResponseMessage> responseObserver) {
            try {
                UserComputeEngineAPI engine = new UserComputeEnginePrototype();
                DataProcessingAPI client = DataStoreClient.connect("localhost:50051");

                // Input and output file paths
                String inputPath = "temp/input.txt";
                String outputPath = "temp/output.txt";

                Files.writeString(Paths.get(inputPath), request.getRequestData());

                engine.processData(client, inputPath, outputPath, new String[]{","});

                String result = Files.readString(Paths.get(outputPath)).trim();
                responseObserver.onNext(NetworkAPIProto.ResponseMessage.newBuilder().setResponseData(result).build());
                responseObserver.onCompleted();
            } catch (Exception e) {
                responseObserver.onError(e);
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 50051;
        Server server = ServerBuilder.forPort(port)
                                     .addService(new NetworkAPIImpl())
                                     .build()
                                     .start();
        System.out.println("NetworkAPIServer running on port " + port);
        server.awaitTermination();
    }
}
}
