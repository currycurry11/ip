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

                try {
                    executeCommand(tracker, command);
                } catch (CommandException e) {
                    System.out.println(" " + e.getMessage());
                }

                printSeparator();
            }
        }
    }

    /**
     * Validates and performs one user command.
     *
     * @param tracker the tracker used to manage tasks
     * @param command the command entered by the user
     * @throws CommandException if the command is incomplete or unknown
     */
    private static void executeCommand(Tracker tracker, String command) throws CommandException {
        if (command.equals("list")) {
            tracker.printTasks();
        } else if (command.equals("todo") || command.startsWith("todo ")) {
            String description = command.substring(4).trim();
            if (description.isEmpty()) {
                throw new CommandException("A todo needs a description. Use: todo <description>");
            }
            tracker.addTask(new Todo(description));
        } else if (command.equals("deadline") || command.startsWith("deadline ")) {
            addDeadline(tracker, command);
        } else if (command.equals("event") || command.startsWith("event ")) {
            addEvent(tracker, command);
        } else if (command.equals("mark") || command.startsWith("mark ")) {
            changeTaskStatus(tracker, command.substring(4).trim(), true);
        } else if (command.equals("unmark") || command.startsWith("unmark ")) {
            changeTaskStatus(tracker, command.substring(6).trim(), false);
        } else if (command.equals("delete") || command.startsWith("delete ")) {
            deleteTask(tracker, command.substring(6).trim());
        } else {
            throw new CommandException(getCommandInstructions());
        }
    }

    /**
     * Adds a validated deadline task.
     *
     * @param tracker the tracker used to manage tasks
     * @param command the deadline command entered by the user
     * @throws CommandException if the command is incomplete
     */
    private static void addDeadline(Tracker tracker, String command) throws CommandException {
        String details = command.substring(8).trim();
        int byIndex = details.indexOf(" /by ");
        if (byIndex < 0) {
            throw new CommandException("A deadline needs /by information. Use: deadline <description> /by <time>");
        }

        String description = details.substring(0, byIndex).trim();
        String by = details.substring(byIndex + 5).trim();
        if (description.isEmpty() || by.isEmpty()) {
            throw new CommandException("A deadline needs both a description and a time. "
                    + "Use: deadline <description> /by <time>");
        }
        tracker.addTask(new Deadline(description, by));
    }

    /**
     * Adds a validated event task.
     *
     * @param tracker the tracker used to manage tasks
     * @param command the event command entered by the user
     * @throws CommandException if the command is incomplete
     */
    private static void addEvent(Tracker tracker, String command) throws CommandException {
        String details = command.substring(5).trim();
        int fromIndex = details.indexOf(" /from ");
        int toIndex = details.indexOf(" /to ");
        if (fromIndex < 0 || toIndex < 0 || fromIndex >= toIndex) {
            throw new CommandException("An event needs /from and /to information. "
                    + "Use: event <description> /from <start> /to <end>");
        }

        String description = details.substring(0, fromIndex).trim();
        String from = details.substring(fromIndex + 7, toIndex).trim();
        String to = details.substring(toIndex + 5).trim();
        if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new CommandException("An event needs a description, start, and end time. "
                    + "Use: event <description> /from <start> /to <end>");
        }
        tracker.addTask(new Event(description, from, to));
    }

    /**
     * Changes the completion status of a numbered task.
     *
     * @param tracker the tracker used to manage tasks
     * @param taskNumberText the task number entered by the user
     * @param shouldMarkDone whether the task should be marked as complete
     * @throws CommandException if the task number is invalid
     */
    private static void changeTaskStatus(Tracker tracker, String taskNumberText, boolean shouldMarkDone)
            throws CommandException {
        try {
            int taskNumber = Integer.parseInt(taskNumberText);
            if (!tracker.isValidTaskNumber(taskNumber)) {
                throw new CommandException("That task number does not exist. Use list to see your tasks.");
            }
            if (shouldMarkDone) {
                tracker.markTask(taskNumber);
            } else {
                tracker.unmarkTask(taskNumber);
            }
        } catch (NumberFormatException e) {
            throw new CommandException("Use mark <task number> or unmark <task number>.");
        }
    }

    /**
     * Deletes a numbered task after checking that its number is valid.
     *
     * @param tracker the tracker used to manage tasks
     * @param taskNumberText the task number entered by the user
     * @throws CommandException if the task number is invalid
     */
    private static void deleteTask(Tracker tracker, String taskNumberText) throws CommandException {
        try {
            int taskNumber = Integer.parseInt(taskNumberText);
            if (!tracker.isValidTaskNumber(taskNumber)) {
                throw new CommandException("That task number does not exist. Use list to see your tasks.");
            }
            tracker.deleteTask(taskNumber);
        } catch (NumberFormatException e) {
            throw new CommandException("Use delete <task number> to remove a task.");
        }
    }

    /**
     * Returns the commands that Bo understands.
     *
     * @return a multi-line command guide
     */
    private static String getCommandInstructions() {
        return "I don't recognize that command. Try one of these:\n"
                + "todo <description>\n"
                + "deadline <description> /by <time>\n"
                + "event <description> /from <start> /to <end>\n"
                + "list\n"
                + "mark <task number>\n"
                + "unmark <task number>\n"
                + "delete <task number>\n"
                + "bye";
    }

    /**
     * Prints a horizontal line to separate sections of Bo's messages.
     */
    public static void printSeparator() {
        System.out.println("----------------------------------------");
    }
}
