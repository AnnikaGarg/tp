package seedu.duke.command;

import seedu.duke.model.ExpenseList;
import seedu.duke.model.Loan;
import seedu.duke.ui.Ui;

import java.util.ArrayList;

/**
 * Handles the logic for repaying a loan (fully or partially).
 */
public class RepayCommand extends Command {

    private final int index;
    private final Double amount;

    /**
     * Constructs a RepayCommand for full repayment (backward compatible).
     *
     * @param ui    The Ui object used to display user-facing messages.
     * @param index The 1-based position in the outstanding loans list.
     */
    public RepayCommand(Ui ui, int index) {
        this(ui, index, null);
    }

    /**
     * Constructs a RepayCommand with an optional partial amount.
     *
     * @param ui     The Ui object used to display user-facing messages.
     * @param index  The 1-based position in the outstanding loans list.
     * @param amount The amount to repay, or null for full repayment.
     */
    public RepayCommand(Ui ui, int index, Double amount) {
        super(ui);
        assert ui != null : "Ui must not be null";
        assert index > 0 : "Index must be positive";
        this.index = index;
        this.amount = amount;
    }

    /**
     * Executes the repay command.
     * Finds the nth outstanding loan (1-based) and repays it fully or partially.
     * Displays an error if the index is out of range.
     * Displays an error if there are no outstanding loans at all.
     *
     * @param expenseList The ExpenseList whose loans will be updated.
     */
    @Override
    public void execute(ExpenseList expenseList) {
        assert expenseList != null : "ExpenseList must not be null";

        ArrayList<Loan> outstanding = expenseList.getOutstandingLoans();

        if (outstanding.isEmpty()) {
            ui.showNoOutstandingLoans();
            return;
        }

        int zeroIndex = index - 1;
        if (zeroIndex < 0 || zeroIndex >= outstanding.size()) {
            ui.showInvalidLoanIndex(outstanding.size());
            return;
        }

        assert zeroIndex >= 0 && zeroIndex < outstanding.size()
                : "Loan index should be in range after validation";

        Loan loan = outstanding.get(zeroIndex);

        if (amount == null) {
            loan.markRepaid();
            ui.showRepaid(loan);
        } else {
            if (amount > loan.getOutstandingAmount() + 0.001) {
                ui.showRepayExceedsOutstanding(loan.getOutstandingAmount());
                return;
            }
            loan.repay(amount);
            if (loan.isRepaid()) {
                ui.showRepaid(loan);
            } else {
                ui.showPartialRepay(loan, amount);
            }
        }
    }

    /**
     * Returns true because marking a loan repaid updates persisted data.
     *
     * @return true to indicate this command should trigger a save.
     */
    @Override
    public boolean shouldPersist() {
        return true;
    }
}
