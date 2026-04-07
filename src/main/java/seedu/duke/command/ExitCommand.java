package seedu.duke.command;

import seedu.duke.model.ExpenseList;
import seedu.duke.ui.Ui;

/**
 * Handles the logic for exiting the application.
 */
public class ExitCommand extends Command {
    /**
     * Constructs an ExitCommand with the specified Ui instance.
     *
     * @param ui The Ui object used to display messages.
     */
    public ExitCommand(Ui ui) {
        super(ui);
    }

    @Override
    public void execute(ExpenseList expenseList) {
        // Sets the flag so the main loop knows to stop
        this.isExit = true;
        ui.showExit();
    }

    /**
     * Returns true so data is persisted before application shutdown.
     *
     * @return true to indicate this command should be saved.
     */
    @Override
    public boolean shouldPersist() {
        return true;
    }
}
