package seedu.duke;

import seedu.duke.ui.Ui;

import java.time.YearMonth;

/**
 * Handles the logic for listing tracked expenses.
 * If a month is provided, only expenses from that month are shown.
 */
public class ListCommand extends Command {
    private final YearMonth month;

    /**
     * Constructs a ListCommand with the specified Ui instance and optional month filter.
     *
     * @param ui The Ui object used to display messages.
     * @param month The month to filter by, or null to list all expenses.
     */
    public ListCommand(Ui ui, YearMonth month) {
        super(ui);
        this.month = month;
    }

    @Override
    public void execute(ExpenseList expenseList) {
        assert expenseList != null : "ExpenseList should not be null in ListCommand";

        if (month == null) {
            ui.showExpenseList(expenseList);
            return;
        }

        ui.showExpenseList(expenseList.getMonthlyExpenses(month), month);
    }
}