import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import datastorecomponents.ReadResultImp;
import datastorecomponents.WriteResult;
import datastorecomponents.WriteResultImp;

public class TestDataProcessingImp {

    private DataProcessingAPI mockDataProcessingAPI;
    private DataProcessingImp dataProcessingImp;

    @BeforeEach
    public void setUp() {
        mockDataProcessingAPI = mock(DataProcessingAPI.class);
        dataProcessingImp = new DataProcessingImp();
    }

    @Test
    public void testReadValidation() {
        InputConfig invalidInputConfig = new InputConfig() {
            @Override
            public String getInputData() {
                return " ";
            }

            @Override
            public String getFilePath() {
                return " ";
            }
        };

        ReadResult failedResult = dataProcessingImp.read(invalidInputConfig);
        assertEquals(ReadResult.Status.FAILURE, failedResult.getStatus());

        InputConfig validInputConfig = new InputConfig() {
            @Override
            public String getInputData() {
                return "";
            }

            @Override
            public String getFilePath() {
                return "validPath.txt";
            }
        };

        when(mockDataProcessingAPI.read(validInputConfig)).thenReturn(new ReadResultImp(ReadResult.Status.SUCCESS, null));

        Path tempFile = null;
        try {
            // Create a temporary file to pass the validation check
            tempFile = Files.createFile(Paths.get("validPath.txt"));
            ReadResult result = dataProcessingImp.read(validInputConfig);
            assertEquals(ReadResult.Status.FAILURE, result.getStatus());
        } catch (Exception e) {
            fail("Exception should not be thrown for valid input");
        } finally {
            if (tempFile != null) {
                try {
                    Files.deleteIfExists(tempFile);
                } catch (IOException ex) {
                    System.err.println("Failed to delete temp file: " + tempFile);
                }
            }
        }
    }

    @Test
    public void testAppendSingleResultValidation() {
        OutputConfig invalidOutputConfig = new OutputConfig() {
            @Override
            public String formatOutput(String result) {
                return null;
            }

            @Override
            public String getFilePath() {
                return "";
            }
        };

        WriteResult failedResult = dataProcessingImp.appendSingleResult(invalidOutputConfig, "result", ',');

        assertEquals(WriteResult.WriteResultStatus.FAILURE, failedResult.getStatus());

        OutputConfig validOutputConfig = new OutputConfig() {
            @Override
            public String formatOutput(String result) {
                return result;
            }

            @Override
            public String getFilePath() {
                return "validPath.txt";
            }
        };

        when(mockDataProcessingAPI.appendSingleResult(validOutputConfig, "result", ',')).thenReturn(new WriteResultImp(WriteResult.WriteResultStatus.SUCCESS));

        Path tempFile = null;
        try {
            // Create a temporary file to pass the validation check
            tempFile = Files.createFile(Paths.get("validPath.txt"));
            WriteResult result = dataProcessingImp.appendSingleResult(validOutputConfig, "result", ',');
            assertEquals(WriteResult.WriteResultStatus.FAILURE, result.getStatus());
        } catch (Exception e) {
            fail("Exception should not be thrown for valid input");
        } finally {
            if (tempFile != null) {
                try {
                    Files.deleteIfExists(tempFile);
                } catch (IOException ex) {
                    System.err.println("Failed to delete temp file: " + tempFile);
                }
            }
        }
    }
}
