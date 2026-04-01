package seedu.duke;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

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
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        action.run();
        return out.toString().replace("\r\n", "\n");
    }

    private Ui createUiWithInput(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        return new Ui();
    }

    private Expense makeExpense(String description, double amount) {
        return new Expense(description, amount);
    }

    private Loan makeLoan(String borrowerName, double amount, LocalDate date) {
        return new Loan(borrowerName, amount, date);
    }

    /**
     * Stub ExpenseList to control Ui branches for testing.
     */
    private static class StubExpenseList extends ExpenseList {
        private final ArrayList<Expense> expenses;
        private final ArrayList<Loan> allLoans;
        private final ArrayList<Loan> outstandingLoans;

        StubExpenseList(ArrayList<Expense> expenses, ArrayList<Loan> allLoans, ArrayList<Loan> outstandingLoans) {
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
    public void showHelp_printsHelpMenu() {
        Ui ui = new Ui();
        String output = captureOutput(ui::showHelp);

        assertTrue(output.contains("Here are the available commands:"));
        assertTrue(output.contains("budget [AMOUNT]"));
        assertTrue(output.contains("find KEYWORD"));
        assertTrue(output.contains("loans /all"));
        assertTrue(output.contains("Available categories: Food, Transport, Shopping, Entertainment, Health, Others"));
    }

    @Test
    public void showExpenseList_emptyList_printsEmptyMessage() {
        Ui ui = new Ui();
        StubExpenseList list = new StubExpenseList(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        String output = captureOutput(() -> ui.showExpenseList(list));

        assertTrue(output.contains("Your expense list is currently empty."));
    }

    @Test
    public void showExpenseList_nonEmptyList_printsExpenses() {
        Ui ui = new Ui();
        ArrayList<Expense> expenses = new ArrayList<>();
        expenses.add(makeExpense("Coffee", 5.50));
        expenses.add(makeExpense("Bus", 2.00));
        StubExpenseList list = new StubExpenseList(expenses, new ArrayList<>(), new ArrayList<>());

        String output = captureOutput(() -> ui.showExpenseList(list));

        assertTrue(output.contains("Here are your tracked expenses:"));
        assertTrue(output.contains("1. " + expenses.get(0)));
        assertTrue(output.contains("2. " + expenses.get(1)));
    }

    @Test
    public void basicUiMessages_printExpectedText() {
        Ui ui = new Ui();

        assertTrue(captureOutput(ui::showExit).contains("Bye. Hope to see you again soon!"));
        assertTrue(captureOutput(ui::showUnknownCommand).contains("Unknown command. Type 'help'"));
        assertTrue(captureOutput(ui::showAddUsage).contains("Usage: add <amount>"));
        assertTrue(captureOutput(ui::showDeleteUsage).contains("Usage: delete <index>"));
        assertTrue(captureOutput(ui::showEditUsage).contains("At least one flag must be provided."));
        assertTrue(captureOutput(ui::showInvalidAmount).contains("Amount must be a valid non-negative number."));
        assertTrue(captureOutput(ui::showZeroAmountWarning).contains("Expense amounts must be greater than $0.00."));
        assertTrue(captureOutput(ui::showInvalidDateFormat).contains("Date must be in YYYY-MM-DD format"));
        assertTrue(captureOutput(ui::showInvalidIndexFormat).contains("Index must be a valid integer."));
        assertTrue(captureOutput(ui::showInvalidIndex).contains("Invalid index! Use 'list' to see valid numbers."));
        assertTrue(captureOutput(ui::showLoadWarning).contains("Warning: Could not load data file."));
        assertTrue(captureOutput(ui::showSaveWarning).contains("Warning: Could not save data to file."));
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

        String budgetSetOutput = captureOutput(() -> ui.showBudgetSet(200));
        assertTrue(budgetSetOutput.contains("Budget set to $200.00"));

        String exceededOutput = captureOutput(() -> ui.showBudgetExceededWarning(100, 150.5));
        assertTrue(exceededOutput.contains("Warning: You have exceeded your budget of $100.00!"));
        assertTrue(exceededOutput.contains("Current total: $150.50"));
    }

    @Test
    public void budgetMethods_coverAllBranches() {
        Ui ui = new Ui();

        String notSetOutput = captureOutput(ui::showBudgetNotSet);
        assertTrue(notSetOutput.contains("Budget not set yet! Use: budget <amount>"));

        String remainingOutput = captureOutput(() -> ui.showBudgetDetails(200.0, 50.0, 150.0));
        assertTrue(remainingOutput.contains("Current budget: $200.0"));
        assertTrue(remainingOutput.contains("Total spent: $50.0"));
        assertTrue(remainingOutput.contains("Remaining budget: $150.0"));

        String exceededOutput = captureOutput(() -> ui.showBudgetDetails(200.0, 250.0, -50.0));
        assertTrue(exceededOutput.contains("Budget exceeded by $50.0"));

        assertTrue(captureOutput(ui::showBudgetUsage).contains("Usage: budget AMOUNT"));
        assertTrue(captureOutput(ui::showInvalidBudget).contains("Budget must be a number greater then 0."));
    }

    @Test
    public void findMethods_coverAllSearchDescriptionBranches() {
        Ui ui = new Ui();
        ArrayList<Expense> results = new ArrayList<>();
        Expense expense = makeExpense("Lunch", 8.0);
        results.add(expense);

        assertTrue(captureOutput(ui::showFindUsage).contains("Usage: find KEYWORD"));

        String emptyKeywordOnly = captureOutput(() -> ui.showFindResults(new ArrayList<>(),
                "lunch", null));
        assertTrue(emptyKeywordOnly.contains("No expenses found matching: \"lunch\""));

        String emptyCategoryOnly = captureOutput(() -> ui.showFindResults(new ArrayList<>(),
                "", "Food"));
        assertTrue(emptyCategoryOnly.contains("No expenses found matching: category [Food]"));

        String resultKeywordAndCategory = captureOutput(() -> ui.showFindResults(results,
                "lunch", "Food"));
        assertTrue(resultKeywordAndCategory.contains("Here are the matching expenses for \"lunch\"" +
                " in category [Food]:"));
        assertTrue(resultKeywordAndCategory.contains("1. " + expense));
    }

    @Test
    public void sortAndStatisticsMethods_coverAllBranches() {
        Ui ui = new Ui();

        StubExpenseList emptyList = new StubExpenseList(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        String emptySorted = captureOutput(() -> ui.showSorted(emptyList, "date"));
        assertTrue(emptySorted.contains("Expenses sorted by date:"));
        assertTrue(emptySorted.contains("(no expenses to display)"));

        ArrayList<Expense> expenses = new ArrayList<>();
        expenses.add(makeExpense("Coffee", 5.50));
        expenses.add(makeExpense("Movie", 14.00));
        StubExpenseList filledList = new StubExpenseList(expenses, new ArrayList<>(), new ArrayList<>());
        String nonEmptySorted = captureOutput(() -> ui.showSorted(filledList, "category"));
        assertTrue(nonEmptySorted.contains("Expenses sorted by category:"));
        assertTrue(nonEmptySorted.contains("1. " + expenses.get(0)));
        assertTrue(nonEmptySorted.contains("2. " + expenses.get(1)));

        assertTrue(captureOutput(ui::showSortUsage).contains("Usage: sort category"));

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

        String lendAdded = captureOutput(() -> ui.showLendAdded(loan1, 2));
        assertTrue(lendAdded.contains("Got it. I've recorded this loan:"));
        assertTrue(lendAdded.contains(loan1.toString()));
        assertTrue(lendAdded.contains("You now have 2 loan(s) on record."));
        assertTrue(lendAdded.contains("This does not affect your expense total or budget."));

        StubExpenseList noLoans = new StubExpenseList(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        String noLoansOutput = captureOutput(() -> ui.showLoans(noLoans, true));
        assertTrue(noLoansOutput.contains("No loans on record."));

        ArrayList<Loan> allLoansOnly = new ArrayList<>();
        allLoansOnly.add(loan1);
        StubExpenseList allRepaid = new StubExpenseList(new ArrayList<>(), allLoansOnly, new ArrayList<>());
        String noOutstandingOutput = captureOutput(() -> ui.showLoans(allRepaid, false));
        assertTrue(noOutstandingOutput.contains("No outstanding loans."));
        assertTrue(noOutstandingOutput.contains("All loans have been repaid"));

        ArrayList<Loan> allLoans = new ArrayList<>();
        allLoans.add(loan1);
        allLoans.add(loan2);

        ArrayList<Loan> outstanding = new ArrayList<>();
        outstanding.add(loan1);

        StubExpenseList loansPresent = new StubExpenseList(new ArrayList<>(), allLoans, outstanding);

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

        assertTrue(captureOutput(ui::showNoOutstandingLoans).contains("There are no outstanding loans to repay."));
        assertTrue(captureOutput(() -> ui.showInvalidLoanIndex(3)).contains("Invalid loan index! " +
                "There are 3 outstanding loan(s)."));
        assertTrue(captureOutput(ui::showLendUsage).contains("Usage: lend <amount> <borrower name>"));
        assertTrue(captureOutput(ui::showLoansUsage).contains("loans /all"));
        assertTrue(captureOutput(ui::showRepayUsage).contains("Usage: repay <index>"));
    }
}
