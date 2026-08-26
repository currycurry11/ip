import java.util.ArrayList;
import java.util.List;

/**
 * Stores and displays the tasks entered during one run of Bo.
 */
public class Tracker {
    private final List<Task> tasks;
    private final Storage storage;

    /**
     * Creates an empty tracker.
     */
    public Tracker() throws CommandException {
        tasks = new ArrayList<>();
        storage = new Storage();
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
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
    }

    /**
     * Prints all saved tasks as a numbered list.
     */
    public void printTasks() {
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
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
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("   " + task);
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
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.println("   " + task);
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
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
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
