package project.annotations;

public class ReadDataResponse implements DataProcessingResponse {
	private final String data;
	
	public ReadDataResponse(String data) {
		this.data = data;
	}
	
	public String getData() {
		return data;
	}
}

