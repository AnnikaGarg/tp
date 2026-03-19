package seedu.duke;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class TotalCommandTest {

    @Test
    public void execute_emptyList_executesWithoutException() {
        Ui ui = new Ui();
        ExpenseList expenseList = new ExpenseList();
        TotalCommand totalCommand = new TotalCommand(ui);

        assertDoesNotThrow(() -> totalCommand.execute(expenseList),
                "TotalCommand should execute successfully on an empty list");
    }

    @Test
    public void execute_singleExpense_executesWithoutException() {
        Ui ui = new Ui();
        ExpenseList expenseList = new ExpenseList();

        expenseList.addExpense(new Expense("Coffee", 5.50));

        TotalCommand totalCommand = new TotalCommand(ui);

        assertDoesNotThrow(() -> totalCommand.execute(expenseList),
                "TotalCommand should execute successfully with one expense");
    }

    @Test
    public void execute_multipleExpenses_executesWithoutException() {
        Ui ui = new Ui();
        ExpenseList expenseList = new ExpenseList();

        expenseList.addExpense(new Expense("Lunch", 10.50));
        expenseList.addExpense(new Expense("Bus Fare", 2.00));
        expenseList.addExpense(new Expense("Movie", 15.00));

        TotalCommand totalCommand = new TotalCommand(ui);

        assertDoesNotThrow(() -> totalCommand.execute(expenseList),
                "TotalCommand should execute successfully on a populated list");
    }
}
