package seedu.duke;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExpenseListTest {

    @Test
    public void getSize_emptyList_returnsZero() {
        ExpenseList expenseList = new ExpenseList();
        assertEquals(0, expenseList.getSize());
    }

    @Test
    public void addExpense_singleExpense_sizeIncreasesToOne() {
        ExpenseList expenseList = new ExpenseList();
        Expense testExpense = new Expense("Lunch", 5.50, null, null);

        expenseList.addExpense(testExpense);

        assertEquals(1, expenseList.getSize());
    }

    @Test
    public void getExpense_validIndex_returnsCorrectExpense() {
        ExpenseList expenseList = new ExpenseList();
        Expense testExpense = new Expense("Bus fare", 2.00, null, null);
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

    @Test
    public void hasBudget_noBudgetSet_returnsFalse() {
        ExpenseList expenseList = new ExpenseList();

        assertFalse(expenseList.hasBudget());
    }

    @Test
    public void setBudget_validBudget_budgetStoredCorrectly() {
        ExpenseList expenseList = new ExpenseList();

        expenseList.setBudget(200.0);

        assertTrue(expenseList.hasBudget());
        assertEquals(200.0, expenseList.getBudget());
    }

    @Test
    public void setBudget_negativeBudget_throwsIllegalArgumentException() {
        ExpenseList expenseList = new ExpenseList();

        assertThrows(IllegalArgumentException.class, () -> expenseList.setBudget(-1.0));
    }

    @Test
    public void getTotalAmount_emptyList_returnsZero() {
        ExpenseList expenseList = new ExpenseList();

        assertEquals(0.0, expenseList.getTotalAmount());
    }

    @Test
    public void getTotalAmount_multipleExpenses_returnsCorrectTotal() {
        ExpenseList expenseList = new ExpenseList();
        expenseList.addExpense(new Expense("Lunch", 5.50, null, null));
        expenseList.addExpense(new Expense("Bus fare", 2.00, null, null));

        assertEquals(7.50, expenseList.getTotalAmount());
    }

    @Test
    public void isOverBudget_noBudgetSet_returnsFalse() {
        ExpenseList expenseList = new ExpenseList();
        expenseList.addExpense(new Expense("Lunch", 5.50, null, null));

        assertFalse(expenseList.isOverBudget());
    }

    @Test
    public void isOverBudget_totalLessThanBudget_returnsFalse() {
        ExpenseList expenseList = new ExpenseList();
        expenseList.setBudget(20.0);
        expenseList.addExpense(new Expense("Lunch", 5.50, null, null));
        expenseList.addExpense(new Expense("Bus fare", 2.00, null, null));

        assertFalse(expenseList.isOverBudget());
    }

    @Test
    public void isOverBudget_totalEqualToBudget_returnsFalse() {
        ExpenseList expenseList = new ExpenseList();
        expenseList.setBudget(7.50);
        expenseList.addExpense(new Expense("Lunch", 5.50, null, null));
        expenseList.addExpense(new Expense("Bus fare", 2.00, null, null));

        assertFalse(expenseList.isOverBudget());
    }

    @Test
    public void isOverBudget_totalGreaterThanBudget_returnsTrue() {
        ExpenseList expenseList = new ExpenseList();
        expenseList.setBudget(7.00);
        expenseList.addExpense(new Expense("Lunch", 5.50, null, null));
        expenseList.addExpense(new Expense("Bus fare", 2.00, null, null));

        assertTrue(expenseList.isOverBudget());
    }
}
