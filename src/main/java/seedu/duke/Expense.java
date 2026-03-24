package seedu.duke;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a single financial expense tracked by the user.
 * Contains the description, monetary amount, category, and date of the expense.
 */
public class Expense {
    private final String description;
    private final double amount;
    private final String category;
    private final LocalDate date;

    /**
     * Constructs an Expense object with the specified details.
     * If category is null/empty, defaults to "Others".
     * If date is null, defaults to today's date.
     *
     * @param description The details or name of the expense.
     * @param amount The monetary cost of the expense.
     * @param category The category of the expense.
     * @param date The date the expense occurred.
     */
    public Expense(String description, double amount, String category, LocalDate date) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description must not be empty");
        }
        if (Double.isNaN(amount) || Double.isInfinite(amount) || amount < 0) {
            throw new IllegalArgumentException("Amount must be a non-negative finite value");
        }
        assert amount >= 0 : "Expense amount cannot be negative";

        this.description = description.trim();
        this.amount = amount;

        // Default category logic
        if (category == null || category.trim().isEmpty()) {
            this.category = "Others";
        } else {
            this.category = category.trim();
        }
        assert this.category != null && !this.category.isEmpty() : "Category initialization failed";

        // Default date logic
        if (date == null) {
            this.date = LocalDate.now();
        } else {
            this.date = date;
        }
        assert this.date != null : "Date initialization failed";
    }

    /**
     * Retrieves the description of the expense.
     *
     * @return The description string.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Retrieves the monetary amount of the expense.
     *
     * @return The cost of the expense.
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Retrieves the category of the expense.
     *
     * @return The category string.
     */
    public String getCategory() {
        return category;
    }

    /**
     * Retrieves the date of the expense.
     *
     * @return The LocalDate object representing when the expense occurred.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns a string representation of the expense, formatting the amount to two decimal places
     * and the date to a readable "MMM dd yyyy" format.
     *
     * @return A formatted string showing the description, cost, category, and date.
     */
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy");
        return String.format("%s ($%.2f) [Cat: %s] [Date: %s]",
                description, amount, category, date.format(formatter));
    }
}