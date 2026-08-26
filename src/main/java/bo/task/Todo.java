package bo.task;

/**
 * Represents a task without a date or time.
 */
public class Todo extends Task {
    /**
     * Creates a to-do task.
     *
     * @param description the task description
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns this to-do in the task-list format.
     *
     * @return the formatted to-do
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    /**
     * Returns this to-do in the save-file format.
     *
     * @return the saved to-do text
     */
    @Override
    public String toFileString() {
        return "T | " + getSaveStatus() + " | " + getDescription();
    }
}
