/*package main.java.projectannotations;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import networkapi.NetworkAPIGrpc;
import networkapi.NetworkAPIProto;

import java.io.IOException;

public class NetworkAPIServer {

    static class NetworkAPIImpl extends NetworkAPIGrpc.NetworkAPIImplBase {

        @Override
        public void processRequest(NetworkAPIProto.RequestMessage request,
                                   StreamObserver<NetworkAPIProto.ResponseMessage> responseObserver) {

            System.out.println("Received request: " + request.getRequestData());

            // Simulate some logic
            String result = "Server processed: " + request.getRequestData();

            // Build response
            NetworkAPIProto.ResponseMessage response = NetworkAPIProto.ResponseMessage.newBuilder()
                    .setResponseData(result)
                    .build();

            // Send response
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(9090)
                .addService(new NetworkAPIImpl())
                .build();

        server.start();
        System.out.println("gRPC Server is running on port 9090");
        server.awaitTermination();
    }
}
*/