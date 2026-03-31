package seedu.duke;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Represents the list of expenses tracked by the user.
 * Handles adding, deleting, retrieving, and replacing expenses.
 */
public class ExpenseList {
    private java.util.ArrayList<String> categories;
    private final ArrayList<Expense> expenses;
    private double budget;
    public ExpenseList() {
        this.expenses = new ArrayList<>();
        this.budget = -1;
        this.categories = new ArrayList<>(Arrays.asList(
                "Food", "Transport", "Shopping", "Entertainment", "Health", "Others"
        ));
        assert this.expenses != null : "Expenses list should be initialised in constructor";
    }

    /**
     * Returns the master list of available categories.
     * * @return ArrayList of category strings.
     */
    public ArrayList<String> getCategoryList() {
        return categories;
    }

    /**
     * Retrieves a specific category from the master list by index.
     * * @param index The index of the category.
     * @return The category string.
     */
    public String getCategory(int index) {
        return categories.get(index);
    }

    /**
     * Formats and adds a new custom category to the list just above "Others".
     * * @param newCategory The raw string typed by the user.
     */
    public void addCategory(String newCategory) {
        if (newCategory == null || newCategory.trim().isEmpty()) {
            return;
        }

        // Format it to Title Case so it looks clean in the list
        String trimmed = newCategory.trim();
        String formatted = trimmed.substring(0, 1).toUpperCase() + trimmed.substring(1).toLowerCase();

        // If it doesn't exist yet, insert it right before "Others"
        if (!categories.contains(formatted)) {
            categories.add(categories.size() - 1, formatted);
        }
    }
    /**
     * Adds a new expense to the end of the list.
     *
     * @param expense The expense to add.
     */
    public void addExpense(Expense expense) {
        if (expense == null) {
            throw new IllegalArgumentException("Expense must not be null");
        }
        assert expense != null : "Expense should be validated before adding";
        expenses.add(expense);
        addCategory(expense.getCategory());
        assert expenses.get(expenses.size() - 1) == expense : "Expense should be at end of list after add";
    }
    /**
     * Returns the current number of expenses in the list.
     *
     * @return The number of expenses currently in the list.
     */
    public int getSize() {
        return expenses.size();
    }
    /**
     * Returns the expense at the specified 0-based index.
     *
     * @param index The 0-based position of the expense to retrieve.
     * @return The expense at the given index.
     */
    public Expense getExpense(int index) {
        return expenses.get(index);
    }
    /**
     * Replaces the expense at the given 0-based index with a new one.
     *
     * @param index   The 0-based position of the expense to replace.
     * @param expense The new Expense object to store at that position.
     */
    public void setExpense(int index, Expense expense) {
        if (expense == null) {
            throw new IllegalArgumentException("Expense must not be null");
        }
        expenses.set(index, expense);
    }
    /**
     * Sets the spending budget.
     *
     * @param budget The budget amount to set. Must be non-negative.
     * @throws IllegalArgumentException If the budget is negative.
     */
    public void setBudget(double budget) {
        if (budget < 0) {
            throw new IllegalArgumentException("Budget cannot be negative");
        }
        this.budget = budget;
    }
    /**
     * Returns the currently set budget.
     *
     * @return The budget amount, or -1 if no budget has been set.
     */
    public double getBudget() {
        return budget;
    }
    /**
     * Returns whether a budget has been set.
     *
     * @return true if a budget exists, false otherwise.
     */
    public boolean hasBudget() {
        return budget >= 0;
    }
    /**
     * Calculates the total amount of all expenses.
     *
     * @return The total expense amount.
     */
    public double getTotalAmount() {
        double total = 0.0;
        for (Expense expense : expenses) {
            total += expense.getAmount();
        }
        return total;
    }
    /**
     * Checks whether the total spending exceeds the budget.
     *
     * @return true if total spending is greater than the budget, false otherwise.
     */
    public boolean isOverBudget() {
        return hasBudget() && getTotalAmount() > budget;
    }
    /**
     * Removes and returns the expense at the given 0-based index.
     *
     * @param index The 0-based position of the expense to delete.
     * @return The expense that was removed.
     * @throws IndexOutOfBoundsException If the index is out of range.
     */
    public Expense deleteExpense(int index) throws IndexOutOfBoundsException {
        return expenses.remove(index);
    }

    /**
     * Sorts the expense list in place using the given comparator.
     *
     * @param comparator The comparator to determine the order of expenses.
     */
    public void sortExpenses(Comparator<Expense> comparator) {
        assert comparator != null : "Comparator must not be null";
        expenses.sort(comparator);
    }
}

