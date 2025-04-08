package main.java.projectannotations;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import networkapi.NetworkAPIGrpc;
import networkapi.RequestMessage;
import networkapi.ResponseMessage;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class NetworkAPIServer {
    private static final Logger logger = Logger.getLogger(NetworkAPIServer.class.getName());
    private final Server server;
    private final ExecutorService executorService;

    public NetworkAPIServer() {
        this.executorService = Executors.newFixedThreadPool(10); // Adjust pool size as needed
        this.server = ServerBuilder.forPort(50051)
                .addService(new NetworkAPIImpl())
                .build();
    }

    public void start() throws IOException {
        server.start();
        logger.info("Server started, listening on port 50051");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down gRPC server...");
            NetworkAPIServer.this.stop();
        }));
    }

    public void stop() {
        if (server != null) {
            server.shutdown();
        }
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    private class NetworkAPIImpl extends NetworkAPIGrpc.NetworkAPIImplBase {
        @Override
        public void processRequest(RequestMessage request, StreamObserver<ResponseMessage> responseObserver) {
            try {
                // Input validation
                if (request == null) {
                    throw new IllegalArgumentException("Request cannot be null");
                }
                if (request.getRequestData().isEmpty()) {
                    throw new IllegalArgumentException("Request data cannot be empty");
                }

                // Create task for processing
                Runnable task = () -> {
                    try {
                        // Process the request
                        String processedData = processRequestData(request.getRequestData());

                        // Build and send response
                        ResponseMessage response = ResponseMessage.newBuilder()
                                .setResponseData(processedData)
                                .build();

                        responseObserver.onNext(response);
                        responseObserver.onCompleted();
                    } catch (Exception e) {
                        logger.severe("Error processing request: " + e.getMessage());
                        responseObserver.onError(e);
                    }
                };

                // Submit task to executor service
                executorService.submit(task);

            } catch (Exception e) {
                logger.severe("Error handling request: " + e.getMessage());
                responseObserver.onError(e);
            }
        }

        private String processRequestData(String requestData) {
            // Add your request processing logic here
            return "Processed: " + requestData;
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final NetworkAPIServer server = new NetworkAPIServer();
        server.start();
        server.blockUntilShutdown();
    }
}