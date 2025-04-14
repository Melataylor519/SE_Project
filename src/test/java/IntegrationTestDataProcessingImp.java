import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import datastorecomponents.DataProcessingAPI;
import datastorecomponents.DataProcessingImp;
import datastorecomponents.InputConfig;
import datastorecomponents.OutputConfig;
import datastorecomponents.ReadResult;
import mdatastorecomponents.ReadResultImp;
import datastorecomponents.WriteResult;
import datastorecomponents.WriteResultImp;

public class IntegrationTestDataProcessingImp {

    private DataProcessingAPI mockDataProcessingAPI;
    private DataProcessingImp dataProcessingImp;

    @BeforeEach
    public void setUp() {
        mockDataProcessingAPI = mock(DataProcessingAPI.class);
        dataProcessingImp = new DataProcessingImp();
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

        Path tempFile = null;
        try {
            // Create a temporary file to pass the validation check
            tempFile = Files.createFile(Paths.get("validPath.txt"));
            ReadResult result = dataProcessingImp.read(validInputConfig);
            assertEquals(ReadResult.Status.SUCCESS, result.getStatus());
        } catch (Exception e) {
        	fail("Exception should not be thrown for valid input");
        } finally {
            if (tempFile != null) {
                try {
                    Files.deleteIfExists(tempFile);
                } catch (IOException e) {
                    System.err.println("Failed to delete temp file: " + tempFile);
                }
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

        Path tempFile = null;
        try {
            // Create a temporary file to pass the validation check
            tempFile = Files.createFile(Paths.get("validPath.txt"));
            WriteResult result = dataProcessingImp.appendSingleResult(validOutputConfig, "result", ',');
            assertEquals(WriteResult.WriteResultStatus.SUCCESS, result.getStatus());
        } catch (Exception e) {
            fail("Exception should not be thrown for valid input");
        } finally {
            if (tempFile != null) {
                try {
                    Files.deleteIfExists(tempFile);
                } catch (IOException e) {
                    System.err.println("Failed to delete temp file: " + tempFile);
                }
            }
        }
    }
}
