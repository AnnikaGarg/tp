package seedu.duke.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.duke.model.Expense;
import seedu.duke.model.ExpenseList;
import seedu.duke.ui.Ui;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    public void execute_editZeroAmount_expenseUnchanged() {
        double originalAmount = expenseList.getExpense(0).getAmount();

        EditCommand command = new EditCommand(ui, 1, 0.00, null, null, null);
        command.execute(expenseList);

        assertEquals(originalAmount, expenseList.getExpense(0).getAmount());
    }

    @Test
    public void execute_editNegativeAmount_expenseUnchanged() {
        double originalAmount = expenseList.getExpense(0).getAmount();

        EditCommand command = new EditCommand(ui, 1, -5.00, null, null, null);
        command.execute(expenseList);

        assertEquals(originalAmount, expenseList.getExpense(0).getAmount());
    }

    @Test
    public void execute_editDateToImpossibleYear_expenseUnchanged() {
        LocalDate originalDate = expenseList.getExpense(0).getDate();
        LocalDate badDate = LocalDate.of(0, 1, 1);

        EditCommand command = new EditCommand(ui, 1, null, null, null, badDate);
        command.execute(expenseList);

        assertEquals(originalDate, expenseList.getExpense(0).getDate());
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

    @Test
    public void execute_editDateOnly_updatesDateCorrectly() {
        // Expense moves position after re-sort, so find it by description rather than index.
        LocalDate newDate = LocalDate.of(2026, 1, 15);
        new EditCommand(ui, 1, null, null, null, newDate).execute(expenseList);

        Expense updated = null;
        for (int i = 0; i < expenseList.getSize(); i++) {
            if (expenseList.getExpense(i).getDescription().equals("Chicken Rice")) {
                updated = expenseList.getExpense(i);
                break;
            }
        }
        assertTrue(updated != null);
        assertEquals(newDate, updated.getDate());
        assertEquals(3.50, updated.getAmount());
    }

    @Test
    public void execute_editAllFourFields_updatesAllCorrectly() {
        // newDate is in the future so after re-sort this expense stays at index 0.
        LocalDate newDate = LocalDate.of(2026, 6, 1);
        EditCommand command = new EditCommand(ui, 1, 8.00, "Laksa", "Food", newDate);
        command.execute(expenseList);

        Expense updated = expenseList.getExpense(0);
        assertEquals("Laksa", updated.getDescription());
        assertEquals(8.00, updated.getAmount());
        assertEquals("Food", updated.getCategory());
        assertEquals(newDate, updated.getDate());
    }

    @Test
    public void shouldPersist_returnsTrue() {
        EditCommand command = new EditCommand(ui, 1, 5.00, null, null, null);
        assertTrue(command.shouldPersist());
    }

    @Test
    public void execute_editDate_listRemainsInChronologicalOrder() {
        ExpenseList list = new ExpenseList();
        list.addExpense(new Expense("Lunch", 5.00, "Food",      LocalDate.of(2026, 3, 10)));
        list.addExpense(new Expense("Bus",   1.50, "Transport", LocalDate.of(2026, 3,  5)));

        new EditCommand(ui, 1, null, null, null, LocalDate.of(2026, 3, 1)).execute(list);

        assertEquals(LocalDate.of(2026, 3, 5), list.getExpense(0).getDate());
        assertEquals(LocalDate.of(2026, 3, 1), list.getExpense(1).getDate());
        for (int i = 0; i < list.getSize() - 1; i++) {
            assertTrue(!list.getExpense(i).getDate().isBefore(list.getExpense(i + 1).getDate()));
        }
    }
}