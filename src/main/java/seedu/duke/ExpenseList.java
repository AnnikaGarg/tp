package seedu.duke;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the list of expenses tracked by the user.
 * Handles adding, deleting, retrieving, and replacing expenses.
 * Maintains a separate list of loans that does not affect expense totals or budget.
 */
public class ExpenseList {
    private java.util.ArrayList<String> categories;
    private final ArrayList<Expense> expenses;
    private final ArrayList<Loan> loans;
    private final Map<YearMonth, Double> monthlyBudgets;

    public ExpenseList() {
        this.expenses = new ArrayList<>();
        this.loans = new ArrayList<>();
        this.monthlyBudgets = new HashMap<>();
        this.categories = new ArrayList<>(Arrays.asList(
                "Food", "Transport", "Shopping", "Entertainment", "Health", "Others"
        ));
        assert this.expenses != null : "Expenses list should be initialised in constructor";
        assert this.loans != null : "Loans list should be initialised in constructor";
        assert this.monthlyBudgets != null : "Monthly budgets map should be initialised in constructor";
    }

    /**
     * Returns the master list of available categories.
     *
     * @return ArrayList of category strings.
     */
    public ArrayList<String> getCategoryList() {
        return categories;
    }

    /**
     * Retrieves a specific category from the master list by index.
     *
     * @param index The index of the category.
     * @return The category string.
     */
    public String getCategory(int index) {
        return categories.get(index);
    }

    /**
     * Formats and adds a new custom category to the list just above "Others".
     *
     * @param newCategory The raw string typed by the user.
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
     * Adds a new expense to the list in chronological order (newest date first).
     * Iterates through the existing expenses and inserts the new expense at the
     * first position where its date is after the existing expense's date,
     * keeping the list always sorted by date descending.
     *
     * @param expense The expense to add.
     * @throws IllegalArgumentException If expense is null.
     */
    public void addExpense(Expense expense) {
        if (expense == null) {
            throw new IllegalArgumentException("Expense must not be null");
        }
        assert expense != null : "Expense should be validated before adding";

        LocalDate newDate = expense.getDate();
        int insertIndex = expenses.size(); // default: append at end
        for (int i = 0; i < expenses.size(); i++) {
            if (newDate.isAfter(expenses.get(i).getDate())) {
                insertIndex = i;
                break;
            }
        }
        expenses.add(insertIndex, expense);
        addCategory(expense.getCategory());
        assert expenses.contains(expense) : "Expense should be present in list after add";
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
     * Sets or overwrites the budget for the specified month.
     *
     * @param month The month for which the budget is being set.
     * @param budget The budget amount to store. Must be greater than 0.
     * @throws IllegalArgumentException If the month is null or the budget is not greater than 0.
     */
    public void setBudget(YearMonth month, double budget) {
        if (month == null) {
            throw new IllegalArgumentException("Month must not be null");
        }
        if (budget <= 0) {
            throw new IllegalArgumentException("Budget must be greater than 0");
        }
        monthlyBudgets.put(month, budget);
    }

    /**
     * Returns the budget for the specified month.
     *
     * @param month The month whose budget is requested.
     * @return The budget for that month, or -1 if no budget has been set.
     */
    public double getBudget(YearMonth month) {
        return hasBudget(month) ? monthlyBudgets.get(month) : -1;
    }

    /**
     * Returns whether a budget has been set for the specified month.
     *
     * @param month The month to check.
     * @return true if a budget exists for the month, false otherwise.
     */
    public boolean hasBudget(YearMonth month) {
        return month != null && monthlyBudgets.containsKey(month);
    }

    /**
     * Returns the remaining budget for the specified month after subtracting
     * that month's total expenses from its budget.
     *
     * @param month The month whose remaining budget is requested.
     * @return The remaining budget. Can be negative if the budget is exceeded.
     */
    public double getRemainingBudget(YearMonth month) {
        return getBudget(month) - getTotalAmountForMonth(month);
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
     * Calculates the total amount of expenses recorded in the specified month.
     *
     * @param month The month whose total expense amount is requested.
     * @return The total expense amount for that month.
     */
    public double getTotalAmountForMonth(YearMonth month) {
        double total = 0.0;
        for (Expense expense : expenses) {
            if (YearMonth.from(expense.getDate()).equals(month)) {
                total += expense.getAmount();
            }
        }
        return total;
    }

    /**
     * Checks whether spending in the specified month exceeds that month's budget.
     *
     * @param month The month to check.
     * @return true if total monthly spending is greater than the month's budget, false otherwise.
     */
    public boolean isOverBudget(YearMonth month) {
        return hasBudget(month) && getTotalAmountForMonth(month) > getBudget(month);
    }

    /**
     * Returns a copy of all stored monthly budgets.
     *
     * @return A new map containing all monthly budgets.
     */
    public Map<YearMonth, Double> getMonthlyBudgets() {
        return new HashMap<>(monthlyBudgets);
    }

    /**
     * Returns all expenses recorded in the specified month.
     *
     * @param month The month to filter expenses by.
     * @return A new list containing only expenses from that month.
     */
    public ArrayList<Expense> getMonthlyExpenses(YearMonth month) {
        ArrayList<Expense> matchingExpenses = new ArrayList<>();
        for (Expense expense : expenses) {
            if (YearMonth.from(expense.getDate()).equals(month)) {
                matchingExpenses.add(expense);
            }
        }
        return matchingExpenses;
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
        java.util.Collections.sort(expenses, comparator);
    }

    /**
     * Adds a loan to the loan sub-list.
     * Loans are appended in the order they are recorded.
     * They do not appear in the expense list and do not affect totals or budget.
     *
     * @param loan The loan to add. Must not be null.
     * @throws IllegalArgumentException If loan is null.
     */
    public void addLoan(Loan loan) {
        if (loan == null) {
            throw new IllegalArgumentException("Loan must not be null");
        }
        loans.add(loan);
        assert loans.contains(loan) : "Loan should be present after add";
    }

    /**
     * Returns the total number of loans (outstanding + repaid).
     *
     * @return The size of the full loan list.
     */
    public int getLoanCount() {
        return loans.size();
    }

    /**
     * Returns the loan at the given 0-based index in the full loan list.
     *
     * @param index 0-based index.
     * @return The Loan at that position.
     */
    public Loan getLoan(int index) {
        return loans.get(index);
    }

    /**
     * Returns all loans, including repaid ones.
     *
     * @return A new list containing every recorded loan.
     */
    public ArrayList<Loan> getAllLoans() {
        return new ArrayList<>(loans);
    }

    /**
     * Returns only the loans that have not yet been repaid.
     * The order mirrors their position in the full list,
     * so the 1-based index used by RepayCommand is stable.
     *
     * @return A new list of outstanding loans.
     */
    public ArrayList<Loan> getOutstandingLoans() {
        ArrayList<Loan> outstanding = new ArrayList<>();
        for (Loan loan : loans) {
            if (!loan.isRepaid()) {
                outstanding.add(loan);
            }
        }
        return outstanding;
    }
}
