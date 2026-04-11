package seedu.duke.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.YearMonth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.duke.model.Expense;
import seedu.duke.model.ExpenseList;
import seedu.duke.model.Loan;
import seedu.duke.ui.Ui;

public class StorageTest {
    private static final YearMonth TEST_MONTH = YearMonth.of(2026, 3);

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
        assertEquals("Transport", loadedList.getExpense(0).getDescription());
        assertEquals(2.10, loadedList.getExpense(0).getAmount(), 0.0001);
        assertEquals("Travel", loadedList.getExpense(0).getCategory());
        assertEquals(LocalDate.parse("2026-03-25"), loadedList.getExpense(0).getDate());

        assertEquals("Lunch", loadedList.getExpense(1).getDescription());
        assertEquals(12.50, loadedList.getExpense(1).getAmount(), 0.0001);
        assertEquals("Food", loadedList.getExpense(1).getCategory());
        assertEquals(LocalDate.parse("2026-03-24"), loadedList.getExpense(1).getDate());
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

        originalList.setBudget(TEST_MONTH, 100.00);
        originalList.addExpense(new Expense("Coffee", 5.50, "Food", LocalDate.parse("2026-03-24")));

        storage.save(originalList);

        ExpenseList loadedList = new ExpenseList();
        storage.load(loadedList);

        assertEquals(1, loadedList.getSize(),
                "Expense saved alongside budget should be reloaded");
        assertEquals(100.00, loadedList.getBudget(TEST_MONTH), 0.0001,
                "Budget should survive a save-load cycle");
        assertTrue(loadedList.hasBudget(TEST_MONTH),
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

    @Test
    public void saveAndLoad_validLoans_roundTripMaintainsData() {
        Path dataFilePath = tempDir.resolve("loans.txt");
        Storage storage = new Storage(dataFilePath.toString(), new Ui());
        ExpenseList originalList = new ExpenseList();

        originalList.addLoan(new Loan("John", 20.00, LocalDate.parse("2026-04-01")));
        originalList.addLoan(new Loan("Alice", 50.00, LocalDate.parse("2026-04-02")));

        storage.save(originalList);

        ExpenseList loadedList = new ExpenseList();
        storage.load(loadedList);

        assertEquals(2, loadedList.getLoanCount());
        assertEquals("John", loadedList.getLoan(0).getBorrowerName());
        assertEquals(20.00, loadedList.getLoan(0).getAmount(), 0.0001);
        assertEquals(LocalDate.parse("2026-04-01"), loadedList.getLoan(0).getDate());
        assertFalse(loadedList.getLoan(0).isRepaid());
    }

    @Test
    public void saveAndLoad_repaidLoan_repaidStatusPersists() {
        Path dataFilePath = tempDir.resolve("repaid-loan.txt");
        Storage storage = new Storage(dataFilePath.toString(), new Ui());
        ExpenseList originalList = new ExpenseList();
        Loan loan = new Loan("John", 20.00, LocalDate.parse("2026-04-01"));
        loan.markRepaid();
        originalList.addLoan(loan);

        storage.save(originalList);

        ExpenseList loadedList = new ExpenseList();
        storage.load(loadedList);

        assertEquals(1, loadedList.getLoanCount());
        assertTrue(loadedList.getLoan(0).isRepaid());
    }

    @Test
    public void saveAndLoad_loansDoNotAppearInExpenseList() {
        Path dataFilePath = tempDir.resolve("loans-only.txt");
        Storage storage = new Storage(dataFilePath.toString(), new Ui());
        ExpenseList originalList = new ExpenseList();
        originalList.addLoan(new Loan("John", 20.00, LocalDate.parse("2026-04-01")));

        storage.save(originalList);

        ExpenseList loadedList = new ExpenseList();
        storage.load(loadedList);

        assertEquals(0, loadedList.getSize());
        assertEquals(1, loadedList.getLoanCount());
    }

    @Test
    public void load_loanLineWrongFieldCount_skipsLine() throws IOException {
        Path dataFilePath = tempDir.resolve("malformed-loan.txt");
        Files.writeString(dataFilePath, "LOAN | only two fields\n");

        Storage storage = new Storage(dataFilePath.toString(), new Ui());
        ExpenseList loadedList = new ExpenseList();

        storage.load(loadedList);

        assertEquals(0, loadedList.getLoanCount());
    }

    @Test
    public void load_loanLineWithEmptyBorrower_skipsLine() throws IOException {
        Path dataFilePath = tempDir.resolve("empty-borrower.txt");
        Files.writeString(dataFilePath, "LOAN | 20.00 | 2026-04-01 |  | false\n");

        Storage storage = new Storage(dataFilePath.toString(), new Ui());
        ExpenseList loadedList = new ExpenseList();

        storage.load(loadedList);

        assertEquals(0, loadedList.getLoanCount());
    }

    @Test
    public void load_loanLineWithZeroAmount_skipsLine() throws IOException {
        Path dataFilePath = tempDir.resolve("zero-loan.txt");
        Files.writeString(dataFilePath, "LOAN | 0.0 | 2026-04-01 | John | false\n");

        Storage storage = new Storage(dataFilePath.toString(), new Ui());
        ExpenseList loadedList = new ExpenseList();

        storage.load(loadedList);

        assertEquals(0, loadedList.getLoanCount());
    }

    @Test
    public void load_loanLineWithInvalidAmount_skipsLine() throws IOException {
        Path dataFilePath = tempDir.resolve("invalid-loan-amount.txt");
        Files.writeString(dataFilePath, "LOAN | notanumber | 2026-04-01 | John | false\n");

        Storage storage = new Storage(dataFilePath.toString(), new Ui());
        ExpenseList loadedList = new ExpenseList();

        storage.load(loadedList);

        assertEquals(0, loadedList.getLoanCount());
    }

    @Test
    public void load_loanLineWithInvalidDate_skipsLine() throws IOException {
        Path dataFilePath = tempDir.resolve("invalid-loan-date.txt");
        Files.writeString(dataFilePath, "LOAN | 20.00 | notadate | John | false\n");

        Storage storage = new Storage(dataFilePath.toString(), new Ui());
        ExpenseList loadedList = new ExpenseList();

        storage.load(loadedList);

        assertEquals(0, loadedList.getLoanCount());
    }

    @Test
    public void constructor_emptyPath_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Storage("  ", new Ui()));
    }

    @Test
    public void load_emptyLines_skippedGracefully() throws IOException {
        Path dataFilePath = tempDir.resolve("empty-lines.txt");
        Files.writeString(dataFilePath, "\n\n5.50 | 2026-03-24 | Food | Coffee\n\n");

        Storage storage = new Storage(dataFilePath.toString(), new Ui());
        ExpenseList loadedList = new ExpenseList();

        storage.load(loadedList);

        assertEquals(1, loadedList.getSize());
    }

    @Test
    public void load_budgetMalformedValue_skipsLine() throws IOException {
        Path dataFilePath = tempDir.resolve("bad-budget.txt");
        Files.writeString(dataFilePath,
                "BUDGET | 2026-03 | notanumber\n"
                        + "5.00 | 2026-03-24 | Food | Lunch\n");

        Storage storage = new Storage(dataFilePath.toString(), new Ui());
        ExpenseList loadedList = new ExpenseList();

        storage.load(loadedList);

        assertEquals(1, loadedList.getSize());
        assertFalse(loadedList.hasBudget(TEST_MONTH), "Malformed budget should not be set");
    }

    @Test
    public void load_v1FormatEmptyDescription_skipsLine() throws IOException {
        Path dataFilePath = tempDir.resolve("v1-empty-desc.txt");
        Files.writeString(dataFilePath, "5.50 | \n");

        Storage storage = new Storage(dataFilePath.toString(), new Ui());
        ExpenseList loadedList = new ExpenseList();

        storage.load(loadedList);

        assertEquals(0, loadedList.getSize(), "v1 line with empty description should be skipped");
    }

    @Test
    public void load_v2FormatEmptyDescription_skipsLine() throws IOException {
        Path dataFilePath = tempDir.resolve("v2-empty-desc.txt");
        Files.writeString(dataFilePath, "5.50 | 2026-03-24 | Food | \n");

        Storage storage = new Storage(dataFilePath.toString(), new Ui());
        ExpenseList loadedList = new ExpenseList();

        storage.load(loadedList);

        assertEquals(0, loadedList.getSize(), "v2 line with empty description should be skipped");
    }

    @Test
    public void load_v2FormatInvalidDate_skipsLine() throws IOException {
        Path dataFilePath = tempDir.resolve("v2-bad-date.txt");
        Files.writeString(dataFilePath, "5.50 | notadate | Food | Coffee\n");

        Storage storage = new Storage(dataFilePath.toString(), new Ui());
        ExpenseList loadedList = new ExpenseList();

        storage.load(loadedList);

        assertEquals(0, loadedList.getSize(), "v2 line with invalid date should be skipped");
    }

    @Test
    public void load_threeFieldLine_skipsAsMalformed() throws IOException {
        Path dataFilePath = tempDir.resolve("three-fields.txt");
        Files.writeString(dataFilePath, "5.50 | 2026-03-24 | Food\n");

        Storage storage = new Storage(dataFilePath.toString(), new Ui());
        ExpenseList loadedList = new ExpenseList();

        storage.load(loadedList);

        assertEquals(0, loadedList.getSize(), "3-field line should be skipped as malformed");
    }

    @Test
    public void saveAndLoad_loansSavedAlongsideExpenses_bothReloaded() {
        Path dataFilePath = tempDir.resolve("mixed.txt");
        Storage storage = new Storage(dataFilePath.toString(), new Ui());
        ExpenseList originalList = new ExpenseList();

        originalList.addExpense(new Expense("Coffee", 5.50, "Food", LocalDate.parse("2026-03-24")));
        originalList.addLoan(new Loan("Alice", 30.00, LocalDate.parse("2026-04-01")));
        originalList.setBudget(TEST_MONTH, 200.00);

        storage.save(originalList);

        ExpenseList loadedList = new ExpenseList();
        storage.load(loadedList);

        assertEquals(1, loadedList.getSize(), "Expense should be reloaded");
        assertEquals(1, loadedList.getLoanCount(), "Loan should be reloaded");
        assertTrue(loadedList.hasBudget(TEST_MONTH), "Budget should be reloaded");
        assertEquals(200.00, loadedList.getBudget(TEST_MONTH), 0.0001);
    }

    @Test
    public void load_loanLineNegativeAmount_skipsLine() throws IOException {
        Path dataFilePath = tempDir.resolve("negative-loan.txt");
        Files.writeString(dataFilePath, "LOAN | -10.00 | 2026-04-01 | John | false\n");

        Storage storage = new Storage(dataFilePath.toString(), new Ui());
        ExpenseList loadedList = new ExpenseList();

        storage.load(loadedList);

        assertEquals(0, loadedList.getLoanCount());
    }

    @Test
    public void saveAndLoad_partiallyRepaidLoan_roundTripMaintainsData() {
        Path dataFilePath = tempDir.resolve("partial-loan.txt");
        Storage storage = new Storage(dataFilePath.toString(), new Ui());

        ExpenseList originalList = new ExpenseList();
        Loan loan = new Loan("John", 200.00, LocalDate.parse("2026-04-01"));
        loan.repay(75.00);
        originalList.addLoan(loan);
        storage.save(originalList);

        ExpenseList loadedList = new ExpenseList();
        storage.load(loadedList);

        assertEquals(1, loadedList.getLoanCount());
        Loan loaded = loadedList.getLoan(0);
        assertFalse(loaded.isRepaid());
        assertEquals(75.00, loaded.getAmountRepaid(), 0.0001);
        assertEquals(125.00, loaded.getOutstandingAmount(), 0.0001);
    }

    @Test
    public void load_oldFormatLoanLine_loadsWithZeroAmountRepaid() throws IOException {
        Path dataFilePath = tempDir.resolve("old-format-loan.txt");
        Files.writeString(dataFilePath, "LOAN | 100.00 | 2026-04-01 | Alice | false\n");

        Storage storage = new Storage(dataFilePath.toString(), new Ui());
        ExpenseList loadedList = new ExpenseList();
        storage.load(loadedList);

        assertEquals(1, loadedList.getLoanCount());
        Loan loaded = loadedList.getLoan(0);
        assertFalse(loaded.isRepaid());
        assertEquals(0, loaded.getAmountRepaid(), 0.0001);
        assertEquals(100.00, loaded.getOutstandingAmount(), 0.0001);
    }

    @Test
    public void load_newFormatLoanLine_loadsAmountRepaid() throws IOException {
        Path dataFilePath = tempDir.resolve("new-format-loan.txt");
        Files.writeString(dataFilePath, "LOAN | 200.00 | 2026-04-01 | Bob | false | 50.0\n");

        Storage storage = new Storage(dataFilePath.toString(), new Ui());
        ExpenseList loadedList = new ExpenseList();
        storage.load(loadedList);

        assertEquals(1, loadedList.getLoanCount());
        Loan loaded = loadedList.getLoan(0);
        assertFalse(loaded.isRepaid());
        assertEquals(50.00, loaded.getAmountRepaid(), 0.0001);
        assertEquals(150.00, loaded.getOutstandingAmount(), 0.0001);
    }
}
