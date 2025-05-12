import usercomputecomponents.UserComputeEnginePrototype;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Arrays;
import datastorecomponents.DataProcessingAPI;
import datastorecomponents.ReadResult;
import datastorecomponents.WriteResult;
import datastorecomponents.WriteResultImp;
import datastorecomponents.InputConfig;
import datastorecomponents.OutputConfig;
import datastorecomponents.ReadResultImp;

class TestUserComputeEngine {
    private UserComputeEnginePrototype computeEngine;
    private DataProcessingAPI mockClient;

    @BeforeEach
    void setUp() {
        computeEngine = Mockito.spy(new UserComputeEnginePrototype());
        mockClient = mock(DataProcessingAPI.class);
    }

    @Test
    void testProcessData_withDefaultDelimiters() throws InterruptedException {
        // Given
        String inputSource = "input.txt";
        String outputSource = "output.txt";
        final String[] DEFAULT_DELIMITERS = {",", ";", " "};

        // Mock readData to return a predefined string
        doReturn(new ReadResultImp(ReadResult.Status.SUCCESS, Arrays.asList(1,2,3))).when(mockClient).read(any(InputConfig.class));

        doReturn(new WriteResultImp(WriteResult.WriteResultStatus.SUCCESS)).when(mockClient)
            .appendSingleResult(any(OutputConfig.class), any(String.class), any(char.class));   

        // Act
        computeEngine.processData(mockClient, inputSource, outputSource, DEFAULT_DELIMITERS);
        
        Thread.sleep(500);

        // Verify process method is called correctly
        verify(computeEngine).writeData(eq(mockClient), eq(outputSource), eq("1 2 3"));
    }

    @Test
    void testProcessData_withUserDelimiters() throws InterruptedException {
        // Given
        String inputSource = "input.txt";
        String outputSource = "output.txt";

        // Mock readData to return a predefined string
        doReturn(new ReadResultImp(ReadResult.Status.SUCCESS, Arrays.asList(1,2,3))).when(mockClient).read(any(InputConfig.class));

        doReturn(new WriteResultImp(WriteResult.WriteResultStatus.SUCCESS)).when(mockClient)
            .appendSingleResult(any(OutputConfig.class), any(String.class), any(char.class));   

        // Act
        computeEngine.processData(mockClient, inputSource, outputSource, null);
        
        Thread.sleep(500);

        // Verify process method is called correctly
        verify(computeEngine).writeData(eq(mockClient), eq(outputSource), eq("1 2 3"));
    }

    @Test
    void testReadData() {
        // Given
        String inputSource = "input.txt";

        // Create input.txt file
        File file = new File(inputSource);
        try {
            file.createNewFile();
            file.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Write data to input.txt file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("Sample data from input.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        doReturn(new ReadResultImp(ReadResult.Status.SUCCESS, Arrays.asList(1,2,3))).when(mockClient).read(any(InputConfig.class));

        // Act
        String result = computeEngine.readData(mockClient, inputSource);

        // Assert
        assertEquals("1 2 3", result);
    }

    @Test
    void testWriteData() {
        // Given
        String outputSource = "output.txt";
        String data = "Processed data";

        // Create output.txt file
        File file = new File(outputSource);
        try {
            file.createNewFile();
            file.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }

        doReturn(new WriteResultImp(WriteResult.WriteResultStatus.SUCCESS)).when(mockClient)
            .appendSingleResult(any(OutputConfig.class), any(String.class), any(char.class));   

        // Act & Verify
        assertDoesNotThrow(() -> computeEngine.writeData(mockClient, outputSource, data));
    }
}
