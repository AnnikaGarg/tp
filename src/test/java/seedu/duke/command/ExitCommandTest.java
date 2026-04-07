package seedu.duke.command;

import org.junit.jupiter.api.Test;
import seedu.duke.model.ExpenseList;
import seedu.duke.ui.Ui;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ExitCommandTest {

    @Test
    public void execute_exitCommand_setsIsExitToTrue() {
        Ui ui = new Ui();
        ExpenseList expenseList = new ExpenseList();
        ExitCommand exitCommand = new ExitCommand(ui);

        // Verify state BEFORE execution
        assertFalse(exitCommand.isExit(), "isExit should be false before execution");

        // Execute the command
        exitCommand.execute(expenseList);

        // Verify state AFTER execution
        assertTrue(exitCommand.isExit(), "isExit should be true after execution");
    }
}
