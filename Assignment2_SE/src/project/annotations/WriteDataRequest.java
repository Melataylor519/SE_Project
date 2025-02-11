package project.annotations;

public class WriteDataRequest implements DataProcessingRequest {
	private String data;
	
	public WriteDataRequest(String data) {
		this.data = data;
	}
	
	public String getData() {
		return data;
	}
}
