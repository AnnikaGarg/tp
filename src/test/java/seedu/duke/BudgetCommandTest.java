package seedu.duke;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
    public void execute_secondBudget_overwritesPreviousBudget() {
        Ui ui = new Ui();
        ExpenseList expenseList = new ExpenseList();

        new BudgetCommand(ui, 150.0).execute(expenseList);
        new BudgetCommand(ui, 300.0).execute(expenseList);

        assertTrue(expenseList.hasBudget());
        assertEquals(300.0, expenseList.getBudget(), 0.0001);
    }

    @Test
    public void execute_invalidZeroBudget_budgetNotSet() {
        Ui ui = new Ui();
        ExpenseList expenseList = new ExpenseList();
        BudgetCommand command = new BudgetCommand(ui, 0.0);

        command.execute(expenseList);

        // budget should NOT be set
        assertFalse(expenseList.hasBudget());
    }

    @Test
    public void execute_negativeBudget_budgetNotSet() {
        Ui ui = new Ui();
        ExpenseList expenseList = new ExpenseList();
        BudgetCommand command = new BudgetCommand(ui, -50.0);

        command.execute(expenseList);

        assertFalse(expenseList.hasBudget());
    }

    @Test
    public void execute_decimalBudget_budgetStoredCorrectly() {
        Ui ui = new Ui();
        ExpenseList expenseList = new ExpenseList();
        BudgetCommand command = new BudgetCommand(ui, 123.45);

        command.execute(expenseList);

        assertTrue(expenseList.hasBudget());
        assertEquals(123.45, expenseList.getBudget(), 0.0001);
    }

    @Test
    public void shouldPersist_returnsTrue() {
        Ui ui = new Ui();
        BudgetCommand command = new BudgetCommand(ui, 150.0);

        assertTrue(command.shouldPersist());
    }

    @Test
    public void isExit_returnsFalse() {
        Ui ui = new Ui();
        BudgetCommand command = new BudgetCommand(ui, 100.0);

        assertFalse(command.isExit());
    }
}
