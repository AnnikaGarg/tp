package seedu.duke;

/**
 * Handles the logic for listing all tracked expenses.
 */
public class ListCommand extends Command {

    @Override
    public void execute(ExpenseList expenseList) {
        // Defensive coding: Ensure expenseList is never null when this executes
        assert expenseList != null : "ExpenseList should not be null in ListCommand";

        if (expenseList.getSize() == 0) {
            System.out.println("Your expense list is currently empty.");
            return;
        }

        System.out.println("Here are your tracked expenses:");
        for (int i = 0; i < expenseList.getSize(); i++) {
            System.out.println((i + 1) + ". " + expenseList.getExpense(i).toString());
        }
    }
}
