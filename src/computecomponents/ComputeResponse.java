package computecomponents;

public interface ComputeResponse {
	static ComputeResponse SUCCESS = new ComputeResponse() {
        
        @Override
        public ComputeReponseStatus getStatus() {
            return ComputeReponseStatus.SUCCESS;
        }
        
        @Override
        public String getFailureMessage() {
            return "";
        }
    };

    ComputeReponseStatus getStatus();
	String getFailureMessage();
	
	public static enum ComputeReponseStatus {
		SUCCESS(true),
		INVALID_REQUEST(false),
		FAILURE(false);
		
		private final boolean success;
		
		private ComputeReponseStatus(boolean success) {
			this.success = success;
		}
		
		public boolean isSuccess() {
			return success;
		}
	}
}
