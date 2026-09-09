package bo.ui;

import java.util.List;

import bo.task.Task;

/**
 * Collects Bo's responses for display in the JavaFX interface.
 */
public class GuiUi extends Ui {
    private final StringBuilder messages = new StringBuilder();

    @Override
    public void showError(String message) {
        messages.append(message).append('\n');
    }

    @Override
    public void showLoadingError() {
        messages.append("I could not load your saved tasks; starting with an empty list.\n");
    }

    @Override
    public void showTaskAdded(Task task, int taskCount) {
        messages.append("Added: ").append(task).append("\n");
    }

    @Override
    public void showTaskList(List<Task> tasks) {
        if (tasks.isEmpty()) {
            messages.append("Your task list is empty.\n");
            return;
        }
        for (int i = 0; i < tasks.size(); i++) {
            messages.append(i + 1).append('.').append(tasks.get(i)).append('\n');
        }
    }

    @Override
    public void showTaskMarked(Task task) {
        messages.append("Marked done: ").append(task).append('\n');
    }

    @Override
    public void showTaskUnmarked(Task task) {
        messages.append("Marked not done: ").append(task).append('\n');
    }

    @Override
    public void showTaskDeleted(Task task, int taskCount) {
        messages.append("Deleted: ").append(task).append('\n');
    }

    @Override
    public void showDeadlines(List<Integer> taskIndexes, List<Task> tasks, String heading) {
        messages.append(heading).append('\n');
        appendIndexedTasks(taskIndexes, tasks);
    }

    @Override
    public void showMatchingTasks(List<Integer> taskIndexes, List<Task> tasks) {
        messages.append("Matching tasks:\n");
        appendIndexedTasks(taskIndexes, tasks);
    }

    /**
     * Appends tasks using their zero-based indexes and one-based task numbers.
     *
     * @param taskIndexes The indexes of the tasks to append.
     * @param tasks The full task list.
     */
    private void appendIndexedTasks(List<Integer> taskIndexes, List<Task> tasks) {
        for (int taskIndex : taskIndexes) {
            messages.append(taskIndex + 1).append('.').append(tasks.get(taskIndex)).append('\n');
        }
    }

    /**
     * Returns and clears responses generated since the previous command.
     *
     * @return the pending response text
     */
    public String takeMessages() {
        String result = messages.toString();
        messages.setLength(0);
        return result;
    }
}
