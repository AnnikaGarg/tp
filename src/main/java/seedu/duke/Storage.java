package seedu.duke;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles reading and writing expense data to a text file
 * so that data persists between sessions.
 */
public class Storage {
    private static final String SEPARATOR = " | ";
    private static final Logger logger = Logger.getLogger(Storage.class.getName());
    private final String filePath;
    private final Ui ui;

    /**
     * Constructs a Storage object with the given file path and Ui instance.
     *
     * @param filePath The path to the data file.
     * @param ui The Ui object used to display messages and warnings.
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
    }

    /**
     * Loads expenses from the data file into the given ExpenseList.
     * If the file does not exist, an empty list is returned.
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
     * Saves all expenses from the given ExpenseList to the data file.
     * Creates the parent directory if it does not exist.
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
            boolean isCreated = parentDir.mkdirs();
            if (!isCreated) {
                ui.showSaveWarning();
                logger.warning("Could not create data directory: " + parentDir.getAbsolutePath());
                return;
            }
        }
        try (FileWriter writer = new FileWriter(file)) {
            for (int i = 0; i < expenseList.getSize(); i++) {
                Expense expense = expenseList.getExpense(i);
                writer.write(expense.getAmount() + SEPARATOR + expense.getDescription()
                        + System.lineSeparator());
            }
        } catch (IOException e) {
            ui.showSaveWarning();
            logger.log(Level.WARNING, "Could not save expense data to file: " + filePath, e);
        }
    }

    /**
     * Parses a single line from the data file into an Expense object.
     *
     * @param line The line to parse, in the format "AMOUNT | DESCRIPTION".
     * @return The parsed Expense, or null if the line is malformed.
     */
    private Expense parseLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        String[] parts = line.split("\\|", 2);
        if (parts.length < 2) {
            ui.showMalformedLineWarning(line);
            logger.warning("Malformed storage line skipped: " + line);
            return null;
        }
        try {
            double amount = Double.parseDouble(parts[0].trim());
            String description = parts[1].trim();
            if (description.isEmpty()) {
                ui.showMalformedLineWarning(line);
                logger.warning("Storage line with empty description skipped: " + line);
                return null;
            }
            return new Expense(description, amount);
        } catch (IllegalArgumentException e) {
            ui.showInvalidAmountLineWarning(line);
            logger.log(Level.WARNING, "Storage line with invalid data skipped: " + line, e);
            return null;
        }
    }
}
