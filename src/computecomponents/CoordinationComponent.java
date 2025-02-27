package computecomponents;

import main.java.com.assignment2.api.UserComputeEngineAPI;
import project.annotations.DataProcessingAPI;
import project.annotations.ReadResult;
import project.annotations.WriteResult;
import project.annotations.InputConfig; 
import project.annotations.OutputConfig; 


/**
 * Coordination component for managing computation requests.
 */
public class CoordinationComponent {
    //private final UserComputeEngineAPI userComputeEngine;
    private final DataProcessingAPI dataStorage;
    private final ComputeSystem computeSystem;

    public CoordinationComponent(UserComputeEngineAPI userComputeEngine, DataProcessingAPI dataStorage) {
        //this.userComputeEngine = userComputeEngine;
        this.dataStorage = dataStorage;
        this.computeSystem = new ComputeSystemImpl();
    }

    public String handleComputation(String inputSource, String outputSource) {
        // Read input data from storage
    	// 초기 InputConfig 객체 생성
    	InputConfig inputConfig = new DefaultInputConfig(""); 

    	// 데이터 읽기
    	ReadResult readResult = dataStorage.read(inputConfig);
    	if (readResult.getStatus() == ReadResult.Status.SUCCESS) {
    	    Iterable<Integer> loadedData = readResult.getResults(); // ✅ Iterable<Integer> 가져오기
    	    StringBuilder inputData = new StringBuilder();
    	    
    	    for (int num : loadedData) {
    	        inputData.append(num).append(",");  // 정수를 쉼표(,)로 구분하여 문자열 변환
    	    }
    	    
    	    // 마지막 쉼표 제거
    	    String inputDataString = inputData.length() > 0 ? inputData.substring(0, inputData.length() - 1) : "";

    	    if (inputDataString.isEmpty()) {
    	        return "Error: No valid input data.";
    	    }

    	    // `DefaultInputConfig`를 사용해 올바르게 설정
    	    inputConfig = new DefaultInputConfig(inputDataString);
    	} else {
    	    return "Error: Failed to read input data.";
    	}


        // Check for valid input
        if (inputConfig.getInputData() == null) {
            return "Error: Failed to read input data.";
        }

        // Compute result
        OutputConfig computeOutputConfig = new OutputConfig() {
            @Override
            public String getFilePath() {
                return "";
            }
            public String formatOutput(String result) {
                return result;
            }
        };

        ComputeRequest request = new ComputeRequest(inputConfig, computeOutputConfig, ',');
        ComputeResponse response = computeSystem.compute(request);

        if (!response.getStatus().isSuccess()) {
            return "Computation failed: " + response.getFailureMessage();
        }

        // Write result to storage
        OutputConfig annotationOutputConfig = new OutputConfig() {
            public String formatOutput(String result) {
                return result;
            }
            @Override
            public String getFilePath() {
                return System.getProperty("user.dir"); // return current dir
            }

        };
        
        WriteResult writeResult = dataStorage.appendSingleResult(annotationOutputConfig, response.getResult(), ',');

        if (writeResult.getStatus() != WriteResult.WriteResultStatus.SUCCESS) {
            return "Error: Failed to write output data.";
        }

        return "Computation successful. Result: " + response.getResult();
    }
}
