package seedu.duke;

/**
 * Represents an executable command.
 * All specific commands must inherit from this class and implement the execute method.
 */
public abstract class Command {
    protected boolean isExit = false;

    /**
     * Executes the command.
     *
     * @param expenseList The list of expenses to operate on.
     */
    public abstract void execute(ExpenseList expenseList);

    /**
     * Checks if this command is intended to exit the application.
     *
     * @return true if it is an exit command, false otherwise.
     */
    public boolean isExit() {
        return isExit;
    }
}
