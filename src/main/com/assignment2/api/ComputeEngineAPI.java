package main.com.assignment2.api;

import project.annotations.NetworkAPI;

/**
 * API interface for a compute engine that processes data from a source
 * and writes it to a specified destination.
 */
@NetworkAPI
public interface ComputeEngineAPI {
    /**
     * Processes data from the specified input source and writes it to the given destination.
     *
     * @param source The input data source (e.g., local file, networked storage)
     * @param destination The output destination
     * @param delimiters Custom delimiters for processing the output, null for defaults
     */
    void processData(DataStorageAPI storage, String inputSource, String outputSource, String[] delimiters);
}
    
