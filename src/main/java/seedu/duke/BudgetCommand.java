package seedu.duke;

import seedu.duke.ui.Ui;

import java.time.YearMonth;

/**
 * Handles the logic for setting or displaying a monthly spending budget.
 */
public class BudgetCommand extends Command {
    private final YearMonth budgetMonth;
    private final Double budgetAmount;

    /**
     * Constructs a BudgetCommand with the specified Ui, target month, and optional budget amount.
     *
     * @param ui The Ui object used to display user-facing messages.
     * @param budgetMonth The month whose budget is being viewed or set.
     * @param budgetAmount The budget amount to set, or null if displaying budget details only.
     */
    public BudgetCommand(Ui ui, YearMonth budgetMonth, Double budgetAmount) {
        super(ui);
        assert ui != null : "Ui must not be null";
        assert budgetMonth != null : "Budget month must not be null";
        this.budgetMonth = budgetMonth;
        this.budgetAmount = budgetAmount;
    }

    /**
     * Executes the budget command.
     * If an amount is provided, sets or overwrites the budget for the month.
     * If no amount is provided, displays budget details for the month.
     *
     * @param expenseList The list whose monthly budget will be updated or queried.
     */
    @Override
    public void execute(ExpenseList expenseList) {
        assert expenseList != null : "ExpenseList must not be null";

        if (budgetAmount == null) {
            if (!expenseList.hasBudget(budgetMonth)) {
                ui.showBudgetNotSet(budgetMonth);
                return;
            }

            double budget = expenseList.getBudget(budgetMonth);
            double totalSpent = expenseList.getTotalAmountForMonth(budgetMonth);
            double remaining = expenseList.getRemainingBudget(budgetMonth);

            ui.showBudgetDetails(budgetMonth, budget, totalSpent, remaining);
            return;
        }

        if (budgetAmount <= 0) {
            ui.showInvalidBudget();
            return;
        }

        boolean isOverwrite = expenseList.hasBudget(budgetMonth);
        double previousBudget = isOverwrite ? expenseList.getBudget(budgetMonth) : 0.0;

        expenseList.setBudget(budgetMonth, budgetAmount);

        if (isOverwrite) {
            ui.showBudgetUpdated(budgetMonth, previousBudget, budgetAmount);
        } else {
            ui.showBudgetSet(budgetMonth, budgetAmount);
        }

        if (expenseList.isOverBudget(budgetMonth)) {
            ui.showBudgetExceededWarning(
                    budgetMonth,
                    expenseList.getBudget(budgetMonth),
                    expenseList.getTotalAmountForMonth(budgetMonth)
            );
        }
    }

    /**
     * Returns true only if the budget is being modified.
     *
     * @return true if setting a budget, false if only viewing.
     */
    @Override
    public boolean shouldPersist() {
        return budgetAmount != null;
    }
}
