package projectannotations;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class MultiThreadedNetworkAPI {

    // Fixed thread pool size of 10 as specified in README.md
    private static final int THREAD_POOL_SIZE = 10;
    
    // Thread pool for network operations 
    private static final ExecutorService NETWORK_EXECUTOR = 
            Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    
    // Thread pool for compute operations
    private static final ExecutorService COMPUTE_EXECUTOR = 
            Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    
    // Manages request to prevent system overload
    private static final Semaphore REQUEST_LIMITER = new Semaphore(THREAD_POOL_SIZE * 2);
    
    // Keeps track of active users
    private static final ConcurrentHashMap<String, Long> activeUsers = new ConcurrentHashMap<>();

    private MultiThreadedNetworkAPI() {
        // Private constructor 
    }

    /**
     * Executes network tasks concurrently
     * 
     * @param tasks List of tasks to execute
     * @return List of futures for the submitted tasks
     */
    public static List<Future<?>> runMultiThreaded(List<Runnable> tasks) {
        List<Future<?>> futures = new ArrayList<>();
        for (Runnable task : tasks) {
            futures.add(NETWORK_EXECUTOR.submit(() -> {
                try {
                    REQUEST_LIMITER.acquire(); // Throttle requests
                    task.run();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    REQUEST_LIMITER.release(); // Release permit when done
                }
            }));
        }
        return futures;
    }
    
    /**
     * Executes compute tasks concurrently
     * 
     * @param <T> Type of result
     * @param computeTasks List of compute tasks to execute
     * @return List of futures for the submitted tasks
     */
    public static <T> List<Future<T>> runComputeTasks(List<Supplier<T>> computeTasks) {
        List<Future<T>> futures = new ArrayList<>();
        for (Supplier<T> task : computeTasks) {
            futures.add(COMPUTE_EXECUTOR.submit(task::get));
        }
        return futures;
    }
    
    /**
     * Registers a user as active in the system
     * 
     * @param userId Unique identifier for the user
     */
    public static void registerUser(String userId) {
        activeUsers.put(userId, System.currentTimeMillis());
    }
    
    /**
     * Deregisters a user from the system
     * 
     * @param userId Unique identifier for the user
     * @return true if the user was active and now removed
     */
    public static boolean deregisterUser(String userId) {
        return activeUsers.remove(userId) != null;
    }
    
    /**
     * Gets the current number of active users
     * 
     * @return Count of active users
     */
    public static int getActiveUserCount() {
        // Clean up stale user sessions (inactive for more than 10 minutes)
        long cutoffTime = System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(10);
        activeUsers.entrySet().removeIf(entry -> entry.getValue() < cutoffTime);
        return activeUsers.size();
    }

    /**
     * Submits a single task to the network thread pool
     * 
     * @param task Task to execute
     * @return Future for the submitted task
     */
    public static Future<?> submitNetworkTask(Runnable task) {
        return NETWORK_EXECUTOR.submit(task);
    }

    /**
     * Submits a single compute task to the compute thread pool
     * 
     * @param <T> Type of result
     * @param task Task to execute
     * @return Future for the submitted task
     */
    public static <T> Future<T> submitComputeTask(Callable<T> task) {
        return COMPUTE_EXECUTOR.submit(task);
    }

    /**
     * Waits for all futures to complete
     * 
     * @param futures List of futures to wait for
     * @param timeoutSeconds Maximum time to wait in seconds
     */
    public static void awaitCompletion(List<Future<?>> futures, long timeoutSeconds) {
        try {
            long deadline = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(timeoutSeconds);
            for (Future<?> future : futures) {
                long timeRemaining = deadline - System.currentTimeMillis();
                if (timeRemaining <= 0) {
                    break;
                }
                future.get(timeRemaining, TimeUnit.MILLISECONDS);
            }
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void shutdown() {
        NETWORK_EXECUTOR.shutdown();
        COMPUTE_EXECUTOR.shutdown();
        try {
            // Wait for existing tasks to terminate
            if (!NETWORK_EXECUTOR.awaitTermination(60, TimeUnit.SECONDS)) {
                NETWORK_EXECUTOR.shutdownNow();
            }
            if (!COMPUTE_EXECUTOR.awaitTermination(60, TimeUnit.SECONDS)) {
                COMPUTE_EXECUTOR.shutdownNow();
            }
        } catch (InterruptedException e) {
            NETWORK_EXECUTOR.shutdownNow();
            COMPUTE_EXECUTOR.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
