package main.java.projectannotations;

import main.java.computecomponents.ComputeRequest;
import main.java.computecomponents.ComputeResponse;
import main.java.computecomponents.ComputeResponse.ComputeResponseStatus;

public class ConceptualAPIUtil {
    
    private ConceptualAPIUtil() {
    }

    public static ComputeResponse process(ComputeRequest request) {
        if (request.getInputConfig() == null || request.getOutputConfig() == null) {
            return new ComputeResponse() {
                @Override
                public ComputeResponseStatus getStatus() {
                    return ComputeResponseStatus.INVALID_REQUEST;
                }

                @Override
                public String getFailureMessage() {
                    return "Missing input or output configuration.";
                }

                @Override
                public String getResult() {
                    return "";
                }
            };
        }

        // Simulate the processing logic
        String result = "Processed: " + request.toString();

        return new ComputeResponse() {
            @Override
            public ComputeResponseStatus getStatus() {
                return ComputeResponseStatus.SUCCESS;
            }

            @Override
            public String getFailureMessage() {
                return "";
            }

            @Override
            public String getResult() {
                return result;
            }
        };
    }
}
