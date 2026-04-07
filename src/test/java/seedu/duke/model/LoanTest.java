package seedu.duke.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoanTest {

    @Test
    public void constructor_validArgs_createsLoanSuccessfully() {
        Loan loan = new Loan("John", 20.00, LocalDate.of(2026, 4, 1));
        assertEquals("John", loan.getBorrowerName());
        assertEquals(20.00, loan.getAmount(), 0.0001);
        assertEquals(LocalDate.of(2026, 4, 1), loan.getDate());
        assertFalse(loan.isRepaid());
    }

    @Test
    public void constructor_nullDate_defaultsToToday() {
        Loan loan = new Loan("Alice", 50.00, null);
        assertEquals(LocalDate.now(), loan.getDate());
    }

    @Test
    public void constructor_nullBorrowerName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Loan(null, 10.00, null));
    }

    @Test
    public void constructor_emptyBorrowerName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Loan("", 10.00, null));
    }

    @Test
    public void constructor_blankBorrowerName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Loan("   ", 10.00, null));
    }

    @Test
    public void constructor_negativeAmount_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Loan("Bob", -5.00, null));
    }

    @Test
    public void constructor_categoryIsAlwaysLoan() {
        Loan loan = new Loan("John", 20.00, null);
        assertEquals("Loan", loan.getCategory());
    }

    @Test
    public void isRepaid_newLoan_returnsFalse() {
        Loan loan = new Loan("John", 20.00, null);
        assertFalse(loan.isRepaid());
    }

    @Test
    public void markRepaid_outstandingLoan_setsRepaidToTrue() {
        Loan loan = new Loan("John", 20.00, null);
        loan.markRepaid();
        assertTrue(loan.isRepaid());
    }

    @Test
    public void toString_outstandingLoan_containsBorrowerAmountAndOutstanding() {
        Loan loan = new Loan("John", 20.00, LocalDate.of(2026, 4, 1));
        String result = loan.toString();
        assertTrue(result.contains("John"));
        assertTrue(result.contains("$20.00"));
        assertTrue(result.contains("[Outstanding]"));
        assertTrue(result.contains("2026-04-01"));
    }

    @Test
    public void toString_repaidLoan_containsRepaid() {
        Loan loan = new Loan("Alice", 30.00, LocalDate.of(2026, 4, 1));
        loan.markRepaid();
        assertTrue(loan.toString().contains("[Repaid]"));
        assertFalse(loan.toString().contains("[Outstanding]"));
    }

    @Test
    public void getBorrowerName_returnsCorrectName() {
        Loan loan = new Loan("  Bob  ", 15.00, null);
        assertEquals("Bob", loan.getBorrowerName());
    }
}
