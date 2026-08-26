/**
 * Represents a task that must be completed by a specified time.
 */
public class Deadline extends Task {
    private final String by;

    /**
     * Creates a deadline task.
     *
     * @param description the task description
     * @param by the deadline text
     */
    public Deadline(String description, String by) {
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
        return "[D]" + super.toString() + " (by: " + by + ")";
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
