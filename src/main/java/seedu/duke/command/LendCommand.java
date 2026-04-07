package seedu.duke.command;

import seedu.duke.model.ExpenseList;
import seedu.duke.model.Loan;
import seedu.duke.ui.Ui;

import java.time.LocalDate;

/**
 * Handles the logic for recording a new loan (money lent to someone).
 */
public class LendCommand extends Command {

    private final String borrowerName;
    private final double amount;
    private final LocalDate date;

    /**
     * Constructs a LendCommand with the given loan details.
     *
     * @param ui           The Ui object used to display user-facing messages.
     * @param borrowerName The name of the person borrowing the money. Must not be blank.
     * @param amount       The amount being lent. Must be greater than zero.
     * @param date         The date of the loan, or null to default to today.
     */
    public LendCommand(Ui ui, String borrowerName, double amount, LocalDate date) {
        super(ui);
        assert ui != null : "Ui must not be null";
        assert borrowerName != null && !borrowerName.trim().isEmpty()
                : "Borrower name must not be empty";
        assert amount > 0 : "Loan amount must be positive";
        this.borrowerName = borrowerName.trim();
        this.amount = amount;
        this.date = date;
    }

    /**
     * Executes the lend command by creating a Loan and adding it to the loan list.
     * The main expense list is not modified.
     *
     * @param expenseList The ExpenseList whose loan sub-list will be updated.
     */
    @Override
    public void execute(ExpenseList expenseList) {
        assert expenseList != null : "ExpenseList must not be null";

        Loan loan = new Loan(borrowerName, amount, date);
        expenseList.addLoan(loan);
        ui.showLendAdded(loan, expenseList.getLoanCount());
    }

    /**
     * Returns true because adding a loan updates persisted data.
     *
     * @return true to indicate this command should trigger a save.
     */
    @Override
    public boolean shouldPersist() {
        return true;
    }
}
