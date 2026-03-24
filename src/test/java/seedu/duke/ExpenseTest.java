package seedu.duke;

import org.junit.jupiter.api.Test;
import java.time.LocalDate; // NEW IMPORT
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ExpenseTest {

    @Test
    public void toString_validExpense_formatsCorrectly() {
        // FIX: Use a hardcoded date so this test never fails on a different day!
        LocalDate fixedDate = LocalDate.parse("2026-03-24");
        Expense expense = new Expense("Chicken Rice Lunch", 5.50, "Food", fixedDate);

        // FIX: Updated expected string to match your new v2.0 format
        String expected = "Chicken Rice Lunch ($5.50) [Cat: Food] [Date: Mar 24 2026]";

        assertEquals(expected, expense.toString());
    }

    @Test
    public void constructor_nullCategory_defaultsToOthers() {
        Expense expense = new Expense("Bus ticket", 1.50, null, null);
        assertEquals("Others", expense.getCategory());
    }

    @Test
    public void constructor_nullDate_defaultsToToday() {
        Expense expense = new Expense("Bus ticket", 1.50, null, null);
        assertEquals(LocalDate.now(), expense.getDate());
    }
    // -----------------------------------

    @Test
    public void getDescription_validExpense_returnsCorrectDescription() {
        Expense expense = new Expense("Textbook", 50.00, null, null);
        assertEquals("Textbook", expense.getDescription());
    }

    @Test
    public void getAmount_validExpense_returnsCorrectAmount() {
        Expense expense = new Expense("Bus ticket", 1.50, null, null);
        assertEquals(1.50, expense.getAmount());
    }

    @Test
    public void constructor_negativeAmount_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Expense("Lunch", -1.00, null, null));
    }

    @Test
    public void constructor_nanAmount_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Expense("Lunch", Double.NaN, null, null));
    }

    @Test
    public void constructor_emptyDescription_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Expense("   ", 2.00, null, null));
    }
}