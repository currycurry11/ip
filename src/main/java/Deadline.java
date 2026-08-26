import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents a task that must be completed by a specified date.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd uuuu", Locale.ENGLISH);

    private final LocalDate by;

    /**
     * Creates a deadline task.
     *
     * @param description the task description
     * @param by the deadline date
     */
    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns this deadline in the task-list format.
     *
     * @return the formatted deadline
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by.format(DISPLAY_DATE_FORMAT) + ")";
    }

    /**
     * Returns this deadline in the save-file format.
     *
     * @return the saved deadline text
     */
    @Override
    public String toFileString() {
        return "D | " + getSaveStatus() + " | " + getDescription() + " | " + by;
    }
}
