package bo.parser;

import bo.command.CommandException;
import bo.Tracker;
import bo.task.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Interprets user commands and performs the requested task operation.
 */
public class Parser {
    /**
     * Validates and performs one user command.
     *
     * @param tracker the tracker used to manage tasks
     * @param command the command entered by the user
     * @throws CommandException if the command is incomplete or unknown
     */
    public void executeCommand(Tracker tracker, String command) throws CommandException {
        if (command.isEmpty()) {
            throw new CommandException("Please enter a command. Type an action such as todo or list.");
        } else if (command.equals("list")) {
            tracker.printTasks();
        } else if (command.equals("upcoming")) {
            tracker.printUpcomingDeadlines(LocalDate.now());
        } else if (isCommand(command, "upcoming")) {
            throw new CommandException("Use upcoming without extra text to see future deadlines.");
        } else if (isCommand(command, "due")) {
            showDeadlinesDueOn(tracker, getArguments(command, "due"));
        } else if (isCommand(command, "todo")) {
            String description = getArguments(command, "todo");
            if (description.isEmpty()) {
                throw new CommandException("A todo needs a description. Use: todo <description>");
            }
            tracker.addTask(new Todo(description));
        } else if (isCommand(command, "deadline")) {
            addDeadline(tracker, command);
        } else if (isCommand(command, "event")) {
            addEvent(tracker, command);
        } else if (isCommand(command, "mark")) {
            changeTaskStatus(tracker, getArguments(command, "mark"), true);
        } else if (isCommand(command, "unmark")) {
            changeTaskStatus(tracker, getArguments(command, "unmark"), false);
        } else if (isCommand(command, "delete")) {
            deleteTask(tracker, getArguments(command, "delete"));
        } else {
            throw new CommandException(getCommandInstructions());
        }
    }

    /**
     * Checks whether an input starts with a complete command word.
     *
     * @param input the trimmed user input
     * @param commandName the command word to check
     * @return true if the input is the command or has arguments after it
     */
    private boolean isCommand(String input, String commandName) {
        return input.equals(commandName)
                || (input.startsWith(commandName)
                && input.length() > commandName.length()
                && Character.isWhitespace(input.charAt(commandName.length())));
    }

    /**
     * Gets the trimmed text following a command word.
     *
     * @param input the complete user input
     * @param commandName the command word at the start of the input
     * @return the command arguments, or an empty string when none were supplied
     */
    private String getArguments(String input, String commandName) {
        return input.substring(commandName.length()).trim();
    }

    /**
     * Adds a validated deadline task.
     *
     * @param tracker the tracker used to manage tasks
     * @param command the deadline command entered by the user
     * @throws CommandException if the command is incomplete
     */
    private void addDeadline(Tracker tracker, String command) throws CommandException {
        String details = getArguments(command, "deadline");
        int byIndex = details.indexOf("/by");
        if (byIndex < 0) {
            throw new CommandException("A deadline needs /by information. "
                    + "Use: deadline <description> /by <yyyy-MM-dd>");
        }

        String description = details.substring(0, byIndex).trim();
        String by = details.substring(byIndex + 3).trim();
        if (description.isEmpty() || by.isEmpty()) {
            throw new CommandException("A deadline needs both a description and a time. "
                    + "Use: deadline <description> /by <yyyy-MM-dd>");
        }
        LocalDate dueDate = parseDate(by, "deadline <description> /by <yyyy-MM-dd>");
        tracker.addTask(new Deadline(description, dueDate));
    }

    /**
     * Displays deadlines that fall on a date supplied by the user.
     *
     * @param tracker the tracker used to manage tasks
     * @param dateText the date entered after the due command
     * @throws CommandException if no valid date is supplied
     */
    private void showDeadlinesDueOn(Tracker tracker, String dateText) throws CommandException {
        LocalDate date = parseDate(dateText, "due <yyyy-MM-dd>");
        tracker.printDeadlinesDueOn(date);
    }

    /**
     * Parses a date entered in ISO date format.
     *
     * @param dateText the date text to parse
     * @param usage the correct command usage to show if parsing fails
     * @return the parsed date
     * @throws CommandException if the date is blank or invalid
     */
    private LocalDate parseDate(String dateText, String usage) throws CommandException {
        if (dateText.isEmpty()) {
            throw new CommandException("A date is required. Use: " + usage);
        }
        try {
            return LocalDate.parse(dateText);
        } catch (DateTimeParseException e) {
            throw new CommandException("Dates must use yyyy-MM-dd. Use: " + usage);
        }
    }

    /**
     * Adds a validated event task.
     *
     * @param tracker the tracker used to manage tasks
     * @param command the event command entered by the user
     * @throws CommandException if the command is incomplete
     */
    private void addEvent(Tracker tracker, String command) throws CommandException {
        String details = getArguments(command, "event");
        int fromIndex = details.indexOf("/from");
        int toIndex = details.indexOf("/to");
        if (fromIndex < 0 || toIndex < 0 || fromIndex >= toIndex) {
            throw new CommandException("An event needs /from and /to information. "
                    + "Use: event <description> /from <start> /to <end>");
        }

        String description = details.substring(0, fromIndex).trim();
        String from = details.substring(fromIndex + 5, toIndex).trim();
        String to = details.substring(toIndex + 3).trim();
        if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new CommandException("An event needs a description, start, and end time. "
                    + "Use: event <description> /from <start> /to <end>");
        }
        tracker.addTask(new Event(description, from, to));
    }

    /**
     * Changes a numbered task's completion status.
     *
     * @param tracker the tracker used to manage tasks
     * @param taskNumberText the task number entered by the user
     * @param shouldMarkDone whether the task should be marked as complete
     * @throws CommandException if the task number is invalid
     */
    private void changeTaskStatus(Tracker tracker, String taskNumberText, boolean shouldMarkDone)
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
    private void deleteTask(Tracker tracker, String taskNumberText) throws CommandException {
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
    private String getCommandInstructions() {
        return "I don't recognize that command. Try one of these:\n"
                + "todo <description>\n"
                + "deadline <description> /by <yyyy-MM-dd>\n"
                + "event <description> /from <start> /to <end>\n"
                + "list\n"
                + "upcoming\n"
                + "due <yyyy-MM-dd>\n"
                + "mark <task number>\n"
                + "unmark <task number>\n"
                + "delete <task number>\n"
                + "bye";
    }
}
