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
import datastorecomponents.ReadResultImp;
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
        InputConfig input = new InputConfig() {
            @Override
            public String getInputData() {
                return null;
            }

            @Override
            public String getFilePath() {
                return "/invalid::path/doesnotexist.txt";  
            }
        };

        try {
            ReadResult result = dataProcessingImp.read(input);
            assertEquals(ReadResult.Status.FAILURE, result.getStatus());
        } catch (Exception e) {
            fail("FAILURE");
        }    
    }


    @Test
    public void testAppendSingleResultExceptionHandling() {
        OutputConfig output = new OutputConfig() {
        @Override
        public String formatOutput(String result) {
            return result;
        }

        @Override
        public String getFilePath() {
            return "/invalid::path/nowrite.txt"; 
        }
    };

        try {
            WriteResult result = dataProcessingImp.appendSingleResult(output, "data", ',');
            assertEquals(WriteResult.WriteResultStatus.FAILURE, result.getStatus());
        } catch (Exception e) {
            fail("FAILURE");
        }
    }

}
