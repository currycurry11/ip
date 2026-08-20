import java.util.ArrayList;
import java.util.List;

/**
 * Stores and displays the tasks entered during one run of Bo.
 */
public class Tracker {
    private final List<Task> tasks;

    /**
     * Creates an empty tracker.
     */
    public Tracker() {
        tasks = new ArrayList<>();
    }

    /**
     * Adds a task to the tracker.
     *
     * @param task the task object to store
     */
    public void addTask(Task task) {
        tasks.add(task);
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
    public void markTask(int taskNumber) {
        Task task = tasks.get(taskNumber - 1);
        task.markAsDone();
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("   " + task);
    }

    /**
     * Marks a numbered task as not completed.
     *
     * @param taskNumber the task number displayed in the list
     */
    public void unmarkTask(int taskNumber) {
        Task task = tasks.get(taskNumber - 1);
        task.markAsNotDone();
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.println("   " + task);
    }

    /**
     * Removes a numbered task and displays its confirmation message.
     *
     * @param taskNumber the task number displayed in the list
     */
    public void deleteTask(int taskNumber) {
        Task task = tasks.remove(taskNumber - 1);
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
    }
}
