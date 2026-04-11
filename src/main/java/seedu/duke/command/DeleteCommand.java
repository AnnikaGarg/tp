package seedu.duke.command;

import seedu.duke.model.Expense;
import seedu.duke.model.ExpenseList;
import seedu.duke.ui.Ui;

import java.time.LocalDate;

/**
 * Handles the logic for deleting an expense.
 */
public class DeleteCommand extends Command {
    private final int index;
    private final String category;
    private final LocalDate date;

    /**
     * Constructs a DeleteCommand with the specified Ui and expense index.
     *
     * @param ui    The Ui object used to display user-facing messages.
     * @param index The 1-based index of the expense to delete.
     */
    public DeleteCommand(Ui ui, int index) {
        super(ui);
        this.index = index;
        this.category = null;
        this.date = null;
    }

    /**
     * Constructs a DeleteCommand for batch deletion by category.
     *
     * @param ui       The Ui object used to display user-facing messages.
     * @param category The category to delete.
     */
    public DeleteCommand(Ui ui, String category) {
        super(ui);
        this.index = -1;
        this.category = category;
        this.date = null;
    }

    /**
     * Constructs a DeleteCommand for batch deletion by date.
     *
     * @param ui   The Ui object used to display user-facing messages.
     * @param date The date to delete.
     */
    public DeleteCommand(Ui ui, LocalDate date) {
        super(ui);
        this.index = -1;
        this.category = null;
        this.date = date;
    }

    /**
     * Executes the delete command by removing the expense at the given index,
     * or by batch deleting expenses matching a category or date.
     *
     * @param expenseList The list of expenses to operate on.
     */
    @Override
    public void execute(ExpenseList expenseList) {
        assert expenseList != null : "ExpenseList must not be null";

        if (category != null) {
            int count = expenseList.deleteByCategory(category);
            ui.showBatchDelete(expenseList.getSize(), count, "category [" + category + "]");
            return;
        }

        if (date != null) {
            int count = expenseList.deleteByDate(date);
            ui.showBatchDelete(expenseList.getSize(), count, "date [" + date + "]");
            return;
        }

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
