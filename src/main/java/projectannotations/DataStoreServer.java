package main.java.projectannotations;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.Server;
import io.grpc.protobuf.services.ProtoReflectionService;

public class DataStoreServer {
    private Server server;
    private void start() throws IOException {
        int port = 50051;

        server = Grpc.newServerBuilderForPort(port, InsecureServerCredentials.create())
                .addService(new DataStoreService())
                .addService(ProtoReflectionService.newInstance())
                .build()
                .start();

        System.out.println("Server started on port " + port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("*** Shutting down gRPC server since JVM is shutting down ***");
            try {
                if (server != null) {
                    server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
                }
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
            System.err.println("*** Server shut down ***");
        }));
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws Exception {
        DataStoreServer server = new DataStoreServer();
        server.start();
        server.blockUntilShutdown();
    }
}
