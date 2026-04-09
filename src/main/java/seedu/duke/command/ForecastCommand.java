package seedu.duke.command;

import seedu.duke.model.ExpenseList;
import seedu.duke.ui.Ui;

import java.time.LocalDate;
import java.time.YearMonth;

/**
 * Handles the logic for forecasting end-of-month expenses based on current spending habits.
 */
public class ForecastCommand extends Command {

    public ForecastCommand(Ui ui) {
        super(ui);
    }

    @Override
    public void execute(ExpenseList expenseList) {
        LocalDate today = LocalDate.now();
        YearMonth currentMonth = YearMonth.from(today);

        int currentDay = today.getDayOfMonth();
        int daysInMonth = currentMonth.lengthOfMonth();
        double spentSoFar = expenseList.getTotalAmountForMonth(currentMonth);

        // Prevent division by zero if they run this on the very first moment of the month
        int effectiveDays = (currentDay == 0) ? 1 : currentDay;

        double dailyBurnRate = spentSoFar / effectiveDays;
        double projectedTotal = dailyBurnRate * daysInMonth;

        // Fetch budget info
        boolean hasBudget = expenseList.hasBudget(currentMonth);
        double currentBudget = hasBudget ? expenseList.getBudget(currentMonth) : 0.0;

        // Pass the calculated metrics to the UI for display
        ui.showForecast(currentMonth, spentSoFar, currentDay, dailyBurnRate, projectedTotal, hasBudget, currentBudget);
    }

    @Override
    public boolean shouldPersist() {
        // Forecasting is a read-only action. We don't need to save anything to the text file!
        return false;
    }
}