package datastorecomponents;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import datastorecomponents.ReadResult.Status;
import datastorecomponents.WriteResult.WriteResultStatus;

/**
 * Implementation of DataProcessingAPI that adds validation and error handling
 * to file operations. This class acts as a decorator for the underlying
 * DataProcessingAPI implementation.
 */
public class DataProcessingImp implements DataProcessingAPI {
    private static final Logger LOGGER = Logger.getLogger(DataProcessingImp.class.getName());
    private final DataProcessingAPI delegate;

    /**
     * Creates a new DataProcessingImp instance that delegates to the provided
     * DataProcessingAPI implementation.
     *
     * @param delegate The underlying DataProcessingAPI implementation
     * @throws IllegalArgumentException if delegate is null
     */
    public DataProcessingImp(DataProcessingAPI delegate) {
        if (delegate == null) {
            throw new IllegalArgumentException("Delegate cannot be null");
        }
        this.delegate = delegate;
    }

    /**
     * Reads data from the specified input configuration.
     * Validates the input configuration and file before delegating to the
     * underlying implementation.
     *
     * @param inputConfig The input configuration containing the file path
     * @return A ReadResult containing the data or an error message
     */
    @Override
    public ReadResult read(InputConfig inputConfig) {
        if (inputConfig == null) {
            LOGGER.log(Level.SEVERE, "InputConfig cannot be null");
            return new ReadResultImp(Collections.emptyList(), "InputConfig cannot be null");
        }

        String filePath = inputConfig.getFilePath();
        if (filePath == null || filePath.trim().isEmpty()) {
            LOGGER.log(Level.SEVERE, "File path cannot be null or empty");
            return new ReadResultImp(Collections.emptyList(), "File path cannot be null or empty");
        }

        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            LOGGER.log(Level.SEVERE, "File does not exist: " + filePath);
            return new ReadResultImp(Collections.emptyList(), "File does not exist: " + filePath);
        }

        if (!Files.isReadable(path)) {
            LOGGER.log(Level.SEVERE, "File is not readable: " + filePath);
            return new ReadResultImp(Collections.emptyList(), "File is not readable: " + filePath);
        }

        try {
            return delegate.read(inputConfig);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error reading file: " + filePath, e);
            return new ReadResultImp(Collections.emptyList(), "Error reading file: " + e.getMessage());
        }
    }

    /**
     * Appends a single result to the specified output configuration.
     * Validates the output configuration and directory before delegating to the
     * underlying implementation.
     *
     * @param outputConfig The output configuration containing the file path
     * @param result       The result to append
     * @return A WriteResult indicating success or failure
     */
    @Override
    public WriteResult appendSingleResult(OutputConfig outputConfig, String result) {
        if (outputConfig == null) {
            LOGGER.log(Level.SEVERE, "OutputConfig cannot be null");
            return new WriteResultImp(false, "OutputConfig cannot be null");
        }

        if (result == null) {
            LOGGER.log(Level.SEVERE, "Result cannot be null");
            return new WriteResultImp(false, "Result cannot be null");
        }

        String filePath = outputConfig.getFilePath();
        if (filePath == null || filePath.trim().isEmpty()) {
            LOGGER.log(Level.SEVERE, "File path cannot be null or empty");
            return new WriteResultImp(false, "File path cannot be null or empty");
        }

        Path path = Paths.get(filePath);
        Path parentDir = path.getParent();

        if (parentDir != null) {
            try {
                if (!Files.exists(parentDir)) {
                    LOGGER.log(Level.INFO, "Creating parent directory: " + parentDir);
                    Files.createDirectories(parentDir);
                }

                if (!Files.isWritable(parentDir)) {
                    LOGGER.log(Level.SEVERE, "Parent directory is not writable: " + parentDir);
                    return new WriteResultImp(false, "Parent directory is not writable: " + parentDir);
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error creating parent directory: " + parentDir, e);
                return new WriteResultImp(false, "Error creating parent directory: " + e.getMessage());
            }
        }

        try {
            return delegate.appendSingleResult(outputConfig, result);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error writing to file: " + filePath, e);
            return new WriteResultImp(false, "Error writing to file: " + e.getMessage());
        }
    }
}
