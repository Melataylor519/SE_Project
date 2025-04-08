package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import computecomponents.ComputeSystem;
import computecomponents.ComputeSystemImpl;
import computecomponents.ComputeRequest;
import computecomponents.ComputeResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.nio.file.Path;

import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import usercomputecomponents.UserComputeEngineAPI;

public class TestComputeSystem {

	@Mock
	private UserComputeEngineAPI computeEngine;

	@TempDir
	Path tempDir;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testComputeSystem() {
		// Setup test paths
		Path inputPath = tempDir.resolve("input.txt");
		Path outputPath = tempDir.resolve("output.txt");

		// Setup mock behavior
		doNothing().when(computeEngine).processData(anyString(), anyString(), any(String[].class));

		// Test with valid paths and single delimiter
		computeEngine.processData(inputPath.toString(), outputPath.toString(), new String[] { "," });
		verify(computeEngine).processData(inputPath.toString(), outputPath.toString(), new String[] { "," });

		// Test with multiple delimiters
		computeEngine.processData(inputPath.toString(), outputPath.toString(), new String[] { ",", ";", "\t" });
		verify(computeEngine).processData(inputPath.toString(), outputPath.toString(), new String[] { ",", ";", "\t" });

		// Verify total number of calls
		verify(computeEngine, times(2)).processData(anyString(), anyString(), any(String[].class));
	}

	@Test
	void testComputeSystemWithEdgeCases() {
		// Test with empty delimiter array
		computeEngine.processData(tempDir.resolve("test.txt").toString(),
				tempDir.resolve("out.txt").toString(),
				new String[0]);
		verify(computeEngine).processData(anyString(), anyString(), eq(new String[0]));

		// Test with null delimiter array
		computeEngine.processData(tempDir.resolve("test.txt").toString(),
				tempDir.resolve("out.txt").toString(),
				null);
		verify(computeEngine).processData(anyString(), anyString(), isNull());
	}
}
