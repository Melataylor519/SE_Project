package project.annotations;

class WriteResultImp implements WriteResult {
	private final WriteResultStatus status; 
	
	/**
	 * Constructor for WriteResultImp.
	 * @param status the status of the write operation
	 */
	public WriteResultImp(WriteResultStatus status) {
		this.status = status;
	}
	
	@Override
	public WriteResultStatus getStatus() {
		return status;
	}
}
