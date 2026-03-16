package seedu.duke;

/**
 * Handles the logic for displaying the help menu.
 */
public class HelpCommand extends Command {

    @Override
    public void execute(ExpenseList expenseList) {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("Here are the available commands:");
        System.out.println("  add AMOUNT DESCRIPTION  - Add a new expense");
        System.out.println("  list                    - List all expenses");
        System.out.println("  delete INDEX            - Delete an expense by index");
        System.out.println("  help                    - Show this help menu");
        System.out.println("  exit                    - Exit the application");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }
}
