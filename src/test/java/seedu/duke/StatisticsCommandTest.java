package seedu.duke;

import org.junit.jupiter.api.Test;
import seedu.duke.ui.Ui;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StatisticsCommandTest {

    private final Ui ui = new Ui();

    /**
     * Simple container to capture the map passed into showStatistics from an anonymous Ui subclass.
     * This avoids using a raw-type array (which causes unchecked compiler warnings).
     */
    private static class MapHolder {
        Map<String, Double> value;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private ExpenseList buildList() {
        ExpenseList list = new ExpenseList();
        list.addExpense(new Expense("Lunch", 10.00, "Food", LocalDate.of(2026, 3, 1)));
        list.addExpense(new Expense("Dinner", 20.00, "Food", LocalDate.of(2026, 3, 2)));
        list.addExpense(new Expense("Bus fare", 5.00, "Transport", LocalDate.of(2026, 3, 3)));
        return list;
    }

    // ── Category totals ───────────────────────────────────────────────────────

    @Test
    public void execute_twoCategories_correctTotals() {
        MapHolder holder = new MapHolder();
        Ui capturingUi = new Ui() {
            @Override
            public void showStatistics(Map<String, Double> totals, int count) {
                holder.value = new LinkedHashMap<>(totals);
            }
        };

        new StatisticsCommand(capturingUi).execute(buildList());

        assertNotNull(holder.value);
        assertEquals(2, holder.value.size(), "Should have exactly two categories");
        assertEquals(30.00, holder.value.get("Food"), 0.001);
        assertEquals(5.00, holder.value.get("Transport"), 0.001);
    }

    @Test
    public void execute_emptyList_emptyMap() {
        MapHolder holder = new MapHolder();
        Ui capturingUi = new Ui() {
            @Override
            public void showStatistics(Map<String, Double> totals, int count) {
                holder.value = new LinkedHashMap<>(totals);
            }
        };

        new StatisticsCommand(capturingUi).execute(new ExpenseList());

        assertNotNull(holder.value);
        assertTrue(holder.value.isEmpty(), "Totals map should be empty for an empty expense list");
    }

    @Test
    public void execute_sameCategory_combinedTotal() {
        MapHolder holder = new MapHolder();
        Ui capturingUi = new Ui() {
            @Override
            public void showStatistics(Map<String, Double> totals, int count) {
                holder.value = new LinkedHashMap<>(totals);
            }
        };

        ExpenseList list = new ExpenseList();
        list.addExpense(new Expense("A", 7.00, "Food", null));
        list.addExpense(new Expense("B", 3.00, "Food", null));
        new StatisticsCommand(capturingUi).execute(list);

        assertEquals(10.00, holder.value.get("Food"), 0.001);
        assertEquals(1, holder.value.size());
    }

    // ── shouldPersist ─────────────────────────────────────────────────────────

    @Test
    public void shouldPersist_returnsFalse() {
        assertFalse(new StatisticsCommand(ui).shouldPersist());
    }

    // ── Parser integration ────────────────────────────────────────────────────

    @Test
    public void parse_statsCommand_returnsStatisticsCommand() {
        Command cmd = Parser.parse("stats", ui);
        assertNotNull(cmd);
        assertTrue(cmd instanceof StatisticsCommand);
    }

    @Test
    public void parse_statsWithExtraArgs_returnsNull() {
        Command cmd = Parser.parse("stats extra", ui);
        assertNull(cmd);
    }
}
