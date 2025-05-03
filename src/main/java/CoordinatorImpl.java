public class CoordinatorImpl extends CoordinatorBase {
    public CoordinatorImpl(ComputeEngine engine, DataStore store) {
        super(engine, store);
    }

    @Override
    public ComputeResult compute(ComputeRequest request) {
        return handleSingleJob(request, request.getInputConfig().getValue());
    }
}
