package seedu.duke;

import seedu.duke.ui.Ui;

/**
 * Handles the logic for displaying the loan list.
 */
public class LoansCommand extends Command {

    private final boolean showAll;

    /**
     * Constructs a LoansCommand.
     *
     * @param ui      The Ui object used to display user-facing messages.
     * @param showAll If true, repaid loans are included in the output.
     */
    public LoansCommand(Ui ui, boolean showAll) {
        super(ui);
        assert ui != null : "Ui must not be null";
        this.showAll = showAll;
    }

    /**
     * Executes the loans command by displaying the relevant subset of loans.
     *
     * @param expenseList The ExpenseList whose loan sub-list will be displayed.
     */
    @Override
    public void execute(ExpenseList expenseList) {
        assert expenseList != null : "ExpenseList must not be null";
        ui.showLoans(expenseList, showAll);
    }

    /**
     * Returns false because viewing loans does not modify data.
     *
     * @return false
     */
    @Override
    public boolean shouldPersist() {
        return false;
    }
}
