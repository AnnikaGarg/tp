package seedu.duke;
import java.time.LocalDate;
/**
 * Represents a single financial expense tracked by the user.
 * Contains the description, monetary amount, category, and date of the expense.
 */
public class Expense {
    public static final String DEFAULT_CATEGORY = "Others";
    private final String description;
    private final double amount;
    private final String category;
    private final LocalDate date;
    /**
     * Constructs an Expense with all four fields supplied explicitly.
     * A null or blank category defaults to "Others".
     * A null date defaults to today's date.
     *
     * @param description Human-readable label for the expense.
     * @param amount      Non-negative, finite monetary cost.
     * @param category    Spending category. Null or blank defaults to "Others".
     * @param date        Date of the expense. Null defaults to today.
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
        this.category = (category == null || category.trim().isEmpty())
                ? DEFAULT_CATEGORY
                : category.trim();
        this.date = (date == null) ? LocalDate.now() : date;
    }
    /**
     * Constructs an Expense using the default category "Others" and today's date.
     *
     * @param description Human-readable label for the expense.
     * @param amount      Non-negative, finite monetary cost.
     */
    public Expense(String description, double amount) {
        this(description, amount, DEFAULT_CATEGORY, LocalDate.now());
    }
    /**
     * Returns the description of the expense.
     *
     * @return The description string.
     */
    public String getDescription() {
        return description;
    }
    /**
     * Returns the monetary amount of the expense.
     *
     * @return The cost of the expense.
     */
    public double getAmount() {
        return amount;
    }
    /**
     * Returns the spending category of the expense.
     *
     * @return The category string. Never null; defaults to "Others".
     */
    public String getCategory() {
        return category;
    }
    /**
     * Returns the date of the expense.
     *
     * @return The date of the expense. Never null; defaults to today.
     */
    public LocalDate getDate() {
        return date;
    }
    /**
     * Returns a formatted string showing the description, amount, category, and date.
     *
     * @return A string in the form: DESCRIPTION ($AMOUNT) [CATEGORY] [DATE].
     */
    @Override
    public String toString() {
        return description
                + " ($" + String.format("%.2f", amount) + ")"
                + " [" + category + "]"
                + " [" + date + "]";
    }
}
