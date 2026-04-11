package seedu.duke.storage;
import seedu.duke.model.Expense;
import seedu.duke.model.ExpenseList;
import seedu.duke.model.Loan;
import seedu.duke.ui.Ui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.YearMonth;
import java.util.Map;
import java.util.TreeMap;
/**
 * Handles reading and writing expense data to a text file
 * so that data persists between sessions.
 * Expense lines use the format: AMOUNT | DATE | CATEGORY | DESCRIPTION.
 * Budget lines use the format: BUDGET | YYYY-MM | AMOUNT.
 * Loan lines use the format: LOAN | AMOUNT | DATE | BORROWER | REPAID | AMOUNT_REPAID.
 */
public class Storage {
    private static final String SEPARATOR = " | ";
    private static final String SPLIT_REGEX = "\\s*\\|\\s*";
    /** Number of fields expected on each saved expense line. */
    private static final int FIELD_COUNT = 4;
    private static final int IDX_AMOUNT      = 0;
    private static final int IDX_DATE        = 1;
    private static final int IDX_CATEGORY    = 2;
    private static final int IDX_DESCRIPTION = 3;
    /** Number of fields expected on each saved loan line, excluding the LOAN prefix. */
    private static final int LOAN_FIELD_COUNT  = 5;
    private static final int LOAN_FIELD_COUNT_OLD = 4;
    private static final int LOAN_IDX_AMOUNT   = 0;
    private static final int LOAN_IDX_DATE     = 1;
    private static final int LOAN_IDX_BORROWER = 2;
    private static final int LOAN_IDX_REPAID   = 3;
    private static final int LOAN_IDX_AMOUNT_REPAID = 4;
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter YEAR_MONTH_FORMAT =
            DateTimeFormatter.ofPattern("uuuu-MM").withResolverStyle(ResolverStyle.STRICT);
    private static final Logger logger = Logger.getLogger(Storage.class.getName());
    private final String filePath;
    private final Ui ui;
    /**
     * Constructs a Storage object with the given file path and Ui instance.
     *
     * @param filePath The path to the data file.
     * @param ui       The Ui object used to display messages and warnings.
     */
    public Storage(String filePath, Ui ui) {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path must not be empty");
        }
        if (ui == null) {
            throw new IllegalArgumentException("Ui must not be null");
        }
        this.filePath = filePath;
        this.ui = ui;
        assert !this.filePath.trim().isEmpty() : "Storage file path should be initialised";
        assert this.ui != null : "Storage UI should be initialised";
    }
    /**
     * Loads expenses and loans from the data file into the given ExpenseList.
     * Malformed lines are skipped with a warning; a missing file is silently ignored.
     *
     * @param expenseList The ExpenseList to populate with saved data.
     */
    public void load(ExpenseList expenseList) {
        if (expenseList == null) {
            throw new IllegalArgumentException("ExpenseList must not be null");
        }
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                if (line.startsWith("BUDGET" + SEPARATOR)) {
                    parseBudgetLine(line, expenseList);
                    continue;
                }
                if (line.startsWith("LOAN" + SEPARATOR)) {
                    Loan loan = parseLoanLine(line);
                    if (loan != null) {
                        expenseList.addLoan(loan);
                    }
                    continue;
                }
                Expense expense = parseLine(line);
                if (expense != null) {
                    expenseList.addExpense(expense);
                }
            }
        } catch (IOException e) {
            ui.showLoadWarning();
            logger.log(Level.WARNING, "Could not load expense data from file: " + filePath, e);
        }
    }
    /**
     * Saves all expenses, loans, and budget data from the given ExpenseList to the file.
     * Creates the parent directory if it does not exist.
     * Saves using the v2.0 format: AMOUNT | DATE | CATEGORY | DESCRIPTION,
     * with an optional budget line.
     *
     * @param expenseList The ExpenseList whose data should be saved.
     */
    public void save(ExpenseList expenseList) {
        if (expenseList == null) {
            throw new IllegalArgumentException("ExpenseList must not be null");
        }
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            if (!created) {
                ui.showSaveWarning();
                logger.warning("Could not create data directory: " + parentDir.getAbsolutePath());
                return;
            }
        }
        try (FileWriter writer = new FileWriter(file)) {
            for (Map.Entry<YearMonth, Double> entry : new TreeMap<>(expenseList.getMonthlyBudgets()).entrySet()) {
                writer.write(
                        "BUDGET"
                                + SEPARATOR + entry.getKey().format(YEAR_MONTH_FORMAT)
                                + SEPARATOR + entry.getValue()
                                + System.lineSeparator()
                );
            }
            for (int i = 0; i < expenseList.getSize(); i++) {
                Expense expense = expenseList.getExpense(i);
                assert expense != null : "Stored expenses should never contain null values";
                writer.write(
                        expense.getAmount()
                                + SEPARATOR + expense.getDate()
                                + SEPARATOR + expense.getCategory()
                                + SEPARATOR + expense.getDescription()
                                + System.lineSeparator()
                );
            }
            for (int i = 0; i < expenseList.getLoanCount(); i++) {
                Loan loan = expenseList.getLoan(i);
                assert loan != null : "Stored loans should never contain null values";
                writer.write(
                        "LOAN"
                                + SEPARATOR + loan.getAmount()
                                + SEPARATOR + loan.getDate()
                                + SEPARATOR + loan.getBorrowerName()
                                + SEPARATOR + loan.isRepaid()
                                + SEPARATOR + loan.getAmountRepaid()
                                + System.lineSeparator()
                );
            }
        } catch (IOException e) {
            ui.showSaveWarning();
            logger.log(Level.WARNING, "Could not save expense data to file: " + filePath, e);
        }
    }
    /**
     * Parses a single line from the data file into an Expense object.
     * Returns null and displays a warning if the line is malformed or contains invalid values.
     * Handles backwards compatibility for old v1.0 saves.
     *
     * @param line The line to parse.
     * @return The parsed Expense, or null if the line is malformed.
     */
    private Expense parseLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        String[] parts = line.split(SPLIT_REGEX, FIELD_COUNT);
        try {
            if (parts.length == 2) {
                double amount = Double.parseDouble(parts[0].trim());
                String description = parts[1].trim();
                if (description.isEmpty()) {
                    ui.showMalformedLineWarning(line);
                    logger.warning("Storage line with empty description skipped: " + line);
                    return null;
                }
                return new Expense(description, amount, null, null);
            } else if (parts.length == FIELD_COUNT) {
                double amount = Double.parseDouble(parts[IDX_AMOUNT].trim());
                LocalDate date = LocalDate.parse(parts[IDX_DATE].trim(), DATE_FORMAT);
                String category = parts[IDX_CATEGORY].trim();
                String description = parts[IDX_DESCRIPTION].trim();
                if (description.isEmpty()) {
                    ui.showMalformedLineWarning(line);
                    logger.warning("Storage line with empty description skipped: " + line);
                    return null;
                }
                assert !description.isEmpty() : "Parsed description should not be empty after validation";
                return new Expense(description, amount, category, date);
            } else {
                ui.showMalformedLineWarning(line);
                logger.warning("Malformed storage line skipped (incorrect segment count): " + line);
                return null;
            }
        } catch (IllegalArgumentException | DateTimeParseException e) {
            ui.showInvalidAmountLineWarning(line);
            logger.log(Level.WARNING, "Storage line rejected by Expense constructor: " + line, e);
            return null;
        }
    }
    /**
     * Parses a loan line from the data file and returns a Loan object.
     * Returns null and displays a warning if the line is malformed or contains invalid values.
     *
     * @param line The full line including the LOAN prefix.
     * @return The parsed Loan, or null if the line is malformed.
     */
    private Loan parseLoanLine(String line) {
        String payload = line.substring(("LOAN" + SEPARATOR).length()).trim();
        String[] parts = payload.split(SPLIT_REGEX, LOAN_FIELD_COUNT);
        if (parts.length != LOAN_FIELD_COUNT && parts.length != LOAN_FIELD_COUNT_OLD) {
            ui.showMalformedLineWarning(line);
            logger.warning("Malformed loan line (wrong field count): " + line);
            return null;
        }
        try {
            double amount = Double.parseDouble(parts[LOAN_IDX_AMOUNT].trim());
            LocalDate date = LocalDate.parse(parts[LOAN_IDX_DATE].trim(), DATE_FORMAT);
            String borrower = parts[LOAN_IDX_BORROWER].trim();
            boolean isRepaid = Boolean.parseBoolean(parts[LOAN_IDX_REPAID].trim());
            if (borrower.isEmpty()) {
                ui.showMalformedLineWarning(line);
                logger.warning("Loan line with empty borrower skipped: " + line);
                return null;
            }
            if (amount <= 0) {
                ui.showInvalidAmountLineWarning(line);
                logger.warning("Loan line with non-positive amount skipped: " + line);
                return null;
            }
            Loan loan = new Loan(borrower, amount, date);
            if (parts.length == LOAN_FIELD_COUNT) {
                double amountRepaid = Double.parseDouble(parts[LOAN_IDX_AMOUNT_REPAID].trim());
                loan.setAmountRepaid(amountRepaid);
            }
            if (isRepaid) {
                loan.markRepaid();
            }
            return loan;
        } catch (DateTimeParseException | IllegalArgumentException e) {
            ui.showInvalidAmountLineWarning(line);
            logger.log(Level.WARNING, "Loan line rejected: " + line, e);
            return null;
        }
    }

    /**
     * Parses a budget line from the data file and stores it in the given ExpenseList.
     * Supports both the old format:
     * BUDGET | AMOUNT
     * and the new format:
     * BUDGET | YYYY-MM | AMOUNT
     *
     * @param line The full budget line including the BUDGET prefix.
     * @param expenseList The ExpenseList to update.
     */
    private void parseBudgetLine(String line, ExpenseList expenseList) {
        String payload = line.substring(("BUDGET" + SEPARATOR).length()).trim();
        String[] parts = payload.split(SPLIT_REGEX);

        try {
            if (parts.length == 1) {
                double budget = Double.parseDouble(parts[0].trim());
                if (budget > 0) {
                    expenseList.setBudget(YearMonth.now(), budget);
                } else {
                    ui.showMalformedLineWarning(line);
                }
                return;
            }

            if (parts.length == 2) {
                YearMonth month = YearMonth.parse(parts[0].trim(), YEAR_MONTH_FORMAT);
                double budget = Double.parseDouble(parts[1].trim());

                if (budget > 0) {
                    expenseList.setBudget(month, budget);
                } else {
                    ui.showMalformedLineWarning(line);
                }
                return;
            }

            ui.showMalformedLineWarning(line);
            logger.warning("Malformed budget line skipped: " + line);
        } catch (DateTimeParseException | IllegalArgumentException e) {
            ui.showMalformedLineWarning(line);
            logger.log(Level.WARNING, "Budget line rejected: " + line, e);
        }
    }
}
