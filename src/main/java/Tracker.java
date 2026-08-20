/**
 * Stores and displays the tasks entered during one run of Bo.
 */
public class Tracker {
    private static final int MAX_TASKS = 100;

    private final Task[] tasks;
    private int taskCount;

    /**
     * Creates an empty tracker that can store up to 100 tasks.
     */
    public Tracker() {
        tasks = new Task[MAX_TASKS];
        taskCount = 0;
    }

    /**
     * Adds a task to the tracker.
     *
     * @param task the task text entered by the user
     */
    public void addTask(String task) {
        if (taskCount < MAX_TASKS) {
            tasks[taskCount] = new Task(task);
            taskCount++;
        }
    }

    /**
     * Prints all saved tasks as a numbered list.
     */
    public void printTasks() {
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            System.out.println(" " + (i + 1) + "." + tasks[i]);
        }
    }

    /**
     * Marks a numbered task as completed.
     *
     * @param taskNumber the task number displayed in the list
     * @return the completed task
     */
    public Task markTask(int taskNumber) {
        Task task = tasks[taskNumber - 1];
        task.markAsDone();
        return task;
    }

    /**
     * Marks a numbered task as not completed.
     *
     * @param taskNumber the task number displayed in the list
     * @return the incomplete task
     */
    public Task unmarkTask(int taskNumber) {
        Task task = tasks[taskNumber - 1];
        task.markAsNotDone();
        return task;
    }
}
