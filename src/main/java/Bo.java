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
                    tracker.markTask(taskNumber);
                } else if (command.startsWith("unmark ")) {
                    int taskNumber = Integer.parseInt(command.substring(7));
                    tracker.unmarkTask(taskNumber);
                } else if (command.startsWith("todo ")) {
                    tracker.addTask(new Todo(command.substring(5)));
                } else if (command.startsWith("deadline ")) {
                    int byIndex = command.indexOf(" /by ");
                    String description = command.substring(9, byIndex);
                    String by = command.substring(byIndex + 5);
                    tracker.addTask(new Deadline(description, by));
                } else if (command.startsWith("event ")) {
                    int fromIndex = command.indexOf(" /from ");
                    int toIndex = command.indexOf(" /to ");
                    String description = command.substring(6, fromIndex);
                    String from = command.substring(fromIndex + 7, toIndex);
                    String to = command.substring(toIndex + 5);
                    tracker.addTask(new Event(description, from, to));
                } else {
                    System.out.println(" Sorry, I don't understand that command.");
                }

                printSeparator();
            }
        }
    }

    /**
     * Prints a horizontal line to separate sections of Bo's messages.
     */
    public static void printSeparator() {
        System.out.println("----------------------------------------");
    }
}
