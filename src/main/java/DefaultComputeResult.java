public class DefaultComputeResult implements ComputeResult {
    private final ComputeResultStatus status;
    private final String failureMessage;

    public DefaultComputeResult(ComputeResultStatus status, String failureMessage) {
        this.status = status;
        this.failureMessage = failureMessage;
    }

    @Override
    public ComputeResultStatus getStatus() {
        return status;
    }

    @Override
    public String getFailureMessage() {
        return failureMessage;
    }
}



