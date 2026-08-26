import java.util.List;
import java.util.Scanner;

/**
 * Handles console input and output for Bo.
 */
public class Ui {
    private final Scanner scanner;

    /**
     * Creates a user interface that reads commands from standard input.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Reports whether another command can be read.
     *
     * @return true if another input line is available
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads one command from the user.
     *
     * @return the entered command
     */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /**
     * Displays Bo's welcome message.
     *
     * @param banner the banner to display
     */
    public void showWelcome(String banner) {
        showSeparator();
        System.out.println(banner);
        System.out.println("Hello! I'm Bo.");
        System.out.println("What can I do for you?");
        showSeparator();
    }

    /**
     * Displays an error message for an invalid command.
     *
     * @param message the error message
     */
    public void showError(String message) {
        System.out.println(" " + message);
    }

    /**
     * Explains that saved tasks could not be loaded and Bo will start empty.
     */
    public void showLoadingError() {
        System.out.println(" I could not load your saved tasks, so Bo is starting with an empty list.");
    }

    /**
     * Displays Bo's farewell message.
     */
    public void showGoodbye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    /**
     * Displays confirmation that a task was added.
     *
     * @param task the added task
     * @param taskCount the current number of tasks
     */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Displays every task in the task list.
     *
     * @param tasks the tasks to display
     */
    public void showTaskList(List<Task> tasks) {
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Displays selected deadlines using their original task numbers.
     *
     * @param taskIndexes the indexes of the deadlines to display
     * @param tasks the full task list
     * @param heading the heading to print before the deadlines
     */
    public void showDeadlines(List<Integer> taskIndexes, List<Task> tasks, String heading) {
        System.out.println(heading);
        if (taskIndexes.isEmpty()) {
            System.out.println(" No matching deadlines found.");
            return;
        }

        for (int taskIndex : taskIndexes) {
            System.out.println(" " + (taskIndex + 1) + "." + tasks.get(taskIndex));
        }
    }

    /**
     * Displays confirmation that a task was marked as done.
     *
     * @param task the marked task
     */
    public void showTaskMarked(Task task) {
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("   " + task);
    }

    /**
     * Displays confirmation that a task was marked as not done.
     *
     * @param task the unmarked task
     */
    public void showTaskUnmarked(Task task) {
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.println("   " + task);
    }

    /**
     * Displays confirmation that a task was removed.
     *
     * @param task the removed task
     * @param taskCount the current number of tasks
     */
    public void showTaskDeleted(Task task, int taskCount) {
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Prints a horizontal line to separate sections of Bo's messages.
     */
    public void showSeparator() {
        System.out.println("----------------------------------------");
    }
}
