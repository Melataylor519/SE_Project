package projectannotations;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import networkapi.NetworkAPIGrpc;
import networkapi.NetworkAPIProto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class NetworkAPIClient {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            // Input
            System.out.print("Enter numbers (comma-separated) OR type 'file:<path>': ");
            String input = scanner.nextLine().trim();

            // Output file path
            System.out.print("Enter output file path: ");
            String outputPath = scanner.nextLine().trim();

            // Overwrite confirmation
            File outputFile = new File(outputPath);
            if (outputFile.exists()) {
                System.out.print("Output file exists. Do you want to overwrite it? (y/n): ");
                if (!scanner.nextLine().trim().equalsIgnoreCase("y")) {
                    System.out.println("Operation canceled.");
                    return;
                }
            }

            // Delimiter
            System.out.print("Enter output delimiter (or press Enter to use default ','): ");
            String delimiter = scanner.nextLine().trim();
            if (delimiter.isEmpty()) {
                delimiter = ",";
            }

            // Setup gRPC connection
            ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                    .usePlaintext()
                    .build();

            NetworkAPIGrpc.NetworkAPIStub asyncStub = NetworkAPIGrpc.newStub(channel);

            try {
                // Stream data if input is a file
                if (input.startsWith("file:")) {
                    String filePath = input.substring(5).trim();
                    File inputFile = new File(filePath);
                    if (!inputFile.exists()) {
                        System.err.println("Input file does not exist.");
                        return;
                    }

                    processFileInChunks(inputFile, outputFile, delimiter, asyncStub);
                } else {
                    // Process direct input
                    processDirectInput(input, outputFile, delimiter, asyncStub);
                }
            } finally {
                // Shutdown
                channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
            }
        } finally {
            scanner.close();
        }
    }

    private static void processFileInChunks(File inputFile, File outputFile, String delimiter, NetworkAPIGrpc.NetworkAPIStub asyncStub) {
        CountDownLatch latch = new CountDownLatch(1);

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            StreamObserver<NetworkAPIProto.RequestMessage> requestObserver = asyncStub.processLargeFile(new StreamObserver<>() {
                @Override
                public void onNext(NetworkAPIProto.ResponseMessage response) {
                    try {
                        writer.write(response.getResponseData().replace(",", delimiter));
                        writer.newLine();
                    } catch (IOException e) {
                        System.err.println("Error writing output: " + e.getMessage());
                    }
                }

                @Override
                public void onError(Throwable t) {
                    System.err.println("gRPC error: " + t.getMessage());
                    latch.countDown();
                }

                @Override
                public void onCompleted() {
                    System.out.println("Task complete. Output saved to: " + outputFile.getAbsolutePath());
                    latch.countDown();
                }
            });

            String line;
            while ((line = reader.readLine()) != null) {
                NetworkAPIProto.RequestMessage request = NetworkAPIProto.RequestMessage.newBuilder()
                        .setRequestData(line)
                        .build();
                requestObserver.onNext(request);
            }

            // Signal completion
            requestObserver.onCompleted();
            latch.await();
        } catch (IOException | InterruptedException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void processDirectInput(String input, File outputFile, String delimiter, NetworkAPIGrpc.NetworkAPIStub asyncStub) {
        CountDownLatch latch = new CountDownLatch(1);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            StreamObserver<NetworkAPIProto.RequestMessage> requestObserver = asyncStub.processLargeFile(new StreamObserver<>() {
                @Override
                public void onNext(NetworkAPIProto.ResponseMessage response) {
                    try {
                        writer.write(response.getResponseData().replace(",", delimiter));
                        writer.newLine();
                    } catch (IOException e) {
                        System.err.println("Error writing output: " + e.getMessage());
                    }
                }

                @Override
                public void onError(Throwable t) {
                    System.err.println("gRPC error: " + t.getMessage());
                    latch.countDown();
                }

                @Override
                public void onCompleted() {
                    System.out.println("Task complete. Output saved to: " + outputFile.getAbsolutePath());
                    latch.countDown();
                }
            });

            // Send single request
            NetworkAPIProto.RequestMessage request = NetworkAPIProto.RequestMessage.newBuilder()
                    .setRequestData(input)
                    .build();
            requestObserver.onNext(request);

            // Signal completion
            requestObserver.onCompleted();
            latch.await();
        } catch (IOException | InterruptedException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
