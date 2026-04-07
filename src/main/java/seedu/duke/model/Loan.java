package seedu.duke.model;

import java.time.LocalDate;

/**
 * Represents a loan — money lent by the user to another person.
 * Extends Expense so it can reuse amount/date validation,
 * but adds a borrower name and a repayment status flag.
 */
public class Loan extends Expense {

    private final String borrowerName;
    private boolean isRepaid;

    /**
     * Constructs a Loan with the given borrower, amount, and date.
     * The category is always fixed to "Loan" and cannot be changed.
     *
     * @param borrowerName The name of the person who borrowed the money. Must not be blank.
     * @param amount       The amount lent. Must be greater than zero.
     * @param date         The date the loan was made. Null defaults to today.
     */
    public Loan(String borrowerName, double amount, LocalDate date) {
        super(borrowerName, amount, "Loan", date);
        assert borrowerName != null && !borrowerName.trim().isEmpty()
                : "Borrower name must not be empty";
        assert amount > 0 : "Loan amount must be positive";
        this.borrowerName = borrowerName.trim();
        this.isRepaid = false;
    }

    /**
     * Returns the name of the person who borrowed the money.
     *
     * @return The borrower's name.
     */
    public String getBorrowerName() {
        return borrowerName;
    }

    /**
     * Returns whether this loan has been marked as repaid.
     *
     * @return true if repaid, false if still outstanding.
     */
    public boolean isRepaid() {
        return isRepaid;
    }

    /**
     * Marks this loan as repaid.
     * Can only move from outstanding → repaid (never reversed).
     */
    public void markRepaid() {
        this.isRepaid = true;
    }

    /**
     * Returns a formatted string showing borrower, amount, date, and repayment status.
     *
     * @return e.g. "John ($20.00) [Loan] [2026-04-01] [Outstanding]"
     */
    @Override
    public String toString() {
        String status = isRepaid ? "[Repaid]" : "[Outstanding]";
        return borrowerName
                + " ($" + String.format("%.2f", getAmount()) + ")"
                + " [Loan]"
                + " [" + getDate() + "]"
                + " " + status;
    }
}
