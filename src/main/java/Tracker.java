import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.time.LocalDate;

/**
 * Stores and displays the tasks entered during one run of Bo.
 */
public class Tracker {
    private final List<Task> tasks;
    private final Storage storage;
    private final Ui ui;

    /**
     * Creates an empty tracker.
     *
     * @param ui the user interface used to display task messages
     */
    public Tracker(Ui ui) throws CommandException {
        tasks = new ArrayList<>();
        storage = new Storage();
        this.ui = ui;
        initializeStorage();
    }

    /**
     * Adds a task to the tracker.
     *
     * @param task the task object to store
     */
    public void addTask(Task task) throws CommandException {
        tasks.add(task);
        try {
            saveTasks();
        } catch (CommandException e) {
            tasks.remove(tasks.size() - 1);
            throw e;
        }
        ui.showTaskAdded(task, tasks.size());
    }

    /**
     * Prints all saved tasks as a numbered list.
     */
    public void printTasks() {
        ui.showTaskList(tasks);
    }

    /**
     * Displays incomplete deadlines that are due today or later, ordered by date.
     *
     * @param currentDate the date used to decide which deadlines are upcoming
     */
    public void printUpcomingDeadlines(LocalDate currentDate) {
        List<Integer> taskIndexes = getDeadlineIndexes();
        taskIndexes.removeIf(index -> {
            Deadline deadline = (Deadline) tasks.get(index);
            return deadline.isDone() || deadline.getDueDate().isBefore(currentDate);
        });
        printDeadlineIndexes(taskIndexes, " Upcoming deadlines:");
    }

    /**
     * Displays all deadlines due on one specified date.
     *
     * @param dueDate the date to match
     */
    public void printDeadlinesDueOn(LocalDate dueDate) {
        List<Integer> taskIndexes = getDeadlineIndexes();
        taskIndexes.removeIf(index -> !((Deadline) tasks.get(index)).getDueDate().equals(dueDate));
        printDeadlineIndexes(taskIndexes, " Deadlines due on " + Deadline.formatDate(dueDate) + ":");
    }

    /**
     * Gets the indexes of all deadline tasks, ordered by their due dates.
     *
     * @return the ordered indexes of deadline tasks
     */
    private List<Integer> getDeadlineIndexes() {
        List<Integer> taskIndexes = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i) instanceof Deadline) {
                taskIndexes.add(i);
            }
        }
        taskIndexes.sort(Comparator.comparing(index -> ((Deadline) tasks.get(index)).getDueDate()));
        return taskIndexes;
    }

    /**
     * Prints deadline tasks using their original task numbers.
     *
     * @param taskIndexes indexes of deadlines to display
     * @param heading the heading to print before the deadlines
     */
    private void printDeadlineIndexes(List<Integer> taskIndexes, String heading) {
        ui.showDeadlines(taskIndexes, tasks, heading);
    }

    /**
     * Checks whether a task number refers to a saved task.
     *
     * @param taskNumber the task number entered by the user
     * @return true if the task number is valid
     */
    public boolean isValidTaskNumber(int taskNumber) {
        return taskNumber >= 1 && taskNumber <= tasks.size();
    }

    /**
     * Marks a numbered task as completed.
     *
     * @param taskNumber the task number displayed in the list
     */
    public void markTask(int taskNumber) throws CommandException {
        Task task = tasks.get(taskNumber - 1);
        boolean wasDone = task.isDone();
        task.markAsDone();
        try {
            saveTasks();
        } catch (CommandException e) {
            if (!wasDone) {
                task.markAsNotDone();
            }
            throw e;
        }
        ui.showTaskMarked(task);
    }

    /**
     * Marks a numbered task as not completed.
     *
     * @param taskNumber the task number displayed in the list
     */
    public void unmarkTask(int taskNumber) throws CommandException {
        Task task = tasks.get(taskNumber - 1);
        boolean wasDone = task.isDone();
        task.markAsNotDone();
        try {
            saveTasks();
        } catch (CommandException e) {
            if (wasDone) {
                task.markAsDone();
            }
            throw e;
        }
        ui.showTaskUnmarked(task);
    }

    /**
     * Removes a numbered task and displays its confirmation message.
     *
     * @param taskNumber the task number displayed in the list
     */
    public void deleteTask(int taskNumber) throws CommandException {
        int taskIndex = taskNumber - 1;
        Task task = tasks.remove(taskIndex);
        try {
            saveTasks();
        } catch (CommandException e) {
            tasks.add(taskIndex, task);
            throw e;
        }
        ui.showTaskDeleted(task, tasks.size());
    }

    /**
     * Saves the current task list and reports a file error as a command error.
     *
     * @throws CommandException if the task list cannot be saved
     */
    private void saveTasks() throws CommandException {
        try {
            storage.save(tasks);
        } catch (java.io.IOException e) {
            throw new CommandException("I could not save your tasks. Please try again.");
        }
    }

    /**
     * Ensures that the save file exists before commands begin changing tasks.
     *
     * @throws CommandException if the save file cannot be created
     */
    private void initializeStorage() throws CommandException {
        try {
            storage.initialize();
        } catch (java.io.IOException e) {
            throw new CommandException("I could not prepare the task save file.");
        }
    }
}
