package seedu.duke;

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
}
