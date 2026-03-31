package seedu.duke;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Handles the logic for finding expenses using keyword, category, date range,
 * and amount range filters. Results can be sorted ascending or descending by amount.
 */
public class FindCommand extends Command {
    private final String keyword;
    private final String categoryFilter;
    private final LocalDate dateMin;
    private final LocalDate dateMax;
    private final Double amountMin;
    private final Double amountMax;
    private final String sortOrder;

    /**
     * Constructs a FindCommand with all filter and sort options.
     *
     * @param ui             The Ui object used to display results.
     * @param keyword        The keyword to search for. May be empty if other filters are set.
     * @param categoryFilter The category to filter by, or null for no category filter.
     * @param dateMin        Minimum date (inclusive), or null for no lower bound.
     * @param dateMax        Maximum date (inclusive), or null for no upper bound.
     * @param amountMin      Minimum amount (inclusive), or null for no lower bound.
     * @param amountMax      Maximum amount (inclusive), or null for no upper bound.
     * @param sortOrder      "asc" or "desc" to sort by amount, or null for default order.
     */
    public FindCommand(Ui ui, String keyword, String categoryFilter,
                       LocalDate dateMin, LocalDate dateMax,
                       Double amountMin, Double amountMax, String sortOrder) {
        super(ui);
        this.keyword = keyword;
        this.categoryFilter = categoryFilter;
        this.dateMin = dateMin;
        this.dateMax = dateMax;
        this.amountMin = amountMin;
        this.amountMax = amountMax;
        this.sortOrder = sortOrder;
    }

    /**
     * Constructs a FindCommand with keyword search only (no filters).
     *
     * @param ui      The Ui object used to display results.
     * @param keyword The keyword to search for.
     */
    public FindCommand(Ui ui, String keyword) {
        this(ui, keyword, null, null, null, null, null, null);
    }

    /**
     * Executes the find command by scanning all expenses for matches based on
     * the configured filters, optionally sorting, then displaying the results.
     *
     * @param expenseList The list of expenses to search through.
     */
    @Override
    public void execute(ExpenseList expenseList) {
        ArrayList<Expense> matches = new ArrayList<>();

        for (int i = 0; i < expenseList.getSize(); i++) {
            Expense expense = expenseList.getExpense(i);

            if (!matchesCategory(expense)) {
                continue;
            }
            if (!matchesKeyword(expense)) {
                continue;
            }
            if (!matchesDateRange(expense)) {
                continue;
            }
            if (!matchesAmountRange(expense)) {
                continue;
            }

            matches.add(expense);
        }

        if (sortOrder != null) {
            sortMatches(matches);
        }

        ui.showFindResults(matches, keyword, categoryFilter);
    }

    private boolean matchesCategory(Expense expense) {
        if (categoryFilter == null) {
            return true;
        }
        return expense.getCategory().toLowerCase().equals(categoryFilter.toLowerCase());
    }

    private boolean matchesKeyword(Expense expense) {
        if (keyword.isEmpty()) {
            return true;
        }
        String lowerKeyword = keyword.toLowerCase();
        boolean descriptionMatches = expense.getDescription().toLowerCase().contains(lowerKeyword);
        boolean categoryMatches = expense.getCategory().toLowerCase().contains(lowerKeyword);
        return descriptionMatches || categoryMatches;
    }

    private boolean matchesDateRange(Expense expense) {
        LocalDate date = expense.getDate();
        if (dateMin != null && date.isBefore(dateMin)) {
            return false;
        }
        if (dateMax != null && date.isAfter(dateMax)) {
            return false;
        }
        return true;
    }

    private boolean matchesAmountRange(Expense expense) {
        double amount = expense.getAmount();
        if (amountMin != null && amount < amountMin) {
            return false;
        }
        if (amountMax != null && amount > amountMax) {
            return false;
        }
        return true;
    }

    private void sortMatches(ArrayList<Expense> matches) {
        Comparator<Expense> comparator = Comparator.comparingDouble(Expense::getAmount);
        if ("desc".equals(sortOrder)) {
            comparator = comparator.reversed();
        }
        matches.sort(comparator);
    }

    /**
     * Returns false because finding expenses does not modify the expense list.
     *
     * @return false
     */
    @Override
    public boolean shouldPersist() {
        return false;
    }
}
