package projectannotations;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import networkapi.NetworkAPIGrpc;
import networkapi.NetworkAPIProto;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;

public class NetworkAPIClient {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // choose input method
        System.out.print("Enter numbers (comma-separated) OR type 'file:<path>': ");
        String input = scanner.nextLine();

        String requestData;

        if (input.startsWith("file:")) {
            String filePath = input.substring(5).trim();
            try {
                requestData = Files.readString(new File(filePath).toPath());
            } catch (IOException e) {
                System.out.println("Failed to read file: " + e.getMessage());
                return;
            }
        } else {
            requestData = input;
        }

        // get output file path
        System.out.print("Enter output file path: ");
        String outputPath = scanner.nextLine().trim();

        // choose Delimiter
        System.out.print("Enter output delimiter (or press Enter to use default ','): ");
        String delimiter = scanner.nextLine().trim();
        if (delimiter.isEmpty()) {
            delimiter = ",";
        }

        // gRPC server connect
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        NetworkAPIGrpc.NetworkAPIBlockingStub stub = NetworkAPIGrpc.newBlockingStub(channel);

        // gRPC request and response
        try {
            NetworkAPIProto.RequestMessage request = NetworkAPIProto.RequestMessage.newBuilder()
                    .setRequestData(requestData)
                    .build();

            NetworkAPIProto.ResponseMessage response = stub.processRequest(request);

            // save response
            try (FileWriter writer = new FileWriter(outputPath)) {
                writer.write(response.getResponseData().replace(",", delimiter));
            }

            System.out.println("Task complete! Result saved to: " + outputPath);

        } catch (Exception e) {
            System.out.println("Error during RPC: " + e.getMessage());
        }

        channel.shutdown();
    }
}
