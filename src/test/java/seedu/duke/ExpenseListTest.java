package seedu.duke;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ExpenseListTest {

    @Test
    public void getSize_emptyList_returnsZero() {
        ExpenseList expenseList = new ExpenseList();
        assertEquals(0, expenseList.getSize());
    }

    @Test
    public void addExpense_singleExpense_sizeIncreasesToOne() {
        ExpenseList expenseList = new ExpenseList();
        Expense testExpense = new Expense("Lunch", 5.50);

        expenseList.addExpense(testExpense);

        assertEquals(1, expenseList.getSize());
    }

    @Test
    public void getExpense_validIndex_returnsCorrectExpense() {
        ExpenseList expenseList = new ExpenseList();
        Expense testExpense = new Expense("Bus fare", 2.00);
        expenseList.addExpense(testExpense);

        Expense retrievedExpense = expenseList.getExpense(0);

        assertEquals("Bus fare", retrievedExpense.getDescription());
        assertEquals(2.00, retrievedExpense.getAmount());
    }

    @Test
    public void addExpense_nullExpense_throwsIllegalArgumentException() {
        ExpenseList expenseList = new ExpenseList();
        assertThrows(IllegalArgumentException.class, () -> expenseList.addExpense(null));
    }
}
