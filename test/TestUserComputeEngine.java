package test;

import src.usercomputecomponents.UserComputeEnginePrototype;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;

class TestUserComputeEngine {
    private UserComputeEnginePrototype computeEngine;
    @BeforeEach
    void setUp() {
        computeEngine = Mockito.spy(new UserComputeEnginePrototype());
    }

    @Test
    void testProcessData_withDefaultDelimiters() {
        // Given
        String inputSource = "input.txt";
        String outputSource = "output.txt";
        final String[] DEFAULT_DELIMITERS = {",", ";", " "};

        // Mock readData to return a predefined string
        doReturn("Sample,data;test").when(computeEngine).readData(inputSource);

        // Act
        computeEngine.processData(inputSource, outputSource, DEFAULT_DELIMITERS);

        // Verify process method is called correctly
        verify(computeEngine).writeData(outputSource, "Sample data test");
    }

    @Test
    void testProcessData_withUserDelimiters() {
        // Given
        String inputSource = "input.txt";
        String outputSource = "output.txt";

        // Mock readData to return a predefined string
        doReturn("Sample,data;test text").when(computeEngine).readData(inputSource);

        // Act
        computeEngine.processData(inputSource, outputSource, null);

        // Verify process method is called correctly
        verify(computeEngine).writeData(outputSource, "Sample data test text");
    }

    @Test
    void testReadData() {
        // Given
        String inputSource = "input.txt";

        // Act
        String result = computeEngine.readData(inputSource);

        // Assert
        assertEquals("Sample data from input.txt", result);
    }

    @Test
    void testWriteData() {
        // Given
        String outputSource = "output.txt";
        String data = "Processed data";

        // Act & Verify
        assertDoesNotThrow(() -> computeEngine.writeData(outputSource, data));
    }
}
