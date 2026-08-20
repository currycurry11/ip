import java.util.Scanner;

/**
 * Starts Bo, a simple personal assistant.
 */
public class Bo {
    /**
     * Reads and echoes user commands until the user enters {@code bye}.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        String banner = """
                 ____
                | __ )  ___
                |  _ \\ / _ \\
                | |_) | (_) |
                |____/ \\___/
                """;

        printSeparator();
        System.out.println(banner);
        System.out.println("Hello! I'm Bo.");
        System.out.println("What can I do for you?");
        printSeparator();

        try (Scanner scanner = new Scanner(System.in)) {
            Tracker tracker = new Tracker();

            while (true) {
                String command = scanner.nextLine();
                printSeparator();

                if (command.equals("bye")) {
                    System.out.println("Bye. Hope to see you again soon!");
                    printSeparator();
                    break;
                }

                if (command.equals("list")) {
                    tracker.printTasks();
                } else if (command.startsWith("mark ")) {
                    int taskNumber = Integer.parseInt(command.substring(5));
                    Task task = tracker.markTask(taskNumber);
                    System.out.println(" Nice! I've marked this task as done:");
                    System.out.println("   " + task);
                } else if (command.startsWith("unmark ")) {
                    int taskNumber = Integer.parseInt(command.substring(7));
                    Task task = tracker.unmarkTask(taskNumber);
                    System.out.println(" OK, I've marked this task as not done yet:");
                    System.out.println("   " + task);
                } else if (command.startsWith("todo ")) {
                    addTask(tracker, new Todo(command.substring(5)));
                } else if (command.startsWith("deadline ")) {
                    int byIndex = command.indexOf(" /by ");
                    String description = command.substring(9, byIndex);
                    String by = command.substring(byIndex + 5);
                    addTask(tracker, new Deadline(description, by));
                } else if (command.startsWith("event ")) {
                    int fromIndex = command.indexOf(" /from ");
                    int toIndex = command.indexOf(" /to ");
                    String description = command.substring(6, fromIndex);
                    String from = command.substring(fromIndex + 7, toIndex);
                    String to = command.substring(toIndex + 5);
                    addTask(tracker, new Event(description, from, to));
                } else {
                    System.out.println(" Sorry, I don't understand that command.");
                }

                printSeparator();
            }
        }
    }

    /**
     * Adds a task and displays its confirmation message.
     *
     * @param tracker the tracker that stores the task
     * @param task the task to add
     */
    private static void addTask(Tracker tracker, Task task) {
        tracker.addTask(task);
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + tracker.getTaskCount() + " tasks in the list.");
    }

    /**
     * Prints a horizontal line to separate sections of Bo's messages.
     */
    public static void printSeparator() {
        System.out.println("----------------------------------------");
    }
}
