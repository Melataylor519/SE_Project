package projectannotations;

import computecomponents.ComputeRequest;
import computecomponents.ComputeResponse;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

public class MultiThreadedNetworkAPI {

    private static final int THREAD_POOL_SIZE = 10;  // Fixed size of 10
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

    public static void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, java.util.concurrent.TimeUnit.SECONDS)) {
                executor.shutdownNow();  // Force shutdown if tasks don't finish in time
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }
}
