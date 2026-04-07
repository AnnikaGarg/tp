package seedu.duke;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import seedu.duke.ui.Ui;

public class AddCommandTest {

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
}
