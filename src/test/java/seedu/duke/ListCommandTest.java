package seedu.duke;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ListCommandTest {

    @Test
    public void execute_emptyList_executesWithoutException() {
        Ui ui = new Ui();
        ExpenseList expenseList = new ExpenseList();
        ListCommand listCommand = new ListCommand(ui);

        // Verify it handles an empty list gracefully
        assertDoesNotThrow(() -> listCommand.execute(expenseList),
                "ListCommand should execute successfully on an empty list");
    }

    @Test
    public void execute_populatedList_executesWithoutException() {
        Ui ui = new Ui();
        ExpenseList expenseList = new ExpenseList();

        // Add some dummy data
        expenseList.addExpense(new Expense("Lunch", 10.50, null, null));
        expenseList.addExpense(new Expense("Bus Fare", 2.00, null, null));

        ListCommand listCommand = new ListCommand(ui);

        // Verify it iterates and prints a populated list without crashing
        assertDoesNotThrow(() -> listCommand.execute(expenseList),
                "ListCommand should execute successfully on a populated list");
    }
}
