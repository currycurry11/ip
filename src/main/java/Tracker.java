/**
 * Stores and displays the tasks entered during one run of Bo.
 */
public class Tracker {
    private static final int MAX_TASKS = 100;

    private final String[] tasks;
    private int taskCount;

    /**
     * Creates an empty tracker that can store up to 100 tasks.
     */
    public Tracker() {
        tasks = new String[MAX_TASKS];
        taskCount = 0;
    }

    /**
     * Adds a task to the tracker.
     *
     * @param task the task text entered by the user
     */
    public void addTask(String task) {
        if (taskCount < MAX_TASKS) {
            tasks[taskCount] = task;
            taskCount++;
        }
    }

    /**
     * Prints all saved tasks as a numbered list.
     */
    public void printTasks() {
        for (int i = 0; i < taskCount; i++) {
            System.out.println(" " + (i + 1) + ". " + tasks[i]);
        }
    }
}
