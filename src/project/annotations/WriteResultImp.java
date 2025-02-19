package project.annotations;

class WriteResultImp implements WriteResult {
	private WriteResultStatus status; 
	private String message;
	
	public WriteResultImp(WriteResultStatus status) {
		this.status = status;
	}
	
	public WriteResultImp(String message) {
		this.message = message;
	}
	
	@Override
	public WriteResultStatus getStatus() {
		return status;
	}
}
