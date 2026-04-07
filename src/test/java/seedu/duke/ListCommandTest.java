package seedu.duke;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import seedu.duke.ui.Ui;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ListCommandTest {

    private static final String LINE = "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~";

    private final PrintStream originalOut = System.out;

    @AfterEach
    public void restoreSystemOut() {
        System.setOut(originalOut);
    }

    private String captureOutput(Runnable action) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        action.run();
        return out.toString().replace("\r\n", "\n");
    }

    @Test
    public void execute_emptyList_executesWithoutException() {
        Ui ui = new Ui();
        ExpenseList expenseList = new ExpenseList();
        ListCommand listCommand = new ListCommand(ui, null);

        assertDoesNotThrow(() -> listCommand.execute(expenseList));
    }

    @Test
    public void execute_emptyList_outputWrappedWithLines() {
        Ui ui = new Ui();
        ExpenseList expenseList = new ExpenseList();

        String output = captureOutput(() -> new ListCommand(ui, null).execute(expenseList));

        assertTrue(output.startsWith(LINE));
        assertTrue(output.contains("Your expense list is currently empty."));
        assertTrue(output.trim().endsWith(LINE));
    }

    @Test
    public void execute_populatedList_executesWithoutException() {
        Ui ui = new Ui();
        ExpenseList expenseList = new ExpenseList();
        expenseList.addExpense(new Expense("Lunch", 10.50, "Food", LocalDate.of(2026, 4, 5)));
        expenseList.addExpense(new Expense("Bus Fare", 2.00, "Transport", LocalDate.of(2026, 4, 6)));

        ListCommand listCommand = new ListCommand(ui, null);

        assertDoesNotThrow(() -> listCommand.execute(expenseList));
    }

    @Test
    public void execute_populatedList_printsExpenses() {
        Ui ui = new Ui();
        ExpenseList expenseList = new ExpenseList();
        Expense lunch = new Expense("Lunch", 10.50, "Food", LocalDate.of(2026, 4, 5));
        Expense busFare = new Expense("Bus Fare", 2.00, "Transport", LocalDate.of(2026, 4, 6));
        expenseList.addExpense(lunch);
        expenseList.addExpense(busFare);

        String output = captureOutput(() -> new ListCommand(ui, null).execute(expenseList));

        assertTrue(output.contains("Here are your tracked expenses:"));
        assertTrue(output.contains("1. "));
        assertTrue(output.contains("2. "));
        assertTrue(output.contains(lunch.toString()));
        assertTrue(output.contains(busFare.toString()));
    }

    @Test
    public void execute_monthlyList_executesWithoutException() {
        Ui ui = new Ui();
        ExpenseList expenseList = new ExpenseList();
        expenseList.addExpense(new Expense("Lunch", 10.50, "Food", LocalDate.of(2026, 4, 5)));
        expenseList.addExpense(new Expense("Movie", 15.00, "Entertainment", LocalDate.of(2026, 5, 1)));

        ListCommand listCommand = new ListCommand(ui, YearMonth.of(2026, 4));

        assertDoesNotThrow(() -> listCommand.execute(expenseList));
    }

    @Test
    public void execute_monthlyList_printsOnlyRequestedMonth() {
        Ui ui = new Ui();
        ExpenseList expenseList = new ExpenseList();

        Expense aprilExpense = new Expense("Lunch", 10.50, "Food", LocalDate.of(2026, 4, 5));
        Expense mayExpense = new Expense("Movie", 15.00, "Entertainment", LocalDate.of(2026, 5, 1));

        expenseList.addExpense(aprilExpense);
        expenseList.addExpense(mayExpense);

        String output = captureOutput(
                () -> new ListCommand(ui, YearMonth.of(2026, 4)).execute(expenseList)
        );

        assertTrue(output.contains("Here are your tracked expenses for 2026-04:"));
        assertTrue(output.contains(aprilExpense.toString()));
        assertTrue(!output.contains(mayExpense.toString()));
    }

    @Test
    public void execute_monthlyList_printsEmptyMonthMessage() {
        Ui ui = new Ui();
        ExpenseList expenseList = new ExpenseList();
        expenseList.addExpense(new Expense("Movie", 15.00, "Entertainment", LocalDate.of(2026, 5, 1)));

        String output = captureOutput(
                () -> new ListCommand(ui, YearMonth.of(2026, 4)).execute(expenseList)
        );

        assertTrue(output.contains("No expenses found for 2026-04."));
    }
}
