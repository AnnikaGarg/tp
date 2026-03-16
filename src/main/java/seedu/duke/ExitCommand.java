package seedu.duke;

/**
 * Handles the logic for exiting the application.
 */
public class ExitCommand extends Command {

    @Override
    public void execute(ExpenseList expenseList) {
        // Sets the flag so the main loop knows to stop
        this.isExit = true;
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }
}
