package seedu.duke.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.duke.model.Expense;
import seedu.duke.model.ExpenseList;
import seedu.duke.ui.Ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeleteCommandTest {
    private ExpenseList expenseList;
    private Ui ui;

    @BeforeEach
    public void setUp() {
        expenseList = new ExpenseList();
        ui = new Ui();
        expenseList.addExpense(new Expense("Lunch", 10.00, null, null));
        expenseList.addExpense(new Expense("Dinner", 20.00, null, null));
    }

    @Test
    public void execute_validIndex_removesCorrectExpense() {
        // User types "delete 1" -> refers to "Lunch"
        DeleteCommand deleteCommand = new DeleteCommand(ui, 1);
        deleteCommand.execute(expenseList);

        assertEquals(1, expenseList.getSize());
        assertEquals("Dinner", expenseList.getExpense(0).getDescription());
    }

    @Test
    public void execute_invalidIndex_doesNotChangeListSize() {
        // User types "delete 5" (out of bounds)
        DeleteCommand deleteCommand = new DeleteCommand(ui, 5);
        deleteCommand.execute(expenseList);

        // Should handle the error gracefully and keep the list intact
        assertEquals(2, expenseList.getSize());
    }

    @Test
    public void execute_zeroIndex_doesNotChangeListSize() {
        DeleteCommand deleteCommand = new DeleteCommand(ui, 0);
        deleteCommand.execute(expenseList);

        assertEquals(2, expenseList.getSize());
    }
}
