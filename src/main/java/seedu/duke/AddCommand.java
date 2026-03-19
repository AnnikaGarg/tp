package seedu.duke;

/**
 * Handles the logic for adding a new expense.
 */
public class AddCommand extends Command {
    private final String description;
    private final double amount;

    /**
     * Constructs an AddCommand with the specified Ui, description and amount.
     *
     * @param ui          The Ui object used to display user-facing messages.
     * @param description Description of the expense.
     * @param amount      Monetary value of the expense.
     */
    public AddCommand(Ui ui, String description, double amount) {
        super(ui);
        this.description = description;
        this.amount = amount;
    }

    /**
     * Executes the add command by creating an Expense and adding it to the list.
     *
     * @param expenseList The list where the expense will be stored.
     */
    @Override
    public void execute(ExpenseList expenseList) {
        Expense expense = new Expense(description, amount);
        expenseList.addExpense(expense);
        ui.showAddExpense(expense, expenseList.getSize());
    }

    /**
     * Returns true because adding an expense updates persisted data.
     *
     * @return true to indicate this command should be saved.
     */
    @Override
    public boolean shouldPersist() {
        return true;
    }
}
