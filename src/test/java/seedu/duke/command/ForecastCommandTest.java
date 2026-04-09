package seedu.duke.command;

import org.junit.jupiter.api.Test;
import seedu.duke.model.Expense;
import seedu.duke.model.ExpenseList;
import seedu.duke.ui.Ui;

import java.time.LocalDate;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ForecastCommandTest {

    @Test
    public void execute_forecastWithBudget_runsSuccessfully() {
        // 1. Setup
        Ui ui = new Ui();
        ExpenseList expenseList = new ExpenseList();
        YearMonth currentMonth = YearMonth.now();

        // Add a budget and an expense so the math has real numbers to work with
        expenseList.setBudget(currentMonth, 500.00);
        expenseList.addExpense(new Expense("Test Coffee", 5.00, "Food", LocalDate.now()));

        ForecastCommand command = new ForecastCommand(ui);

        // 2. Execution & Assertion
        // We want to ensure no ArithmeticExceptions (like division by zero) or NullPointerExceptions occur.
        assertDoesNotThrow(() -> command.execute(expenseList),
                "ForecastCommand should execute mathematically without throwing any exceptions.");
    }

    @Test
    public void shouldPersist_forecastCommand_returnsFalse() {
        Ui ui = new Ui();
        ForecastCommand command = new ForecastCommand(ui);

        // 3. Verify read-only status
        assertFalse(command.shouldPersist(),
                "Forecast is a read-only query and should not trigger a file save.");
    }
}
