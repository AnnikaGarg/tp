package seedu.duke;

import seedu.duke.ui.Ui;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Handles the logic for displaying a per-category spending summary.
 * Iterates through all expenses and aggregates totals by category using a map.
 *
 * <p>Usage: {@code stats}</p>
 */
public class StatisticsCommand extends Command {

    /**
     * Constructs a StatisticsCommand with the given Ui instance.
     *
     * @param ui The Ui object used to display user-facing messages.
     */
    public StatisticsCommand(Ui ui) {
        super(ui);
        assert ui != null : "Ui must not be null";
    }

    /**
     * Executes the statistics command by iterating over all expenses,
     * computing a per-category total using a LinkedHashMap, and displaying
     * the result via the Ui.
     *
     * <p>An empty list is handled gracefully — the Ui will display a suitable message.</p>
     *
     * @param expenseList The list of expenses to analyse.
     */
    @Override
    public void execute(ExpenseList expenseList) {
        assert expenseList != null : "ExpenseList must not be null";

        // LinkedHashMap preserves insertion order so categories appear in first-seen order
        Map<String, Double> totals = new LinkedHashMap<>();

        for (int i = 0; i < expenseList.getSize(); i++) {
            Expense expense = expenseList.getExpense(i);
            String category = expense.getCategory();
            double current = totals.getOrDefault(category, 0.0);
            totals.put(category, current + expense.getAmount());
        }

        ui.showStatistics(totals, expenseList.getSize());
    }

    /**
     * Returns false because statistics is a read-only view command.
     *
     * @return false — statistics does not modify data.
     */
    @Override
    public boolean shouldPersist() {
        return false;
    }
}

