package main.java.datastorecomponents;

public class WriteResultImp implements WriteResult {
	private final WriteResultStatus status; 
	
	public WriteResultImp(WriteResultStatus status) {
		this.status = status;
	}
	
	@Override
	public WriteResultStatus getStatus() {
		return status;
	}
}
