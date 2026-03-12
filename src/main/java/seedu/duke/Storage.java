package seedu.duke;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Handles reading and writing expense data to a text file
 * so that data persists between sessions.
 */
public class Storage {
    private static final String SEPARATOR = " | ";
    private final String filePath;

    /**
     * Constructs a Storage object with the given file path.
     *
     * @param filePath The path to the data file.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads expenses from the data file into the given ExpenseList.
     * If the file does not exist, an empty list is returned.
     *
     * @param expenseList The ExpenseList to populate with saved data.
     */
    public void load(ExpenseList expenseList) {
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
            System.out.println("Warning: Could not load data file. Starting with empty list.");
        }
    }

    /**
     * Saves all expenses from the given ExpenseList to the data file.
     * Creates the parent directory if it does not exist.
     *
     * @param expenseList The ExpenseList whose data should be saved.
     */
    public void save(ExpenseList expenseList) {
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        try (FileWriter writer = new FileWriter(file)) {
            for (int i = 0; i < expenseList.getSize(); i++) {
                Expense expense = expenseList.getExpense(i);
                writer.write(expense.getAmount() + SEPARATOR + expense.getDescription()
                        + System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("Warning: Could not save data to file.");
        }
    }

    /**
     * Parses a single line from the data file into an Expense object.
     *
     * @param line The line to parse, in the format "AMOUNT | DESCRIPTION".
     * @return The parsed Expense, or null if the line is malformed.
     */
    private Expense parseLine(String line) {
        String[] parts = line.split("\\|", 2);
        if (parts.length < 2) {
            System.out.println("Warning: Skipping malformed line: " + line);
            return null;
        }
        try {
            double amount = Double.parseDouble(parts[0].trim());
            String description = parts[1].trim();
            return new Expense(description, amount);
        } catch (NumberFormatException e) {
            System.out.println("Warning: Skipping line with invalid amount: " + line);
            return null;
        }
    }
}
