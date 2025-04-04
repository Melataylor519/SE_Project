package projectannotations;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.ArrayList;
import java.util.List;


public class MultiThreadedNetworkAPI {

    private static final int THREAD_POOL_SIZE = 10; 
    private static final ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    private MultiThreadedNetworkAPI() {
        // Private constructor to prevent instantiation
    }
  
    public static List<Future<?>> runMultiThreaded(List<Runnable> tasks) {
        List<Future<?>> futures = new ArrayList<>();
        for (Runnable task : tasks) {
            futures.add(executor.submit(task)); 
        }
        return futures;
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
