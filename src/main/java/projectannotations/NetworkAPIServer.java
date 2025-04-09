package main.java.projectannotations;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import networkapi.NetworkAPIGrpc;
import networkapi.NetworkAPI;

import java.io.IOException;

public class NetworkAPIServer {

    // gRPC Service Implementation
    static class NetworkAPIImpl extends NetworkAPIGrpc.NetworkAPIImplBase {
        @Override
        public void processRequest(NetworkAPI.RequestMessage request,
                                   StreamObserver<NetworkAPI.ResponseMessage> responseObserver) {
            System.out.println("Received request: " + request.getRequestData());

            // Simulate processing logic
            String result = "Processed: " + request.getRequestData();

            // Build and send response
            NetworkAPI.ResponseMessage response = NetworkAPI.ResponseMessage.newBuilder()
                    .setResponseData(result)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    // Start the gRPC Server
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(9090)
                .addService(new NetworkAPIImpl())
                .build();

        System.out.println("gRPC Server started on port 9090");
        server.start();
        server.awaitTermination();
    }
}
