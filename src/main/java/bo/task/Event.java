package bo.task;

/**
 * Represents a task with a start and end time.
 */
public class Event extends Task {
    private final String from;
    private final String to;

    /**
     * Creates an event task.
     *
     * @param description The event description.
     * @param from The start time text.
     * @param to The end time text.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns this event in the task-list format.
     *
     * @return The formatted event.
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }

    /**
     * Returns this event in the save-file format.
     *
     * @return The saved event text.
     */
    @Override
    public String toFileString() {
        return "E | " + getSaveStatus() + " | " + getDescription() + " | " + from + " | " + to;
    }
}
