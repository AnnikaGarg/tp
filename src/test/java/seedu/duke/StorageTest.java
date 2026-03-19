package seedu.duke;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
        originalList.addExpense(new Expense("Lunch", 12.50));
        originalList.addExpense(new Expense("Transport", 2.10));

        storage.save(originalList);

        ExpenseList loadedList = new ExpenseList();
        storage.load(loadedList);

        assertEquals(2, loadedList.getSize());
        assertEquals("Lunch", loadedList.getExpense(0).getDescription());
        assertEquals(12.50, loadedList.getExpense(0).getAmount(), 0.0001);
        assertEquals("Transport", loadedList.getExpense(1).getDescription());
        assertEquals(2.10, loadedList.getExpense(1).getAmount(), 0.0001);
    }

    @Test
    public void load_malformedLine_skipsMalformedData() throws IOException {
        Path dataFilePath = tempDir.resolve("expenses-malformed.txt");
        Files.writeString(dataFilePath, "8.50 | Breakfast\nmalformed line\n3.20 | Coffee\n");

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
        Files.writeString(dataFilePath, "invalidAmount | Dinner\n6.75 | Snacks\n");

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
        Files.writeString(dataFilePath, "-2.50 | Dinner\n1.00 | Apple\n");

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
}
