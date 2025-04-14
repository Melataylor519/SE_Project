package computecomponents;

public interface ComputeResponse {
	static ComputeResponse SUCCESS = new ComputeResponse() {
        
        @Override
        public ComputeResponseStatus getStatus() {
            return ComputeResponseStatus.SUCCESS;
        }
        
        @Override
        public String getFailureMessage() {
            return "";
        }
        
        @Override
        public String getResult() {
            return "";
        }
    };

    ComputeResponseStatus getStatus();
	String getFailureMessage();
	String getResult(); // return result
	
	public static enum ComputeResponseStatus {
		SUCCESS(true),
		INVALID_REQUEST(false),
		FAILURE(false);
		
		private final boolean success;
		
		private ComputeResponseStatus(boolean success) {
			this.success = success;
		}
		
		public boolean isSuccess() {
			return success;
		}
	}
}
