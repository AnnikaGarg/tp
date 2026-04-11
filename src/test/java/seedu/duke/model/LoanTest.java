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

    @Test
    public void newLoan_amountRepaidIsZero() {
        Loan loan = new Loan("John", 100.00, null);
        assertEquals(0, loan.getAmountRepaid(), 0.0001);
        assertEquals(100.00, loan.getOutstandingAmount(), 0.0001);
    }

    @Test
    public void repay_partialAmount_updatesAmountRepaid() {
        Loan loan = new Loan("John", 200.00, null);
        loan.repay(50.00);
        assertEquals(50.00, loan.getAmountRepaid(), 0.0001);
        assertEquals(150.00, loan.getOutstandingAmount(), 0.0001);
        assertFalse(loan.isRepaid());
    }

    @Test
    public void repay_fullAmount_marksAsRepaid() {
        Loan loan = new Loan("John", 200.00, null);
        loan.repay(200.00);
        assertTrue(loan.isRepaid());
        assertEquals(200.00, loan.getAmountRepaid(), 0.0001);
        assertEquals(0, loan.getOutstandingAmount(), 0.01);
    }

    @Test
    public void repay_multiplePartial_accumulatesCorrectly() {
        Loan loan = new Loan("John", 200.00, null);
        loan.repay(50.00);
        loan.repay(100.00);
        assertEquals(150.00, loan.getAmountRepaid(), 0.0001);
        assertEquals(50.00, loan.getOutstandingAmount(), 0.0001);
        assertFalse(loan.isRepaid());
    }

    @Test
    public void repay_multiplePartialCoveringFull_marksAsRepaid() {
        Loan loan = new Loan("John", 200.00, null);
        loan.repay(100.00);
        loan.repay(100.00);
        assertTrue(loan.isRepaid());
    }

    @Test
    public void repay_zeroAmount_throwsIllegalArgumentException() {
        Loan loan = new Loan("John", 200.00, null);
        assertThrows(IllegalArgumentException.class, () -> loan.repay(0));
    }

    @Test
    public void repay_negativeAmount_throwsIllegalArgumentException() {
        Loan loan = new Loan("John", 200.00, null);
        assertThrows(IllegalArgumentException.class, () -> loan.repay(-50.00));
    }

    @Test
    public void repay_exceedsOutstanding_throwsIllegalArgumentException() {
        Loan loan = new Loan("John", 200.00, null);
        assertThrows(IllegalArgumentException.class, () -> loan.repay(300.00));
    }

    @Test
    public void setAmountRepaid_setsValueDirectly() {
        Loan loan = new Loan("John", 200.00, null);
        loan.setAmountRepaid(75.00);
        assertEquals(75.00, loan.getAmountRepaid(), 0.0001);
        assertEquals(125.00, loan.getOutstandingAmount(), 0.0001);
        assertFalse(loan.isRepaid());
    }

    @Test
    public void setAmountRepaid_fullAmount_marksAsRepaid() {
        Loan loan = new Loan("John", 200.00, null);
        loan.setAmountRepaid(200.00);
        assertTrue(loan.isRepaid());
    }

    @Test
    public void markRepaid_setsAmountRepaidToFullAmount() {
        Loan loan = new Loan("John", 200.00, null);
        loan.markRepaid();
        assertEquals(200.00, loan.getAmountRepaid(), 0.0001);
    }

    @Test
    public void toString_partiallyRepaidLoan_containsRemaining() {
        Loan loan = new Loan("John", 200.00, LocalDate.of(2026, 4, 1));
        loan.repay(50.00);
        String result = loan.toString();
        assertTrue(result.contains("$150.00"));
        assertTrue(result.contains("remaining"));
    }
}
