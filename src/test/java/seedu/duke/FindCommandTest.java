package seedu.duke;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FindCommandTest {

    private ExpenseList buildList() {
        ExpenseList expenseList = new ExpenseList();
        Ui ui = new Ui();
        new AddCommand(ui, "Coffee", 5.50, "Food", null).execute(expenseList);
        new AddCommand(ui, "Bus ride", 1.20, "Transport", null).execute(expenseList);
        new AddCommand(ui, "Chicken Rice", 4.00, "Food", null).execute(expenseList);
        new AddCommand(ui, "Movie ticket", 12.00, "Entertainment", null).execute(expenseList);
        return expenseList;
    }

    @Test
    public void execute_matchingKeyword_doesNotChangeListSize() {
        ExpenseList expenseList = buildList();
        Ui ui = new Ui();
        FindCommand cmd = new FindCommand(ui, "coffee");
        cmd.execute(expenseList);
        assertEquals(4, expenseList.getSize());
    }

    @Test
    public void execute_caseInsensitiveMatch_findsExpense() {
        ExpenseList expenseList = buildList();
        Ui ui = new Ui();
        // "COFFEE" should match "Coffee" in description
        FindCommand cmd = new FindCommand(ui, "COFFEE");
        cmd.execute(expenseList);
        assertEquals(4, expenseList.getSize()); // list is unchanged
    }

    @Test
    public void execute_noMatch_listUnchanged() {
        ExpenseList expenseList = buildList();
        Ui ui = new Ui();
        FindCommand cmd = new FindCommand(ui, "sushi");
        cmd.execute(expenseList);
        assertEquals(4, expenseList.getSize());
    }

    @Test
    public void execute_categoryKeyword_doesNotChangeListSize() {
        ExpenseList expenseList = buildList();
        Ui ui = new Ui();
        // "Transport" is a category, not a description — should still match
        FindCommand cmd = new FindCommand(ui, "transport");
        cmd.execute(expenseList);
        assertEquals(4, expenseList.getSize());
    }

    @Test
    public void execute_emptyList_executesWithoutException() {
        ExpenseList expenseList = new ExpenseList();
        Ui ui = new Ui();
        FindCommand cmd = new FindCommand(ui, "coffee");
        cmd.execute(expenseList);
        assertEquals(0, expenseList.getSize());
    }

    @Test
    public void shouldPersist_returnsFalse() {
        Ui ui = new Ui();
        FindCommand cmd = new FindCommand(ui, "coffee");
        assertFalse(cmd.shouldPersist());
    }

    @Test
    public void execute_matchingKeyword_outputContainsDescription() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));

        ExpenseList expenseList = buildList();
        new FindCommand(new Ui(), "coffee").execute(expenseList);

        System.setOut(original);
        assertTrue(out.toString().contains("Coffee"), "Output should contain the matched expense description");
    }

    @Test
    public void execute_noMatch_outputContainsNoExpensesMessage() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));

        ExpenseList expenseList = buildList();
        new FindCommand(new Ui(), "sushi").execute(expenseList);

        System.setOut(original);
        assertTrue(out.toString().contains("No expenses found matching"), "Output should state no matches found");
    }

    @Test
    public void execute_categoryKeyword_outputContainsMatchedExpense() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));

        ExpenseList expenseList = buildList();
        new FindCommand(new Ui(), "transport").execute(expenseList);

        System.setOut(original);
        assertTrue(out.toString().contains("Bus ride"), "Output should contain expense whose category matched");
    }

    @Test
    public void execute_categoryFilter_onlyMatchingCategory() {
        ExpenseList expenseList = buildList();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));
        new FindCommand(new Ui(), "", "Food").execute(expenseList);
        System.setOut(original);

        String output = out.toString();
        assertTrue(output.contains("Coffee"), "Output should contain Food expense Coffee");
        assertTrue(output.contains("Chicken Rice"), "Output should contain Food expense Chicken Rice");
        assertFalse(output.contains("Bus ride"), "Output should not contain Transport expense");
        assertFalse(output.contains("Movie ticket"), "Output should not contain Entertainment expense");
    }

    @Test
    public void execute_categoryFilterCaseInsensitive_matchesCategory() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));

        ExpenseList expenseList = buildList();
        new FindCommand(new Ui(), "", "food").execute(expenseList);

        System.setOut(original);
        assertTrue(out.toString().contains("Coffee"), "Case-insensitive category filter should match");
    }

    @Test
    public void execute_categoryFilterNoMatch_showsNoResults() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));

        ExpenseList expenseList = buildList();
        new FindCommand(new Ui(), "", "Utilities").execute(expenseList);

        System.setOut(original);
        assertTrue(out.toString().contains("No expenses found"), "Non-existent category should show no results");
    }

    @Test
    public void execute_keywordWithCategoryFilter_filtersOnBoth() {
        ExpenseList expenseList = buildList();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));
        new FindCommand(new Ui(), "coffee", "Food").execute(expenseList);
        System.setOut(original);

        String output = out.toString();
        assertTrue(output.contains("Coffee"), "Should find Coffee in Food category");
        assertFalse(output.contains("Chicken Rice"), "Chicken Rice doesn't match keyword coffee");
    }

    @Test
    public void execute_keywordWithWrongCategory_showsNoResults() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));

        ExpenseList expenseList = buildList();
        new FindCommand(new Ui(), "coffee", "Transport").execute(expenseList);

        System.setOut(original);
        assertTrue(out.toString().contains("No expenses found"),
                "Coffee exists but not in Transport category");
    }
}
