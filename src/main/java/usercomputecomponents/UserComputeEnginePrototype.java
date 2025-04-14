package usercomputecomponents;

import projectannotations.NetworkAPIPrototype;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class UserComputeEnginePrototype implements UserComputeEngineAPI {

    // Default delimiters
    private static final String[] DEFAULT_DELIMITERS = {",", ";", " "};

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
        System.out.println("Reading data from " + source);

        // read data from file
        File file = new File(source);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                return reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "";
    }

    public void writeData(String destination, String data) {
        // Placeholder for writing data logic
        System.out.println("Writing to " + destination + ": " + data);

        // write to file destination if destination is exist
        File file = new File(destination);
        if (file.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                System.out.println("Writing to " + destination + ": " + data);
                writer.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
