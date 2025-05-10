package computecomponents;

import projectannotations.ProcessAPI;

/**
 * API for coordination component that manages data flow between storage and compute engine.
 */
@ProcessAPI
public interface CoordinationAPI {
    /**
     * Handles a full computation task from input to output.
     * @param inputSource path to the input file or raw input
     * @param outputSource path to the output file
     * @return a message indicating success or failure
     */
    String handleComputation(String inputSource, String outputSource);
}
