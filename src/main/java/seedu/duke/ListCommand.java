package seedu.duke;

/**
 * Handles the logic for listing all tracked expenses.
 */
public class ListCommand extends Command {

    /**
     * Constructs a ListCommand with the specified Ui instance.
     *
     * @param ui The Ui object used to display messages.
     */
    public ListCommand(Ui ui) {
        super(ui);
    }

    @Override
    public void execute(ExpenseList expenseList) {
        // Defensive coding: Ensure expenseList is never null when this executes
        assert expenseList != null : "ExpenseList should not be null in ListCommand";
        ui.showExpenseList(expenseList);
    }
}
