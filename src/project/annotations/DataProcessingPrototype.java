package project.annotations;

import java.util.Arrays;

import computecomponents.InputConfig;  // computecomponents.InputConfig 사용
import computecomponents.OutputConfig;  // computecomponents.OutputConfig 사용

public class DataProcessingPrototype implements DataProcessingAPI {

    @Override
    public ReadResult read(InputConfig input) {
        // 실제 read 구현
        String inputData = input.getInputData();
        if (inputData == null || inputData.isEmpty()) {
            return new ReadResultImp(ReadResult.Status.FAILURE, null);
        }
        
        // 데이터를 처리하여 성공적인 결과 반환
        Iterable<Integer> processedData = processInputData(inputData);
        return new ReadResultImp(ReadResult.Status.SUCCESS, processedData);
    }

    @Override
    public WriteResult appendSingleResult(project.annotations.OutputConfig output, String result, char delimiter) {
        if (output == null || result == null) {
            return new WriteResultImp(WriteResult.WriteResultStatus.FAILURE);
        }
        
        // OutputConfig의 형식에 맞춰서 결과를 저장하는 처리
        String formattedResult = output.formatOutput(result);  // OutputConfig의 formatOutput 사용
        boolean writeSuccess = writeResultToStorage(formattedResult, delimiter);
        
        if (writeSuccess) {
            return new WriteResultImp(WriteResult.WriteResultStatus.SUCCESS);
        }
        
        return new WriteResultImp(WriteResult.WriteResultStatus.FAILURE);
    }

    // Helper method to process input data (example)
    private Iterable<Integer> processInputData(String inputData) {
        // 예시로 입력 데이터를 숫자로 처리하는 로직 (여기에 실제 로직 구현)
        return Arrays.asList(1, 2, 3);  // 임시 반환 값
    }

    // Helper method to simulate writing the result (example)
    private boolean writeResultToStorage(String result, char delimiter) {
        // 실제 저장 로직 구현 (예: 파일이나 DB에 저장)
        System.out.println("Result saved: " + result);
        return true;  // 성공적으로 저장했다고 가정
    }

    public void prototype(DataProcessingAPI apiCall) {
        InputConfig inputConfig = new InputConfig() {
            @Override
            public String getInputData() {
                return "Sample input data";  // 예시 데이터 반환
            }
        };

        
        project.annotations.OutputConfig outputConfig = new project.annotations.OutputConfig() {
            @Override
            public String formatOutput(String result) {
                return "Formatted: " + result;
            }
        };

        ReadResult dataStoreReadResult = apiCall.read(inputConfig);

        if (dataStoreReadResult.getStatus() == ReadResult.Status.SUCCESS) {
            Iterable<Integer> loadedData = dataStoreReadResult.getResults();

            for (int i : loadedData) {
                String result = "" + i;
                WriteResult writeResult = apiCall.appendSingleResult(outputConfig, result, ',');

                if (writeResult.getStatus() != WriteResult.WriteResultStatus.SUCCESS) {
                    System.out.println("Fail. Please try again.");
                }
            }
        }
    }
}