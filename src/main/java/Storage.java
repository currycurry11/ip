import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.AtomicMoveNotSupportedException;
import java.util.List;

/**
 * Saves Bo's tasks to a text file in the project data directory.
 */
public class Storage {
    private static final Path FILE_PATH = Path.of("data", "bo.txt");

    /**
     * Creates the data directory and an empty save file if they do not exist.
     * Existing save data is not changed.
     *
     * @throws IOException if the directory or save file cannot be created
     */
    public void initialize() throws IOException {
        Files.createDirectories(FILE_PATH.getParent());
        try {
            Files.createFile(FILE_PATH);
        } catch (FileAlreadyExistsException e) {
            // The existing save file must be preserved.
        }
    }

    /**
     * Saves every task in a simple, line-based format.
     *
     * @param tasks the tasks to save
     * @throws IOException if the data directory or save file cannot be written
     */
    public void save(List<Task> tasks) throws IOException {
        initialize();
        List<String> taskLines = tasks.stream()
                .map(Task::toFileString)
                .toList();
        Path temporaryFile = Files.createTempFile(FILE_PATH.getParent(), "bo-", ".tmp");
        try {
            Files.write(temporaryFile, taskLines, StandardCharsets.UTF_8);
            try {
                Files.move(temporaryFile, FILE_PATH, StandardCopyOption.ATOMIC_MOVE,
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temporaryFile, FILE_PATH, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temporaryFile);
        }
    }
}
