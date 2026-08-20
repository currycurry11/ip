/**
 * Represents one task and whether it has been completed.
 */
public class Task {
    private final String description;
    private boolean isDone;

    /**
     * Creates an incomplete task with the given description.
     *
     * @param description the text that describes the task
     */
    public Task(String description) {
        this.description = description;
        isDone = false;
    }

    /**
     * Marks this task as completed.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks this task as incomplete.
     */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns this task in the format used by Bo's task list.
     *
     * @return the task's status and description
     */
    @Override
    public String toString() {
        String status = isDone ? "[X]" : "[ ]";
        return status + " " + description;
    }
}
