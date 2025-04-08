package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import usercomputecomponents.UserComputeEngineAPI;
import usercomputecomponents.UserComputeEngineImpl;
import datastorecomponents.InputConfig;
import datastorecomponents.OutputConfig;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockitoAnnotations;

import usercomputecomponents.UserComputeEnginePrototype;

public class TestUserComputeEngine {

    private UserComputeEnginePrototype computeEngine;

    @TempDir
    Path tempDir;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        computeEngine = new UserComputeEnginePrototype();
    }

    @Test
    public void testProcessData() throws IOException {
        // Setup test paths and create files
        Path inputPath = tempDir.resolve("input.txt");
        Path outputPath = tempDir.resolve("output.txt");

        // Create test files with content
        Files.writeString(inputPath, "test,data;here");
        Files.writeString(outputPath, "");

        // Test with valid paths
        computeEngine.processData(inputPath.toString(), outputPath.toString(), new String[] { "," });

        // Verify output
        String output = Files.readString(outputPath);
        assertNotNull(output);
        assertTrue(output.length() > 0);

        // Create new files for next test
        Path inputPath2 = tempDir.resolve("input2.txt");
        Path outputPath2 = tempDir.resolve("output2.txt");
        Files.writeString(inputPath2, "test;data,here");
        Files.writeString(outputPath2, "");

        // Test with multiple delimiters
        computeEngine.processData(inputPath2.toString(), outputPath2.toString(), new String[] { ",", ";" });

        // Verify output
        String output2 = Files.readString(outputPath2);
        assertNotNull(output2);
        assertTrue(output2.length() > 0);
    }

    @Test
    public void testProcessDataWithEmptyDelimiters() throws IOException {
        // Setup test paths and create files
        Path inputPath = tempDir.resolve("input3.txt");
        Path outputPath = tempDir.resolve("output3.txt");

        // Create test files with content
        Files.writeString(inputPath, "test,data;here");
        Files.writeString(outputPath, "");

        // Test with empty delimiter array
        computeEngine.processData(inputPath.toString(), outputPath.toString(), new String[0]);

        // Verify output
        String output = Files.readString(outputPath);
        assertNotNull(output);
        assertTrue(output.length() > 0);
    }
}
