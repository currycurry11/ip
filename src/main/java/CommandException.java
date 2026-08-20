/**
 * Represents an error caused by an incomplete or unsupported user command.
 */
public class CommandException extends Exception {
    /**
     * Creates a command error with a message that helps the user correct it.
     *
     * @param message the explanation of the command error
     */
    public CommandException(String message) {
        super(message);
    }
}
