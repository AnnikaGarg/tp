package seedu.duke;

import java.time.LocalDate;

/**
 * Handles the logic for adding a new expense.
 */
public class AddCommand extends Command {
    private final String description;
    private final double amount;
    private final String category;
    private final LocalDate date;

    /**
     * Constructs an AddCommand with all four expense fields.
     * A null category defaults to "Others" and a null date defaults to today.
     *
     * @param ui          The Ui object used to display user-facing messages.
     * @param description Description of the expense.
     * @param amount      Monetary value of the expense.
     * @param category    Spending category, or null to use the default.
     * @param date        Date of the expense, or null to use today.
     */
    public AddCommand(Ui ui, String description, double amount, String category, LocalDate date) {
        super(ui);
        assert ui != null : "Ui cannot be null";
        assert description != null : "Description cannot be null";
        assert !description.isEmpty() : "Description cannot be empty";
        assert amount >= 0 : "Amount must be non-negative";
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    /**
     * Executes the add command by creating an Expense and appending it to the list.
     *
     * @param expenseList The list where the expense will be stored.
     */
    @Override
    public void execute(ExpenseList expenseList) {
        assert expenseList != null : "ExpenseList cannot be null";
        Expense expense = new Expense(description, amount, category, date);
        expenseList.addExpense(expense);
        ui.showAddExpense(expense, expenseList.getSize());
        if (expenseList.isOverBudget()) {
            ui.showBudgetExceededWarning(expenseList.getBudget(), expenseList.getTotalAmount());
        }
    }

    /**
     * Returns true because adding an expense updates persisted data.
     *
     * @return true to indicate this command should trigger a save.
     */
    @Override
    public boolean shouldPersist() {
        return true;
    }
}
