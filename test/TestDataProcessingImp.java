import project.annotations.DataProcessingAPI;
import project.annotations.DataProcessingImp;
import project.annotations.DataProcessingPrototype;
import project.annotations.InputConfig;
import project.annotations.OutputConfig;
import project.annotations.ReadResult;
import project.annotations.WriteResult;
import project.annotations.ReadResultImp;
import project.annotations.WriteResultImp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.annotations.*;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestDataProcessingImp {

    private DataProcessingAPI mockDataProcessingAPI;
    private DataProcessingImp dataProcessingImp;

    @BeforeEach
    public void setUp() {
        mockDataProcessingAPI = mock(DataProcessingAPI.class);
        dataProcessingImp = new DataProcessingImp(mockDataProcessingAPI);
    }

    @Test
    public void testReadValidation() {
        InputConfig invalidInputConfig = new InputConfig() {
            @Override
            public String getFilePath() {
                return " ";
            }
        };

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            dataProcessingImp.read(invalidInputConfig);
        });

        assertEquals("InputConfig or file path cannot be null or empty", exception.getMessage());

        InputConfig validInputConfig = new InputConfig() {
            @Override
            public String getFilePath() {
                return "validPath.txt";
            }
        };

        when(mockDataProcessingAPI.read(validInputConfig)).thenReturn(new ReadResultImp(ReadResult.Status.SUCCESS, null));

        try {
            // Create a temporary file to pass the validation check
            Files.createFile(Paths.get("validPath.txt"));
            ReadResult result = dataProcessingImp.read(validInputConfig);
            assertEquals(ReadResult.Status.SUCCESS, result.getStatus());
        } catch (Exception e) {
            fail("Exception should not be thrown for valid input");
        } finally {
            try {
                Files.deleteIfExists(Paths.get("validPath.txt"));
            } catch (Exception e) {
                // Ignore cleanup errors
            }
        }
    }

    @Test
    public void testAppendSingleResultValidation() {
        OutputConfig invalidOutputConfig = new OutputConfig() {
            @Override
            public String getFilePath() {
                return " ";
            }
        };

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            dataProcessingImp.appendSingleResult(invalidOutputConfig, "result", ',');
        });

        assertEquals("OutputConfig or file path cannot be null or empty", exception.getMessage());

        OutputConfig validOutputConfig = new OutputConfig() {
            @Override
            public String getFilePath() {
                return "validPath.txt";
            }
        };

        when(mockDataProcessingAPI.appendSingleResult(validOutputConfig, "result", ',')).thenReturn(new WriteResultImp(WriteResult.WriteResultStatus.SUCCESS));

        try {
            // Create a temporary file to pass the validation check
            Files.createFile(Paths.get("validPath.txt"));
            WriteResult result = dataProcessingImp.appendSingleResult(validOutputConfig, "result", ',');
            assertEquals(WriteResult.WriteResultStatus.SUCCESS, result.getStatus());
        } catch (Exception e) {
            fail("Exception should not be thrown for valid input");
        } finally {
            try {
                Files.deleteIfExists(Paths.get("validPath.txt"));
            } catch (Exception e) {
                // Ignore cleanup errors
            }
        }
    }
}
