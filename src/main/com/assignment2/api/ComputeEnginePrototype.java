package main.com.assignment2.api;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import project.annotations.NetworkAPIPrototype;

/**
 * Prototype implementation of ComputeEngineAPI.
 */
public class ComputeEnginePrototype implements ComputeEngineAPI {

    // Default delimiters
    private static final String[] DEFAULT_DELIMITERS = {",", ";", " "};

    /**
     * Processes data from the source and writes it to the destination using specified delimiters.
     *
     * @param source      The input data source (local file or networked storage)
     * @param destination The output destination
     * @param delimiters  Custom delimiters for output formatting, uses defaults if null
     */

    @NetworkAPIPrototype
    @Override
    public void processData(DataStorageAPI storage, String inputSource, String outputSource, String[] delimiters) {
    	// Read input data using the storage API
        String rawData = storage.readData(inputSource);

        // Process data using delimiters
        String processedData = process(rawData, delimiters);

        // Write processed data to the output destination
        storage.writeData(outputSource, processedData);
    }
    
    private String process(String data, String[] delimiters) {
        // If delimiters are not provided, use default delimiters (e.g., whitespace and comma)
        if (delimiters == null || delimiters.length == 0) {
            delimiters = new String[]{" ", ","};
        }

        // Replace each delimiter with a standard delimiter (e.g., a single space)
        for (String delimiter : delimiters) {
            data = data.replace(delimiter, " ");
        }

        return data.trim();
    }

}
