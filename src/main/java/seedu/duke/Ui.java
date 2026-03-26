package seedu.duke;

/**
 * Handles all user interaction for the SpendSwift application.
 * Responsible for displaying messages, prompts, and feedback to the user.
 */
public class Ui {
    private static final String LINE = "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~";

    /**
     * Displays the welcome message when the application starts.
     */
    public void showWelcome() {
        System.out.println(LINE);
        System.out.println("Hello! I'm SpendSwift.");
        System.out.println("What expenses are we tracking today?");
        System.out.println(LINE);
    }

    /**
     * Displays confirmation after adding a new expense.
     *
     * @param expense The expense that was added.
     * @param size    The current total number of expenses.
     */
    public void showAddExpense(Expense expense, int size) {
        System.out.println(LINE);
        System.out.println("Got it. I've added this expense:");
        System.out.println("  " + expense);
        System.out.println("Now you have " + size + " expense(s) in the list.");
        System.out.println(LINE);
    }

    /**
     * Displays confirmation after deleting an expense.
     *
     * @param expense The expense that was removed.
     * @param size    The current total number of expenses.
     */
    public void showDeleteExpense(Expense expense, int size) {
        System.out.println(LINE);
        System.out.println("Noted. I've removed this expense:");
        System.out.println("  " + expense);
        System.out.println("Now you have " + size + " expense(s) in the list.");
        System.out.println(LINE);
    }

    /**
     * Displays a before and after summary after editing an expense.
     *
     * @param before The original expense before the edit.
     * @param after  The updated expense after the edit.
     * @param index  The 1-based position that was edited.
     */
    public void showEditExpense(Expense before, Expense after, int index) {
        System.out.println(LINE);
        System.out.println("Got it. I've updated expense #" + index + ":");
        System.out.println("  Before: " + before);
        System.out.println("  After:  " + after);
        System.out.println(LINE);
    }

    /**
     * Displays the help menu with all available commands.
     */
    public void showHelp() {
        System.out.println(LINE);
        System.out.println("Here are the available commands:");
        System.out.println("  add AMOUNT [/c CATEGORY] [/d DATE] DESC                  - Add a new expense");
        System.out.println("  list                                                     - List all expenses");
        System.out.println("  total                                                    - Show total amount spent");
        System.out.println("  budget AMOUNT                                            - Set a spending budget");
        System.out.println("  delete INDEX                                             - Delete an expense by index");
        System.out.println("  edit INDEX [/amount A] [/desc D] [/category C] [/date D] - Edit an existing expense");
        System.out.println("  help                                                     - Show this help menu");
        System.out.println("  exit                                                     - Exit the application");
        System.out.println("Note: DATE must be in YYYY-MM-DD format (e.g. 2026-03-24).");
        System.out.println(LINE);
    }

    /**
     * Displays all expenses currently stored in the expense list.
     *
     * @param expenseList The list of expenses to display.
     */
    public void showExpenseList(ExpenseList expenseList) {
        System.out.println(LINE);
        if (expenseList.getSize() == 0) {
            System.out.println("Your expense list is currently empty.");
            System.out.println(LINE);
            return;
        }
        System.out.println("Here are your tracked expenses:");
        for (int i = 0; i < expenseList.getSize(); i++) {
            System.out.println((i + 1) + ". " + expenseList.getExpense(i));
        }
        System.out.println(LINE);
    }

    /**
     * Displays the exit message when the application terminates.
     */
    public void showExit() {
        System.out.println(LINE);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }

    /**
     * Displays a message when the user enters an unknown command.
     */
    public void showUnknownCommand() {
        System.out.println(LINE);
        System.out.println("Unknown command. Type 'help' to see available commands.");
        System.out.println(LINE);
    }

    /**
     * Displays usage instructions for the add command.
     */
    public void showAddUsage() {
        System.out.println(LINE);
        System.out.println("Usage: add <amount> [/c <category>] [/d <YYYY-MM-DD>] <description>");
        System.out.println("Example: add 5.50 /c Food /d 2026-03-24 Chicken Rice");
        System.out.println(LINE);
    }

    /**
     * Displays usage instructions for the delete command.
     */
    public void showDeleteUsage() {
        System.out.println(LINE);
        System.out.println("Usage: delete <index>");
        System.out.println(LINE);
    }

    /**
     * Displays usage instructions for the edit command.
     */
    public void showEditUsage() {
        System.out.println(LINE);
        System.out.println("Usage: edit <index> [/amount <amount>] [/desc <description>]"
                + " [/category <category>] [/date <YYYY-MM-DD>]");
        System.out.println("At least one flag must be provided.");
        System.out.println(LINE);
    }

    /**
     * Displays an error when the amount value is invalid.
     */
    public void showInvalidAmount() {
        System.out.println(LINE);
        System.out.println("Amount must be a valid non-negative number.");
        System.out.println(LINE);
    }

    /**
     * Displays an error when the date does not match YYYY-MM-DD or is not a real calendar date.
     */
    public void showInvalidDateFormat() {
        System.out.println(LINE);
        System.out.println("Date must be in YYYY-MM-DD format and be a valid calendar date.");
        System.out.println("Example: /d 2026-03-24");
        System.out.println(LINE);
    }

    /**
     * Displays an error when the index provided is not a valid integer.
     */
    public void showInvalidIndexFormat() {
        System.out.println(LINE);
        System.out.println("Index must be a valid integer.");
        System.out.println(LINE);
    }

    /**
     * Displays an error when the index is out of bounds.
     */
    public void showInvalidIndex() {
        System.out.println(LINE);
        System.out.println("Invalid index! Use 'list' to see valid numbers.");
        System.out.println(LINE);
    }

    /**
     * Displays a warning when the data file cannot be loaded.
     */
    public void showLoadWarning() {
        System.out.println(LINE);
        System.out.println("Warning: Could not load data file. Starting with empty list.");
        System.out.println(LINE);
    }

    /**
     * Displays a warning when the data file cannot be saved.
     */
    public void showSaveWarning() {
        System.out.println(LINE);
        System.out.println("Warning: Could not save data to file.");
        System.out.println(LINE);
    }

    /**
     * Displays a warning when a malformed line is encountered during file parsing.
     *
     * @param line The malformed line that was skipped.
     */
    public void showMalformedLineWarning(String line) {
        System.out.println(LINE);
        System.out.println("Warning: Skipping malformed line: " + line);
        System.out.println(LINE);
    }

    /**
     * Displays a warning when a line contains an invalid amount during file parsing.
     *
     * @param line The line with the invalid amount.
     */
    public void showInvalidAmountLineWarning(String line) {
        System.out.println(LINE);
        System.out.println("Warning: Skipping line with invalid amount: " + line);
        System.out.println(LINE);
    }

    /**
     * Displays the total sum of all expenses.
     *
     * @param total The calculated total amount.
     * @param count The number of expenses tracked.
     */
    public void showTotal(double total, int count) {
        System.out.println(LINE);
        System.out.println("You have " + count + " expense(s) in your list.");
        System.out.println("Your total spending is: $" + String.format("%.2f", total));
        System.out.println(LINE);
    }

    /**
     * Displays a confirmation message when a budget is successfully set.
     *
     * @param budget The budget amount that was set.
     */
    public void showBudgetSet(double budget) {
        System.out.println(LINE);
        System.out.println("Budget set to $" + String.format("%.2f", budget));
        System.out.println(LINE);
    }

    /**
     * Displays a warning message when the total spending exceeds the budget.
     *
     * @param budget The budget limit.
     * @param total The current total spending.
     */
    public void showBudgetExceededWarning(double budget, double total) {
        System.out.println(LINE);
        System.out.println("Warning: You have exceeded your budget of $"
                + String.format("%.2f", budget) + "!");
        System.out.println("Current total: $" + String.format("%.2f", total));
        System.out.println(LINE);
    }

    /**
     * Displays the correct usage format for the budget command.
     */
    public void showBudgetUsage() {
        System.out.println(LINE);
        System.out.println("Usage: budget AMOUNT");
        System.out.println(LINE);
    }

    /**
     * Displays an error message when the budget input is invalid.
     */
    public void showInvalidBudget() {
        System.out.println(LINE);
        System.out.println("Budget must be a valid non-negative number.");
        System.out.println(LINE);
    }
}
