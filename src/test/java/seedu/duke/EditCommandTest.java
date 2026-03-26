package seedu.duke;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EditCommandTest {

    private ExpenseList expenseList;
    private Ui ui;

    @BeforeEach
    public void setUp() {
        ui = new Ui();
        expenseList = new ExpenseList();
        expenseList.addExpense(new Expense("Chicken Rice", 3.50));
        expenseList.addExpense(new Expense("Bus Fare", 1.20));
    }

    @Test
    public void execute_editBothFields_updatesBothCorrectly() {
        EditCommand command = new EditCommand(ui, 1, 4.00, "Nasi Lemak", null, null);
        command.execute(expenseList);

        Expense updated = expenseList.getExpense(0);
        assertEquals("Nasi Lemak", updated.getDescription());
        assertEquals(4.00, updated.getAmount());
    }

    @Test
    public void execute_editAmountOnly_keepsOriginalDescription() {
        EditCommand command = new EditCommand(ui, 2, 2.50, null, null, null);
        command.execute(expenseList);

        Expense updated = expenseList.getExpense(1);
        assertEquals("Bus Fare", updated.getDescription());
        assertEquals(2.50, updated.getAmount());
    }

    @Test
    public void execute_editDescriptionOnly_keepsOriginalAmount() {
        EditCommand command = new EditCommand(ui, 1, null, "Wonton Noodles", null, null);
        command.execute(expenseList);

        Expense updated = expenseList.getExpense(0);
        assertEquals("Wonton Noodles", updated.getDescription());
        assertEquals(3.50, updated.getAmount());
    }

    @Test
    public void execute_editCategoryOnly_keepsOtherFields() {
        EditCommand command = new EditCommand(ui, 1, null, null, "Food", null);
        command.execute(expenseList);

        Expense updated = expenseList.getExpense(0);
        assertEquals("Chicken Rice", updated.getDescription());
        assertEquals(3.50, updated.getAmount());
        assertEquals("Food", updated.getCategory());
    }

    @Test
    public void execute_editDoesNotAffectOtherExpenses() {
        EditCommand command = new EditCommand(ui, 1, 9.99, "Sushi", null, null);
        command.execute(expenseList);

        Expense untouched = expenseList.getExpense(1);
        assertEquals("Bus Fare", untouched.getDescription());
        assertEquals(1.20, untouched.getAmount());
    }

    @Test
    public void execute_listSizeUnchangedAfterEdit() {
        int sizeBefore = expenseList.getSize();
        EditCommand command = new EditCommand(ui, 1, 5.00, "Laksa", null, null);
        command.execute(expenseList);

        assertEquals(sizeBefore, expenseList.getSize());
    }

    @Test
    public void execute_editZeroAmount_updatesAmountToZero() {
        EditCommand command = new EditCommand(ui, 1, 0.00, null, null, null);
        command.execute(expenseList);

        assertEquals(0.00, expenseList.getExpense(0).getAmount());
    }

    @Test
    public void execute_indexTooLarge_doesNotModifyList() {
        EditCommand command = new EditCommand(ui, 99, 10.00, "Ghost Item", null, null);
        command.execute(expenseList);

        assertEquals("Chicken Rice", expenseList.getExpense(0).getDescription());
        assertEquals("Bus Fare", expenseList.getExpense(1).getDescription());
    }

    @Test
    public void constructor_nullUi_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> new EditCommand(null, 1, 5.00, "Lunch", null, null));
    }
}
