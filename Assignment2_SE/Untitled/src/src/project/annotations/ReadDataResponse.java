package project.annotations;

public class ReadDataResponse implements DataProcessingResponse {
	private String data;
	
	public ReadDataResponse(String data) {
		this.data = data;
	}
	
	public String getData() {
		return data;
	}
}

