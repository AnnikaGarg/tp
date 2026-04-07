package seedu.duke.command;

import org.junit.jupiter.api.Test;
import seedu.duke.model.ExpenseList;
import seedu.duke.model.Loan;
import seedu.duke.ui.Ui;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoansCommandTest {

    private final Ui ui = new Ui();

    @Test
    public void execute_emptyListShowAllFalse_doesNotThrow() {
        assertDoesNotThrow(() -> new LoansCommand(ui, false).execute(new ExpenseList()));
    }

    @Test
    public void execute_emptyListShowAllTrue_doesNotThrow() {
        assertDoesNotThrow(() -> new LoansCommand(ui, true).execute(new ExpenseList()));
    }

    @Test
    public void execute_emptyListShowAllTrue_printsNoLoansOnRecord() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));

        new LoansCommand(ui, true).execute(new ExpenseList());

        System.setOut(original);
        assertTrue(out.toString().contains("No loans on record"));
    }

    @Test
    public void execute_emptyListShowAllFalse_printsNoOutstandingLoans() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));

        new LoansCommand(ui, false).execute(new ExpenseList());

        System.setOut(original);
        assertTrue(out.toString().contains("No outstanding loans"));
    }

    @Test
    public void execute_allRepaidShowAllFalse_printsRepaidMessage() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));

        ExpenseList expenseList = new ExpenseList();
        Loan loan = new Loan("John", 20.00, null);
        loan.markRepaid();
        expenseList.addLoan(loan);
        new LoansCommand(ui, false).execute(expenseList);

        System.setOut(original);
        assertTrue(out.toString().contains("All loans have been repaid"));
    }

    @Test
    public void execute_outstandingLoans_displaysBorrowerName() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));

        ExpenseList expenseList = new ExpenseList();
        expenseList.addLoan(new Loan("John", 20.00, null));
        new LoansCommand(ui, false).execute(expenseList);

        System.setOut(original);
        assertTrue(out.toString().contains("John"));
    }

    @Test
    public void execute_mixedLoansShowAllTrue_displaysAllBorrowers() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));

        ExpenseList expenseList = new ExpenseList();
        expenseList.addLoan(new Loan("John", 20.00, null));
        Loan repaid = new Loan("Alice", 30.00, null);
        repaid.markRepaid();
        expenseList.addLoan(repaid);
        new LoansCommand(ui, true).execute(expenseList);

        System.setOut(original);
        String output = out.toString();
        assertTrue(output.contains("John"));
        assertTrue(output.contains("Alice"));
    }

    @Test
    public void execute_mixedLoansShowAllFalse_displaysOnlyOutstanding() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));

        ExpenseList expenseList = new ExpenseList();
        expenseList.addLoan(new Loan("John", 20.00, null));
        Loan repaid = new Loan("Alice", 30.00, null);
        repaid.markRepaid();
        expenseList.addLoan(repaid);
        new LoansCommand(ui, false).execute(expenseList);

        System.setOut(original);
        String output = out.toString();
        assertTrue(output.contains("John"));
        assertFalse(output.contains("Alice"));
    }

    @Test
    public void shouldPersist_showAllFalse_returnsFalse() {
        assertFalse(new LoansCommand(ui, false).shouldPersist());
    }

    @Test
    public void shouldPersist_showAllTrue_returnsFalse() {
        assertFalse(new LoansCommand(ui, true).shouldPersist());
    }
}
