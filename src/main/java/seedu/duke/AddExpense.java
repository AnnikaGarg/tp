package seedu.duke;

public class AddExpense {
    private String description;
    private double amount;

    /**
     * Constructs an AddCommand with the specified description and amount.
     *
     * @param description Description of the expense.
     * @param amount Monetary value of the expense.
     */
    public AddExpense(String description, double amount) {
        this.description = description;
        this.amount = amount;
    }

    /**
     * Executes the add command by creating an Expense and adding it to the list.
     *
     * @param expenseList The list where the expense will be stored.
     */
    public void execute(ExpenseList expenseList, Ui ui) {
        Expense expense = new Expense(description, amount);
        expenseList.addExpense(expense);
        ui.showAddExpense(expense, expenseList.getSize());
    }
}

