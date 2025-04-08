package projectannotations;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import networkapi.NetworkAPIGrpc;
import networkapi.RequestMessage;
import networkapi.ResponseMessage;

import java.io.IOException;
import java.util.logging.Logger;

public class NetworkAPIServer {
    private static final Logger logger = Logger.getLogger(NetworkAPIServer.class.getName());
    private final Server server;

    public NetworkAPIServer() {
        this.server = ServerBuilder.forPort(50051)
                .addService(new NetworkAPIImpl())
                .build();
    }

    public void start() throws IOException {
        server.start();
        logger.info("Server started, listening on " + server.getPort());
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down gRPC server");
            NetworkAPIServer.this.stop();
        }));
    }

    public void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    private class NetworkAPIImpl extends NetworkAPIGrpc.NetworkAPIImplBase {
        @Override
        public void processRequest(RequestMessage request, StreamObserver<ResponseMessage> responseObserver) {
            try {
                String requestData = request.getRequestData();
                logger.info("Received request: " + requestData);

                // Process the request
                String responseData = "Processed: " + requestData;

                // Build and send the response
                ResponseMessage response = ResponseMessage.newBuilder()
                        .setResponseData(responseData)
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            } catch (Exception e) {
                logger.severe("Error processing request: " + e.getMessage());
                responseObserver.onError(e);
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final NetworkAPIServer server = new NetworkAPIServer();
        server.start();
        server.blockUntilShutdown();
    }
}
