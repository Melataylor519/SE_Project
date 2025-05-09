package projectannotations;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import networkapi.NetworkAPIGrpc;
import networkapi.NetworkAPIProto;

import java.io.IOException;

public class NetworkAPIServer {

    static class NetworkAPIImpl extends NetworkAPIGrpc.NetworkAPIImplBase implements GrpcNetworkAPI {

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
        int port = 50051;
        Server server = ServerBuilder.forPort(port)
                .addService(new NetworkAPIImpl())
                .build();

        server.start();
        System.out.println("gRPC Server is running on port " + port);
        server.awaitTermination();
    }
}
