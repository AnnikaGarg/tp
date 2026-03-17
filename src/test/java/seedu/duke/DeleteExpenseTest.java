package seedu.duke;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeleteExpenseTest {
    private ExpenseList expenseList;
    private Ui ui;

    @BeforeEach
    public void setUp() {
        expenseList = new ExpenseList();
        ui = new Ui();
        expenseList.addExpense(new Expense("Lunch", 10.00));
        expenseList.addExpense(new Expense("Dinner", 20.00));
    }

    @Test
    public void execute_validIndex_removesCorrectExpense() {
        // User types "delete 1" -> refers to "Lunch"
        DeleteExpense deleteCommand = new DeleteExpense(1);
        deleteCommand.execute(expenseList, ui);

        assertEquals(1, expenseList.getSize());
        assertEquals("Dinner", expenseList.getExpense(0).getDescription());
    }

    @Test
    public void execute_invalidIndex_doesNotChangeListSize() {
        // User types "delete 5" (out of bounds)
        DeleteExpense deleteCommand = new DeleteExpense(5);
        deleteCommand.execute(expenseList, ui);

        // Should handle the error gracefully and keep the list intact
        assertEquals(2, expenseList.getSize());
    }
}