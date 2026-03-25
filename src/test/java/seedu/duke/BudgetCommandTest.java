package seedu.duke;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BudgetCommandTest {

    @Test
    public void execute_validBudget_budgetIsSet() {
        Ui ui = new Ui();
        ExpenseList expenseList = new ExpenseList();
        BudgetCommand command = new BudgetCommand(ui, 200.0);

        command.execute(expenseList);

        assertTrue(expenseList.hasBudget());
        assertEquals(200.0, expenseList.getBudget(), 0.0001);
    }

    @Test
    public void shouldPersist_returnsTrue() {
        Ui ui = new Ui();
        BudgetCommand command = new BudgetCommand(ui, 150.0);

        assertTrue(command.shouldPersist());
    }
}
