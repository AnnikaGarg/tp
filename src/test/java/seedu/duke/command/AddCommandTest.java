package seedu.duke.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.YearMonth;

import org.junit.jupiter.api.Test;

import seedu.duke.model.ExpenseList;
import seedu.duke.ui.Ui;

public class AddCommandTest {
    private static final YearMonth TEST_MONTH = YearMonth.of(2026, 4);

    @Test
    public void execute_singleExpense_expenseAddedToList() {
        ExpenseList expenseList = new ExpenseList();
        Ui ui = new Ui();
        AddCommand addCommand = new AddCommand(ui, "Coffee", 5.50, "Others", null);

        addCommand.execute(expenseList);

        assertEquals(1, expenseList.getSize());
        assertEquals("Coffee", expenseList.getExpense(0).getDescription());
        assertEquals(5.50, expenseList.getExpense(0).getAmount(), 0.0001);
        assertEquals("Others", expenseList.getExpense(0).getCategory());
    }

    @Test
    public void execute_multipleExpenses_expensesAddedCorrectly() {
        ExpenseList expenseList = new ExpenseList();
        Ui ui = new Ui();
        AddCommand firstExpense = new AddCommand(ui, "Coffee", 5.50, "Food", null);
        AddCommand secondExpense = new AddCommand(ui, "Lunch", 12.30, "Food", null);

        firstExpense.execute(expenseList);
        secondExpense.execute(expenseList);

        assertEquals(2, expenseList.getSize());
        assertEquals("Coffee", expenseList.getExpense(0).getDescription());
        assertEquals(5.50, expenseList.getExpense(0).getAmount(), 0.0001);
        assertEquals("Food", expenseList.getExpense(0).getCategory());
        assertEquals("Lunch", expenseList.getExpense(1).getDescription());
        assertEquals(12.30, expenseList.getExpense(1).getAmount(), 0.0001);
        assertEquals("Food", expenseList.getExpense(1).getCategory());
    }

    @Test
    public void execute_withCategory_expenseStoredCorrectly() {
        ExpenseList expenseList = new ExpenseList();
        Ui ui = new Ui();
        AddCommand addCommand = new AddCommand(ui, "Groceries", 18.40, "Shopping", null);

        addCommand.execute(expenseList);

        assertEquals(1, expenseList.getSize());
        assertEquals("Groceries", expenseList.getExpense(0).getDescription());
        assertEquals(18.40, expenseList.getExpense(0).getAmount(), 0.0001);
        assertEquals("Shopping", expenseList.getExpense(0).getCategory());
    }

    @Test
    public void execute_withExplicitDate_expenseStoredWithGivenDate() {
        ExpenseList expenseList = new ExpenseList();
        Ui ui = new Ui();
        LocalDate date = LocalDate.of(2026, 4, 1);
        AddCommand addCommand = new AddCommand(ui, "Dinner", 15.00, "Food", date);

        addCommand.execute(expenseList);

        assertEquals(1, expenseList.getSize());
        assertEquals(date, expenseList.getExpense(0).getDate());
    }

    @Test
    public void execute_withNullDate_expenseStoredWithTodayDate() {
        ExpenseList expenseList = new ExpenseList();
        Ui ui = new Ui();
        LocalDate today = LocalDate.now();
        AddCommand addCommand = new AddCommand(ui, "Snack", 3.20, "Food", null);

        addCommand.execute(expenseList);

        assertEquals(1, expenseList.getSize());
        assertEquals(today, expenseList.getExpense(0).getDate());
    }

    @Test
    public void execute_zeroAmount_expenseAddedWithZeroAmount() {
        ExpenseList expenseList = new ExpenseList();
        Ui ui = new Ui();
        AddCommand addCommand = new AddCommand(ui, "Free sample", 0.0, "Others", null);

        addCommand.execute(expenseList);

        assertEquals(1, expenseList.getSize());
        assertEquals("Free sample", expenseList.getExpense(0).getDescription());
        assertEquals(0.0, expenseList.getExpense(0).getAmount(), 0.0001);
        assertEquals("Others", expenseList.getExpense(0).getCategory());
    }

    @Test
    public void isExit_returnsFalse() {
        Ui ui = new Ui();
        AddCommand addCommand = new AddCommand(ui, "Coffee", 5.50, "Food", null);

        assertFalse(addCommand.isExit());
    }

    @Test
    public void shouldPersist_returnsTrue() {
        Ui ui = new Ui();
        AddCommand addCommand = new AddCommand(ui, "Coffee", 5.50, "Food", null);

        assertTrue(addCommand.shouldPersist());
    }

    @Test
    public void execute_nullCategory_selectsFromPromptByNumber() {
        java.io.InputStream originalIn = System.in;
        System.setIn(new java.io.ByteArrayInputStream("1\n".getBytes()));

        ExpenseList expenseList = new ExpenseList();
        Ui ui = new Ui();
        AddCommand cmd = new AddCommand(ui, "Coffee", 5.50, null, null);

        cmd.execute(expenseList);
        System.setIn(originalIn);

        assertEquals(1, expenseList.getSize());
        assertEquals("Food", expenseList.getExpense(0).getCategory());
    }

    @Test
    public void execute_nullCategoryEmptyInput_defaultsToOthers() {
        java.io.InputStream originalIn = System.in;
        System.setIn(new java.io.ByteArrayInputStream("\n".getBytes()));

        ExpenseList expenseList = new ExpenseList();
        Ui ui = new Ui();
        AddCommand cmd = new AddCommand(ui, "Coffee", 5.50, null, null);

        cmd.execute(expenseList);
        System.setIn(originalIn);

        assertEquals(1, expenseList.getSize());
        assertEquals("Others", expenseList.getExpense(0).getCategory());
    }

    @Test
    public void execute_nullCategoryInvalidNumber_defaultsToOthers() {
        java.io.InputStream originalIn = System.in;
        System.setIn(new java.io.ByteArrayInputStream("999\n".getBytes()));

        ExpenseList expenseList = new ExpenseList();
        Ui ui = new Ui();
        AddCommand cmd = new AddCommand(ui, "Coffee", 5.50, null, null);

        cmd.execute(expenseList);
        System.setIn(originalIn);

        assertEquals(1, expenseList.getSize());
        assertEquals("Others", expenseList.getExpense(0).getCategory());
    }

    @Test
    public void execute_nullCategoryNewName_createsCategory() {
        java.io.InputStream originalIn = System.in;
        System.setIn(new java.io.ByteArrayInputStream("Snacks\n".getBytes()));

        ExpenseList expenseList = new ExpenseList();
        Ui ui = new Ui();
        AddCommand cmd = new AddCommand(ui, "Chips", 3.00, null, null);

        cmd.execute(expenseList);
        System.setIn(originalIn);

        assertEquals(1, expenseList.getSize());
        assertEquals("Snacks", expenseList.getExpense(0).getCategory());
        assertTrue(expenseList.getCategoryList().contains("Snacks"));
    }

    @Test
    public void execute_overBudget_showsWarning() {
        ExpenseList expenseList = new ExpenseList();
        expenseList.setBudget(TEST_MONTH, 5.00);

        Ui ui = new Ui();
        LocalDate date = LocalDate.of(2026, 4, 10);

        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        java.io.PrintStream original = System.out;
        System.setOut(new java.io.PrintStream(out));

        new AddCommand(ui, "Dinner", 10.00, "Food", date).execute(expenseList);

        System.setOut(original);

        assertTrue(
                out.toString().contains("exceeded"),
                "Adding over budget should show exceeded warning"
        );
    }
}
