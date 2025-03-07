package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import src.project.annotations.DataProcessingAPI;
import src.project.annotations.DataProcessingImp;
import src.project.annotations.InputConfig;
import src.project.annotations.OutputConfig;
import src.project.annotations.ReadResult;
import src.project.annotations.ReadResultImp;
import src.project.annotations.WriteResult;
import src.project.annotations.WriteResultImp;

public class IntegrationTestDataProcessingImp {

    private DataProcessingAPI mockDataProcessingAPI;
    private DataProcessingImp dataProcessingImp;

    @BeforeEach
    public void setUp() {
        mockDataProcessingAPI = mock(DataProcessingAPI.class);
        dataProcessingImp = new DataProcessingImp(mockDataProcessingAPI);
    }

    @Test
    public void testReadExceptionHandling() {
        InputConfig validInputConfig = new InputConfig() {
            @Override
            public String getInputData() {
                return null;
            }

            @Override
            public String getFilePath() {
                return "validPath.txt";
            }
        };

        doThrow(new RuntimeException("Test Exception")).when(mockDataProcessingAPI)
            .read(validInputConfig);

        try {
            // Create a temporary file to pass the validation check
            Files.createFile(Paths.get("validPath.txt"));
            ReadResult result = dataProcessingImp.read(validInputConfig);
            assertEquals(ReadResult.Status.FAILURE, result.getStatus());
        } catch (Exception e) {
            fail("Exception should not be thrown for valid input");
        } finally {
            try {
                Files.deleteIfExists(Paths.get("validPath.txt"));
            } catch (Exception e) {
              
            }
        }
    }

    @Test
    public void testAppendSingleResultExceptionHandling() {
        OutputConfig validOutputConfig = new OutputConfig() {
            @Override
            public String getFilePath() {
                return "validPath.txt";
            }

            @Override
            public String formatOutput(String result) {
                return result;
            }
        };

        doThrow(new RuntimeException("Test Exception")).when(mockDataProcessingAPI)
            .appendSingleResult(validOutputConfig, "result", ',');

        try {
            // Create a temporary file to pass the validation check
            Files.createFile(Paths.get("validPath.txt"));
            WriteResult result = dataProcessingImp.appendSingleResult(validOutputConfig, "result", ',');
            assertEquals(WriteResult.WriteResultStatus.FAILURE, result.getStatus());
        } catch (Exception e) {
            fail("Exception should not be thrown for valid input");
        } finally {
            try {
                Files.deleteIfExists(Paths.get("validPath.txt"));
            } catch (Exception e) {
              
            }
        }
    }
}
