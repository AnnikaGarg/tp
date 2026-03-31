package seedu.duke;

import java.util.Comparator;

/**
 * Handles the logic for sorting the expense list.
 * Supports sorting alphabetically by category or chronologically by date.
 *
 * <p>Usage: {@code sort category} or {@code sort date}</p>
 */
public class SortCommand extends Command {

    /** Comparator that sorts expenses alphabetically by category (case-insensitive) and then by date. */
    public static final Comparator<Expense> BY_CATEGORY =
            Comparator.comparing(Expense::getCategory, String.CASE_INSENSITIVE_ORDER)
                      .thenComparing(Expense::getDate);

    /** Comparator that sorts expenses chronologically by date (earliest first). */
    public static final Comparator<Expense> BY_DATE =
            Comparator.comparing(Expense::getDate);

    private final String sortBy;

    /**
     * Constructs a SortCommand for the given sort criterion.
     *
     * @param ui     The Ui object used to display user-facing messages.
     * @param sortBy The sort criterion: {@code "category"} or {@code "date"}.
     */
    public SortCommand(Ui ui, String sortBy) {
        super(ui);
        assert sortBy != null : "sortBy must not be null";
        assert sortBy.equals("category") || sortBy.equals("date")
                : "sortBy must be 'category' or 'date'";
        this.sortBy = sortBy;
    }

    /**
     * Executes the sort command by sorting the expense list in place,
     * then displaying the sorted list to the user.
     *
     * @param expenseList The list of expenses to sort.
     */
    @Override
    public void execute(ExpenseList expenseList) {
        assert expenseList != null : "ExpenseList must not be null";

        Comparator<Expense> comparator = sortBy.equals("category") ? BY_CATEGORY : BY_DATE;
        expenseList.sortExpenses(comparator);

        ui.showSorted(expenseList, sortBy);
    }

    /**
     * Returns true because sorting changes the order of persisted data.
     *
     * @return true to indicate the sorted order should be saved.
     */
    @Override
    public boolean shouldPersist() {
        return true;
    }
}

