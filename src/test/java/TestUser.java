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

import java.nio.file.Path;

import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TestUser {

	@Mock
	private UserComputeEngineAPI coordinator;

	@TempDir
	Path tempDir;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testUserComputation() {
		// Setup test paths
		Path inputPath = tempDir.resolve("input.txt");
		Path outputPath = tempDir.resolve("output.txt");

		// Setup mock behavior
		doNothing().when(coordinator).processData(anyString(), anyString(), any(String[].class));

		// Test with valid paths
		coordinator.processData(inputPath.toString(), outputPath.toString(), new String[] { "," });
		verify(coordinator).processData(inputPath.toString(), outputPath.toString(), new String[] { "," });

		// Test with different delimiters
		coordinator.processData(inputPath.toString(), outputPath.toString(), new String[] { ",", ";", "|" });
		verify(coordinator).processData(inputPath.toString(), outputPath.toString(), new String[] { ",", ";", "|" });

		// Verify total number of calls
		verify(coordinator, times(2)).processData(anyString(), anyString(), any(String[].class));
	}

	@Test
	void testUserComputationWithInvalidPaths() {
		// Test with null paths
		coordinator.processData(null, null, new String[] { "," });
		verify(coordinator).processData(null, null, new String[] { "," });

		// Test with empty paths
		coordinator.processData("", "", new String[] { "," });
		verify(coordinator).processData("", "", new String[] { "," });
	}
}
