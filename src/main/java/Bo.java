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
                } else {
                    tracker.addTask(command);
                    System.out.println(" added: " + command);
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
