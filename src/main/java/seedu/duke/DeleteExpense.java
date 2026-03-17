package seedu.duke;

/**
 * Handles the logic for deleting an expense.
 */
public class DeleteExpense {
    private int index;

    public DeleteExpense(int index) {
        this.index = index;
    }

    public void execute(ExpenseList expenseList, Ui ui) {
        try {
            // Convert 1-based user input to 0-based index
            Expense removed = expenseList.deleteExpense(index - 1);
            ui.showDeleteExpense(removed, expenseList.getSize());
        } catch (IndexOutOfBoundsException e) {
            ui.showInvalidIndex();
        }
    }
}
