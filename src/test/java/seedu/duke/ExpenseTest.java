package seedu.duke;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ExpenseTest {

    @Test
    public void toString_validExpense_formatsCorrectly() {
        Expense expense = new Expense("Chicken Rice Lunch", 5.50);

        // This checks if your String.format("%.2f", amount) logic works!
        String expected = "Chicken Rice Lunch ($5.50)";

        assertEquals(expected, expense.toString());
    }

    @Test
    public void getDescription_validExpense_returnsCorrectDescription() {
        Expense expense = new Expense("Textbook", 50.00);
        assertEquals("Textbook", expense.getDescription());
    }

    @Test
    public void getAmount_validExpense_returnsCorrectAmount() {
        Expense expense = new Expense("Bus ticket", 1.50);
        assertEquals(1.50, expense.getAmount());
    }

    @Test
    public void constructor_negativeAmount_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Expense("Lunch", -1.00));
    }

    @Test
    public void constructor_nanAmount_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Expense("Lunch", Double.NaN));
    }

    @Test
    public void constructor_emptyDescription_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Expense("   ", 2.00));
    }
}
