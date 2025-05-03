public class SimpleWriteResult implements WriteResult {
    private final WriteResultStatus status;
    public SimpleWriteResult(WriteResultStatus status) {
        this.status = status;
    }

    @Override
    public WriteResultStatus getStatus() {
        return status;
    }
}
