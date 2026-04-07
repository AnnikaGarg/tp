package seedu.duke;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.duke.ui.Ui;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RepayCommandTest {

    private Ui ui;
    private ExpenseList expenseList;

    @BeforeEach
    public void setUp() {
        ui = new Ui();
        expenseList = new ExpenseList();
        expenseList.addLoan(new Loan("John", 20.00, null));
        expenseList.addLoan(new Loan("Alice", 30.00, null));
    }

    @Test
    public void constructor_nullUi_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new RepayCommand(null, 1));
    }

    @Test
    public void execute_validFirstIndex_marksFirstLoanAsRepaid() {
        new RepayCommand(ui, 1).execute(expenseList);
        assertTrue(expenseList.getLoan(0).isRepaid());
    }

    @Test
    public void execute_validFirstIndex_doesNotRepayOtherLoans() {
        new RepayCommand(ui, 1).execute(expenseList);
        assertFalse(expenseList.getLoan(1).isRepaid());
    }

    @Test
    public void execute_validSecondIndex_marksSecondLoanAsRepaid() {
        new RepayCommand(ui, 2).execute(expenseList);
        assertFalse(expenseList.getLoan(0).isRepaid());
        assertTrue(expenseList.getLoan(1).isRepaid());
    }

    @Test
    public void execute_indexTooLarge_doesNotModifyAnyLoan() {
        new RepayCommand(ui, 99).execute(expenseList);
        assertFalse(expenseList.getLoan(0).isRepaid());
        assertFalse(expenseList.getLoan(1).isRepaid());
    }

    @Test
    public void execute_noOutstandingLoans_doesNotThrow() {
        Loan repaid = new Loan("Bob", 10.00, null);
        repaid.markRepaid();
        ExpenseList allRepaid = new ExpenseList();
        allRepaid.addLoan(repaid);
        assertDoesNotThrow(() -> new RepayCommand(ui, 1).execute(allRepaid));
    }

    @Test
    public void execute_emptyLoanList_doesNotThrow() {
        assertDoesNotThrow(() -> new RepayCommand(ui, 1).execute(new ExpenseList()));
    }

    @Test
    public void execute_afterRepay_outstandingCountDecreases() {
        new RepayCommand(ui, 1).execute(expenseList);
        assertEquals(1, expenseList.getOutstandingLoans().size());
    }

    @Test
    public void execute_repayReducesOutstandingNotTotalLoanCount() {
        new RepayCommand(ui, 1).execute(expenseList);
        assertEquals(2, expenseList.getLoanCount());
        assertEquals(1, expenseList.getOutstandingLoans().size());
    }

    @Test
    public void shouldPersist_returnsTrue() {
        assertTrue(new RepayCommand(ui, 1).shouldPersist());
    }
}
