package seedu.duke;

/**
 * Represents an executable command.
 * All specific commands must inherit from this class and implement the execute method.
 */
public abstract class Command {
    protected boolean isExit = false;
    protected Ui ui;

    /**
     * Constructs a Command with the specified Ui instance.
     *
     * @param ui The Ui object used to display user-facing messages.
     */
    public Command(Ui ui) {
        this.ui = ui;
    }

    /**
     * Executes the command.
     *
     * @param expenseList The list of expenses to operate on.
     */
    public abstract void execute(ExpenseList expenseList);

    /**
     * Returns whether this command should trigger data persistence after execution.
     *
     * @return true if this command mutates data that should be saved.
     */
    public boolean shouldPersist() {
        return false;
    }

    /**
     * Checks if this command is intended to exit the application.
     *
     * @return true if it is an exit command, false otherwise.
     */
    public boolean isExit() {
        return isExit;
    }
}
