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

        Path tempFile = null;
        try {
            tempFile = Files.createFile(Paths.get("validPath.txt"));

            ReadResult result = dataProcessingImp.read(validInputConfig);
            assertEquals(ReadResult.Status.FAILURE, result.getStatus());  
        } catch (IOException e) {
            fail("Setup failed: " + e.getMessage());
        } finally {
                try {
                    if (tempFile != null) Files.deleteIfExists(tempFile);
                } catch (IOException ex) {
                System.err.println("Cleanup failed: " + ex.getMessage());
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
    
        Path tempFile = null;
        try {
            tempFile = Files.createFile(Paths.get("validPath.txt"));
    
            WriteResult result = dataProcessingImp.appendSingleResult(validOutputConfig, "result", ',');
            assertEquals(WriteResult.WriteResultStatus.SUCCESS, result.getStatus());
        } catch (IOException e) {
            fail("Test setup failed: " + e.getMessage());
        } finally {
            try {
                if (tempFile != null) Files.deleteIfExists(tempFile);
            } catch (IOException ex) {
                System.err.println("Cleanup failed: " + ex.getMessage());
            }
        }
    }

}
