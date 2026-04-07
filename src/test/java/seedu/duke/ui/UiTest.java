package seedu.duke.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import seedu.duke.model.Expense;
import seedu.duke.model.ExpenseList;
import seedu.duke.model.Loan;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class UiTest {
    private static final String LINE = "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~";

    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @AfterEach
    public void restoreSystemStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    private String captureOutput(Runnable action) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        action.run();
        return new String(outputStream.toByteArray(), StandardCharsets.UTF_8)
                .replace("\r\n", "\n");
    }

    private Ui createUiWithInput(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
        return new Ui();
    }

    private Expense makeExpense(String description, double amount) {
        return new Expense(description, amount);
    }

    private Expense makeExpense(String description, double amount, String category, LocalDate date) {
        return new Expense(description, amount, category, date);
    }

    private Loan makeLoan(String borrowerName, double amount, LocalDate date) {
        return new Loan(borrowerName, amount, date);
    }

    /**
     * Stub ExpenseList used to control Ui branches for testing.
     */
    private static class StubExpenseList extends ExpenseList {
        private final ArrayList<Expense> expenses;
        private final ArrayList<Loan> allLoans;
        private final ArrayList<Loan> outstandingLoans;

        StubExpenseList(ArrayList<Expense> expenses, ArrayList<Loan> allLoans,
                        ArrayList<Loan> outstandingLoans) {
            this.expenses = expenses;
            this.allLoans = allLoans;
            this.outstandingLoans = outstandingLoans;
        }

        @Override
        public int getSize() {
            return expenses.size();
        }

        @Override
        public Expense getExpense(int index) {
            return expenses.get(index);
        }

        @Override
        public ArrayList<Loan> getAllLoans() {
            return allLoans;
        }

        @Override
        public ArrayList<Loan> getOutstandingLoans() {
            return outstandingLoans;
        }

        @Override
        public int getLoanCount() {
            return allLoans.size();
        }
    }

    @Test
    public void showWelcome_printsWelcomeMessage() {
        Ui ui = new Ui();

        String output = captureOutput(ui::showWelcome);

        assertTrue(output.contains(LINE));
        assertTrue(output.contains("Hello! I'm SpendSwift."));
        assertTrue(output.contains("What expenses are we tracking today?"));
    }

    @Test
    public void expenseMessageMethods_printExpectedContent() {
        Ui ui = new Ui();
        Expense before = makeExpense("Coffee", 5.50);
        Expense after = makeExpense("Lunch", 12.30);

        String addOutput = captureOutput(() -> ui.showAddExpense(before, 1));
        assertTrue(addOutput.contains("Got it. I've added this expense:"));
        assertTrue(addOutput.contains(before.toString()));
        assertTrue(addOutput.contains("Now you have 1 expense(s) in the list."));

        String deleteOutput = captureOutput(() -> ui.showDeleteExpense(before, 0));
        assertTrue(deleteOutput.contains("Noted. I've removed this expense:"));
        assertTrue(deleteOutput.contains(before.toString()));
        assertTrue(deleteOutput.contains("Now you have 0 expense(s) in the list."));

        String editOutput = captureOutput(() -> ui.showEditExpense(before, after, 2));
        assertTrue(editOutput.contains("Got it. I've updated expense #2:"));
        assertTrue(editOutput.contains("Before: " + before));
        assertTrue(editOutput.contains("After:  " + after));
    }

    @Test
    public void showHelp_printsUpdatedHelpMenu() {
        Ui ui = new Ui();

        String output = captureOutput(ui::showHelp);

        assertTrue(output.contains("Here are the available commands:"));
        assertTrue(output.contains("list [YYYY-MM]"));
        assertTrue(output.contains("budget [AMOUNT]"));
        assertTrue(output.contains("budget [YYYY-MM] [AMOUNT]"));
        assertTrue(output.contains("find KEYWORD [/c CAT]"));
        assertTrue(output.contains("sort category|date"));
        assertTrue(output.contains("loans /all"));
        assertTrue(output.contains("repay INDEX"));
        assertTrue(output.contains("Available categories: Food, Transport, Shopping, Entertainment, "
                + "Health, Others"));
    }

    @Test
    public void showExpenseList_emptyList_printsEmptyMessage() {
        Ui ui = new Ui();
        StubExpenseList expenseList =
                new StubExpenseList(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        String output = captureOutput(() -> ui.showExpenseList(expenseList));

        assertTrue(output.contains("Your expense list is currently empty."));
    }

    @Test
    public void showExpenseList_nonEmptyList_printsExpenses() {
        Ui ui = new Ui();
        ArrayList<Expense> expenses = new ArrayList<>();
        expenses.add(makeExpense("Coffee", 5.50));
        expenses.add(makeExpense("Bus", 2.00));

        StubExpenseList expenseList =
                new StubExpenseList(expenses, new ArrayList<>(), new ArrayList<>());

        String output = captureOutput(() -> ui.showExpenseList(expenseList));

        assertTrue(output.contains("Here are your tracked expenses:"));
        assertTrue(output.contains("1. " + expenses.get(0)));
        assertTrue(output.contains("2. " + expenses.get(1)));
    }

    @Test
    public void showExpenseListForMonth_emptyList_printsNoExpensesFound() {
        Ui ui = new Ui();
        ArrayList<Expense> expenses = new ArrayList<>();
        YearMonth month = YearMonth.of(2026, 4);

        String output = captureOutput(() -> ui.showExpenseList(expenses, month));

        assertTrue(output.contains("No expenses found for 2026-04."));
    }

    @Test
    public void showExpenseListForMonth_nonEmptyList_printsMonthlyExpenses() {
        Ui ui = new Ui();
        ArrayList<Expense> expenses = new ArrayList<>();
        YearMonth month = YearMonth.of(2026, 4);

        expenses.add(makeExpense("Groceries", 12.50, "Food", LocalDate.of(2026, 4, 3)));
        expenses.add(makeExpense("Train", 2.00, "Transport", LocalDate.of(2026, 4, 5)));

        String output = captureOutput(() -> ui.showExpenseList(expenses, month));

        assertTrue(output.contains("Here are your tracked expenses for 2026-04:"));
        assertTrue(output.contains("1. " + expenses.get(0)));
        assertTrue(output.contains("2. " + expenses.get(1)));
    }

    @Test
    public void basicUiMessages_printExpectedText() {
        Ui ui = new Ui();

        assertTrue(captureOutput(ui::showExit).contains("Bye. Hope to see you again soon!"));
        assertTrue(captureOutput(ui::showUnknownCommand)
                .contains("Unknown command. Type 'help' to see available commands."));
        assertTrue(captureOutput(ui::showAddUsage).contains("Usage: add <amount>"));
        assertTrue(captureOutput(ui::showAddUsage)
                .contains("Example: add 5.50 /c Food /da 2026-03-24 Chicken Rice"));
        assertTrue(captureOutput(ui::showDeleteUsage).contains("Usage: delete <index>"));
        assertTrue(captureOutput(ui::showEditUsage).contains("Usage: edit <index>"));
        assertTrue(captureOutput(ui::showEditUsage).contains("At least one flag must be provided."));
        assertTrue(captureOutput(ui::showInvalidAmount)
                .contains("Amount must be a valid non-negative number."));
        assertTrue(captureOutput(ui::showZeroAmountWarning)
                .contains("Expense amounts must be greater than $0.00."));
        assertTrue(captureOutput(ui::showInvalidDateFormat)
                .contains("Date must be in YYYY-MM-DD format and be a valid calendar date."));
        assertTrue(captureOutput(ui::showInvalidIndexFormat)
                .contains("Index must be a valid integer."));
        assertTrue(captureOutput(ui::showInvalidIndex)
                .contains("Invalid index! Use 'list' to see valid numbers."));
        assertTrue(captureOutput(ui::showLoadWarning)
                .contains("Warning: Could not load data file. Starting with empty list."));
        assertTrue(captureOutput(ui::showSaveWarning)
                .contains("Warning: Could not save data to file."));
    }

    @Test
    public void warningAndSummaryMethods_printExpectedText() {
        Ui ui = new Ui();

        String malformedOutput = captureOutput(() -> ui.showMalformedLineWarning("bad line"));
        assertTrue(malformedOutput.contains("Warning: Skipping malformed line: bad line"));

        String invalidAmountLineOutput = captureOutput(() -> ui.showInvalidAmountLineWarning("oops"));
        assertTrue(invalidAmountLineOutput.contains("Warning: Skipping line with invalid amount: oops"));

        String totalOutput = captureOutput(() -> ui.showTotal(17.8, 3));
        assertTrue(totalOutput.contains("You have 3 expense(s) in your list."));
        assertTrue(totalOutput.contains("Your total spending is: $17.80"));
    }

    @Test
    public void monthlyBudgetMethods_coverAllBudgetBranches() {
        Ui ui = new Ui();
        YearMonth month = YearMonth.of(2026, 4);

        String budgetSetOutput = captureOutput(() -> ui.showBudgetSet(month, 200.0));
        assertTrue(budgetSetOutput.contains("Budget for 2026-04 set to $200.00"));

        String budgetExceededWarningOutput = captureOutput(
                () -> ui.showBudgetExceededWarning(month, 100.0, 150.5)
        );
        assertTrue(budgetExceededWarningOutput
                .contains("Warning: You have exceeded your budget for 2026-04!"));
        assertTrue(budgetExceededWarningOutput.contains("Budget: $100.00"));
        assertTrue(budgetExceededWarningOutput.contains("Current total: $150.50"));

        String budgetUpdatedOutput = captureOutput(() -> ui.showBudgetUpdated(month, 120.0, 180.0));
        assertTrue(budgetUpdatedOutput
                .contains("Budget for 2026-04 updated from $120.00 to $180.00"));

        String budgetNotSetOutput = captureOutput(() -> ui.showBudgetNotSet(month));
        assertTrue(budgetNotSetOutput.contains("No budget set for 2026-04."));
        assertTrue(budgetNotSetOutput.contains("Use: budget 2026-04 <amount>"));

        String budgetDetailsRemainingOutput = captureOutput(
                () -> ui.showBudgetDetails(month, 200.0, 50.0, 150.0)
        );
        assertTrue(budgetDetailsRemainingOutput.contains("Budget summary for 2026-04:"));
        assertTrue(budgetDetailsRemainingOutput.contains("Budget: $200.00"));
        assertTrue(budgetDetailsRemainingOutput.contains("Spent: $50.00"));
        assertTrue(budgetDetailsRemainingOutput.contains("Remaining: $150.00"));

        String budgetDetailsExceededOutput = captureOutput(
                () -> ui.showBudgetDetails(month, 200.0, 250.0, -50.0)
        );
        assertTrue(budgetDetailsExceededOutput.contains("Exceeded by: $50.00"));

        String budgetUsageOutput = captureOutput(ui::showBudgetUsage);
        assertTrue(budgetUsageOutput.contains("Usage: budget"));
        assertTrue(budgetUsageOutput.contains("budget <amount>"));
        assertTrue(budgetUsageOutput.contains("budget <YYYY-MM>"));
        assertTrue(budgetUsageOutput.contains("budget <YYYY-MM> <amount>"));

        assertTrue(captureOutput(ui::showInvalidBudget)
                .contains("Budget must be a number greater then 0."));
    }

    @Test
    public void findMethods_coverAllSearchDescriptionBranches() {
        Ui ui = new Ui();
        ArrayList<Expense> results = new ArrayList<>();
        Expense expense = new Expense("Lunch", 8.0);
        results.add(expense);

        String usageOutput = captureOutput(ui::showFindUsage);
        assertTrue(usageOutput.contains("Usage: find KEYWORD [/c CATEGORY] [/dmin DATE]"));
        assertTrue(usageOutput.contains("find lunch"));
        assertTrue(usageOutput.contains("find /c Food"));
        assertTrue(usageOutput.contains("/sort asc|desc"));
        assertTrue(usageOutput.contains("find /c Food /sort desc"));

        String emptyKeywordOnly = captureOutput(
                () -> ui.showFindResults(new ArrayList<>(), "lunch", null)
        );
        assertTrue(emptyKeywordOnly.contains("No expenses found matching: \"lunch\""));

        String emptyCategoryOnly = captureOutput(
                () -> ui.showFindResults(new ArrayList<>(), "", "Food")
        );
        assertTrue(emptyCategoryOnly.contains("No expenses found matching: category [Food]"));

        String keywordAndCategoryOutput = captureOutput(
                () -> ui.showFindResults(results, "lunch", "Food")
        );
        assertTrue(keywordAndCategoryOutput
                .contains("Here are the matching expenses for \"lunch\" in category [Food]:"));
        assertTrue(keywordAndCategoryOutput.contains("1. " + expense));
    }

    @Test
    public void sortAndStatisticsMethods_coverAllBranches() {
        Ui ui = new Ui();

        StubExpenseList emptyList =
                new StubExpenseList(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        String emptySortedOutput = captureOutput(() -> ui.showSorted(emptyList, "date"));
        assertTrue(emptySortedOutput.contains("Expenses sorted by date:"));
        assertTrue(emptySortedOutput.contains("(no expenses to display)"));

        ArrayList<Expense> expenses = new ArrayList<>();
        expenses.add(makeExpense("Coffee", 5.50));
        expenses.add(makeExpense("Movie", 14.00));

        StubExpenseList filledList =
                new StubExpenseList(expenses, new ArrayList<>(), new ArrayList<>());
        String filledSortedOutput = captureOutput(() -> ui.showSorted(filledList, "category"));
        assertTrue(filledSortedOutput.contains("Expenses sorted by category:"));
        assertTrue(filledSortedOutput.contains("1. " + expenses.get(0)));
        assertTrue(filledSortedOutput.contains("2. " + expenses.get(1)));

        String sortUsageOutput = captureOutput(ui::showSortUsage);
        assertTrue(sortUsageOutput.contains("Usage: sort category"));
        assertTrue(sortUsageOutput.contains("sort date"));

        Map<String, Double> emptyStats = new LinkedHashMap<>();
        String emptyStatsOutput = captureOutput(() -> ui.showStatistics(emptyStats, 0));
        assertTrue(emptyStatsOutput.contains("Spending statistics (0 expense(s)):"));
        assertTrue(emptyStatsOutput.contains("No expenses to summarise."));

        Map<String, Double> stats = new LinkedHashMap<>();
        stats.put("Food", 12.50);
        stats.put("Transport", 3.20);

        String statsOutput = captureOutput(() -> ui.showStatistics(stats, 2));
        assertTrue(statsOutput.contains("Food: $12.50"));
        assertTrue(statsOutput.contains("Transport: $3.20"));
    }

    @Test
    public void getUserInput_trimsWhitespace() {
        Ui ui = createUiWithInput("   hello world   \n");

        assertEquals("hello world", ui.getUserInput());
    }

    @Test
    public void showCategoryPrompt_printsPromptAndOptions() {
        Ui ui = new Ui();
        ArrayList<String> categories = new ArrayList<>();
        categories.add("Food");
        categories.add("Transport");
        categories.add("Others");

        String output = captureOutput(() -> ui.showCategoryPrompt(categories));

        assertTrue(output.contains("You didn't specify a category! Choose one from the list:"));
        assertTrue(output.contains("1. Food"));
        assertTrue(output.contains("2. Transport"));
        assertTrue(output.contains("3. Others"));
        assertTrue(output.contains("Enter a number, or type a new category name:"));
    }

    @Test
    public void loanUiMethods_coverAllBranches() {
        Ui ui = new Ui();
        Loan loan1 = makeLoan("John", 20.0, LocalDate.of(2026, 4, 1));
        Loan loan2 = makeLoan("Jane", 30.0, LocalDate.of(2026, 4, 2));

        String lendAddedOutput = captureOutput(() -> ui.showLendAdded(loan1, 2));
        assertTrue(lendAddedOutput.contains("Got it. I've recorded this loan:"));
        assertTrue(lendAddedOutput.contains(loan1.toString()));
        assertTrue(lendAddedOutput.contains("You now have 2 loan(s) on record."));
        assertTrue(lendAddedOutput.contains("This does not affect your expense total or budget."));

        StubExpenseList noLoansAnywhere =
                new StubExpenseList(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        String noLoansOnRecordOutput = captureOutput(() -> ui.showLoans(noLoansAnywhere, true));
        assertTrue(noLoansOnRecordOutput.contains("No loans on record."));

        String noOutstandingAndNoPastLoansOutput =
                captureOutput(() -> ui.showLoans(noLoansAnywhere, false));
        assertTrue(noOutstandingAndNoPastLoansOutput.contains("No outstanding loans."));

        ArrayList<Loan> allLoansOnly = new ArrayList<>();
        allLoansOnly.add(loan1);
        StubExpenseList allRepaid =
                new StubExpenseList(new ArrayList<>(), allLoansOnly, new ArrayList<>());
        String allRepaidOutput = captureOutput(() -> ui.showLoans(allRepaid, false));
        assertTrue(allRepaidOutput.contains("No outstanding loans."));
        assertTrue(allRepaidOutput.contains("All loans have been repaid"));

        ArrayList<Loan> allLoans = new ArrayList<>();
        allLoans.add(loan1);
        allLoans.add(loan2);

        ArrayList<Loan> outstandingLoans = new ArrayList<>();
        outstandingLoans.add(loan1);

        StubExpenseList loansPresent =
                new StubExpenseList(new ArrayList<>(), allLoans, outstandingLoans);

        String allLoansOutput = captureOutput(() -> ui.showLoans(loansPresent, true));
        assertTrue(allLoansOutput.contains("All loans (2):"));
        assertTrue(allLoansOutput.contains("1. " + loan1));
        assertTrue(allLoansOutput.contains("2. " + loan2));

        String outstandingOutput = captureOutput(() -> ui.showLoans(loansPresent, false));
        assertTrue(outstandingOutput.contains("Outstanding loans (1):"));
        assertTrue(outstandingOutput.contains("1. " + loan1));

        String repaidOutput = captureOutput(() -> ui.showRepaid(loan1));
        assertTrue(repaidOutput.contains("Great! Marked as repaid:"));
        assertTrue(repaidOutput.contains(loan1.toString()));

        String noOutstandingLoansOutput = captureOutput(ui::showNoOutstandingLoans);
        assertTrue(noOutstandingLoansOutput.contains("There are no outstanding loans to repay."));
        assertTrue(noOutstandingLoansOutput.contains("Use 'loans /all' to see all past loans."));

        String invalidLoanIndexOutput = captureOutput(() -> ui.showInvalidLoanIndex(3));
        assertTrue(invalidLoanIndexOutput
                .contains("Invalid loan index! There are 3 outstanding loan(s)."));
        assertTrue(invalidLoanIndexOutput.contains("Use 'loans' to see the current list."));

        String lendUsageOutput = captureOutput(ui::showLendUsage);
        assertTrue(lendUsageOutput.contains("Usage: lend <amount> <borrower name>"));
        assertTrue(lendUsageOutput.contains("Example: lend 20.00 John"));
        assertTrue(lendUsageOutput.contains("Example: lend 50.00 Jane /da 2026-04-01"));

        String loansUsageOutput = captureOutput(ui::showLoansUsage);
        assertTrue(loansUsageOutput.contains("Usage: loans"));
        assertTrue(loansUsageOutput.contains("loans /all"));

        String repayUsageOutput = captureOutput(ui::showRepayUsage);
        assertTrue(repayUsageOutput.contains("Usage: repay <index>"));
        assertTrue(repayUsageOutput.contains("Use 'loans' to see the index of each outstanding loan."));
    }
}
