package seedu.duke.command;

import org.junit.jupiter.api.Test;
import seedu.duke.model.Expense;
import seedu.duke.model.ExpenseList;
import seedu.duke.parser.Parser;
import seedu.duke.ui.Ui;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SortCommandTest {

    private final Ui ui = new Ui();

    // ── Helper ───────────────────────────────────────────────────────────────

    private ExpenseList buildList() {
        ExpenseList list = new ExpenseList();
        list.addExpense(new Expense("Coffee",    3.50, "Food",      LocalDate.of(2026, 3, 10)));
        list.addExpense(new Expense("Bus fare",  1.80, "Transport", LocalDate.of(2026, 3,  5)));
        list.addExpense(new Expense("Movie",    15.00, "Entertainment", LocalDate.of(2026, 3, 15)));
        return list;
    }

    // ── Sort by category ──────────────────────────────────────────────────────

    @Test
    public void execute_sortByCategory_firstItemIsAlphabeticallyFirst() {
        ExpenseList list = buildList();
        new SortCommand(ui, "category").execute(list);

        // Entertainment < Food < Transport alphabetically
        assertEquals("Entertainment", list.getExpense(0).getCategory());
        assertEquals("Food",          list.getExpense(1).getCategory());
        assertEquals("Transport",     list.getExpense(2).getCategory());
    }

    @Test
    public void execute_sortByCategory_sizeUnchanged() {
        ExpenseList list = buildList();
        new SortCommand(ui, "category").execute(list);
        assertEquals(3, list.getSize());
    }

    // ── Sort by date ──────────────────────────────────────────────────────────

    @Test
    public void execute_sortByDate_newestDateFirst() {
        ExpenseList list = buildList();
        new SortCommand(ui, "date").execute(list);

        // 2026-03-15 > 2026-03-10 > 2026-03-05
        assertEquals(LocalDate.of(2026, 3, 15), list.getExpense(0).getDate());
        assertEquals(LocalDate.of(2026, 3, 10), list.getExpense(1).getDate());
        assertEquals(LocalDate.of(2026, 3,  5), list.getExpense(2).getDate());
    }

    @Test
    public void execute_sortByDate_emptyList() {
        ExpenseList list = new ExpenseList();
        new SortCommand(ui, "date").execute(list);
        assertEquals(0, list.getSize());
    }

    // ── shouldPersist ─────────────────────────────────────────────────────────

    @Test
    public void shouldPersist_returnsTrue() {
        assertTrue(new SortCommand(ui, "category").shouldPersist());
    }

    // ── Parser integration ────────────────────────────────────────────────────

    @Test
    public void parse_sortCategory_returnsSortCommand() {
        Command cmd = Parser.parse("sort category", ui);
        assertNotNull(cmd);
        assertTrue(cmd instanceof SortCommand);
    }

    @Test
    public void parse_sortDate_returnsSortCommand() {
        Command cmd = Parser.parse("sort date", ui);
        assertNotNull(cmd);
        assertTrue(cmd instanceof SortCommand);
    }

    @Test
    public void parse_sortInvalidArg_returnsNull() {
        Command cmd = Parser.parse("sort xyz", ui);
        assertNull(cmd);
    }

    @Test
    public void parse_sortNoArg_returnsNull() {
        Command cmd = Parser.parse("sort", ui);
        assertNull(cmd);
    }
}

