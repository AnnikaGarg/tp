package seedu.duke;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
}
