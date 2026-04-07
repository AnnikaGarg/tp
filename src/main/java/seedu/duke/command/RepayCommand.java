package seedu.duke.command;

import seedu.duke.model.ExpenseList;
import seedu.duke.model.Loan;
import seedu.duke.ui.Ui;

import java.util.ArrayList;

/**
 * Handles the logic for marking a loan as repaid.
 */
public class RepayCommand extends Command {

    private final int index;

    /**
     * Constructs a RepayCommand for the given 1-based index.
     *
     * @param ui    The Ui object used to display user-facing messages.
     * @param index The 1-based position in the outstanding loans list.
     */
    public RepayCommand(Ui ui, int index) {
        super(ui);
        assert ui != null : "Ui must not be null";
        assert index > 0 : "Index must be positive";
        this.index = index;
    }

    /**
     * Executes the repay command.
     * Finds the nth outstanding loan (1-based) and marks it as repaid.
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
        loan.markRepaid();
        ui.showRepaid(loan);
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
