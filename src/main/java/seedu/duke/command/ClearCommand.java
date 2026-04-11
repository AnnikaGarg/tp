package seedu.duke.command;

import seedu.duke.model.ExpenseList;
import seedu.duke.ui.Ui;

/**
 * Handles the logic for clearing the entire expense list.
 *
 * <p>Usage: {@code clear}</p>
 */
public class ClearCommand extends Command {

    private boolean hasCleared = false;

    /**
     * Constructs a ClearCommand with the specified Ui.
     *
     * @param ui The Ui object used to display user-facing messages.
     */
    public ClearCommand(Ui ui) {
        super(ui);
        assert ui != null : "Ui must not be null";
    }

    /**
     * Executes the clear command by asking for confirmation,
     * and removing all expenses from the list if confirmed.
     *
     * @param expenseList The list of expenses to operate on.
     */
    @Override
    public void execute(ExpenseList expenseList) {
        assert expenseList != null : "ExpenseList must not be null";

        ui.showClearConfirmationPrompt();
        String confirmation = ui.getUserInput();

        if (confirmation != null && confirmation.trim().equalsIgnoreCase("confirm")) {
            int originalSize = expenseList.getSize();
            expenseList.clearExpenses();
            hasCleared = true;
            ui.showClear(originalSize);
        } else {
            ui.showClearCancelled();
            hasCleared = false;
        }
    }

    /**
     * Returns true only if the user confirmed and the list was cleared.
     *
     * @return true if data was cleared, false if cancelled.
     */
    @Override
    public boolean shouldPersist() {
        return hasCleared;
    }
}

