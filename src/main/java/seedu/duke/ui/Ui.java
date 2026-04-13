package seedu.duke.ui;

import seedu.duke.model.Expense;
import seedu.duke.model.ExpenseList;
import seedu.duke.model.Loan;

import java.time.YearMonth;
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
        WelcomeBanner.printBanner();
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
     * Displays confirmation after clearing all expenses.
     *
     * @param originalSize The number of expenses that were cleared.
     */
    public void showClear(int originalSize) {
        System.out.println(LINE);
        System.out.println("Cleared " + originalSize + " expense(s) from the list.");
        System.out.println("Your expense list is now empty.");
        System.out.println(LINE);
    }

    /**
     * Displays a confirmation prompt before completely clearing the expense list.
     */
    public void showClearConfirmationPrompt() {
        System.out.println(LINE);
        System.out.println("Are you sure you want to completely clear all expenses?");
        System.out.println("This action cannot be undone.");
        System.out.println("Type 'confirm' to clear, or anything else to cancel:");
        System.out.print("> ");
    }

    /**
     * Displays a message indicating that the clear action was cancelled.
     */
    public void showClearCancelled() {
        System.out.println(LINE);
        System.out.println("Clear command cancelled. Your expenses are safe!");
        System.out.println(LINE);
    }

    /**
     * Displays confirmation after batch deleting expenses by category or date.
     *
     * @param currentSize  The total number of expenses remaining.
     * @param deletedCount The number of expenses deleted.
     * @param criteria     The search criteria used for deletion.
     */
    public void showBatchDelete(int currentSize, int deletedCount, String criteria) {
        System.out.println(LINE);
        System.out.println("Deleted " + deletedCount + " expense(s) matching: " + criteria);
        System.out.println("Now you have " + currentSize + " expense(s) in the list.");
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
        System.out.println("  list [YYYY-MM]                            - List all expenses or expenses for a month");
        System.out.println("  total                                     - Show total amount spent");
        System.out.println("  budget [AMOUNT]                           - View/set current month's budget");
        System.out.println("  budget [YYYY-MM] [AMOUNT]                 - View/set a specific month's budget");
        System.out.println("  delete INDEX                              - Delete an expense by index");
        System.out.println("  edit INDEX [/a AMOUNT] [/de DESC]         - Edit an existing expense");
        System.out.println("             [/c CATEGORY] [/da DATE]");
        System.out.println("  find KEYWORD [/c CAT] [/dmin DATE]        - Find/filter expenses");
        System.out.println("       [/dmax DATE] [/amin AMT] [/amax AMT] [/sort asc|desc]");
        System.out.println("  sort category|date                        - Sort expenses");
        System.out.println("  stats                                     - Show spending statistics and graph " +
                "by category");
        System.out.println("  lend AMOUNT BORROWER [/da DATE]           - Record money lent to someone");
        System.out.println("  loans                                     - Show outstanding loans");
        System.out.println("  loans /all                                - Show all loans (incl. repaid)");
        System.out.println("  repay INDEX [AMOUNT]                      - Repay a loan (fully or partially)");
        System.out.println("  help                                      - Show this help menu");
        System.out.println("  exit                                      - Exit the application");
        System.out.println("  forecast                                  - Show end-of-month spending forecast");
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
     * Displays all expenses recorded in the specified month.
     *
     * @param expenses The list of expenses to display.
     * @param month    The month being displayed.
     */
    public void showExpenseList(ArrayList<Expense> expenses, YearMonth month) {
        System.out.println(LINE);
        if (expenses.isEmpty()) {
            System.out.println("No expenses found for " + month + ".");
            System.out.println(LINE);
            return;
        }

        System.out.println("Here are your tracked expenses for " + month + ":");
        for (int i = 0; i < expenses.size(); i++) {
            System.out.println((i + 1) + ". " + expenses.get(i));
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
        System.out.println("       delete /c <category>");
        System.out.println("       delete /da <YYYY-MM-DD>");
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
     * Displays an error when the entered amount is not a valid non-negative number.
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
     * Displays an error when the user attempts to lend $0.00.
     */
    public void showZeroLoanAmountWarning() {
        System.out.println(LINE);
        System.out.println("Oops! Loan amounts must be greater than $0.00.");
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
     * Displays an error when the repay index is invalid.
     */
    public void showInvalidRepayIndex() {
        System.out.println(LINE);
        System.out.println("Invalid index! Use 'loans' to see valid numbers.");
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
     * Displays a confirmation message when a monthly budget is successfully set.
     *
     * @param month  The month for which the budget was set.
     * @param budget The budget amount that was set.
     */
    public void showBudgetSet(YearMonth month, double budget) {
        System.out.println(LINE);
        System.out.println("Budget for " + month + " set to $" + String.format("%.2f", budget));
        System.out.println(LINE);
    }

    /**
     * Displays a warning message when spending for a month exceeds that month's budget.
     *
     * @param month  The month whose budget has been exceeded.
     * @param budget The budget limit for the month.
     * @param total  The current total spending for the month.
     */
    public void showBudgetExceededWarning(YearMonth month, double budget, double total) {
        System.out.println(LINE);
        System.out.println("Warning: You have exceeded your budget for " + month + "!");
        System.out.println("Budget: $" + String.format("%.2f", budget));
        System.out.println("Current total: $" + String.format("%.2f", total));
        System.out.println(LINE);
    }

    /**
     * Displays a confirmation message when an existing monthly budget is overwritten.
     *
     * @param month     The month whose budget was updated.
     * @param oldBudget The previous budget amount.
     * @param newBudget The new budget amount.
     */
    public void showBudgetUpdated(YearMonth month, double oldBudget, double newBudget) {
        System.out.println(LINE);
        System.out.println("Budget for " + month + " updated from $"
                + String.format("%.2f", oldBudget) + " to $" + String.format("%.2f", newBudget));
        System.out.println(LINE);
    }

    /**
     * Displays a message indicating that no budget has been set for the given month.
     *
     * @param month The month with no budget set.
     */
    public void showBudgetNotSet(YearMonth month) {
        System.out.println(LINE);
        System.out.println("No budget set for " + month + ".");
        System.out.println("Use: budget " + month + " <amount>");
        System.out.println(LINE);
    }

    /**
     * Displays the budget details for a specific month, including total spent and remaining budget.
     * If the budget is exceeded, the exceeded amount is shown instead.
     *
     * @param month      The month whose budget details are being displayed.
     * @param budget     The budget set for that month.
     * @param totalSpent The total amount spent in that month.
     * @param remaining  The remaining budget. Can be negative if exceeded.
     */
    public void showBudgetDetails(YearMonth month, double budget, double totalSpent, double remaining) {
        System.out.println(LINE);
        System.out.println("Budget summary for " + month + ":");
        System.out.println("Budget: $" + String.format("%.2f", budget));
        System.out.println("Spent: $" + String.format("%.2f", totalSpent));

        if (remaining >= 0) {
            System.out.println("Remaining: $" + String.format("%.2f", remaining));
        } else {
            System.out.println("Exceeded by: $" + String.format("%.2f", -remaining));
        }

        System.out.println(LINE);
    }

    /**
     * Displays the correct usage format for the budget command.
     */
    public void showBudgetUsage() {
        System.out.println(LINE);
        System.out.println("Usage: budget");
        System.out.println("       budget <amount>");
        System.out.println("       budget <YYYY-MM>");
        System.out.println("       budget <YYYY-MM> <amount>");
        System.out.println(LINE);
    }

    /**
     * Displays an error when the budget amount is invalid or not greater than zero.
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
        System.out.println("Usage: find KEYWORD [/c CATEGORY] [/dmin DATE]"
                + " [/dmax DATE] [/amin AMT] [/amax AMT] [/sort asc|desc]");
        System.out.println("  find lunch                    - search by keyword");
        System.out.println("  find /c Food                  - list all in category");
        System.out.println("  find /dmin 2026-01-01          - from date onwards");
        System.out.println("  find /amin 5 /amax 20          - amount between 5 and 20");
        System.out.println("  find /c Food /sort desc        - Food expenses, highest first");
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
     * @param sortBy The criterion used for sorting, such as "category", "date", or "amount".
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
        System.out.println("       sort amount     (highest amount first)");
        System.out.println(LINE);
    }

    /**
     * Displays a per-category breakdown of spending as a text-based bar graph.
     * Categories are shown in descending order of total amount spent, with bar lengths
     * scaled relative to the highest category total.
     *
     * If there are no expenses to summarise, a message is shown instead.
     *
     * @param totals A map of category names to total amounts spent in each category.
     * @param count The total number of expenses analysed.
     */
    public void showStatistics(Map<String, Double> totals, int count) {
        System.out.println(LINE);
        System.out.println("Spending statistics (" + count + " expense(s)):");
        if (totals.isEmpty()) {
            System.out.println("No expenses to summarise.");
            System.out.println(LINE);
            return;
        }

        double maxAmount = 0.0;
        for (double amount : totals.values()) {
            if (amount > maxAmount) {
                maxAmount = amount;
            }
        }

        final int maxBarLength = 20;

        for (Map.Entry<String, Double> entry : totals.entrySet().stream()
                .sorted((entry1, entry2) -> Double.compare(entry2.getValue(), entry1.getValue()))
                .toList()) {

            String category = entry.getKey();
            double amount = entry.getValue();

            int barLength = 0;
            if (maxAmount > 0) {
                barLength = (int) Math.round((amount / maxAmount) * maxBarLength);
            }

            if (amount > 0 && barLength == 0) {
                barLength = 1;
            }

            String bar = "#".repeat(barLength);
            System.out.printf("  %-15s | %-20s $%.2f%n", category, bar, amount);
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
     * Displays confirmation after a loan is marked as fully repaid.
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
     * Displays confirmation after a partial repayment is recorded.
     *
     * @param loan   The loan that was partially repaid.
     * @param amount The amount that was repaid.
     */
    public void showPartialRepay(Loan loan, double amount) {
        System.out.println(LINE);
        System.out.println("Partial repayment of $" + String.format("%.2f", amount) + " recorded:");
        System.out.println("  " + loan);
        System.out.println(LINE);
    }

    /**
     * Displays an error when the repay amount exceeds the outstanding balance.
     *
     * @param outstanding The current outstanding balance of the loan.
     */
    public void showRepayExceedsOutstanding(double outstanding) {
        System.out.println(LINE);
        System.out.println("Repayment amount exceeds the outstanding balance of $"
                + String.format("%.2f", outstanding) + ".");
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
        System.out.println("Usage: repay <index>            (full repayment)");
        System.out.println("       repay <index> <amount>   (partial repayment)");
        System.out.println("Use 'loans' to see the index of each outstanding loan.");
        System.out.println(LINE);
    }

    /**
     * Displays the spending forecast for the current month, including the projected
     * total and budget status if a monthly budget exists.
     *
     * @param month The month for which the forecast is shown.
     * @param spentSoFar The total amount spent so far in the month.
     * @param currentDay The current day of the month used for projection.
     * @param dailyBurnRate The average amount spent per day so far.
     * @param projectedTotal The projected total spending by the end of the month.
     * @param hasBudget Whether a budget exists for the month.
     * @param budget The monthly budget amount, if one exists.
     */
    public void showForecast(java.time.YearMonth month, double spentSoFar, int currentDay,
                             double dailyBurnRate, double projectedTotal,
                             boolean hasBudget, double budget) {
        System.out.println("Here is your spending forecast for " + month + ":");
        System.out.printf("  Spent so far: $%.2f (over %d days)\n", spentSoFar, currentDay);
        System.out.printf("  Average daily burn rate: $%.2f/day\n", dailyBurnRate);
        System.out.printf("  Projected end-of-month total: $%.2f\n", projectedTotal);

        System.out.println("-".repeat(50));

        if (hasBudget) {
            System.out.printf("  Current Budget: $%.2f\n", budget);
            if (projectedTotal > budget) {
                double overage = projectedTotal - budget;
                System.out.printf("  WARNING: At this rate, you will EXCEED your budget by $%.2f!\n", overage);
            } else {
                double remaining = budget - projectedTotal;
                System.out.printf("  Great job! You are on track to stay under budget by $%.2f.\n", remaining);
            }
        } else {
            System.out.println("  (No budget set for this month. Use 'budget AMOUNT' to track your goals!)");
        }
    }

    /**
     * Displays a comprehensive yearly dashboard for a given year.
     * Includes a month-by-month budget breakdown, a category spending summary,
     * and annual totals.
     *
     * @param expenseList The list of expenses.
     * @param year        The year to display.
     */
    public void showYearlyDashboard(ExpenseList expenseList, int year) {
        System.out.println(LINE);
        System.out.println("=== YEARLY BUDGET SUMMARY FOR " + year + " ===");
        System.out.println();
        System.out.println("Month      | Budget      | Spent       | Remaining   | Used  | Trend");
        System.out.println("-----------+-------------+-------------+-------------+-------+-------------------------");

        double annualBudget = 0;
        double annualSpent = 0;

        for (int m = 1; m <= 12; m++) {
            YearMonth ym = YearMonth.of(year, m);
            double monthlyBudget = expenseList.getBudget(ym);
            if (monthlyBudget < 0) {
                monthlyBudget = 0.0;
            }

            double spent = expenseList.getTotalAmountForMonth(ym);
            double remaining = monthlyBudget - spent;

            annualBudget += monthlyBudget;
            annualSpent += spent;

            long usedPercent = 0;
            if (monthlyBudget > 0) {
                usedPercent = Math.round((spent / monthlyBudget) * 100);
            } else if (spent > 0) {
                usedPercent = 100;
            }

            int barLength = (int) (usedPercent / 5);
            if (barLength > 20) {
                barLength = 20;
            } else if (barLength <= 0 && usedPercent > 0) {
                barLength = 1;
            }
            String bar = "▒".repeat(barLength);

            String monthName = java.time.Month.of(m).name();
            monthName = monthName.substring(0, 1).toUpperCase() + monthName.substring(1).toLowerCase();
            String usedStr = usedPercent + "%";

            System.out.printf("%-10s | $ %9.2f | $ %9.2f | $ %9.2f | %5s | %-20s %s%n",
                    monthName, monthlyBudget, spent, remaining, usedStr, bar, usedStr);
        }

        System.out.println();
        System.out.println("=== PER CATEGORY BREAKDOWN ===");
        System.out.println();

        java.util.Map<String, Double> categoryTotals = new java.util.LinkedHashMap<>();
        for (int i = 0; i < expenseList.getSize(); i++) {
            Expense e = expenseList.getExpense(i);
            if (e.getDate().getYear() == year) {
                categoryTotals.put(e.getCategory(), categoryTotals.getOrDefault(e.getCategory(), 0.0) + e.getAmount());
            }
        }

        if (categoryTotals.isEmpty()) {
            System.out.println("No expenses for this year.");
        } else {
            double maxCat = categoryTotals.values().stream().max(Double::compare).orElse(0.0);
            System.out.println("Category             | Spent        | Trend");
            System.out.println("---------------------+--------------+-------------------------");
            for (java.util.Map.Entry<String, Double> entry : categoryTotals.entrySet().stream()
                    .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                    .toList()) {

                int bLen = 0;
                if (maxCat > 0) {
                    bLen = (int) Math.round((entry.getValue() / maxCat) * 20);
                }
                if (entry.getValue() > 0 && bLen == 0) {
                    bLen = 1;
                }
                String cBar = "▓".repeat(bLen);
                System.out.printf("%-20s | $ %10.2f | %-20s%n", entry.getKey(), entry.getValue(), cBar);
            }
        }

        System.out.println();
        System.out.println("=== ANNUAL TOTALS ===");
        System.out.println();
        System.out.printf("Total Budget:      $%.2f%n", annualBudget);
        System.out.printf("Total Spent:       $%.2f%n", annualSpent);
        System.out.printf("Net Balance:       $%.2f%n", annualBudget - annualSpent);
        System.out.printf("Average Monthly:   $%.2f%n", annualSpent / 12.0);
        System.out.println(LINE);
    }

    /**
     * Displays an error when the month input does not match the expected YYYY-MM format.
     */
    public void showInvalidMonthYear() {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("Invalid month/year format.");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    /**
     * Displays a warning when the user inputs an amount that is too large for the system to safely handle.
     */
    public void showAmountTooLargeWarning() {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("Whoa there, high roller! 🤑");
        System.out.println("Please enter an amount less than $1,000,000,000,000 (One Trillion).");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    /**
     * Displays a warning when the user adds trailing text to strict commands like exit.
     */
    public void showStrictCommandUsage(String command) {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("Invalid format! The '" + command + "' command does not take any extra arguments.");
        System.out.println("Please just type '" + command + "'.");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    /**
     * Displays a warning when the user inputs the restricted pipe character.
     */
    public void showInvalidCharacterWarning() {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("Invalid input! The pipe character '|' is reserved for system storage.");
        System.out.println("Please remove any '|' characters from your description or category.");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    /**
     * Displays a warning when the user provides the same flag more than once.
     */
    public void showDuplicateFlagWarning(String flag) {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("Invalid format! You can only use the '" + flag + "' flag once per command.");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }
}
