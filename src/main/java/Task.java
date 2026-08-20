/**
 * Represents the shared information for every type of task.
 */
public abstract class Task {
    private final String description;
    private boolean isDone;

    /**
     * Creates an incomplete task with the given description.
     *
     * @param description the text that describes the task
     */
    protected Task(String description) {
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
     * Returns the shared completion-status part of a task display.
     *
     * @return the task's completion status and description
     */
    @Override
    public String toString() {
        String status = isDone ? "[X]" : "[ ]";
        return status + " " + description;
    }
}
