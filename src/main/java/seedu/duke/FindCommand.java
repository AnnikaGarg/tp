package seedu.duke;

import java.util.ArrayList;

/**
 * Handles the logic for finding expenses that match a keyword and/or category filter.
 * Searches case-insensitively across the description and category of each expense.
 * When a category filter is specified, only expenses in that category are shown.
 */
public class FindCommand extends Command {
    private final String keyword;
    private final String categoryFilter;

    /**
     * Constructs a FindCommand with the given UI, search keyword, and optional category filter.
     *
     * @param ui             The Ui object used to display results.
     * @param keyword        The keyword to search for. May be empty if categoryFilter is set.
     * @param categoryFilter The category to filter by, or null for no category filter.
     */
    public FindCommand(Ui ui, String keyword, String categoryFilter) {
        super(ui);
        this.keyword = keyword;
        this.categoryFilter = categoryFilter;
    }

    /**
     * Constructs a FindCommand with keyword search only (no category filter).
     *
     * @param ui      The Ui object used to display results.
     * @param keyword The keyword to search for.
     */
    public FindCommand(Ui ui, String keyword) {
        this(ui, keyword, null);
    }

    /**
     * Executes the find command by scanning all expenses for matches based on
     * keyword and/or category filter, then displaying the results.
     *
     * @param expenseList The list of expenses to search through.
     */
    @Override
    public void execute(ExpenseList expenseList) {
        ArrayList<Expense> matches = new ArrayList<>();

        for (int i = 0; i < expenseList.getSize(); i++) {
            Expense expense = expenseList.getExpense(i);

            if (categoryFilter != null) {
                boolean categoryMatches = expense.getCategory().toLowerCase()
                        .equals(categoryFilter.toLowerCase());
                if (!categoryMatches) {
                    continue;
                }
            }

            if (!keyword.isEmpty()) {
                String lowerKeyword = keyword.toLowerCase();
                boolean descriptionMatches = expense.getDescription().toLowerCase()
                        .contains(lowerKeyword);
                boolean categoryMatches = expense.getCategory().toLowerCase()
                        .contains(lowerKeyword);
                if (!descriptionMatches && !categoryMatches) {
                    continue;
                }
            }

            matches.add(expense);
        }

        ui.showFindResults(matches, keyword, categoryFilter);
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
