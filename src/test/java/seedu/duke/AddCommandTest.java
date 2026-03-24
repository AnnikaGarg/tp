package seedu.duke;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AddCommandTest {

    @Test
    public void execute_singleExpense_expenseAddedToList() {
        ExpenseList expenseList = new ExpenseList();
        Ui ui = new Ui();
        AddCommand addCommand = new AddCommand(ui, "Coffee", 5.50, null, null);

        addCommand.execute(expenseList);

        assertEquals(1, expenseList.getSize());
        assertEquals("Coffee", expenseList.getExpense(0).getDescription());
        assertEquals(5.50, expenseList.getExpense(0).getAmount(), 0.0001);
    }

    @Test
    public void execute_multipleExpenses_expensesAddedCorrectly() {
        ExpenseList expenseList = new ExpenseList();
        Ui ui = new Ui();

        AddCommand firstExpense = new AddCommand(ui, "Coffee", 5.50, null, null);
        AddCommand secondExpense = new AddCommand(ui, "Lunch", 12.30, null, null);

        firstExpense.execute(expenseList);
        secondExpense.execute(expenseList);

        assertEquals(2, expenseList.getSize());

        assertEquals("Coffee", expenseList.getExpense(0).getDescription());
        assertEquals(5.50, expenseList.getExpense(0).getAmount(), 0.0001);

        assertEquals("Lunch", expenseList.getExpense(1).getDescription());
        assertEquals(12.30, expenseList.getExpense(1).getAmount(), 0.0001);
    }

    @Test
    public void execute_zeroAmount_throwsAssertionError() {
        Ui ui = new Ui();

        AssertionError thrown = assertThrows(AssertionError.class, () -> {
            new AddCommand(ui, "Free sample", 0.0, null, null);
        });

        assertEquals("Amount must be positive", thrown.getMessage());
    }
}
