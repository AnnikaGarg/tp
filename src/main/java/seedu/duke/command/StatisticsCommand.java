package seedu.duke.command;

import seedu.duke.model.ExpenseList;
import seedu.duke.ui.Ui;


/**
 * Handles the logic for displaying a per-category spending summary.
 * Iterates through all expenses and aggregates totals by category using a map.
 *
 * <p>Usage: {@code stats}</p>
 */
public class StatisticsCommand extends Command {

    private final Integer year;

    /**
     * Constructs a StatisticsCommand without a specific year.
     * Starts by showing stats for the current year.
     *
     * @param ui The Ui object used to display user-facing messages.
     */
    public StatisticsCommand(Ui ui) {
        super(ui);
        this.year = null;
    }

    /**
     * Constructs a StatisticsCommand for a specific year.
     *
     * @param ui   The Ui object used to display user-facing messages.
     * @param year The year to display statistics for.
     */
    public StatisticsCommand(Ui ui, int year) {
        super(ui);
        this.year = year;
    }

    /**
     * Executes the statistics command by generating the yearly dashboard
     * using the Ui.
     *
     * @param expenseList The list of expenses to analyse.
     */
    @Override
    public void execute(ExpenseList expenseList) {
        assert expenseList != null : "ExpenseList must not be null";

        int targetYear = this.year != null ? this.year : java.time.LocalDate.now().getYear();
        
        ui.showYearlyDashboard(expenseList, targetYear);
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

