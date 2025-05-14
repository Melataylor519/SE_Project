package computecomponents;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.CountDownLatch;

import datastorecomponents.DataProcessingAPI;
import datastorecomponents.FileInputConfig;
import datastorecomponents.FileOutputConfig;
import datastorecomponents.InputConfig;
import datastorecomponents.ReadResult;
import datastorecomponents.WriteResult;
import usercomputecomponents.UserComputeEngineAPI;

/**
 * Fixed parallel version with proper synchronization
 */
public class CoordinationComponentParallel implements CoordinationAPI {

    private final DataProcessingAPI dataStorage;
    private final ComputeSystem computeSystem;
    private final int computeThreads;
    private final int ioThreads;
    
    // Use valueOf to avoid deprecated constructor
    private static final Integer END_MARKER = Integer.valueOf(-999999);

    public CoordinationComponentParallel(UserComputeEngineAPI userComputeEngine, DataProcessingAPI dataStorage) {
        this(userComputeEngine, dataStorage, 4, 2);
    }

    public CoordinationComponentParallel(UserComputeEngineAPI userComputeEngine, DataProcessingAPI dataStorage, 
                                       int computeThreads, int ioThreads) {
        if (userComputeEngine == null || dataStorage == null) {
            throw new IllegalArgumentException("Dependencies cannot be null");
        }

        this.dataStorage = dataStorage;
        this.computeSystem = new ComputeSystemImpl(dataStorage, userComputeEngine);
        this.computeThreads = computeThreads;
        this.ioThreads = ioThreads;
    }

    @Override
    public String handleComputation(String inputSource, String outputSource) {
        if (inputSource == null || inputSource.trim().isEmpty()) {
            return "Error: Input source cannot be null or empty";
        }
        
        if (outputSource == null || outputSource.trim().isEmpty()) {
            return "Error: Output source cannot be null or empty";
        }

        BlockingQueue<Integer> inputQueue = new LinkedBlockingQueue<>();
        BlockingQueue<Integer> outputQueue = new LinkedBlockingQueue<>();
        
        ExecutorService ioThreadPool = Executors.newFixedThreadPool(ioThreads);
        ExecutorService computeThreadPool = Executors.newFixedThreadPool(computeThreads);
        
        // Error capture for async operations
        final AtomicReference<String> errorMessage = new AtomicReference<>();
        
        // Latch to ensure compute threads are ready before input starts
        final CountDownLatch computeThreadsReady = new CountDownLatch(computeThreads);
        
        try {
            System.out.println("PARALLEL: Starting parallel processing with " + computeThreads + " compute threads...");
            
            // === Start Computation Tasks First ===
            List<CompletableFuture<Void>> computeTasks = new ArrayList<>();
            for (int i = 0; i < computeThreads; i++) {
                final int computeId = i;
                computeTasks.add(CompletableFuture.runAsync(() -> {
                    try {
                        System.out.println("COMPUTE THREAD " + computeId + ": Starting...");
                        computeThreadsReady.countDown(); // Signal this thread is ready
                        
                        while (true) {
                            Integer input = inputQueue.take();
                            if (input == END_MARKER) {
                                System.out.println("COMPUTE THREAD " + computeId + ": Received end marker, exiting");
                                outputQueue.put(END_MARKER);
                                break;
                            }

                            System.out.println("COMPUTE THREAD " + computeId + ": Processing: " + input);
                            ComputeRequest request = new ComputeRequest(
                                new FileInputConfig(input.toString()),
                                new FileOutputConfig(outputSource),
                                ','
                            );
                            ComputeResponse response = computeSystem.compute(request);

                            if (response.getStatus().isSuccess()) {
                                // For prime factorization, the result should be the largest prime factor
                                // The compute system should return the largest prime factor as a string
                                Integer largestPrimeFactor = Integer.parseInt(response.getResult());
                                outputQueue.put(largestPrimeFactor);
                                System.out.println("COMPUTE THREAD " + computeId + ": Largest prime factor of " + input + " is: " + largestPrimeFactor);
                            } else {
                                errorMessage.compareAndSet(null, "Error: Computation failed - " + response.getFailureMessage());
                                outputQueue.put(END_MARKER);
                                break;
                            }
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.err.println("COMPUTE THREAD " + computeId + ": Interrupted - " + e.getMessage());
                        errorMessage.compareAndSet(null, "Error: Computation interrupted - " + e.getMessage());
                        try {
                            outputQueue.put(END_MARKER);
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                        }
                    } catch (Exception e) {
                        System.err.println("COMPUTE THREAD " + computeId + ": Error - " + e.getMessage());
                        e.printStackTrace(); // Print full stack trace for debugging
                        errorMessage.compareAndSet(null, "Error: Computation error - " + e.getMessage());
                        try {
                            outputQueue.put(END_MARKER);
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }, computeThreadPool));
            }
            
            // === Input Reading Task ===
            CompletableFuture<Void> inputTask = CompletableFuture.runAsync(() -> {
                try {
                    // Wait for all compute threads to be ready
                    computeThreadsReady.await();
                    
                    System.out.println("INPUT THREAD: Starting parallel input reading...");
                    InputConfig inputConfig = new FileInputConfig(inputSource);
                    ReadResult readResult = dataStorage.read(inputConfig);

                    if (readResult.getStatus() == ReadResult.Status.SUCCESS) {
                        for (Integer num : readResult.getResults()) {
                            inputQueue.put(num);
                            System.out.println("INPUT THREAD: Read and queued: " + num);
                        }
                    } else {
                        errorMessage.compareAndSet(null, "Error: Failed to read input data");
                        // Still send end markers even if read failed
                        for (int i = 0; i < computeThreads; i++) {
                            inputQueue.put(END_MARKER);
                        }
                        return;
                    }

                    // Signal end of input to all compute threads
                    System.out.println("INPUT THREAD: Sending end markers to " + computeThreads + " compute threads");
                    for (int i = 0; i < computeThreads; i++) {
                        inputQueue.put(END_MARKER);
                    }
                    System.out.println("INPUT THREAD: Finished reading");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("INPUT THREAD: Interrupted - " + e.getMessage());
                    errorMessage.compareAndSet(null, "Error: Input reading interrupted - " + e.getMessage());
                } catch (Exception e) {
                    System.err.println("INPUT THREAD: Unexpected error - " + e.getMessage());
                    e.printStackTrace();
                    errorMessage.compareAndSet(null, "Error: Unexpected error during input reading - " + e.getMessage());
                }
            }, ioThreadPool);

            // === Output Writing Task ===
            CompletableFuture<Void> outputTask = CompletableFuture.runAsync(() -> {
                try {
                    System.out.println("OUTPUT THREAD: Starting parallel output writing...");
                    int endSignals = 0;
                    List<Integer> results = new ArrayList<>();

                    while (endSignals < computeThreads) {
                        Integer result;
                        try {
                            result = outputQueue.take();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            System.err.println("OUTPUT THREAD: Interrupted while taking from outputQueue - " + e.getMessage());
                            errorMessage.compareAndSet(null, "Error: Output writing interrupted - " + e.getMessage());
                            return;
                        }
                        
                        if (result == END_MARKER) {
                            endSignals++;
                            System.out.println("OUTPUT THREAD: Received end signal " + endSignals + "/" + computeThreads);
                        } else {
                            results.add(result);
                            System.out.println("OUTPUT THREAD: Collected largest prime factor: " + result);
                        }
                    }

                    // For prime factorization, we want the overall largest prime factor
                    // across all input numbers
                    Integer overallLargestPrimeFactor = results.stream()
                            .max(Integer::compareTo)
                            .orElse(0);
                    
                    System.out.println("OUTPUT THREAD: Overall largest prime factor from " + results.size() + " numbers = " + overallLargestPrimeFactor);
                    
                    WriteResult writeResult = dataStorage.appendSingleResult(
                        new FileOutputConfig(outputSource),
                        String.valueOf(overallLargestPrimeFactor),
                        ','
                    );

                    if (writeResult.getStatus() != WriteResult.WriteResultStatus.SUCCESS) {
                        errorMessage.compareAndSet(null, "Error: Failed to write output data");
                        return;
                    }

                    System.out.println("OUTPUT THREAD: Final result written: " + overallLargestPrimeFactor);
                } catch (Exception e) {
                    System.err.println("OUTPUT THREAD: Unexpected error - " + e.getMessage());
                    e.printStackTrace();
                    errorMessage.compareAndSet(null, "Error: Unexpected error during output writing - " + e.getMessage());
                }
            }, ioThreadPool);

            // Wait for input to complete first
            inputTask.join();
            System.out.println("MAIN THREAD: Input task completed");
            
            // Then wait for all compute tasks
            CompletableFuture.allOf(computeTasks.toArray(new CompletableFuture[0])).join();
            System.out.println("MAIN THREAD: All compute tasks completed");
            
            // Finally wait for output
            outputTask.join();
            System.out.println("MAIN THREAD: Output task completed");
            
            // Check if any errors occurred during async execution
            String error = errorMessage.get();
            if (error != null) {
                return error;
            }

        } catch (Exception e) {
            System.err.println("Error: Unexpected error during task coordination - " + e.getMessage());
            e.printStackTrace();
            return "Error: Unexpected error during task coordination - " + e.getMessage();
        } finally {
            // Graceful shutdown of thread pools
            shutdownThreadPools(ioThreadPool, computeThreadPool);
        }

        return "Success: Computation completed successfully";
    }
    
    private void shutdownThreadPools(ExecutorService ioThreadPool, ExecutorService computeThreadPool) {
        ioThreadPool.shutdown();
        computeThreadPool.shutdown();
        try {
            if (!ioThreadPool.awaitTermination(30, TimeUnit.SECONDS)) {
                ioThreadPool.shutdownNow();
            }
            if (!computeThreadPool.awaitTermination(30, TimeUnit.SECONDS)) {
                computeThreadPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            ioThreadPool.shutdownNow();
            computeThreadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
