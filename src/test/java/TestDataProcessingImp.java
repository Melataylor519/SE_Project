package datastorecomponents;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TestDataProcessingImp {
    @Mock
    private DataProcessingAPI mockDelegate;

    private DataProcessingImp validator;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new DataProcessingImp(mockDelegate);
    }

    @Test
    void testReadValidation() throws IOException {
        // Create a test file
        Path testFile = tempDir.resolve("test.txt");
        Files.writeString(testFile, "test content");

        // Test valid input
        InputConfig validConfig = new SimpleInputConfig(testFile.toString());
        ReadResult expectedResult = new ReadResultImp(ReadResult.Status.SUCCESS, Collections.emptyList());
        when(mockDelegate.read(validConfig)).thenReturn(expectedResult);

        ReadResult result = validator.read(validConfig);
        assertEquals(ReadResult.Status.SUCCESS, result.getStatus());
        verify(mockDelegate).read(validConfig);

        // Test null config
        result = validator.read(null);
        assertEquals(ReadResult.Status.FAILURE, result.getStatus());
        assertFalse(result.getResults().iterator().hasNext(), "Should have no results");
        verifyNoMoreInteractions(mockDelegate);

        // Test non-existent file
        InputConfig nonExistentConfig = new SimpleInputConfig(tempDir.resolve("nonexistent.txt").toString());
        result = validator.read(nonExistentConfig);
        assertEquals(ReadResult.Status.FAILURE, result.getStatus());
        assertFalse(result.getResults().iterator().hasNext(), "Should have no results");
        verifyNoMoreInteractions(mockDelegate);

        // Test delegate throwing exception
        when(mockDelegate.read(validConfig)).thenThrow(new RuntimeException("Test error"));
        result = validator.read(validConfig);
        assertEquals(ReadResult.Status.FAILURE, result.getStatus());
        assertFalse(result.getResults().iterator().hasNext(), "Should have no results");
    }

    @Test
    void testAppendSingleResultValidation() throws IOException {
        // Create output directory
        Path outputDir = tempDir.resolve("output");
        Files.createDirectories(outputDir);
        Path outputFile = outputDir.resolve("output.txt");

        // Test valid output
        OutputConfig validConfig = new SimpleOutputConfig(outputFile.toString());
        WriteResult expectedResult = new WriteResultImp(WriteResult.WriteResultStatus.SUCCESS);
        when(mockDelegate.appendSingleResult(validConfig, "test", ',')).thenReturn(expectedResult);

        WriteResult result = validator.appendSingleResult(validConfig, "test", ',');
        assertEquals(WriteResult.WriteResultStatus.SUCCESS, result.getStatus());
        verify(mockDelegate).appendSingleResult(validConfig, "test", ',');

        // Test null config
        result = validator.appendSingleResult(null, "test", ',');
        assertEquals(WriteResult.WriteResultStatus.FAILURE, result.getStatus());
        verifyNoMoreInteractions(mockDelegate);

        // Test non-writable directory
        Path readOnlyDir = tempDir.resolve("readonly");
        Files.createDirectories(readOnlyDir);
        readOnlyDir.toFile().setWritable(false);
        OutputConfig readOnlyConfig = new SimpleOutputConfig(readOnlyDir.resolve("test.txt").toString());

        result = validator.appendSingleResult(readOnlyConfig, "test", ',');
        assertEquals(WriteResult.WriteResultStatus.FAILURE, result.getStatus());
        verifyNoMoreInteractions(mockDelegate);

        // Test delegate throwing exception
        when(mockDelegate.appendSingleResult(validConfig, "test", ',')).thenThrow(new RuntimeException("Test error"));
        result = validator.appendSingleResult(validConfig, "test", ',');
        assertEquals(WriteResult.WriteResultStatus.FAILURE, result.getStatus());
    }
}
