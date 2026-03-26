package seedu.duke;

import java.util.ArrayList;

/**
 * Handles the logic for finding expenses that match a keyword.
 * Searches case-insensitively across the description and category of each expense.
 */
public class FindCommand extends Command {
    private final String keyword;

    /**
     * Constructs a FindCommand with the given UI and search keyword.
     *
     * @param ui      The Ui object used to display results.
     * @param keyword The keyword to search for.
     */
    public FindCommand(Ui ui, String keyword) {
        super(ui);
        this.keyword = keyword;
    }

    /**
     * Executes the find command by scanning all expenses for a case-insensitive
     * keyword match in the description or category, then displaying the results.
     *
     * @param expenseList The list of expenses to search through.
     */
    @Override
    public void execute(ExpenseList expenseList) {
        String lowerKeyword = keyword.toLowerCase();
        ArrayList<Expense> matches = new ArrayList<>();

        for (int i = 0; i < expenseList.getSize(); i++) {
            Expense expense = expenseList.getExpense(i);
            boolean descriptionMatches = expense.getDescription().toLowerCase().contains(lowerKeyword);
            boolean categoryMatches = expense.getCategory().toLowerCase().contains(lowerKeyword);
            if (descriptionMatches || categoryMatches) {
                matches.add(expense);
            }
        }

        ui.showFindResults(matches, keyword);
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
