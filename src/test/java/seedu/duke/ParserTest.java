package seedu.duke;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ParserTest {
    private final Ui ui = new Ui();

    @Test
    public void parse_strictCommandWithArguments_returnsNull() {
        // Tests the bug fix: "exit s" should return null instead of executing
        Command command = Parser.parse("exit s", ui);
        assertNull(command, "Parser should return null when strict commands have extra arguments");
    }

    @Test
    public void parse_addCommandWithCategoryAndDate_returnsCommand() {
        // Tests the new v2.0 parsing logic
        Command command = Parser.parse("add 10.00 Lunch /c food /da 2026-03-24", ui);
        assertNotNull(command, "Parser should successfully parse complete v2.0 add commands");
    }

    @Test
    public void parse_listCommand_returnsListCommand() {
        Command command = Parser.parse("list", ui);
        assertTrue(command instanceof ListCommand, "Parser should return a ListCommand object");
    }

    @Test
    public void parse_exitCommand_returnsExitCommand() {
        Command command = Parser.parse("exit", ui);
        assertTrue(command instanceof ExitCommand, "Parser should return an ExitCommand object");
    }

    @Test
    public void parse_invalidCommandWord_returnsNull() {
        Command command = Parser.parse("jump", ui);
        assertNull(command, "Parser should return null for unknown commands");
    }

    @Test
    public void parse_addCommandMissingArguments_returnsNull() {
        Command command = Parser.parse("add", ui);
        assertNull(command, "Parser should return null when add arguments are missing");
    }

    @Test
    public void parse_addCommandValidArguments_returnsCommand() {
        Command command = Parser.parse("add 5.50 Coffee", ui);
        assertNotNull(command, "Parser should return a Command object for valid add inputs");
        assertFalse(command.isExit(), "Add command should not trigger exit");
    }

    @Test
    public void parse_deleteCommandInvalidNumber_returnsNull() {
        Command command = Parser.parse("delete abc", ui);
        assertNull(command, "Parser should return null if index is not a number");
    }

    @Test
    public void parse_addCommandNegativeAmount_returnsNull() {
        Command command = Parser.parse("add -3.50 Coffee", ui);
        assertNull(command, "Parser should return null when amount is negative");
    }

    @Test
    public void parse_deleteCommandZeroIndex_returnsNull() {
        Command command = Parser.parse("delete 0", ui);
        assertNull(command, "Parser should return null when index is zero");
    }

    @Test
    public void parse_nullCommand_returnsNull() {
        Command command = Parser.parse(null, ui);
        assertNull(command, "Parser should return null for null commands");
    }

    @Test
    public void parse_nullUi_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> Parser.parse("list", null));
    }

    @Test
    public void parse_findCommandValidKeyword_returnsFindCommand() {
        Command command = Parser.parse("find coffee", ui);
        assertTrue(command instanceof FindCommand, "Parser should return a FindCommand for valid find input");
    }

    @Test
    public void parse_findCommandNoKeyword_returnsNull() {
        Command command = Parser.parse("find", ui);
        assertNull(command, "Parser should return null when find has no keyword");
    }
}
