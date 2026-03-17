package seedu.duke;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddExpenseTest {

    @Test
    public void execute_singleExpense_expenseAddedToList() {
        ExpenseList expenseList = new ExpenseList();
        Ui ui = new Ui();
        AddExpense addExpense = new AddExpense("Coffee", 5.50);

        addExpense.execute(expenseList, ui);

        assertEquals(1, expenseList.getSize());
        assertEquals("Coffee", expenseList.getExpense(0).getDescription());
        assertEquals(5.50, expenseList.getExpense(0).getAmount(), 0.0001);
    }

    @Test
    public void execute_multipleExpenses_expensesAddedCorrectly() {
        ExpenseList expenseList = new ExpenseList();
        Ui ui = new Ui();

        AddExpense firstExpense = new AddExpense("Coffee", 5.50);
        AddExpense secondExpense = new AddExpense("Lunch", 12.30);

        firstExpense.execute(expenseList, ui);
        secondExpense.execute(expenseList, ui);

        assertEquals(2, expenseList.getSize());

        assertEquals("Coffee", expenseList.getExpense(0).getDescription());
        assertEquals(5.50, expenseList.getExpense(0).getAmount(), 0.0001);

        assertEquals("Lunch", expenseList.getExpense(1).getDescription());
        assertEquals(12.30, expenseList.getExpense(1).getAmount(), 0.0001);
    }

    @Test
    public void execute_zeroAmount_expenseAdded() {
        ExpenseList expenseList = new ExpenseList();
        Ui ui = new Ui();
        AddExpense addExpense = new AddExpense("Free sample", 0.0);

        addExpense.execute(expenseList, ui);

        assertEquals(1, expenseList.getSize());
        assertEquals("Free sample", expenseList.getExpense(0).getDescription());
        assertEquals(0.0, expenseList.getExpense(0).getAmount(), 0.0001);
    }
}
