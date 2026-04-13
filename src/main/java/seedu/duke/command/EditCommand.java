package seedu.duke.command;

import seedu.duke.model.Expense;
import seedu.duke.model.ExpenseList;
import seedu.duke.ui.Ui;

import java.time.YearMonth;
import java.time.LocalDate;

/**
 * Handles the logic for editing an existing expense.
 * Supports updating the amount, description, category, and date.
 * At least one field must be provided; all others retain their existing values.
 */
public class EditCommand extends Command {
    private final int index;
    private final Double newAmount;
    private final String newDescription;
    private final String newCategory;
    private final LocalDate newDate;

    /**
     * Constructs an EditCommand. At least one of the four optional fields must be non-null.
     *
     * @param ui             The Ui object used to display user-facing messages.
     * @param index          The 1-based index of the expense to edit.
     * @param newAmount      Replacement amount, or null to keep the existing value.
     * @param newDescription Replacement description, or null to keep the existing value.
     * @param newCategory    Replacement category, or null to keep the existing value.
     * @param newDate        Replacement date, or null to keep the existing value.
     */
    public EditCommand(Ui ui, int index,
                       Double newAmount, String newDescription,
                       String newCategory, LocalDate newDate) {
        super(ui);
        assert ui != null : "Ui cannot be null";
        assert index > 0 : "Index must be positive";
        assert newAmount != null || newDescription != null
                || newCategory != null || newDate != null
                : "At least one field must be provided for editing";
        this.index = index;
        this.newAmount = newAmount;
        this.newDescription = newDescription;
        this.newCategory = newCategory;
        this.newDate = newDate;
    }

    /**
     * Executes the edit command by replacing the expense at the given index
     * with a new Expense built from the merged original and updated fields.
     * Re-sorts the list by date if the date was changed.
     *
     * @param expenseList The list of expenses to operate on.
     */
    @Override
    public void execute(ExpenseList expenseList) {
        assert expenseList != null : "ExpenseList cannot be null";

        int zeroBasedIndex = index - 1;
        if (zeroBasedIndex < 0 || zeroBasedIndex >= expenseList.getSize()) {
            ui.showInvalidIndex();
            return;
        }

        if (newAmount != null && newAmount <= 0) {
            System.out.println("Invalid amount! You cannot edit an expense to be $0.00 or less.");
            return;
        }

        if (newDate != null && (newDate.getYear() < 2000 || newDate.getYear() > 2100)) {
            System.out.println("Invalid date! Please enter a realistic year (between 2000 and 2100).");
            return;
        }

        Expense existing = expenseList.getExpense(zeroBasedIndex);

        double updatedAmount = (newAmount != null) ? newAmount : existing.getAmount();
        String updatedDescription = (newDescription != null) ? newDescription : existing.getDescription();
        String updatedCategory = (newCategory != null) ? newCategory : existing.getCategory();
        LocalDate updatedDate = (newDate != null) ? newDate : existing.getDate();

        Expense updated = new Expense(updatedDescription, updatedAmount, updatedCategory, updatedDate);
        expenseList.setExpense(zeroBasedIndex, updated);

        if (newDate != null) {
            expenseList.sortExpenses(SortCommand.BY_DATE);
        }

        ui.showEditExpense(existing, updated, index);

        YearMonth updatedMonth = YearMonth.from(updated.getDate());
        if (expenseList.isOverBudget(updatedMonth)) {
            ui.showBudgetExceededWarning(
                    updatedMonth,
                    expenseList.getBudget(updatedMonth),
                    expenseList.getTotalAmountForMonth(updatedMonth)
            );
        }
    }

    /**
     * Returns true because editing an expense mutates persisted data.
     *
     * @return true to indicate this command should trigger a save.
     */
    @Override
    public boolean shouldPersist() {
        return true;
    }
}
