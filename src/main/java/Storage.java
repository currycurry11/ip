import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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
        if (Files.notExists(FILE_PATH)) {
            Files.createFile(FILE_PATH);
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
        Files.write(FILE_PATH, taskLines, StandardCharsets.UTF_8);
    }
}
