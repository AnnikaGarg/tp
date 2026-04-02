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

    @Test
    public void shouldPersist_nullAmount_returnsFalse() {
        Ui ui = new Ui();
        BudgetCommand command = new BudgetCommand(ui, null);
        assertFalse(command.shouldPersist(),
                "Viewing budget should not trigger a save");
    }

    @Test
    public void execute_viewBudgetWhenNotSet_showsNotSetMessage() {
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        java.io.PrintStream original = System.out;
        System.setOut(new java.io.PrintStream(out));

        Ui ui = new Ui();
        ExpenseList expenseList = new ExpenseList();
        new BudgetCommand(ui, null).execute(expenseList);

        System.setOut(original);
        assertTrue(out.toString().contains("Budget not set"),
                "Should show budget not set message");
    }

    @Test
    public void execute_viewBudgetWhenSet_showsBudgetDetails() {
        Ui ui = new Ui();
        ExpenseList expenseList = new ExpenseList();
        expenseList.setBudget(100.00);
        expenseList.addExpense(new Expense("Lunch", 30.00, "Food", null));

        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        java.io.PrintStream original = System.out;
        System.setOut(new java.io.PrintStream(out));

        new BudgetCommand(ui, null).execute(expenseList);

        System.setOut(original);
        String output = out.toString();
        assertTrue(output.contains("100"), "Should show budget amount");
        assertTrue(output.contains("30"), "Should show total spent");
    }

    @Test
    public void execute_setBudgetOverSpending_showsWarning() {
        Ui ui = new Ui();
        ExpenseList expenseList = new ExpenseList();
        expenseList.addExpense(new Expense("Laptop", 500.00, "Shopping", null));

        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        java.io.PrintStream original = System.out;
        System.setOut(new java.io.PrintStream(out));

        new BudgetCommand(ui, 100.0).execute(expenseList);

        System.setOut(original);
        assertTrue(out.toString().contains("exceeded"),
                "Setting budget below total should show warning");
    }
}
