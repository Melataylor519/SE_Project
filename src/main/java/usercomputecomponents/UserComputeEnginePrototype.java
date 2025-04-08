package usercomputecomponents;

import projectannotations.NetworkAPIPrototype;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserComputeEnginePrototype implements UserComputeEngineAPI {
    private static final Logger LOGGER = Logger.getLogger(UserComputeEnginePrototype.class.getName());

    // Default delimiters
    private static final String[] DEFAULT_DELIMITERS = { ",", ";", " " };

    @NetworkAPIPrototype
    @Override
    public void processData(String inputSource, String outputSource, String[] delimiters) {
        if (inputSource == null || outputSource == null) {
            LOGGER.log(Level.SEVERE, "Input or output source is null");
            throw new IllegalArgumentException("Input and output sources cannot be null");
        }

        try {
            // Read input data
            String rawData = readData(inputSource);
            if (rawData == null) {
                LOGGER.log(Level.SEVERE, "Failed to read data from: " + inputSource);
                throw new IOException("Failed to read input data");
            }

            // Process data using delimiters
            String processedData = process(rawData, delimiters);

            // Write processed data to the output destination
            writeData(outputSource, processedData);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error processing data: " + e.getMessage(), e);
            throw new RuntimeException("Failed to process data", e);
        }
    }

    public String readData(String source) throws IOException {
        LOGGER.log(Level.INFO, "Reading data from: " + source);

        Path path = Paths.get(source);
        if (!Files.exists(path)) {
            LOGGER.log(Level.WARNING, "Input file does not exist: " + source);
            throw new IOException("Input file does not exist: " + source);
        }

        if (!Files.isReadable(path)) {
            LOGGER.log(Level.WARNING, "Input file is not readable: " + source);
            throw new IOException("Input file is not readable: " + source);
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            return reader.readLine();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading file: " + source, e);
            throw e;
        }
    }

    public void writeData(String destination, String data) throws IOException {
        LOGGER.log(Level.INFO, "Writing data to: " + destination);

        Path path = Paths.get(destination);
        Path parentDir = path.getParent();

        if (parentDir != null && !Files.exists(parentDir)) {
            LOGGER.log(Level.INFO, "Creating parent directory: " + parentDir);
            Files.createDirectories(parentDir);
        }

        if (Files.exists(path) && !Files.isWritable(path)) {
            LOGGER.log(Level.WARNING, "Output file is not writable: " + destination);
            throw new IOException("Output file is not writable: " + destination);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write(data);
            LOGGER.log(Level.INFO, "Successfully wrote data to: " + destination);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error writing to file: " + destination, e);
            throw e;
        }
    }

    private String process(String data, String[] delimiters) {
        if (data == null) {
            LOGGER.log(Level.WARNING, "Input data is null");
            return "";
        }

        // If delimiters are not provided, use default delimiters
        if (delimiters == null || delimiters.length == 0) {
            LOGGER.log(Level.INFO, "Using default delimiters");
            delimiters = DEFAULT_DELIMITERS;
        }

        // Replace each delimiter with a standard delimiter (e.g., a single space)
        String processedData = data;
        for (String delimiter : delimiters) {
            processedData = processedData.replace(delimiter, " ");
        }

        return processedData.trim();
    }
}
