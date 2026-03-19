package seedu.duke;

import java.util.ArrayList;

/**
 * Represents the list of expenses.
 * Handles adding, deleting, and retrieving expenses.
 */
public class ExpenseList {
    private final ArrayList<Expense> expenses;

    public ExpenseList() {
        this.expenses = new ArrayList<>();
    }

    /**
     * Adds a new expense to the end of the list.
     */
    public void addExpense(Expense expense) {
        if (expense == null) {
            throw new IllegalArgumentException("Expense must not be null");
        }
        expenses.add(expense);
    }

    /**
     * Returns the current number of expenses in the list.
     */
    public int getSize() {
        return expenses.size();
    }

    /**
     * Retrieves an expense from the list based on its index.
     */
    public Expense getExpense(int index) {
        return expenses.get(index);
    }

    /**
     * Removes an expense from the list and returns the removed item.
     * Required for Issue 3.
     */
    public Expense deleteExpense(int index) throws IndexOutOfBoundsException {
        return expenses.remove(index);
    }
}
