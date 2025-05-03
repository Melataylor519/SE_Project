import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MultiThreadedCoordinator extends CoordinatorBase implements AutoCloseable {
    private static final int MAX_THREADS = 4;
    private final ExecutorService threadPool = Executors.newFixedThreadPool(MAX_THREADS);

    public MultiThreadedCoordinator(ComputeEngine engine, DataStore store) {
        super(engine, store);
    }

    @Override
    public ComputeResult compute(ComputeRequest request) {
        List<Integer> inputs;
        if (request.getInputConfig() instanceof FileBasedInputConfig fileBasedInputConfig) {
            inputs = fileBasedInputConfig.getAllValues();
        } else {
            inputs = List.of(request.getInputConfig().getValue());
        }

        List<Future<ComputeResult>> futures = new ArrayList<>();

        for (int value : inputs) {
            futures.add(threadPool.submit(() -> handleSingleJob(request, value)));
        }

        boolean allSuccess = true;
        for (Future<ComputeResult> future : futures) {
            try {
                ComputeResult r = future.get();
                if (!r.getStatus().isSuccess()) {
                    allSuccess = false;
                }
            } catch (InterruptedException | ExecutionException e) {
                allSuccess = false;
            }
        }

        return allSuccess ? ComputeResult.SUCCESS
                           : new DefaultComputeResult(ComputeResult.ComputeResultStatus.FAILURE, "One or more tasks failed");
    }

    @Override
    public void close() {
        threadPool.shutdown();
    }
}

