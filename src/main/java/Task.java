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
     * Reports whether this task is completed.
     *
     * @return true if this task is marked as done
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns the task status in the format used by the save file.
     *
     * @return {@code 1} if completed, otherwise {@code 0}
     */
    protected String getSaveStatus() {
        return isDone ? "1" : "0";
    }

    /**
     * Returns the task description for subclasses that save task data.
     *
     * @return the task description
     */
    protected String getDescription() {
        return description;
    }

    /**
     * Returns this task in a form that can be stored in the save file.
     *
     * @return the file representation of this task
     */
    public abstract String toFileString();

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
