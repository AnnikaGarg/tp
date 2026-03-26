package seedu.duke;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
public class StorageTest {
    @TempDir
    Path tempDir;
    @Test
    public void saveAndLoad_validExpenses_roundTripMaintainsData() {
        Path dataFilePath = tempDir.resolve("expenses.txt");
        Storage storage = new Storage(dataFilePath.toString(), new Ui());
        ExpenseList originalList = new ExpenseList();
        originalList.addExpense(new Expense("Lunch", 12.50, "Food", LocalDate.parse("2026-03-24")));
        originalList.addExpense(new Expense("Transport", 2.10, "Travel", LocalDate.parse("2026-03-25")));
        storage.save(originalList);
        ExpenseList loadedList = new ExpenseList();
        storage.load(loadedList);
        assertEquals(2, loadedList.getSize());
        assertEquals("Lunch", loadedList.getExpense(0).getDescription());
        assertEquals(12.50, loadedList.getExpense(0).getAmount(), 0.0001);
        assertEquals("Food", loadedList.getExpense(0).getCategory());
        assertEquals(LocalDate.parse("2026-03-24"), loadedList.getExpense(0).getDate());
        assertEquals("Transport", loadedList.getExpense(1).getDescription());
        assertEquals(2.10, loadedList.getExpense(1).getAmount(), 0.0001);
        assertEquals("Travel", loadedList.getExpense(1).getCategory());
    }
    @Test
    public void load_malformedLine_skipsMalformedData() throws IOException {
        Path dataFilePath = tempDir.resolve("expenses-malformed.txt");
        Files.writeString(dataFilePath,
                "8.50 | 2026-03-24 | Food | Breakfast\n"
                        + "malformed line\n"
                        + "3.20 | 2026-03-24 | Drinks | Coffee\n");
        Storage storage = new Storage(dataFilePath.toString(), new Ui());
        ExpenseList loadedList = new ExpenseList();
        storage.load(loadedList);
        assertEquals(2, loadedList.getSize());
        assertEquals("Breakfast", loadedList.getExpense(0).getDescription());
        assertEquals("Coffee", loadedList.getExpense(1).getDescription());
    }
    @Test
    public void load_invalidAmountLine_skipsInvalidAmountData() throws IOException {
        Path dataFilePath = tempDir.resolve("expenses-invalid-amount.txt");
        Files.writeString(dataFilePath,
                "invalidAmount | 2026-03-24 | Food | Dinner\n"
                        + "6.75 | 2026-03-24 | Food | Snacks\n");
        Storage storage = new Storage(dataFilePath.toString(), new Ui());
        ExpenseList loadedList = new ExpenseList();
        storage.load(loadedList);
        assertEquals(1, loadedList.getSize());
        assertEquals("Snacks", loadedList.getExpense(0).getDescription());
        assertEquals(6.75, loadedList.getExpense(0).getAmount(), 0.0001);
    }
    @Test
    public void load_negativeAmountLine_skipsInvalidAmountData() throws IOException {
        Path dataFilePath = tempDir.resolve("expenses-negative-amount.txt");
        Files.writeString(dataFilePath,
                "-2.50 | 2026-03-24 | Food | Dinner\n"
                        + "1.00 | 2026-03-24 | Food | Apple\n");
        Storage storage = new Storage(dataFilePath.toString(), new Ui());
        ExpenseList loadedList = new ExpenseList();
        storage.load(loadedList);
        assertEquals(1, loadedList.getSize());
        assertEquals("Apple", loadedList.getExpense(0).getDescription());
        assertEquals(1.00, loadedList.getExpense(0).getAmount(), 0.0001);
    }
    @Test
    public void constructor_nullPath_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Storage(null, new Ui()));
    }
    @Test
    public void constructor_nullUi_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Storage("data/expenses.txt", null));
    }
    @Test
    public void load_nullExpenseList_throwsIllegalArgumentException() {
        Storage storage = new Storage(tempDir.resolve("expenses.txt").toString(), new Ui());
        assertThrows(IllegalArgumentException.class, () -> storage.load(null));
    }
    @Test
    public void save_nullExpenseList_throwsIllegalArgumentException() {
        Storage storage = new Storage(tempDir.resolve("expenses.txt").toString(), new Ui());
        assertThrows(IllegalArgumentException.class, () -> storage.save(null));
    }

    @Test
    public void load_emptyFile_loadsNothingIntoList() throws IOException {
        Path dataFilePath = tempDir.resolve("empty.txt");
        Files.writeString(dataFilePath, "");
        Storage storage = new Storage(dataFilePath.toString(), new Ui());
        ExpenseList loadedList = new ExpenseList();
        storage.load(loadedList);
        assertEquals(0, loadedList.getSize(),
                "Loading an empty file should result in an empty expense list");
    }

    @Test
    public void load_v1FormatTwoFields_loadsExpenseWithDefaults() throws IOException {
        Path dataFilePath = tempDir.resolve("v1-expenses.txt");
        Files.writeString(dataFilePath, "5.50 | Coffee\n12.00 | Lunch\n");
        Storage storage = new Storage(dataFilePath.toString(), new Ui());
        ExpenseList loadedList = new ExpenseList();
        storage.load(loadedList);
        assertEquals(2, loadedList.getSize(),
                "v1.0 two-field format should be parsed and loaded");
        assertEquals("Coffee", loadedList.getExpense(0).getDescription());
        assertEquals(5.50, loadedList.getExpense(0).getAmount(), 0.0001);
        assertEquals("Lunch", loadedList.getExpense(1).getDescription());
        assertEquals(12.00, loadedList.getExpense(1).getAmount(), 0.0001);
    }

    @Test
    public void saveAndLoad_withBudgetSet_budgetPersistsAcrossReload() {
        Path dataFilePath = tempDir.resolve("budget-expenses.txt");
        Storage storage = new Storage(dataFilePath.toString(), new Ui());
        ExpenseList originalList = new ExpenseList();
        originalList.setBudget(100.00);
        originalList.addExpense(new Expense("Coffee", 5.50, "Food", LocalDate.parse("2026-03-24")));
        storage.save(originalList);

        ExpenseList loadedList = new ExpenseList();
        storage.load(loadedList);
        assertEquals(1, loadedList.getSize(),
                "Expense saved alongside budget should be reloaded");
        assertEquals(100.00, loadedList.getBudget(), 0.0001,
                "Budget should survive a save-load cycle");
        assertTrue(loadedList.hasBudget(),
                "hasBudget should return true after reload");
    }

    @Test
    public void load_missingFile_loadsNothingAndDoesNotThrow() {
        Path dataFilePath = tempDir.resolve("nonexistent.txt");
        Storage storage = new Storage(dataFilePath.toString(), new Ui());
        ExpenseList loadedList = new ExpenseList();
        storage.load(loadedList);
        assertEquals(0, loadedList.getSize(),
                "Loading from a missing file should leave the list empty without throwing");
    }
}
