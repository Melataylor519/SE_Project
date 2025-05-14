import datastorecomponents.FileInputConfig;
import datastorecomponents.InputConfig;
import datastorecomponents.ReadResult;
import datastorecomponents.WriteResult;
import datastorecomponents.FileOutputConfig;
import datastorecomponents.OutputConfig;



import datastorecomponents.FileDataProcessing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;



class FileDataProcessingTest {

    private FileDataProcessing processor;

    @BeforeEach
    void setUp() {
        processor = new FileDataProcessing();
    }

    @Test
    void testRead_successfulRead() throws IOException {
        Path tempFile = Files.createTempFile("read_test_", ".txt");
        Files.write(tempFile, List.of("1", "2", "3"));

        InputConfig input = new FileInputConfig(tempFile.toString());
        ReadResult result = processor.read(input);

        assertEquals(ReadResult.Status.SUCCESS, result.getStatus());
        List<Integer> actual = StreamSupport.stream(result.getResults().spliterator(), false)
                .collect(Collectors.toList());
assertEquals(List.of(1, 2, 3), actual);

        Files.deleteIfExists(tempFile);
    }

    @Test
    void testRead_nonIntegerLines() throws IOException {
        Path tempFile = Files.createTempFile("bad_data_", ".txt");
        Files.write(tempFile, List.of("1", "x", "3"));

        InputConfig input = new FileInputConfig(tempFile.toString());
        ReadResult result = processor.read(input);

        assertEquals(ReadResult.Status.FAILURE, result.getStatus());
        assertNull(result.getResults());

        Files.deleteIfExists(tempFile);
    }

    @Test
    void testRead_emptyFilePath() {
        InputConfig input = new FileInputConfig("  ");
        assertThrows(IllegalArgumentException.class, () -> processor.read(input));
    }

    @Test
    void testRead_wrongConfigType() {
    	InputConfig input = new InputConfig() {
    	    @Override
    	    public String getFilePath() {
    	        return "some/path";
    	    }

    	    @Override
    	    public String getInputData() {
    	        return "data";
    	    }
    	};
        ReadResult result = processor.read(input);
        assertEquals(ReadResult.Status.FAILURE, result.getStatus());
        assertNull(result.getResults());
    }

    @Test
    void testAppendSingleResult_success() throws IOException {
        Path tempFile = Files.createTempFile("write_test_", ".txt");

        OutputConfig output = new FileOutputConfig(tempFile.toString());
        WriteResult result = processor.appendSingleResult(output, "ProcessedResult", '|');

        assertEquals(WriteResult.WriteResultStatus.SUCCESS, result.getStatus());

        String content = Files.readString(tempFile);
        assertTrue(content.endsWith("ProcessedResult|"));

        Files.deleteIfExists(tempFile);
    }

    @Test
    void testAppendSingleResult_failure_invalidPath() {
        OutputConfig output = new FileOutputConfig("/invalid/path/to/file.txt");
        WriteResult result = processor.appendSingleResult(output, "test", ',');
        assertEquals(WriteResult.WriteResultStatus.FAILURE, result.getStatus());
    }

    @Test
    void testAppendSingleResult_nullPath() {
        OutputConfig output = new FileOutputConfig(null);
        assertThrows(IllegalArgumentException.class, () ->
            processor.appendSingleResult(output, "test", ',')
        );
    }

    @Test
    void testAppendSingleResult_wrongConfigType() {
    	OutputConfig output = new OutputConfig() {
    	    @Override
    	    public String getFilePath() {
    	        return "some/path";
    	    }

			@Override
			public String formatOutput(String result) {
				return result;
			}


    	};
        WriteResult result = processor.appendSingleResult(output, "test", ',');
        assertEquals(WriteResult.WriteResultStatus.FAILURE, result.getStatus());
    }
}
