import usercomputecomponents.UserComputeEnginePrototype;
import datastorecomponents.DataStoreClient;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;

class TestUserComputeEngine {
    private UserComputeEnginePrototype computeEngine;
    private DataStoreClient mockClient;

    @BeforeEach
    void setUp() {
        computeEngine = Mockito.spy(new UserComputeEnginePrototype());
        mockClient = mock(DataStoreClient.class);
    }

    @Test
    void testProcessData_withDefaultDelimiters() {
        // Given
        String inputSource = "input.txt";
        String outputSource = "output.txt";
        final String[] DEFAULT_DELIMITERS = {",", ";", " "};

        // Mock readData to return a predefined string
        doReturn("Sample,data;test")
            .when(computeEngine).readData(any(DataStoreClient.class), eq(inputSource));

        // Act
        computeEngine.processData(mockClient, inputSource, outputSource, DEFAULT_DELIMITERS);

        // Verify process method is called correctly
        verify(computeEngine).writeData(eq(mockClient), eq(outputSource), eq("Sample data test"));
    }

    @Test
    void testProcessData_withUserDelimiters() {
        // Given
        String inputSource = "input.txt";
        String outputSource = "output.txt";

        // Mock readData to return a predefined string
        doReturn("Sample,data;test text")
            .when(computeEngine).readData(any(DataStoreClient.class), eq(inputSource));

        // Act
        computeEngine.processData(mockClient, inputSource, outputSource, null);

        // Verify process method is called correctly
        verify(computeEngine).writeData(eq(mockClient), eq(outputSource), eq("Sample data test text"));
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

        // Act
        String result = computeEngine.readData(mockClient, inputSource);

        // Assert
        assertEquals("Sample data from input.txt", result);
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

        // Act & Verify
        assertDoesNotThrow(() -> computeEngine.writeData(mockClient, outputSource, data));
    }
}
