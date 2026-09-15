package bo.ui;

import java.util.ArrayList;
import java.util.List;

import bo.task.Task;

/**
 * Collects Bo's responses for display in the JavaFX interface.
 */
public class GuiUi extends Ui {
    private final List<GuiMessage> messages = new ArrayList<>();

    @Override
    public void showError(String message) {
        messages.add(new GuiMessage(message, true));
    }

    @Override
    public void showLoadingError() {
        showError("I could not load your saved tasks; starting with an empty list.");
    }

    @Override
    public void showTaskAdded(Task task, int taskCount) {
        addMessage("Added: " + task);
    }

    @Override
    public void showTaskList(List<Task> tasks) {
        if (tasks.isEmpty()) {
            addMessage("Your task list is empty.");
            return;
        }
        StringBuilder message = new StringBuilder();
        for (int i = 0; i < tasks.size(); i++) {
            message.append(i + 1).append('.').append(tasks.get(i)).append('\n');
        }
        addMessage(message.toString().stripTrailing());
    }

    @Override
    public void showTaskMarked(Task task) {
        addMessage("Marked done: " + task);
    }

    @Override
    public void showTaskUnmarked(Task task) {
        addMessage("Marked not done: " + task);
    }

    @Override
    public void showTaskDeleted(Task task, int taskCount) {
        addMessage("Deleted: " + task);
    }

    @Override
    public void showTaskSnoozed(Task task) {
        addMessage("Snoozed: " + task);
    }

    @Override
    public void showTaskRescheduled(Task task) {
        addMessage("Rescheduled: " + task);
    }

    @Override
    public void showDeadlines(List<Integer> taskIndexes, List<Task> tasks, String heading) {
        addMessage(createIndexedTaskMessage(heading, taskIndexes, tasks));
    }

    @Override
    public void showMatchingTasks(List<Integer> taskIndexes, List<Task> tasks) {
        addMessage(createIndexedTaskMessage("Matching tasks:", taskIndexes, tasks));
    }

    /**
     * Appends tasks using their zero-based indexes and one-based task numbers.
     *
     * @param taskIndexes The indexes of the tasks to append.
     * @param tasks The full task list.
     */
    private String createIndexedTaskMessage(String heading, List<Integer> taskIndexes,
                                            List<Task> tasks) {
        StringBuilder message = new StringBuilder(heading).append('\n');
        for (int taskIndex : taskIndexes) {
            message.append(taskIndex + 1).append('.').append(tasks.get(taskIndex)).append('\n');
        }
        return message.toString().stripTrailing();
    }

    /** Adds a normal response to the queue for the graphical interface. */
    private void addMessage(String message) {
        messages.add(new GuiMessage(message, false));
    }

    /**
     * Returns and clears responses generated since the previous command.
     *
     * @return the pending typed responses
     */
    public List<GuiMessage> takeMessages() {
        List<GuiMessage> result = List.copyOf(messages);
        messages.clear();
        return result;
    }
}
