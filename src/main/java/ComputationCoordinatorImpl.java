import java.io.IOException;

public class ComputationCoordinatorImpl implements ComputationCoordinator {
    private final ComputeEngine engine;
    private final DataStore store;

    public ComputationCoordinatorImpl(ComputeEngine engine, DataStore store) {
        this.engine = engine;
        this.store = store;
    }

    @Override
    public ComputeResult compute(ComputeRequest request) {
    	try {
            int input = request.getInputConfig().getValue();
            String result = engine.compute(input);
            WriteResult write = store.appendSingleResult(
                request.getOutputConfig(), result, request.getDelimiter()
            );
            if (write.getStatus() != WriteResult.WriteResultStatus.SUCCESS) {
                throw new IOException("Write failed.");
            }
            return ComputeResult.SUCCESS;
        } catch (IOException e) {
            return new DefaultComputeResult(ComputeResult.ComputeResultStatus.FAILURE, e.getMessage());
        }
    }
}
