package seedu.duke;

import org.junit.jupiter.api.Test;
import seedu.duke.ui.Ui;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListCommandTest {

    private static final String LINE = "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~";

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
    public void execute_emptyList_outputWrappedWithLines() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));

        Ui ui = new Ui();
        ExpenseList expenseList = new ExpenseList();
        new ListCommand(ui).execute(expenseList);

        System.setOut(original);
        String output = out.toString();
        String[] lines = output.split(System.lineSeparator());

        // First and last printed lines must both be the LINE separator
        assertEquals(LINE, lines[0], "Output should open with LINE");
        assertEquals(LINE, lines[lines.length - 1], "Empty list output must close with LINE");
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
