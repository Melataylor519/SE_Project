package projectannotations;

import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.Server;
import io.grpc.protobuf.services.ProtoReflectionService;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DataStoreServer {
    private Server server;

    public void start() throws IOException {
        int port = 50051; // Unified port
        server = Grpc.newServerBuilderForPort(port, InsecureServerCredentials.create())
                     .addService(new DataProcessingService())
                     .addService(ProtoReflectionService.newInstance())
                     .build()
                     .start();

        System.out.println("DataStoreServer started on port " + port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("*** Shutting down DataStoreServer ***");
            try {
                if (server != null) {
                    server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));
    }

    private Iterable<Integer> convertIntArrayToIterable(int[] array) {
        // Convert int[] to List<Integer> (which implements Iterable)
        return IntStream.of(array).boxed().collect(Collectors.toList());
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        DataStoreServer server = new DataStoreServer();
        server.start();
        server.server.awaitTermination();
    }
}
