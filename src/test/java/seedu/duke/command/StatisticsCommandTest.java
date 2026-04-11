package seedu.duke.command;

import org.junit.jupiter.api.Test;
import seedu.duke.model.Expense;
import seedu.duke.model.ExpenseList;
import seedu.duke.parser.Parser;
import seedu.duke.ui.Ui;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StatisticsCommandTest {

    private final Ui ui = new Ui();

    private static class DashboardHolder {
        int year = -1;
    }

    private ExpenseList buildList() {
        ExpenseList list = new ExpenseList();
        list.addExpense(new Expense("Lunch", 10.00, "Food", LocalDate.of(2026, 3, 1)));
        list.addExpense(new Expense("Dinner", 20.00, "Food", LocalDate.of(2026, 3, 2)));
        list.addExpense(new Expense("Bus fare", 5.00, "Transport", LocalDate.of(2026, 3, 3)));
        return list;
    }

    @Test
    public void execute_callsShowYearlyDashboard_withCurrentYear() {
        DashboardHolder holder = new DashboardHolder();
        Ui capturingUi = new Ui() {
            @Override
            public void showYearlyDashboard(ExpenseList list, int year) {
                holder.year = year;
            }
        };

        new StatisticsCommand(capturingUi).execute(buildList());

        assertEquals(LocalDate.now().getYear(), holder.year);
    }

    @Test
    public void execute_callsShowYearlyDashboard_withSpecificYear() {
        DashboardHolder holder = new DashboardHolder();
        Ui capturingUi = new Ui() {
            @Override
            public void showYearlyDashboard(ExpenseList list, int year) {
                holder.year = year;
            }
        };

        new StatisticsCommand(capturingUi, 2025).execute(buildList());

        assertEquals(2025, holder.year);
    }

    @Test
    public void execute_emptyList_doesNotThrow() {
        DashboardHolder holder = new DashboardHolder();
        Ui capturingUi = new Ui() {
            @Override
            public void showYearlyDashboard(ExpenseList list, int year) {
                holder.year = year;
            }
        };

        new StatisticsCommand(capturingUi).execute(new ExpenseList());

        assertEquals(LocalDate.now().getYear(), holder.year);
    }

    @Test
    public void shouldPersist_returnsFalse() {
        assertFalse(new StatisticsCommand(ui).shouldPersist());
    }

    @Test
    public void parse_statsCommand_returnsStatisticsCommand() {
        Command cmd = Parser.parse("stats", ui);
        assertNotNull(cmd);
        assertTrue(cmd instanceof StatisticsCommand);
    }

    @Test
    public void parse_statsWithYearArgs_returnsStatisticsCommand() {
        Command cmd = Parser.parse("stats 2026", ui);
        assertNotNull(cmd);
        assertTrue(cmd instanceof StatisticsCommand);
    }

    @Test
    public void parse_statsWithInvalidArgs_returnsNull() {
        Command cmd = Parser.parse("stats invalid", ui);
        assertNull(cmd);
    }
}
