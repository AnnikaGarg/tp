package seedu.duke;

import org.junit.jupiter.api.Test;
import seedu.duke.ui.Ui;

import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BudgetCommandTest {

    private static final YearMonth TEST_MONTH = YearMonth.of(2026, 4);

    @Test
    public void execute_validBudget_budgetIsSetForMonth() {
        Ui ui = new Ui();
        ExpenseList expenseList = new ExpenseList();
        BudgetCommand command = new BudgetCommand(ui, TEST_MONTH, 200.0);

        command.execute(expenseList);

        assertTrue(expenseList.hasBudget(TEST_MONTH));
        assertEquals(200.0, expenseList.getBudget(TEST_MONTH), 0.0001);
    }

    @Test
    public void execute_secondBudget_overwritesPreviousBudgetForSameMonth() {
        Ui ui = new Ui();
        ExpenseList expenseList = new ExpenseList();

        new BudgetCommand(ui, TEST_MONTH, 150.0).execute(expenseList);
        new BudgetCommand(ui, TEST_MONTH, 300.0).execute(expenseList);

        assertTrue(expenseList.hasBudget(TEST_MONTH));
        assertEquals(300.0, expenseList.getBudget(TEST_MONTH), 0.0001);
    }

    @Test
    public void execute_budgetsForDifferentMonths_storedSeparately() {
        Ui ui = new Ui();
        ExpenseList expenseList = new ExpenseList();
        YearMonth april = YearMonth.of(2026, 4);
        YearMonth may = YearMonth.of(2026, 5);

        new BudgetCommand(ui, april, 150.0).execute(expenseList);
        new BudgetCommand(ui, may, 300.0).execute(expenseList);

        assertTrue(expenseList.hasBudget(april));
        assertTrue(expenseList.hasBudget(may));
        assertEquals(150.0, expenseList.getBudget(april), 0.0001);
        assertEquals(300.0, expenseList.getBudget(may), 0.0001);
    }

    @Test
    public void execute_invalidZeroBudget_budgetNotSetForMonth() {
        Ui ui = new Ui();
        ExpenseList expenseList = new ExpenseList();
        BudgetCommand command = new BudgetCommand(ui, TEST_MONTH, 0.0);

        command.execute(expenseList);

        assertFalse(expenseList.hasBudget(TEST_MONTH));
    }

    @Test
    public void execute_negativeBudget_budgetNotSetForMonth() {
        Ui ui = new Ui();
        ExpenseList expenseList = new ExpenseList();
        BudgetCommand command = new BudgetCommand(ui, TEST_MONTH, -50.0);

        command.execute(expenseList);

        assertFalse(expenseList.hasBudget(TEST_MONTH));
    }

    @Test
    public void execute_decimalBudget_budgetStoredCorrectlyForMonth() {
        Ui ui = new Ui();
        ExpenseList expenseList = new ExpenseList();
        BudgetCommand command = new BudgetCommand(ui, TEST_MONTH, 123.45);

        command.execute(expenseList);

        assertTrue(expenseList.hasBudget(TEST_MONTH));
        assertEquals(123.45, expenseList.getBudget(TEST_MONTH), 0.0001);
    }

    @Test
    public void execute_nullAmount_viewBudgetDoesNotCreateBudget() {
        Ui ui = new Ui();
        ExpenseList expenseList = new ExpenseList();
        BudgetCommand command = new BudgetCommand(ui, TEST_MONTH, null);

        command.execute(expenseList);

        assertFalse(expenseList.hasBudget(TEST_MONTH));
    }

    @Test
    public void shouldPersist_returnsTrue() {
        Ui ui = new Ui();
        BudgetCommand command = new BudgetCommand(ui, TEST_MONTH, 150.0);

        assertTrue(command.shouldPersist());
    }

    @Test
    public void isExit_returnsFalse() {
        Ui ui = new Ui();
        BudgetCommand command = new BudgetCommand(ui, TEST_MONTH, 100.0);

        assertFalse(command.isExit());
    }
}
