package seedu.duke;
import java.util.ArrayList;
import java.util.Map;

/**
 * Handles all user interaction for the SpendSwift application.
 * Responsible for displaying messages, prompts, and feedback to the user.
 */
public class Ui {
    private static final String LINE = "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~";
    private final java.util.Scanner in = new java.util.Scanner(System.in);

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
        System.out.println("  add AMOUNT [/c CATEGORY] [/da DATE] DESC  - Add a new expense");
        System.out.println("  list                                      - List all expenses");
        System.out.println("  total                                     - Show total amount spent");
        System.out.println("  budget [AMOUNT]                           - Set or view spending budget");
        System.out.println("                                              (no amount = view current budget)");
        System.out.println("  delete INDEX                              - Delete an expense by index");
        System.out.println("  edit INDEX [/a AMOUNT] [/de DESC]         - Edit an existing expense");
        System.out.println("             [/c CATEGORY] [/da DATE]");
        System.out.println("  find KEYWORD [/c CATEGORY]                - Find expenses by keyword/category");
        System.out.println("  sort category|date                        - Sort expenses");
        System.out.println("  stats                                     - Spending breakdown by category");
        System.out.println("  lend AMOUNT BORROWER [/da DATE]           - Record money lent to someone");
        System.out.println("  loans                                     - Show outstanding loans");
        System.out.println("  loans /all                                - Show all loans (incl. repaid)");
        System.out.println("  repay INDEX                               - Mark a loan as repaid");
        System.out.println("  help                                      - Show this help menu");
        System.out.println("  exit                                      - Exit the application");
        System.out.println("Note: DATE must be in YYYY-MM-DD format (e.g. 2026-03-24).");
        System.out.println("Available categories: Food, Transport, Shopping, Entertainment, Health, Others");
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
        System.out.println("Usage: add <amount> [/c <category>] [/da <YYYY-MM-DD>] <description>");
        System.out.println("Example: add 5.50 /c Food /da 2026-03-24 Chicken Rice");
        System.out.println("Available categories: Food, Transport, Shopping, Entertainment, Health, Others");
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
        System.out.println("Usage: edit <index> [/a <amount>] [/de <description>]"
                + " [/c <category>] [/da <YYYY-MM-DD>]");
        System.out.println("Available categories: Food, Transport, Shopping, Entertainment, Health, Others");
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
     * Displays an error when the user attempts to add a $0.00 expense.
     */
    public void showZeroAmountWarning() {
        System.out.println(LINE);
        System.out.println("Oops! Expense amounts must be greater than $0.00.");
        System.out.println("If you didn't spend any money, there is no need to track it!");
        System.out.println(LINE);
    }

    /**
     * Displays an error when the date does not match YYYY-MM-DD or is not a real calendar date.
     */
    public void showInvalidDateFormat() {
        System.out.println(LINE);
        System.out.println("Date must be in YYYY-MM-DD format and be a valid calendar date.");
        System.out.println("Example: /da 2026-03-24");
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
     * @param total  The current total spending.
     */
    public void showBudgetExceededWarning(double budget, double total) {
        System.out.println(LINE);
        System.out.println("Warning: You have exceeded your budget of $"
                + String.format("%.2f", budget) + "!");
        System.out.println("Current total: $" + String.format("%.2f", total));
        System.out.println(LINE);
    }

    /**
     * Displays a message indicating that the budget has not been set.
     */
    public void showBudgetNotSet() {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("Budget not set yet! Use: budget <amount>");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    /**
     * Displays the current budget details, including total spent and remaining budget.
     * If the budget is exceeded, the exceeded amount is shown instead.
     *
     * @param budget The currently set budget.
     * @param totalSpent The total amount spent.
     * @param remaining The remaining budget. Can be negative if exceeded.
     */
    public void showBudgetDetails(double budget, double totalSpent, double remaining) {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("Current budget: $" + budget);
        System.out.println("Total spent: $" + totalSpent);

        if (remaining >= 0) {
            System.out.println("Remaining budget: $" + remaining);
        } else {
            System.out.println("Budget exceeded by $" + (-remaining));
        }

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
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
        System.out.println("Budget must be a number greater then 0.");
        System.out.println(LINE);
    }

    /**
     * Displays the correct usage format for the find command.
     */
    public void showFindUsage() {
        System.out.println(LINE);
        System.out.println("Usage: find KEYWORD [/c CATEGORY]");
        System.out.println("  find lunch           - search by keyword");
        System.out.println("  find /c Food         - list all in category");
        System.out.println("  find lunch /c Food   - keyword within category");
        System.out.println(LINE);
    }

    /**
     * Displays the list of expenses that match the search keyword and/or category filter.
     *
     * @param results        The list of matching expenses.
     * @param keyword        The keyword that was searched (may be empty).
     * @param categoryFilter The category filter applied, or null if none.
     */
    public void showFindResults(java.util.ArrayList<Expense> results,
                                String keyword, String categoryFilter) {
        System.out.println(LINE);
        String searchDescription = buildSearchDescription(keyword, categoryFilter);
        if (results.isEmpty()) {
            System.out.println("No expenses found matching: " + searchDescription);
            System.out.println(LINE);
            return;
        }
        System.out.println("Here are the matching expenses for " + searchDescription + ":");
        for (int i = 0; i < results.size(); i++) {
            System.out.println((i + 1) + ". " + results.get(i));
        }
        System.out.println(LINE);
    }

    /**
     * Builds a human-readable description of the search criteria.
     *
     * @param keyword        The keyword searched for (may be empty).
     * @param categoryFilter The category filter applied, or null if none.
     * @return A formatted string describing the search.
     */
    private String buildSearchDescription(String keyword, String categoryFilter) {
        if (!keyword.isEmpty() && categoryFilter != null) {
            return "\"" + keyword + "\" in category [" + categoryFilter + "]";
        }
        if (categoryFilter != null) {
            return "category [" + categoryFilter + "]";
        }
        return "\"" + keyword + "\"";
    }
    /**
     * Displays the expense list after it has been sorted.
     *
     * @param expenseList The sorted list of expenses.
     * @param sortBy      The criterion used for sorting ("category" or "date").
     */
    public void showSorted(ExpenseList expenseList, String sortBy) {
        System.out.println(LINE);
        System.out.println("Expenses sorted by " + sortBy + ":");
        if (expenseList.getSize() == 0) {
            System.out.println("  (no expenses to display)");
        } else {
            for (int i = 0; i < expenseList.getSize(); i++) {
                System.out.println("  " + (i + 1) + ". " + expenseList.getExpense(i));
            }
        }
        System.out.println(LINE);
    }

    /**
     * Displays the correct usage format for the sort command.
     */
    public void showSortUsage() {
        System.out.println(LINE);
        System.out.println("Usage: sort category   (alphabetical by category)");
        System.out.println("       sort date       (chronological by date)");
        System.out.println(LINE);
    }
    /**
     * Displays a per-category breakdown of total spending.
     *
     * @param totals A map of category name to total amount spent in that category.
     * @param count  The total number of expenses analysed.
     */
    public void showStatistics(Map<String, Double> totals, int count) {
        System.out.println(LINE);
        System.out.println("Spending statistics (" + count + " expense(s)):");
        if (totals.isEmpty()) {
            System.out.println("  No expenses to summarise.");
        } else {
            for (Map.Entry<String, Double> entry : totals.entrySet()) {
                System.out.println("  " + entry.getKey()
                        + ": $" + String.format("%.2f", entry.getValue()));
            }
        }
        System.out.println(LINE);
    }



    /**
     * Reads and returns the next line of user input, trimmed of leading and trailing whitespace.
     */
    public String getUserInput() {
        return in.nextLine().trim();
    }

    /**
     * Displays a numbered list of available categories and prompts the user to choose one or type a new one.
     *
     * @param categories The current list of available categories.
     */
    public void showCategoryPrompt(java.util.ArrayList<String> categories) {
        System.out.println(LINE);
        System.out.println("You didn't specify a category! Choose one from the list:");
        for (int i = 0; i < categories.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + categories.get(i));
        }
        System.out.print("Enter a number, or type a new category name: ");
    }

    /**
     * Displays confirmation after a loan is recorded.
     *
     * @param loan      The loan that was recorded.
     * @param loanCount The total number of loans recorded so far.
     */
    public void showLendAdded(Loan loan, int loanCount) {
        System.out.println(LINE);
        System.out.println("Got it. I've recorded this loan:");
        System.out.println("  " + loan);
        System.out.println("You now have " + loanCount + " loan(s) on record.");
        System.out.println("This does not affect your expense total or budget.");
        System.out.println(LINE);
    }

    /**
     * Displays the loan list, showing either only outstanding loans or all loans depending on showAll.
     *
     * @param expenseList The ExpenseList whose loans will be displayed.
     * @param showAll     If true, repaid loans are included in the output.
     */
    public void showLoans(ExpenseList expenseList, boolean showAll) {
        System.out.println(LINE);

        ArrayList<Loan> toShow = showAll
                ? expenseList.getAllLoans()
                : expenseList.getOutstandingLoans();

        if (toShow.isEmpty()) {
            if (showAll) {
                System.out.println("No loans on record.");
            } else {
                System.out.println("No outstanding loans.");
                if (expenseList.getLoanCount() > 0) {
                    System.out.println("(All loans have been repaid. Use 'loans /all' to see them.)");
                }
            }
            System.out.println(LINE);
            return;
        }

        if (showAll) {
            System.out.println("All loans (" + toShow.size() + "):");
        } else {
            System.out.println("Outstanding loans (" + toShow.size() + "):");
        }

        for (int i = 0; i < toShow.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + toShow.get(i));
        }
        System.out.println(LINE);
    }

    /**
     * Displays confirmation after a loan is marked as repaid.
     *
     * @param loan The loan that was marked repaid.
     */
    public void showRepaid(Loan loan) {
        System.out.println(LINE);
        System.out.println("Great! Marked as repaid:");
        System.out.println("  " + loan);
        System.out.println(LINE);
    }

    /**
     * Displays an error when there are no outstanding loans to repay.
     */
    public void showNoOutstandingLoans() {
        System.out.println(LINE);
        System.out.println("There are no outstanding loans to repay.");
        System.out.println("Use 'loans /all' to see all past loans.");
        System.out.println(LINE);
    }

    /**
     * Displays an error when the repay index is out of range.
     *
     * @param maxIndex The number of currently outstanding loans.
     */
    public void showInvalidLoanIndex(int maxIndex) {
        System.out.println(LINE);
        System.out.println("Invalid loan index! There are " + maxIndex + " outstanding loan(s).");
        System.out.println("Use 'loans' to see the current list.");
        System.out.println(LINE);
    }

    /**
     * Displays usage instructions for the lend command.
     */
    public void showLendUsage() {
        System.out.println(LINE);
        System.out.println("Usage: lend <amount> <borrower name> [/da <YYYY-MM-DD>]");
        System.out.println("Example: lend 20.00 John");
        System.out.println("Example: lend 50.00 Jane /da 2026-04-01");
        System.out.println(LINE);
    }

    /**
     * Displays usage instructions for the loans command.
     */
    public void showLoansUsage() {
        System.out.println(LINE);
        System.out.println("Usage: loans          (show outstanding loans)");
        System.out.println("       loans /all     (show all loans including repaid)");
        System.out.println(LINE);
    }

    /**
     * Displays usage instructions for the repay command.
     */
    public void showRepayUsage() {
        System.out.println(LINE);
        System.out.println("Usage: repay <index>");
        System.out.println("Use 'loans' to see the index of each outstanding loan.");
        System.out.println(LINE);
    }
}
