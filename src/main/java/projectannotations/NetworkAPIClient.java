package projectannotations;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import networkapi.NetworkAPIGrpc;
import networkapi.NetworkAPIProto;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class NetworkAPIClient {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            String requestData;

            // Input
            System.out.print("Enter numbers (comma-separated) OR type 'file:<path>': ");
            String input = scanner.nextLine().trim();

            if (input.startsWith("file:")) {
                String filePath = input.substring(5).trim();
                try {
                    requestData = Files.readString(new File(filePath).toPath()).trim();
                } catch (IOException e) {
                    System.err.println("Failed to read input file: " + e.getMessage());
                    return;
                }
            } else {
                requestData = input.trim();
            }

            // Output file path
            System.out.print("Enter output file path: ");
            String outputPath = scanner.nextLine().trim();

            // Overwrite confirmation
            File outputFile = new File(outputPath);
            if (outputFile.exists()) {
                System.out.print("Output file exists. Do you want to overwrite it? (y/n): ");
                if (!scanner.nextLine().trim().equalsIgnoreCase("y")) {
                    System.out.println("Removed");
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

            NetworkAPIGrpc.NetworkAPIBlockingStub stub = NetworkAPIGrpc.newBlockingStub(channel);

            // Prepare and send request
            try {
                NetworkAPIProto.RequestMessage request = NetworkAPIProto.RequestMessage.newBuilder()
                        .setRequestData(requestData)
                        .build();

                NetworkAPIProto.ResponseMessage response = stub.processRequest(request);

                // Save response to output file
                try (FileWriter writer = new FileWriter(outputFile)) {
                    writer.write(response.getResponseData().replace(",", delimiter));
                }

                // Display response
                System.out.println("Task complete");
                System.out.println("Result: " + response.getResponseData());
                System.out.println("Saved to: " + outputFile.getAbsolutePath());

            } catch (StatusRuntimeException e) {
                System.err.println("gRPC error: " + e.getStatus().getDescription());
            } catch (IOException e) {
                System.err.println("Error writing output: " + e.getMessage());
            } finally {
                // Shutdown
                try {
                    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    System.err.println("Channel shutdown interrupted: " + e.getMessage());
                }
            }
        } finally {
            scanner.close();
        }
    }
}
