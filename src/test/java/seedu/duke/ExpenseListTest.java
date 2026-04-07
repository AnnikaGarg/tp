package seedu.duke;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExpenseListTest {

    private static final YearMonth TEST_MONTH = YearMonth.of(2026, 4);

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
    public void hasBudget_noBudgetSetForMonth_returnsFalse() {
        ExpenseList expenseList = new ExpenseList();

        assertFalse(expenseList.hasBudget(TEST_MONTH));
    }

    @Test
    public void setBudget_validBudget_budgetStoredCorrectlyForMonth() {
        ExpenseList expenseList = new ExpenseList();

        expenseList.setBudget(TEST_MONTH, 200.0);

        assertTrue(expenseList.hasBudget(TEST_MONTH));
        assertEquals(200.0, expenseList.getBudget(TEST_MONTH), 0.0001);
    }

    @Test
    public void setBudget_negativeBudget_throwsIllegalArgumentException() {
        ExpenseList expenseList = new ExpenseList();

        assertThrows(IllegalArgumentException.class, () -> expenseList.setBudget(TEST_MONTH, -1.0));
    }

    @Test
    public void setBudget_differentMonths_storedSeparately() {
        ExpenseList expenseList = new ExpenseList();
        YearMonth may = YearMonth.of(2026, 5);

        expenseList.setBudget(TEST_MONTH, 200.0);
        expenseList.setBudget(may, 350.0);

        assertTrue(expenseList.hasBudget(TEST_MONTH));
        assertTrue(expenseList.hasBudget(may));
        assertEquals(200.0, expenseList.getBudget(TEST_MONTH), 0.0001);
        assertEquals(350.0, expenseList.getBudget(may), 0.0001);
    }

    @Test
    public void getTotalAmount_emptyList_returnsZero() {
        ExpenseList expenseList = new ExpenseList();

        assertEquals(0.0, expenseList.getTotalAmount(), 0.0001);
    }

    @Test
    public void getTotalAmount_multipleExpenses_returnsCorrectTotal() {
        ExpenseList expenseList = new ExpenseList();
        expenseList.addExpense(new Expense("Lunch", 5.50, null, null));
        expenseList.addExpense(new Expense("Bus fare", 2.00, null, null));

        assertEquals(7.50, expenseList.getTotalAmount(), 0.0001);
    }

    @Test
    public void isOverBudget_noBudgetSetForMonth_returnsFalse() {
        ExpenseList expenseList = new ExpenseList();
        expenseList.addExpense(new Expense("Lunch", 5.50, "Food", LocalDate.of(2026, 4, 10)));

        assertFalse(expenseList.isOverBudget(TEST_MONTH));
    }

    @Test
    public void isOverBudget_totalLessThanBudgetForMonth_returnsFalse() {
        ExpenseList expenseList = new ExpenseList();
        expenseList.setBudget(TEST_MONTH, 20.0);
        expenseList.addExpense(new Expense("Lunch", 5.50, "Food", LocalDate.of(2026, 4, 10)));
        expenseList.addExpense(new Expense("Bus fare", 2.00, "Transport", LocalDate.of(2026, 4, 11)));

        assertFalse(expenseList.isOverBudget(TEST_MONTH));
    }

    @Test
    public void isOverBudget_totalEqualToBudgetForMonth_returnsFalse() {
        ExpenseList expenseList = new ExpenseList();
        expenseList.setBudget(TEST_MONTH, 7.50);
        expenseList.addExpense(new Expense("Lunch", 5.50, "Food", LocalDate.of(2026, 4, 10)));
        expenseList.addExpense(new Expense("Bus fare", 2.00, "Transport", LocalDate.of(2026, 4, 11)));

        assertFalse(expenseList.isOverBudget(TEST_MONTH));
    }

    @Test
    public void isOverBudget_totalGreaterThanBudgetForMonth_returnsTrue() {
        ExpenseList expenseList = new ExpenseList();
        expenseList.setBudget(TEST_MONTH, 7.00);
        expenseList.addExpense(new Expense("Lunch", 5.50, "Food", LocalDate.of(2026, 4, 10)));
        expenseList.addExpense(new Expense("Bus fare", 2.00, "Transport", LocalDate.of(2026, 4, 11)));

        assertTrue(expenseList.isOverBudget(TEST_MONTH));
    }

    @Test
    public void isOverBudget_expensesInOtherMonth_doNotAffectCurrentMonthBudget() {
        ExpenseList expenseList = new ExpenseList();
        expenseList.setBudget(TEST_MONTH, 10.0);
        expenseList.addExpense(new Expense("May expense", 50.0, "Food", LocalDate.of(2026, 5, 1)));

        assertFalse(expenseList.isOverBudget(TEST_MONTH));
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
        assertEquals(10.00, expenseList.getExpense(0).getAmount(), 0.0001);
    }

    @Test
    public void deleteExpense_validIndex_removesExpense() {
        ExpenseList expenseList = new ExpenseList();
        Expense testExpense = new Expense("Lunch", 5.50);
        expenseList.addExpense(testExpense);

        Expense deletedExpense = expenseList.deleteExpense(0);

        assertEquals(0, expenseList.getSize());
        assertEquals(testExpense, deletedExpense);
    }

    @Test
    public void addCategory_newCategory_addedSuccessfully() {
        ExpenseList expenseList = new ExpenseList();
        int initialSize = expenseList.getCategoryList().size();

        expenseList.addCategory("Subscriptions");

        assertEquals(initialSize + 1, expenseList.getCategoryList().size());
        assertEquals("Subscriptions", expenseList.getCategory(expenseList.getCategoryList().size() - 2));
    }

    @Test
    public void addCategory_duplicateCategory_doesNotDuplicate() {
        ExpenseList expenseList = new ExpenseList();
        int initialSize = expenseList.getCategoryList().size();

        expenseList.addCategory("fOoD");

        assertEquals(initialSize, expenseList.getCategoryList().size());
    }

    @Test
    public void addCategory_nullOrEmptyCategory_isIgnored() {
        ExpenseList expenseList = new ExpenseList();
        int initialSize = expenseList.getCategoryList().size();

        expenseList.addCategory(null);
        expenseList.addCategory("   ");

        assertEquals(initialSize, expenseList.getCategoryList().size());
    }

    @Test
    public void addExpense_olderDate_insertsAtBack() {
        ExpenseList expenseList = new ExpenseList();
        expenseList.addExpense(new Expense("Lunch", 5.50, "Food", LocalDate.of(2026, 3, 10)));
        expenseList.addExpense(new Expense("Bus fare", 1.80, "Transport", LocalDate.of(2026, 3, 5)));

        assertEquals(LocalDate.of(2026, 3, 10), expenseList.getExpense(0).getDate());
        assertEquals(LocalDate.of(2026, 3, 5), expenseList.getExpense(1).getDate());
    }

    @Test
    public void addExpense_middleDate_insertsInCorrectPosition() {
        ExpenseList expenseList = new ExpenseList();
        expenseList.addExpense(new Expense("Coffee", 3.00, "Food", LocalDate.of(2026, 3, 1)));
        expenseList.addExpense(new Expense("Movie", 15.00, "Entertainment", LocalDate.of(2026, 3, 20)));
        expenseList.addExpense(new Expense("Gym", 50.00, "Health", LocalDate.of(2026, 3, 10)));

        assertEquals(LocalDate.of(2026, 3, 20), expenseList.getExpense(0).getDate());
        assertEquals(LocalDate.of(2026, 3, 10), expenseList.getExpense(1).getDate());
        assertEquals(LocalDate.of(2026, 3, 1), expenseList.getExpense(2).getDate());
    }

    @Test
    public void addExpense_multipleAdds_listAlwaysSortedByDate() {
        ExpenseList expenseList = new ExpenseList();
        expenseList.addExpense(new Expense("D", 4.00, "Others", LocalDate.of(2026, 4, 15)));
        expenseList.addExpense(new Expense("A", 1.00, "Others", LocalDate.of(2026, 1, 1)));
        expenseList.addExpense(new Expense("C", 3.00, "Others", LocalDate.of(2026, 3, 3)));
        expenseList.addExpense(new Expense("B", 2.00, "Others", LocalDate.of(2026, 2, 20)));

        assertEquals(4, expenseList.getSize());
        for (int i = 0; i < expenseList.getSize() - 1; i++) {
            assertFalse(
                    expenseList.getExpense(i).getDate().isBefore(expenseList.getExpense(i + 1).getDate()),
                    "List should be in descending date order at index " + i
            );
        }
    }
}
