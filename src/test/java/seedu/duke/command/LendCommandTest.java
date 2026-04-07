package seedu.duke.command;

import org.junit.jupiter.api.Test;
import seedu.duke.model.Expense;
import seedu.duke.model.ExpenseList;
import seedu.duke.model.Loan;
import seedu.duke.ui.Ui;

import java.time.LocalDate;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LendCommandTest {

    private static final YearMonth TEST_MONTH = YearMonth.of(2026, 4);

    private final Ui ui = new Ui();

    @Test
    public void constructor_nullUi_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new LendCommand(null, "John", 20.00, null));
    }

    @Test
    public void execute_validLoan_addsOneLoanToList() {
        ExpenseList expenseList = new ExpenseList();

        new LendCommand(ui, "John", 20.00, null).execute(expenseList);

        assertEquals(1, expenseList.getLoanCount());
    }

    @Test
    public void execute_validLoan_loanHasCorrectBorrowerAndAmount() {
        ExpenseList expenseList = new ExpenseList();
        LocalDate date = LocalDate.of(2026, 4, 1);

        new LendCommand(ui, "Alice", 50.00, date).execute(expenseList);

        Loan loan = expenseList.getLoan(0);
        assertEquals("Alice", loan.getBorrowerName());
        assertEquals(50.00, loan.getAmount(), 0.0001);
        assertEquals(date, loan.getDate());
    }

    @Test
    public void execute_validLoan_loanIsInitiallyOutstanding() {
        ExpenseList expenseList = new ExpenseList();

        new LendCommand(ui, "John", 20.00, null).execute(expenseList);

        assertFalse(expenseList.getLoan(0).isRepaid());
    }

    @Test
    public void execute_multipleLends_allLoansAddedToList() {
        ExpenseList expenseList = new ExpenseList();

        new LendCommand(ui, "John", 20.00, null).execute(expenseList);
        new LendCommand(ui, "Alice", 50.00, null).execute(expenseList);

        assertEquals(2, expenseList.getLoanCount());
    }

    @Test
    public void execute_loanDoesNotAffectExpenseList() {
        ExpenseList expenseList = new ExpenseList();
        expenseList.addExpense(new Expense("Coffee", 3.50));

        new LendCommand(ui, "John", 100.00, null).execute(expenseList);

        assertEquals(1, expenseList.getSize());
    }

    @Test
    public void execute_loanDoesNotAffectTotalAmount() {
        ExpenseList expenseList = new ExpenseList();
        expenseList.addExpense(new Expense("Coffee", 5.00));

        new LendCommand(ui, "John", 100.00, null).execute(expenseList);

        assertEquals(5.00, expenseList.getTotalAmount(), 0.0001);
    }

    @Test
    public void execute_loanDoesNotTriggerBudgetExceeded() {
        ExpenseList expenseList = new ExpenseList();
        expenseList.setBudget(TEST_MONTH, 10.00);
        expenseList.addExpense(new Expense("Coffee", 5.00, "Food", LocalDate.of(2026, 4, 10)));

        new LendCommand(ui, "John", 100.00, null).execute(expenseList);

        assertFalse(expenseList.isOverBudget(TEST_MONTH));
    }

    @Test
    public void shouldPersist_returnsTrue() {
        assertTrue(new LendCommand(ui, "John", 20.00, null).shouldPersist());
    }
}
