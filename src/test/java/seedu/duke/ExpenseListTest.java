package seedu.duke;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
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

    @Test
    public void setExpense_nullExpense_throwsIllegalArgumentException() {
        ExpenseList expenseList = new ExpenseList();
        expenseList.addExpense(new Expense("Lunch", 5.50));
        assertThrows(IllegalArgumentException.class, () -> expenseList.setExpense(0, null));
    }

    @Test
    public void setExpense_validExpense_replacesCorrectly() {
        ExpenseList expenseList = new ExpenseList();
        expenseList.addExpense(new Expense("Lunch", 5.50));
        Expense newExpense = new Expense("Dinner", 10.00);

        expenseList.setExpense(0, newExpense);

        assertEquals("Dinner", expenseList.getExpense(0).getDescription());
        assertEquals(10.00, expenseList.getExpense(0).getAmount());
    }

    @Test
    public void deleteExpense_validIndex_removesExpense() {
        ExpenseList expenseList = new ExpenseList();
        Expense testExpense = new Expense("Lunch", 5.50);
        expenseList.addExpense(testExpense);

        Expense deleted = expenseList.deleteExpense(0);

        assertEquals(0, expenseList.getSize());
        assertEquals(testExpense, deleted);
    }

    @Test
    public void addCategory_newCategory_addedSuccessfully() {
        ExpenseList list = new ExpenseList();
        int initialSize = list.getCategoryList().size();

        list.addCategory("Subscriptions");

        assertEquals(initialSize + 1, list.getCategoryList().size());
        // Verify it was added right before "Others"
        assertEquals("Subscriptions", list.getCategory(list.getCategoryList().size() - 2));
    }

    @Test
    public void addCategory_duplicateCategory_doesNotDuplicate() {
        ExpenseList list = new ExpenseList();
        int initialSize = list.getCategoryList().size();

        // "Food" is already a default category. Adding it again shouldn't change the size.
        list.addCategory("fOoD");
        assertEquals(initialSize, list.getCategoryList().size());
    }

    @Test
    public void addCategory_nullOrEmptyCategory_isIgnored() {
        ExpenseList list = new ExpenseList();
        int initialSize = list.getCategoryList().size();

        list.addCategory(null);
        list.addCategory("   ");

        assertEquals(initialSize, list.getCategoryList().size());
    }

    // ── Sorted insertion (Task 1) ─────────────────────────────────────────────

    @Test
    public void addExpense_olderDate_insertsAtFront() {
        ExpenseList list = new ExpenseList();
        list.addExpense(new Expense("Lunch",   5.50, "Food", LocalDate.of(2026, 3, 10)));
        list.addExpense(new Expense("Bus fare", 1.80, "Transport", LocalDate.of(2026, 3,  5)));

        // Mar-05 is earlier → should be at index 0
        assertEquals(LocalDate.of(2026, 3,  5), list.getExpense(0).getDate());
        assertEquals(LocalDate.of(2026, 3, 10), list.getExpense(1).getDate());
    }

    @Test
    public void addExpense_middleDate_insertsInCorrectPosition() {
        ExpenseList list = new ExpenseList();
        list.addExpense(new Expense("Coffee", 3.00, "Food",      LocalDate.of(2026, 3,  1)));
        list.addExpense(new Expense("Movie",  15.00, "Entertainment", LocalDate.of(2026, 3, 20)));
        list.addExpense(new Expense("Gym",    50.00, "Health",    LocalDate.of(2026, 3, 10)));

        // Expected order: Mar-01, Mar-10, Mar-20
        assertEquals(LocalDate.of(2026, 3,  1), list.getExpense(0).getDate());
        assertEquals(LocalDate.of(2026, 3, 10), list.getExpense(1).getDate());
        assertEquals(LocalDate.of(2026, 3, 20), list.getExpense(2).getDate());
    }

    @Test
    public void addExpense_multipleAdds_listAlwaysSortedByDate() {
        ExpenseList list = new ExpenseList();
        list.addExpense(new Expense("D", 4.00, "Others", LocalDate.of(2026, 4, 15)));
        list.addExpense(new Expense("A", 1.00, "Others", LocalDate.of(2026, 1,  1)));
        list.addExpense(new Expense("C", 3.00, "Others", LocalDate.of(2026, 3,  3)));
        list.addExpense(new Expense("B", 2.00, "Others", LocalDate.of(2026, 2, 20)));

        assertEquals(4, list.getSize());
        for (int i = 0; i < list.getSize() - 1; i++) {
            assertFalse(list.getExpense(i).getDate().isAfter(list.getExpense(i + 1).getDate()),
                    "List should be in ascending date order at index " + i);
        }
    }
}
