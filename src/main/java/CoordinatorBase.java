public abstract class CoordinatorBase implements ComputationCoordinator {
    protected final ComputeEngine engine;
    protected final DataStore store;

    public CoordinatorBase(ComputeEngine engine, DataStore store) {
        this.engine = engine;
        this.store = store;
    }

    protected ComputeResult handleSingleJob(ComputeRequest request, int input) {
        try {
            String result = engine.compute(input);
            WriteResult write = store.appendSingleResult(request.getOutputConfig(), result, request.getDelimiter());
            if (write.getStatus() == WriteResult.WriteResultStatus.SUCCESS) {
                return ComputeResult.SUCCESS;
            } else {
                return new DefaultComputeResult(ComputeResult.ComputeResultStatus.FAILURE, "Write failed");
            }
        } catch (Exception e) {
            return new DefaultComputeResult(ComputeResult.ComputeResultStatus.FAILURE, e.getMessage());
        }
    }
}