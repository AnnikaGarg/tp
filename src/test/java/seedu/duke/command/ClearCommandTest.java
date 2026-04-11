package seedu.duke.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.duke.model.Expense;
import seedu.duke.model.ExpenseList;
import seedu.duke.parser.Parser;
import seedu.duke.ui.Ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClearCommandTest {
    private ExpenseList expenseList;
    private Ui ui;

    @BeforeEach
    public void setUp() {
        expenseList = new ExpenseList();
        ui = new Ui();
        expenseList.addExpense(new Expense("Lunch", 10.00, "Food", null));
        expenseList.addExpense(new Expense("Dinner", 20.00, "Food", null));
    }

    @Test
    public void execute_clearsExpenseList_whenConfirmed() {
        assertEquals(2, expenseList.getSize());

        Ui capturingUi = new Ui() {
            @Override
            public String getUserInput() {
                return "confirm";
            }
        };

        ClearCommand clearCommand = new ClearCommand(capturingUi);
        clearCommand.execute(expenseList);

        assertEquals(0, expenseList.getSize());
        assertTrue(clearCommand.shouldPersist());
    }

    @Test
    public void execute_doesNotClear_whenCancelled() {
        assertEquals(2, expenseList.getSize());

        Ui capturingUi = new Ui() {
            @Override
            public String getUserInput() {
                return "cancel";
            }
        };

        ClearCommand clearCommand = new ClearCommand(capturingUi);
        clearCommand.execute(expenseList);

        assertEquals(2, expenseList.getSize());
        assertFalse(clearCommand.shouldPersist());
    }

    @Test
    public void execute_emptyList_remainsEmpty() {
        ExpenseList emptyList = new ExpenseList();
        assertEquals(0, emptyList.getSize());

        Ui capturingUi = new Ui() {
            @Override
            public String getUserInput() {
                return "confirm";
            }
        };

        ClearCommand clearCommand = new ClearCommand(capturingUi);
        clearCommand.execute(emptyList);

        assertEquals(0, emptyList.getSize());
        assertTrue(clearCommand.shouldPersist());
    }

    @Test
    public void parse_clearCommand_returnsClearCommand() {
        Command cmd = Parser.parse("clear", ui);
        assertNotNull(cmd);
        assertTrue(cmd instanceof ClearCommand);
    }

    @Test
    public void parse_clearWithExtraArgs_returnsNull() {
        Command cmd = Parser.parse("clear expenses", ui);
        assertNull(cmd);
    }
}
