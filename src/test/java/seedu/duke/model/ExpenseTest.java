package seedu.duke.model;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
public class ExpenseTest {
    @Test
    public void toString_validExpense_formatsCorrectly() {
        LocalDate date = LocalDate.of(2026, 3, 24);
        Expense expense = new Expense("Chicken Rice Lunch", 5.50, "Food", date);
        String expected = "Chicken Rice Lunch ($5.50) [Food] [2026-03-24]";
        assertEquals(expected, expense.toString());
    }
    @Test
    public void toString_defaultCategoryAndDate_usesOthersAndToday() {
        Expense expense = new Expense("Chicken Rice Lunch", 5.50);
        String expected = "Chicken Rice Lunch ($5.50) [Others] [" + LocalDate.now() + "]";
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
    public void getCategory_defaultCategory_returnsOthers() {
        Expense expense = new Expense("Bus ticket", 1.50);
        assertEquals("Others", expense.getCategory());
    }
    @Test
    public void getDate_defaultDate_returnsToday() {
        Expense expense = new Expense("Bus ticket", 1.50);
        assertEquals(LocalDate.now(), expense.getDate());
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

    @Test
    public void constructor_allLowerCaseCategory_formatsToTitleCase() {
        Expense expense = new Expense("Chicken Rice", 5.50, "food", null);
        assertEquals("Food", expense.getCategory());
    }

    @Test
    public void constructor_mixedCaseCategory_formatsToTitleCase() {
        Expense expense = new Expense("Bus", 2.00, "tRaNsPoRt", null);
        assertEquals("Transport", expense.getCategory());
    }

    @Test
    public void constructor_nullDescription_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Expense(null, 5.00, "Food", null));
    }

    @Test
    public void constructor_infiniteAmount_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Expense("Lunch", Double.POSITIVE_INFINITY, null, null));
    }

    @Test
    public void constructor_emptyCategoryString_defaultsToOthers() {
        // Tests the branch where category != null, but it IS completely empty/spaces
        Expense expense = new Expense("Coffee", 5.00, "   ", null);
        assertEquals("Others", expense.getCategory());
    }


}
