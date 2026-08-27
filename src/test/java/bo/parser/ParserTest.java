package bo.parser;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import bo.Tracker;
import bo.command.CommandException;
import bo.storage.Storage;
import bo.ui.Ui;

/**
 * Tests for {@link Parser}.
 *
 * These tests cover commands that should throw CommandException (missing
 * description, missing "/by", bad date format, non-numeric task number,
 * empty task list). Each test uses a Tracker backed by a temporary save
 * file so they never read or write Bo's real data/bo.txt file.
 */
public class ParserTest {

    @TempDir
    Path tempDir;

    private final Parser parser = new Parser();
    private Tracker tracker;

    @BeforeEach
    public void setUp() {
        tracker = new Tracker(new Ui(), new Storage(tempDir.resolve("bo.txt")));
    }

    // ---------- empty / unknown command ----------

    @Test
    public void executeCommand_emptyString_throwsCommandException() {
        CommandException exception = assertThrows(CommandException.class,
                () -> parser.executeCommand(tracker, ""));

        assertTrue(exception.getMessage().toLowerCase().contains("enter a command"));
    }

    @Test
    public void executeCommand_unrecognizedWord_throwsCommandExceptionWithInstructions() {
        CommandException exception = assertThrows(CommandException.class,
                () -> parser.executeCommand(tracker, "fly"));

        assertTrue(exception.getMessage().contains("I don't recognize that command"));
    }

    // ---------- "upcoming" with trailing text ----------

    @Test
    public void executeCommand_upcomingWithExtraText_throwsCommandException() {
        assertThrows(CommandException.class,
                () -> parser.executeCommand(tracker, "upcoming now"));
    }

    // ---------- todo ----------

    @Test
    public void executeCommand_todoWithNoDescription_throwsCommandException() {
        CommandException exception = assertThrows(CommandException.class,
                () -> parser.executeCommand(tracker, "todo"));

        assertTrue(exception.getMessage().contains("todo needs a description"));
    }

    @Test
    public void executeCommand_todoWithOnlyWhitespaceDescription_throwsCommandException() {
        assertThrows(CommandException.class,
                () -> parser.executeCommand(tracker, "todo    "));
    }

    // ---------- deadline ----------

    @Test
    public void executeCommand_deadlineWithoutBy_throwsCommandException() {
        CommandException exception = assertThrows(CommandException.class,
                () -> parser.executeCommand(tracker, "deadline submit report"));

        assertTrue(exception.getMessage().contains("/by"));
    }

    @Test
    public void executeCommand_deadlineWithEmptyDescriptionBeforeBy_throwsCommandException() {
        assertThrows(CommandException.class,
                () -> parser.executeCommand(tracker, "deadline /by 2024-03-02"));
    }

    @Test
    public void executeCommand_deadlineWithEmptyDateAfterBy_throwsCommandException() {
        assertThrows(CommandException.class,
                () -> parser.executeCommand(tracker, "deadline submit report /by"));
    }

    @Test
    public void executeCommand_deadlineWithMalformedDate_throwsCommandException() {
        CommandException exception = assertThrows(CommandException.class,
                () -> parser.executeCommand(tracker, "deadline submit report /by 2-3-2024"));

        assertTrue(exception.getMessage().contains("yyyy-MM-dd"));
    }

    // ---------- due ----------

    @Test
    public void executeCommand_dueWithNoDate_throwsCommandException() {
        assertThrows(CommandException.class,
                () -> parser.executeCommand(tracker, "due"));
    }

    @Test
    public void executeCommand_dueWithInvalidDate_throwsCommandException() {
        assertThrows(CommandException.class,
                () -> parser.executeCommand(tracker, "due not-a-date"));
    }

    // ---------- event ----------

    @Test
    public void executeCommand_eventMissingFromAndTo_throwsCommandException() {
        CommandException exception = assertThrows(CommandException.class,
                () -> parser.executeCommand(tracker, "event meeting"));

        assertTrue(exception.getMessage().contains("/from and /to"));
    }

    @Test
    public void executeCommand_eventMissingTo_throwsCommandException() {
        assertThrows(CommandException.class,
                () -> parser.executeCommand(tracker, "event meeting /from monday"));
    }

    @Test
    public void executeCommand_eventFromAfterTo_throwsCommandException() {
        // "/to" appears before "/from" in the raw text -> fromIndex >= toIndex
        assertThrows(CommandException.class,
                () -> parser.executeCommand(tracker, "event meeting /to friday /from monday"));
    }

    @Test
    public void executeCommand_eventEmptyDescription_throwsCommandException() {
        assertThrows(CommandException.class,
                () -> parser.executeCommand(tracker, "event /from monday /to friday"));
    }

    // ---------- mark / unmark ----------

    @Test
    public void executeCommand_markWithNonNumericArgument_throwsCommandException() {
        CommandException exception = assertThrows(CommandException.class,
                () -> parser.executeCommand(tracker, "mark abc"));

        assertTrue(exception.getMessage().contains("mark <task number>"));
    }

    @Test
    public void executeCommand_unmarkWithNonNumericArgument_throwsCommandException() {
        assertThrows(CommandException.class,
                () -> parser.executeCommand(tracker, "unmark abc"));
    }

    @Test
    public void executeCommand_markWithNoTasksYet_throwsCommandExceptionForInvalidNumber() {
        // No tasks exist, so task number 1 cannot be valid.
        CommandException exception = assertThrows(CommandException.class,
                () -> parser.executeCommand(tracker, "mark 1"));

        assertTrue(exception.getMessage().contains("does not exist"));
    }

    // ---------- delete ----------

    @Test
    public void executeCommand_deleteWithNonNumericArgument_throwsCommandException() {
        CommandException exception = assertThrows(CommandException.class,
                () -> parser.executeCommand(tracker, "delete xyz"));

        assertTrue(exception.getMessage().contains("delete <task number>"));
    }

    @Test
    public void executeCommand_deleteWithNoTasksYet_throwsCommandExceptionForInvalidNumber() {
        assertThrows(CommandException.class,
                () -> parser.executeCommand(tracker, "delete 1"));
    }

    // ---------- isCommand boundary: command word used as a prefix of another word ----------

    @Test
    public void executeCommand_wordStartingWithListButNotList_isTreatedAsUnknown() {
        // "listing" should NOT be treated as the "list" command.
        assertThrows(CommandException.class,
                () -> parser.executeCommand(tracker, "listing"));
    }

    @Test
    public void executeCommand_todoAsPrefixOfLongerWord_isTreatedAsUnknown() {
        // "todos" should NOT be treated as "todo" with argument "s".
        CommandException exception = assertThrows(CommandException.class,
                () -> parser.executeCommand(tracker, "todos"));

        assertTrue(exception.getMessage().contains("I don't recognize that command"));
    }
}
