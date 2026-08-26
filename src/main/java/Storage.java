import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.AtomicMoveNotSupportedException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
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

    /**
     * Loads tasks from the save file.
     *
     * @return the tasks stored in the save file
     * @throws IOException if the save file cannot be read
     * @throws CommandException if a saved task has an invalid format
     */
    public List<Task> load() throws IOException, CommandException {
        initialize();
        List<String> taskLines = Files.readAllLines(FILE_PATH, StandardCharsets.UTF_8);
        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < taskLines.size(); i++) {
            String taskLine = taskLines.get(i).trim();
            if (!taskLine.isEmpty()) {
                tasks.add(parseTask(taskLine, i + 1));
            }
        }
        return tasks;
    }

    /**
     * Converts one save-file line into a task object.
     *
     * @param taskLine one line from the save file
     * @param lineNumber the line number used in error reporting
     * @return the task represented by the line
     * @throws CommandException if the line has an invalid format
     */
    private Task parseTask(String taskLine, int lineNumber) throws CommandException {
        String[] fields = taskLine.split("\\s*\\|\\s*", -1);
        Task task;
        try {
            task = switch (fields[0]) {
            case "T" -> createTodo(fields, lineNumber);
            case "D" -> createDeadline(fields, lineNumber);
            case "E" -> createEvent(fields, lineNumber);
            default -> throw invalidSavedTask(lineNumber);
            };
        } catch (ArrayIndexOutOfBoundsException | DateTimeParseException e) {
            throw invalidSavedTask(lineNumber);
        }

        if (fields.length < 2 || fields[1].isEmpty()) {
            throw invalidSavedTask(lineNumber);
        }
        if (fields[1].equals("1")) {
            task.markAsDone();
        } else if (!fields[1].equals("0")) {
            throw invalidSavedTask(lineNumber);
        }
        return task;
    }

    /**
     * Creates a to-do from save-file fields.
     *
     * @param fields the fields stored for the task
     * @param lineNumber the line number used in error reporting
     * @return the created to-do
     * @throws CommandException if the fields are invalid
     */
    private Todo createTodo(String[] fields, int lineNumber) throws CommandException {
        if (fields.length != 3 || fields[2].isEmpty()) {
            throw invalidSavedTask(lineNumber);
        }
        return new Todo(fields[2]);
    }

    /**
     * Creates a deadline from save-file fields.
     *
     * @param fields the fields stored for the task
     * @param lineNumber the line number used in error reporting
     * @return the created deadline
     * @throws CommandException if the fields are invalid
     */
    private Deadline createDeadline(String[] fields, int lineNumber) throws CommandException {
        if (fields.length != 4 || fields[2].isEmpty() || fields[3].isEmpty()) {
            throw invalidSavedTask(lineNumber);
        }
        return new Deadline(fields[2], LocalDate.parse(fields[3]));
    }

    /**
     * Creates an event from save-file fields.
     *
     * @param fields the fields stored for the task
     * @param lineNumber the line number used in error reporting
     * @return the created event
     * @throws CommandException if the fields are invalid
     */
    private Event createEvent(String[] fields, int lineNumber) throws CommandException {
        if (fields.length != 5 || fields[2].isEmpty() || fields[3].isEmpty() || fields[4].isEmpty()) {
            throw invalidSavedTask(lineNumber);
        }
        return new Event(fields[2], fields[3], fields[4]);
    }

    /**
     * Creates a consistent error for malformed save-file data.
     *
     * @param lineNumber the invalid line number, or zero when it is not available
     * @return the loading exception
     */
    private CommandException invalidSavedTask(int lineNumber) {
        if (lineNumber > 0) {
            return new CommandException("The saved task on line " + lineNumber + " is invalid.");
        }
        return new CommandException("A saved task has an invalid format.");
    }
}
