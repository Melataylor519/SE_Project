//Data Processing Prototype 
package project.annotations;

public class DataProcessingPrototype implements DataProcessingAPI {
	@Override
	public ReadDataResponse readData(StorageType sourceType, String inputSource) {
		// If sourceType = FILE_SYSTEM, then inputSource = " /data.input.txt"
		// If sourceType = DATABASE, then inputSource = "user_table"
		String data = "Data from " + sourceType + " at " + inputSource;
		return new ReadDataResponse(data);
	}
	@Override
	public void writeData(StorageType targetType, String outputSource, DataProcessingRequest request) {
		if(request instanceof WriteDataRequest) {
			WriteDataRequest writeRequest = (WriteDataRequest) request;
			System.out.println("Writing data to " + targetType + " at " + outputSource + ": " + writeRequest.getData());
		}
	}
}
