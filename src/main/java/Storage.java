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
     * Saves every task in a simple, line-based format.
     *
     * @param tasks the tasks to save
     * @throws IOException if the data directory or save file cannot be written
     */
    public void save(List<Task> tasks) throws IOException {
        Files.createDirectories(FILE_PATH.getParent());
        List<String> taskLines = tasks.stream()
                .map(Task::toFileString)
                .toList();
        Files.write(FILE_PATH, taskLines, StandardCharsets.UTF_8);
    }
}
