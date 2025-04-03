package projectannotations;

import usercomputecomponents.UserComputeEngineAPI;
import computecomponents.ComputeRequest;
import computecomponents.ComputeResponse;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

public class MultiThreadedNetworkAPI implements UserComputeEngineAPI {

    private static final int THREAD_POOL_SIZE = 10;
    private static final ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    private static final Logger logger = Logger.getLogger(MultiThreadedNetworkAPI.class.getName());

    public MultiThreadedNetworkAPI() {

    }

    public static Future<ComputeResponse> processRequest(ComputeRequest request) {
        return executor.submit(() -> {
            try {
                logger.info("Processing request: " + request);
                return ConceptualAPIUtil.process(request);
            } catch (Exception e) {
                logger.severe("Error processing request: " + e.getMessage());
                throw new RuntimeException("Request processing failed", e);
            }
        });
    }

    @Override
    public void processData(String inputSource, String outputSource, String[] delimiters) {
        logger.info("Processing data from " + inputSource + " to " + outputSource);
    }

    public static void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, java.util.concurrent.TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }
}
