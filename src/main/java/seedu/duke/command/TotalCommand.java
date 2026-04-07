package seedu.duke.command;

import seedu.duke.model.Expense;
import seedu.duke.model.ExpenseList;
import seedu.duke.ui.Ui;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles the logic for calculating and displaying the total of all tracked expenses.
 */
public class TotalCommand extends Command {

    private static final Logger logger = Logger.getLogger(TotalCommand.class.getName());

    /**
     * Constructs a TotalCommand with the specified Ui instance.
     *
     * @param ui The Ui object used to display messages.
     */
    public TotalCommand(Ui ui) {
        super(ui);
        assert ui != null : "Ui instance cannot be null in TotalCommand";
    }

    /**
     * Executes the total command by summing up all expense amounts and displaying the result.
     *
     * @param expenseList The list of expenses to calculate the total from.
     */
    @Override
    public void execute(ExpenseList expenseList) {
        assert expenseList != null : "ExpenseList cannot be null when executing TotalCommand";

        // Changed to FINE so it doesn't print to the user's console
        logger.log(Level.FINE, "Starting calculation for total expenses.");

        int expenseCount = expenseList.getSize();
        if (expenseCount == 0) {
            logger.log(Level.FINE, "Expense list is empty. Total is $0.00.");
            ui.showTotal(0.0, 0);
            return;
        }

        double total = 0.0;
        for (int i = 0; i < expenseCount; i++) {
            Expense currentExpense = expenseList.getExpense(i);

            // Assertions to ensure our data integrity during the loop
            assert currentExpense != null : "Expense at index " + i + " is null";
            assert currentExpense.getAmount() >= 0 : "Expense amount should not be negative";

            total += currentExpense.getAmount();
        }

        // Final sanity check before displaying
        assert total >= 0 : "Calculated total should never be negative";
        logger.log(Level.FINE, "Successfully calculated total: $" + total + " across " + expenseCount + " expenses.");

        ui.showTotal(total, expenseCount);
    }
}
