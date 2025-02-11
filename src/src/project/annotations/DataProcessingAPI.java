package project.annotations;

public interface DataProcessingAPI {
	
	@ProcessAPIPrototype(description = "Read data from the specified input source: ")
	// Since the method returns data, so I wrap it inside a DataProcessingResponse 
	DataProcessingResponse readData(StorageType sourceType, String inputSource); 
	
	@ProcessAPIPrototype(description = "Write data to the specified output source: ")
	void writeData(StorageType targetType, String outputSource, DataProcessingRequest request);
	
}

