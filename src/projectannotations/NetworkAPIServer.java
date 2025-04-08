package projectannotations;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import networkapi.NetworkAPIGrpc;
import networkapi.NetworkApi.RequestMessage;
import networkapi.NetworkApi.ResponseMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.logging.Logger;

@NetworkAPI 
public class NetworkAPIServer {
    private static final Logger logger = Logger.getLogger(NetworkAPIServer.class.getName());
    private Server server;

    public void start() throws IOException {
        int port = 503030;
        server = ServerBuilder.forPort(port)
                .addService(new NetworkAPIImpl())
                .build()
                .start();
        logger.info("Server started, listening on " + port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down gRPC server...");
            NetworkAPIServer.this.stop();
        }));
    }

    public void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    static class NetworkAPIImpl extends NetworkAPIGrpc.NetworkAPIImplBase {
        @Override
        public void processRequest(RequestMessage request, StreamObserver<ResponseMessage> responseObserver) {
            List<Runnable> tasks = new ArrayList<>();
            
            tasks.add(() -> {
                try {
                    // Exception handling logic from Assignment 5 should be here
                    if (request.getRequestData().isEmpty()) {
                        throw new IllegalArgumentException("Request data cannot be empty");
                    }

                    String responseText = "Processed: " + request.getRequestData();
                    ResponseMessage response = ResponseMessage.newBuilder()
                            .setResponseData(responseText)
                            .build();

                    responseObserver.onNext(response); 
                    responseObserver.onCompleted();
                } catch (Exception e) {
                    responseObserver.onError(e);
                }
            });

            // Run tasks in parallel using MultiThreadedNetworkAPI
            List<Future<?>> futures = MultiThreadedNetworkAPI.runMultiThreaded(tasks);

            // Wait for tasks to complete
            for (Future<?> future : futures) {
                try {
                    future.get(); 
                } catch (Exception e) {
                    logger.warning("Task execution failed: " + e.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final NetworkAPIServer server = new NetworkAPIServer();
        server.start();
        server.blockUntilShutdown();
    }
}
