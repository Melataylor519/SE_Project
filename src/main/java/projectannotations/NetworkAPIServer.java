package projectannotations;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import networkapi.NetworkAPIGrpc;
import networkapi.NetworkAPIProto;

import usercomputecomponents.UserComputeEngineAPI;
import usercomputecomponents.UserComputeEnginePrototype;
import datastorecomponents.DataProcessingAPI;
import datastorecomponents.DataStoreClient;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NetworkAPIServer {

    private static final Logger logger = Logger.getLogger(NetworkAPIServer.class.getName());

    static class NetworkAPIImpl extends NetworkAPIGrpc.NetworkAPIImplBase {
        private final ExecutorService threadPool;

        public NetworkAPIImpl(ExecutorService threadPool) {
            this.threadPool = threadPool;
        }

        @Override
        public void processRequest(NetworkAPIProto.RequestMessage request,
                                   StreamObserver<NetworkAPIProto.ResponseMessage> responseObserver) {
            threadPool.submit(() -> {
                try {
                    // Log the incoming request
                    String requestData = request.getRequestData();
                    logger.info("Received request: " + requestData);

                    // Set up compute engine and datastore client
                    UserComputeEngineAPI engine = new UserComputeEnginePrototype();
                    DataProcessingAPI client = DataStoreClient.connect("localhost:50051");

                    // Process data with the compute engine
                    String result = engine.processData(client, requestData, new String[]{","});

                    // Build and send response
                    responseObserver.onNext(NetworkAPIProto.ResponseMessage.newBuilder()
                            .setResponseData(result)
                            .build());
                    responseObserver.onCompleted();

                    logger.info("Successfully processed request. Response sent.");
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Error processing request", e);
                    responseObserver.onError(e);
                }
            });
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 50052; // Use a different port to avoid conflict with DataStoreServer
        ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        Server server = ServerBuilder.forPort(port)
                                     .addService(new NetworkAPIImpl(threadPool))
                                     .build()
                                     .start();

        logger.info("NetworkAPIServer running on port " + port);

        // Add shutdown hook for graceful termination
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down NetworkAPIServer...");
            threadPool.shutdown();
            server.shutdown();
        }));

        server.awaitTermination();
    }
}
