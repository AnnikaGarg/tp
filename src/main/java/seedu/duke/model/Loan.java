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
    private double amountRepaid;

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
        this.amountRepaid = 0;
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
     * Returns the total amount repaid so far.
     *
     * @return The cumulative repayment amount.
     */
    public double getAmountRepaid() {
        return amountRepaid;
    }

    /**
     * Returns the outstanding amount still owed.
     *
     * @return The remaining balance (original amount minus amount repaid).
     */
    public double getOutstandingAmount() {
        return getAmount() - amountRepaid;
    }

    /**
     * Marks this loan as fully repaid.
     * Can only move from outstanding → repaid (never reversed).
     */
    public void markRepaid() {
        this.amountRepaid = getAmount();
        this.isRepaid = true;
    }

    /**
     * Records a partial repayment. If the repayment covers the remaining
     * outstanding amount, the loan is automatically marked as fully repaid.
     *
     * @param repayment The amount being repaid. Must be positive and
     *                  not exceed the outstanding amount.
     * @throws IllegalArgumentException if repayment is invalid.
     */
    public void repay(double repayment) {
        if (repayment <= 0 || Double.isNaN(repayment) || Double.isInfinite(repayment)) {
            throw new IllegalArgumentException("Repayment amount must be positive");
        }
        double outstanding = getOutstandingAmount();
        if (repayment > outstanding + 0.001) {
            throw new IllegalArgumentException("Repayment exceeds outstanding amount");
        }
        this.amountRepaid += repayment;
        if (getOutstandingAmount() < 0.01) {
            this.isRepaid = true;
            this.amountRepaid = getAmount();
        }
    }

    /**
     * Sets the amount repaid directly (used when loading from storage).
     *
     * @param amountRepaid The cumulative amount already repaid.
     */
    public void setAmountRepaid(double amountRepaid) {
        this.amountRepaid = amountRepaid;
        if (getOutstandingAmount() < 0.01) {
            this.isRepaid = true;
            this.amountRepaid = getAmount();
        }
    }

    /**
     * Returns a formatted string showing borrower, amount, date, and repayment status.
     *
     * @return e.g. "John ($20.00) [Loan] [2026-04-01] [Outstanding]"
     */
    @Override
    public String toString() {
        String status;
        if (isRepaid) {
            status = "[Repaid]";
        } else if (amountRepaid > 0) {
            status = "[Outstanding: $" + String.format("%.2f", getOutstandingAmount())
                    + " remaining]";
        } else {
            status = "[Outstanding]";
        }
        return borrowerName
                + " ($" + String.format("%.2f", getAmount()) + ")"
                + " [Loan]"
                + " [" + getDate() + "]"
                + " " + status;
    }
}
