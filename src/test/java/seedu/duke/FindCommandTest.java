package seedu.duke;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FindCommandTest {

    private ExpenseList buildList() {
        ExpenseList expenseList = new ExpenseList();
        Ui ui = new Ui();
        new AddCommand(ui, "Coffee", 5.50, "Food",
                LocalDate.of(2026, 1, 10)).execute(expenseList);
        new AddCommand(ui, "Bus ride", 1.20, "Transport",
                LocalDate.of(2026, 2, 15)).execute(expenseList);
        new AddCommand(ui, "Chicken Rice", 4.00, "Food",
                LocalDate.of(2026, 3, 1)).execute(expenseList);
        new AddCommand(ui, "Movie ticket", 12.00, "Entertainment",
                LocalDate.of(2026, 3, 20)).execute(expenseList);
        return expenseList;
    }

    private FindCommand findWithCategory(Ui ui, String keyword, String category) {
        return new FindCommand(ui, keyword, category, null, null, null, null, null);
    }

    // ===== Keyword tests =====

    @Test
    public void execute_matchingKeyword_doesNotChangeListSize() {
        ExpenseList expenseList = buildList();
        new FindCommand(new Ui(), "coffee").execute(expenseList);
        assertEquals(4, expenseList.getSize());
    }

    @Test
    public void execute_caseInsensitiveMatch_findsExpense() {
        ExpenseList expenseList = buildList();
        new FindCommand(new Ui(), "COFFEE").execute(expenseList);
        assertEquals(4, expenseList.getSize());
    }

    @Test
    public void execute_noMatch_listUnchanged() {
        ExpenseList expenseList = buildList();
        new FindCommand(new Ui(), "sushi").execute(expenseList);
        assertEquals(4, expenseList.getSize());
    }

    @Test
    public void execute_emptyList_executesWithoutException() {
        ExpenseList expenseList = new ExpenseList();
        new FindCommand(new Ui(), "coffee").execute(expenseList);
        assertEquals(0, expenseList.getSize());
    }

    @Test
    public void shouldPersist_returnsFalse() {
        assertFalse(new FindCommand(new Ui(), "coffee").shouldPersist());
    }

    @Test
    public void execute_matchingKeyword_outputContainsDescription() {
        ExpenseList expenseList = buildList();
        String output = captureOutput(() ->
                new FindCommand(new Ui(), "coffee").execute(expenseList));
        assertTrue(output.contains("Coffee"));
    }

    @Test
    public void execute_noMatch_outputContainsNoExpensesMessage() {
        ExpenseList expenseList = buildList();
        String output = captureOutput(() ->
                new FindCommand(new Ui(), "sushi").execute(expenseList));
        assertTrue(output.contains("No expenses found matching"));
    }

    @Test
    public void execute_categoryKeyword_outputContainsMatchedExpense() {
        ExpenseList expenseList = buildList();
        String output = captureOutput(() ->
                new FindCommand(new Ui(), "transport").execute(expenseList));
        assertTrue(output.contains("Bus ride"));
    }

    // ===== Category filter tests =====

    @Test
    public void execute_categoryFilter_onlyMatchingCategory() {
        ExpenseList expenseList = buildList();
        String output = captureOutput(() ->
                findWithCategory(new Ui(), "", "Food").execute(expenseList));
        assertTrue(output.contains("Coffee"));
        assertTrue(output.contains("Chicken Rice"));
        assertFalse(output.contains("Bus ride"));
        assertFalse(output.contains("Movie ticket"));
    }

    @Test
    public void execute_categoryFilterCaseInsensitive_matchesCategory() {
        ExpenseList expenseList = buildList();
        String output = captureOutput(() ->
                findWithCategory(new Ui(), "", "food").execute(expenseList));
        assertTrue(output.contains("Coffee"));
    }

    @Test
    public void execute_categoryFilterNoMatch_showsNoResults() {
        ExpenseList expenseList = buildList();
        String output = captureOutput(() ->
                findWithCategory(new Ui(), "", "Utilities").execute(expenseList));
        assertTrue(output.contains("No expenses found"));
    }

    @Test
    public void execute_keywordWithCategoryFilter_filtersOnBoth() {
        ExpenseList expenseList = buildList();
        String output = captureOutput(() ->
                findWithCategory(new Ui(), "coffee", "Food").execute(expenseList));
        assertTrue(output.contains("Coffee"));
        assertFalse(output.contains("Chicken Rice"));
    }

    @Test
    public void execute_keywordWithWrongCategory_showsNoResults() {
        ExpenseList expenseList = buildList();
        String output = captureOutput(() ->
                findWithCategory(new Ui(), "coffee", "Transport").execute(expenseList));
        assertTrue(output.contains("No expenses found"));
    }

    // ===== Date range filter tests =====

    @Test
    public void execute_dateMinFilter_excludesEarlierExpenses() {
        ExpenseList expenseList = buildList();
        FindCommand cmd = new FindCommand(new Ui(), "", null,
                LocalDate.of(2026, 3, 1), null, null, null, null);
        String output = captureOutput(() -> cmd.execute(expenseList));
        assertTrue(output.contains("Chicken Rice"), "Expense on dmin date should be included");
        assertTrue(output.contains("Movie ticket"), "Expense after dmin should be included");
        assertFalse(output.contains("Coffee"), "Expense before dmin should be excluded");
        assertFalse(output.contains("Bus ride"), "Expense before dmin should be excluded");
    }

    @Test
    public void execute_dateMaxFilter_excludesLaterExpenses() {
        ExpenseList expenseList = buildList();
        FindCommand cmd = new FindCommand(new Ui(), "", null,
                null, LocalDate.of(2026, 2, 15), null, null, null);
        String output = captureOutput(() -> cmd.execute(expenseList));
        assertTrue(output.contains("Coffee"), "Expense before dmax should be included");
        assertTrue(output.contains("Bus ride"), "Expense on dmax date should be included");
        assertFalse(output.contains("Chicken Rice"), "Expense after dmax should be excluded");
    }

    @Test
    public void execute_dateRange_onlyExpensesInRange() {
        ExpenseList expenseList = buildList();
        FindCommand cmd = new FindCommand(new Ui(), "", null,
                LocalDate.of(2026, 2, 1), LocalDate.of(2026, 3, 10),
                null, null, null);
        String output = captureOutput(() -> cmd.execute(expenseList));
        assertTrue(output.contains("Bus ride"));
        assertTrue(output.contains("Chicken Rice"));
        assertFalse(output.contains("Coffee"));
        assertFalse(output.contains("Movie ticket"));
    }

    // ===== Amount range filter tests =====

    @Test
    public void execute_amountMinFilter_excludesCheaperExpenses() {
        ExpenseList expenseList = buildList();
        FindCommand cmd = new FindCommand(new Ui(), "", null,
                null, null, 5.00, null, null);
        String output = captureOutput(() -> cmd.execute(expenseList));
        assertTrue(output.contains("Coffee"), "$5.50 >= $5.00");
        assertTrue(output.contains("Movie ticket"), "$12.00 >= $5.00");
        assertFalse(output.contains("Bus ride"), "$1.20 < $5.00");
        assertFalse(output.contains("Chicken Rice"), "$4.00 < $5.00");
    }

    @Test
    public void execute_amountMaxFilter_excludesExpensiveExpenses() {
        ExpenseList expenseList = buildList();
        FindCommand cmd = new FindCommand(new Ui(), "", null,
                null, null, null, 5.00, null);
        String output = captureOutput(() -> cmd.execute(expenseList));
        assertTrue(output.contains("Bus ride"), "$1.20 <= $5.00");
        assertTrue(output.contains("Chicken Rice"), "$4.00 <= $5.00");
        assertFalse(output.contains("Movie ticket"), "$12.00 > $5.00");
    }

    @Test
    public void execute_amountRange_onlyExpensesInRange() {
        ExpenseList expenseList = buildList();
        FindCommand cmd = new FindCommand(new Ui(), "", null,
                null, null, 2.00, 6.00, null);
        String output = captureOutput(() -> cmd.execute(expenseList));
        assertTrue(output.contains("Coffee"), "$5.50 in range");
        assertTrue(output.contains("Chicken Rice"), "$4.00 in range");
        assertFalse(output.contains("Bus ride"), "$1.20 below range");
        assertFalse(output.contains("Movie ticket"), "$12.00 above range");
    }

    // ===== Sort order tests =====

    @Test
    public void execute_sortAsc_lowestAmountFirst() {
        ExpenseList expenseList = buildList();
        FindCommand cmd = new FindCommand(new Ui(), "", null,
                null, null, null, null, "asc");
        String output = captureOutput(() -> cmd.execute(expenseList));
        int busPos = output.indexOf("Bus ride");
        int coffeePos = output.indexOf("Coffee");
        int moviePos = output.indexOf("Movie ticket");
        assertTrue(busPos < coffeePos, "Bus ride ($1.20) should appear before Coffee ($5.50)");
        assertTrue(coffeePos < moviePos, "Coffee ($5.50) should appear before Movie ($12.00)");
    }

    @Test
    public void execute_sortDesc_highestAmountFirst() {
        ExpenseList expenseList = buildList();
        FindCommand cmd = new FindCommand(new Ui(), "", null,
                null, null, null, null, "desc");
        String output = captureOutput(() -> cmd.execute(expenseList));
        int moviePos = output.indexOf("Movie ticket");
        int coffeePos = output.indexOf("Coffee");
        int busPos = output.indexOf("Bus ride");
        assertTrue(moviePos < coffeePos, "Movie ($12.00) should appear before Coffee ($5.50)");
        assertTrue(coffeePos < busPos, "Coffee ($5.50) should appear before Bus ride ($1.20)");
    }

    // ===== Combined filter tests =====

    @Test
    public void execute_categoryAndAmountRange_combinedFilter() {
        ExpenseList expenseList = buildList();
        FindCommand cmd = new FindCommand(new Ui(), "", "Food",
                null, null, 5.00, null, null);
        String output = captureOutput(() -> cmd.execute(expenseList));
        assertTrue(output.contains("Coffee"), "Coffee is Food and >= $5.00");
        assertFalse(output.contains("Chicken Rice"), "Chicken Rice is Food but < $5.00");
        assertFalse(output.contains("Bus ride"), "Bus ride is not Food");
    }

    @Test
    public void execute_keywordAndDateRange_combinedFilter() {
        ExpenseList expenseList = buildList();
        FindCommand cmd = new FindCommand(new Ui(), "chicken", null,
                LocalDate.of(2026, 3, 1), null, null, null, null);
        String output = captureOutput(() -> cmd.execute(expenseList));
        assertTrue(output.contains("Chicken Rice"));
        assertFalse(output.contains("Coffee"), "Matches keyword but before dmin");
    }

    // ===== Helper =====

    private String captureOutput(Runnable action) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));
        action.run();
        System.setOut(original);
        return out.toString();
    }
}
