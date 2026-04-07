package seedu.duke.command;

import seedu.duke.model.Expense;
import seedu.duke.model.ExpenseList;
import seedu.duke.ui.Ui;

/**
 * Handles the logic for deleting an expense.
 */
public class DeleteCommand extends Command {
    private final int index;

    /**
     * Constructs a DeleteCommand with the specified Ui and expense index.
     *
     * @param ui    The Ui object used to display user-facing messages.
     * @param index The 1-based index of the expense to delete.
     */
    public DeleteCommand(Ui ui, int index) {
        super(ui);
        this.index = index;
    }

    /**
     * Executes the delete command by removing the expense at the given index.
     *
     * @param expenseList The list of expenses to operate on.
     */
    @Override
    public void execute(ExpenseList expenseList) {
        assert expenseList != null : "ExpenseList must not be null";
        if (index <= 0) {
            ui.showInvalidIndex();
            return;
        }
        assert index > 0 : "Index should be positive after validation";
        try {
            // Convert 1-based user input to 0-based index
            Expense removed = expenseList.deleteExpense(index - 1);
            ui.showDeleteExpense(removed, expenseList.getSize());
        } catch (IndexOutOfBoundsException e) {
            ui.showInvalidIndex();
        }
    }

    /**
     * Returns true because deleting an expense updates persisted data.
     *
     * @return true to indicate this command should be saved.
     */
    @Override
    public boolean shouldPersist() {
        return true;
    }
}
