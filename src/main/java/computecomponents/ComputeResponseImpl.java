package main.java.computecomponents;

public class ComputeResponseImpl implements ComputeResponse{
	
    private final ComputeResponseStatus status;
    private final String result;

    // Initialize status and result
    public ComputeResponseImpl(ComputeResponseStatus status, String result) {
        this.status = status;
        this.result = result;
    }

	@Override
	public ComputeResponseStatus getStatus() {
		return status;
	}

	@Override
	public String getFailureMessage() {
		return status == ComputeResponseStatus.FAILURE ? "Computation failed" : "";
	}
	
	@Override
	public String getResult() {
		return result;  // Return computation result as String
	}

}
