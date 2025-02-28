// UserComputeEnginePrototype.java

package main.java.com.assignment2.api;

import main.java.project.annotations.NetworkAPIPrototype;

/**
 * Prototype implementation of ComputeEngineAPI.
 */
public class UserComputeEnginePrototype implements UserComputeEngineAPI {

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
    public void processData(String inputSource, String outputSource, String[] delimiters) {
    	// Read input data
        String rawData = readData(inputSource);

        // Process data using delimiters
        String processedData = process(rawData, delimiters);

        // Write processed data to the output destination
        writeData(outputSource, processedData);
    }
    
    public String readData(String source) {
        // Placeholder for reading data logic
        return "Sample data from " + source;
    }

    public void writeData(String destination, String data) {
        // Placeholder for writing data logic
        System.out.println("Writing to " + destination + ": " + data);
    }
    
    private String process(String data, String[] delimiters) {
        // If delimiters are not provided, use default delimiters
        if (delimiters == null || delimiters.length == 0) {
            delimiters = DEFAULT_DELIMITERS;
        }

        // Replace each delimiter with a standard delimiter (e.g., a single space)
        for (String delimiter : delimiters) {
            data = data.replace(delimiter, " ");
        }

        return data.trim();
    }

}
