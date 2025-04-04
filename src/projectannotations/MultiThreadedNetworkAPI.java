package projectannotations;

import computecomponents.ComputeRequest;
import computecomponents.ComputeResponse;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MultiThreadedNetworkAPI {

    private static final int THREAD_POOL_SIZE = 10; 
    private static final ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    private MultiThreadedNetworkAPI() {
        // Private constructor to prevent instantiation
    }
  
    public static Future<ComputeResponse> processRequest(ComputeRequest request) {
        return executor.submit(() -> {
            // Simulate processing the request using ConceptualAPIUtil
            return ConceptualAPIUtil.process(request); 
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
