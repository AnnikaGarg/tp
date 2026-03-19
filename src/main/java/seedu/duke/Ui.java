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
     * @param size The current total number of expenses.
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
     * @param size The current total number of expenses.
     */
    public void showDeleteExpense(Expense expense, int size) {
        System.out.println(LINE);
        System.out.println("Noted. I've removed this expense:");
        System.out.println("  " + expense);
        System.out.println("Now you have " + size + " expense(s) in the list.");
        System.out.println(LINE);
    }

    /**
     * Displays the help menu with all available commands.
     */
    public void showHelp() {
        System.out.println(LINE);
        System.out.println("Here are the available commands:");
        System.out.println("  add AMOUNT DESCRIPTION  - Add a new expense");
        System.out.println("  list                    - List all expenses");
        System.out.println("  delete INDEX            - Delete an expense by index");
        System.out.println("  help                    - Show this help menu");
        System.out.println("  exit                    - Exit the application");
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
        System.out.println("Usage: add <amount> <description>");
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
     * Displays an error message when the index format is invalid.
     */
    public void showInvalidAmount() {
        System.out.println(LINE);
        System.out.println("Amount must be a valid number.");
        System.out.println(LINE);
    }

    /**
     * Displays an error message when the index format is invalid.
     */
    public void showInvalidIndexFormat() {
        System.out.println(LINE);
        System.out.println("Index must be a valid integer.");
        System.out.println(LINE);
    }

    /**
     * Displays an error message when the index is out of bounds.
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
}
