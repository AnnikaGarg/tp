package seedu.duke.parser;

import seedu.duke.command.AddCommand;
import seedu.duke.command.BudgetCommand;
import seedu.duke.command.Command;
import seedu.duke.command.ClearCommand;
import seedu.duke.command.DeleteCommand;
import seedu.duke.command.EditCommand;
import seedu.duke.command.ExitCommand;
import seedu.duke.command.FindCommand;
import seedu.duke.command.HelpCommand;
import seedu.duke.command.LendCommand;
import seedu.duke.command.ListCommand;
import seedu.duke.command.LoansCommand;
import seedu.duke.command.RepayCommand;
import seedu.duke.command.SortCommand;
import seedu.duke.command.StatisticsCommand;
import seedu.duke.command.TotalCommand;
import seedu.duke.ui.Ui;
import seedu.duke.command.ForecastCommand;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.YearMonth;

/**
 * Parses raw user input strings into executable Command objects.
 * Both add and edit commands use /c for category and /da for date.
 * The edit command additionally uses /a for amount and /de for description.
 * The lend command uses /da for date.
 */
public class Parser {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter YEAR_MONTH_FORMAT =
            DateTimeFormatter.ofPattern("uuuu-MM").withResolverStyle(ResolverStyle.STRICT);

    /**
     * Parses a full command string entered by the user and returns the matching Command.
     * Returns null when the input is empty, null, or does not match any known command.
     *
     * @param fullCommand The raw string entered by the user.
     * @param ui          The Ui instance used to display error or usage messages.
     * @return The corresponding Command object, or null if the input is invalid.
     * @throws IllegalArgumentException If ui is null.
     */
    public static Command parse(String fullCommand, Ui ui) {
        if (ui == null) {
            throw new IllegalArgumentException("Ui must not be null");
        }
        if (fullCommand == null) {
            return null;
        }

        String trimmedCommand = fullCommand.trim();
        if (trimmedCommand.isEmpty()) {
            return null;
        }

        String[] parts = trimmedCommand.split("\\s+", 2);
        String commandWord = parts[0].toLowerCase();
        String arguments = parts.length > 1 ? parts[1].trim() : "";
        assert !commandWord.isEmpty() : "Command word should not be empty after trimming";

        switch (commandWord) {
        case "list":
            return parseListCommand(arguments, ui);

        case "help":
            if (arguments.isEmpty()) {
                return new HelpCommand(ui);
            }
            return parseHelpCommand(arguments, ui);

        case "exit":
            if (!arguments.trim().isEmpty()) {
                ui.showStrictCommandUsage("exit");
                return null;
            }
            return new ExitCommand(ui);

        case "add":
            return parseAddCommand(arguments, ui);

        case "delete":
            return parseDeleteCommand(arguments, ui);

        case "clear":
            if (!arguments.isEmpty()) {
                ui.showUnknownCommand();
                return null;
            }
            return new ClearCommand(ui);

        case "total":
            if (!arguments.isEmpty()) {
                ui.showUnknownCommand();
                return null;
            }
            return new TotalCommand(ui);

        case "edit":
            return parseEditCommand(arguments, ui);

        case "find":
            return parseFindCommand(arguments, ui);

        case "budget":
            return parseBudgetCommand(arguments, ui);

        case "sort":
            String sortArg = arguments.toLowerCase();
            if (!sortArg.equals("category") && !sortArg.equals("date") && !sortArg.equals("amount")) {
                ui.showSortUsage();
                return null;
            }
            return new SortCommand(ui, sortArg);

        case "stats":
            if (arguments.isEmpty()) {
                return new StatisticsCommand(ui);
            } else {
                try {
                    int year = Integer.parseInt(arguments);
                    return new StatisticsCommand(ui, year);
                } catch (NumberFormatException e) {
                    ui.showUnknownCommand();
                    return null;
                }
            }

        case "lend":
            return parseLendCommand(arguments, ui);

        case "loans":
            return parseLoansCommand(arguments, ui);

        case "repay":
            return parseRepayCommand(arguments, ui);

        case "forecast":
            // Strict validation: forecast should not have any trailing arguments
            if (!arguments.trim().isEmpty()) {
                ui.showUnknownCommand(); // Or your specific usage error message
                return null;
            }
            return new ForecastCommand(ui);

        default:
            ui.showUnknownCommand();
            return null;
        }
    }

    /**
     * Parses the argument string for the add command and returns an AddCommand.
     * Flags /c and /da are extracted first; the remaining text becomes the description.
     *
     * @param arguments The portion of user input after the add keyword.
     * @param ui        The Ui instance used to display error or usage messages.
     * @return A fully constructed AddCommand, or null if the input is invalid.
     */
    private static Command parseAddCommand(String arguments, Ui ui) {
        if (arguments.isEmpty()) {
            ui.showAddUsage();
            return null;
        }

        if (arguments.contains("|")) {
            ui.showInvalidCharacterWarning();
            return null;
        }

        String[] firstSplit = arguments.split("\\s+", 2);
        if (firstSplit.length < 2) {
            ui.showAddUsage();
            return null;
        }

        double amount;
        try {
            amount = Double.parseDouble(firstSplit[0]);

            if (amount == 0) {
                ui.showZeroAmountWarning();
                return null;
            }

            if (amount >= 1_000_000_000_000.00) {
                ui.showAmountTooLargeWarning();
                return null;
            }

            if (Double.isNaN(amount) || Double.isInfinite(amount) || amount < 0) {
                ui.showInvalidAmount();
                return null;
            }
        } catch (NumberFormatException e) {
            ui.showInvalidAmount();
            return null;
        }

        String workingStr = firstSplit[1].trim();

        int firstC = workingStr.indexOf("/c");
        int lastC = workingStr.lastIndexOf("/c");
        if (firstC != -1 && firstC != lastC) {
            ui.showDuplicateFlagWarning("/c");
            return null;
        }

        int firstDa = workingStr.indexOf("/da");
        int lastDa = workingStr.lastIndexOf("/da");
        if (firstDa != -1 && firstDa != lastDa) {
            ui.showDuplicateFlagWarning("/da");
            return null;
        }

        String category = null;
        if (workingStr.contains("/c")) {
            int flagIdx = workingStr.indexOf("/c");
            String after = workingStr.substring(flagIdx + 2).trim();
            int nextSlash = after.indexOf('/');
            String value = (nextSlash >= 0) ? after.substring(0, nextSlash).trim() : after.trim();

            if (value.isEmpty()) {
                ui.showAddUsage();
                return null;
            }
            category = value;
            String before = workingStr.substring(0, flagIdx).trim();
            String remaining = (nextSlash >= 0) ? after.substring(nextSlash).trim() : "";
            workingStr = (before + " " + remaining).trim();
        }

        LocalDate date = null;
        if (workingStr.contains("/da")) {
            int flagIdx = workingStr.indexOf("/da");
            String after = workingStr.substring(flagIdx + "/da".length()).trim();
            String[] tokens = after.split("\\s+", 2);

            if (tokens[0].isEmpty()) {
                ui.showAddUsage();
                return null;
            }
            date = parseDate(tokens[0], ui);
            if (date == null) {
                return null;
            }
            String before = workingStr.substring(0, flagIdx).trim();
            String remaining = tokens.length > 1 ? tokens[1].trim() : "";
            workingStr = (before + " " + remaining).trim();
        }

        String description = workingStr.trim();
        if (description.isEmpty()) {
            ui.showAddUsage();
            return null;
        }
        assert !description.isEmpty() : "Description should not be empty after validation";

        return new AddCommand(ui, description, amount, category, date);
    }

    /**
     * Parses the argument string for the list command.
     * Supports:
     * list
     * list YYYY-MM
     *
     * @param arguments The portion of user input after the list keyword.
     * @param ui        The Ui instance used to display error or usage messages.
     * @return A fully constructed ListCommand, or null if the input is invalid.
     */
    private static Command parseListCommand(String arguments, Ui ui) {
        if (arguments.isEmpty()) {
            return new ListCommand(ui, null);
        }

        YearMonth parsedMonth = parseYearMonth(arguments);
        if (parsedMonth == null) {
            ui.showInvalidMonthYear();
            return null;
        }

        return new ListCommand(ui, parsedMonth);
    }

    /**
     * Parses the argument string for the delete command and returns a DeleteCommand.
     *
     * @param arguments The portion of user input after the delete keyword.
     * @param ui        The Ui instance used to display error or usage messages.
     * @return A fully constructed DeleteCommand, or null if the input is invalid.
     */
    private static Command parseDeleteCommand(String arguments, Ui ui) {
        if (arguments.isEmpty()) {
            ui.showDeleteUsage();
            return null;
        }

        String trimmedArgs = arguments.trim();

        if (trimmedArgs.startsWith("/c")) {
            String category = trimmedArgs.substring(2).trim();
            if (category.isEmpty() || category.startsWith("/")) {
                ui.showDeleteUsage();
                return null;
            }
            return new DeleteCommand(ui, category);
        }

        if (trimmedArgs.startsWith("/da")) {
            String dateStr = trimmedArgs.substring(3).trim();
            if (dateStr.isEmpty() || dateStr.startsWith("/")) {
                ui.showDeleteUsage();
                return null;
            }
            LocalDate date = parseDate(dateStr, ui);
            if (date == null) {
                return null;
            }
            return new DeleteCommand(ui, date);
        }

        try {
            int index = Integer.parseInt(arguments);
            if (index <= 0) {
                ui.showInvalidIndex();
                return null;
            }
            assert index > 0 : "Delete index should be positive after validation";
            return new DeleteCommand(ui, index);
        } catch (NumberFormatException e) {
            ui.showInvalidIndexFormat();
            return null;
        }
    }

    /**
     * Parses the argument string for the edit command and returns an EditCommand.
     * At least one of /a, /de, /c, or /da must be provided.
     * Flags may appear in any order.
     *
     * @param arguments The portion of user input after the edit keyword.
     * @param ui        The Ui instance used to display error or usage messages.
     * @return A fully constructed EditCommand, or null if the input is invalid.
     */
    private static Command parseEditCommand(String arguments, Ui ui) {
        if (arguments.isEmpty()) {
            ui.showEditUsage();
            return null;
        }

        String[] indexSplit = arguments.split("\\s+", 2);
        int editIndex;
        try {
            editIndex = Integer.parseInt(indexSplit[0]);
            if (editIndex <= 0) {
                ui.showInvalidIndex();
                return null;
            }
        } catch (NumberFormatException e) {
            ui.showInvalidIndexFormat();
            return null;
        }

        String flagSection = (indexSplit.length > 1) ? indexSplit[1].trim() : "";
        if (flagSection.isEmpty()) {
            ui.showEditUsage();
            return null;
        }

        Double newAmount = null;
        String newDescription = null;
        String newCategory = null;
        LocalDate newDate = null;

        if (flagSection.contains("/a")) {
            int flagIdx = flagSection.indexOf("/a");
            String after = flagSection.substring(flagIdx + "/a".length()).trim();
            String[] tokens = after.split("\\s+", 2);

            if (tokens[0].isEmpty()) {
                ui.showEditUsage();
                return null;
            }
            try {
                double parsed = Double.parseDouble(tokens[0]);
                if (Double.isNaN(parsed) || Double.isInfinite(parsed) || parsed < 0) {
                    ui.showInvalidAmount();
                    return null;
                }
                newAmount = parsed;
            } catch (NumberFormatException e) {
                ui.showInvalidAmount();
                return null;
            }
            String before = flagSection.substring(0, flagIdx).trim();
            String remaining = tokens.length > 1 ? tokens[1].trim() : "";
            flagSection = (before + " " + remaining).trim();
        }

        // /de must be extracted before /da to avoid /da matching the start of /de
        if (flagSection.contains("/de")) {
            int flagIdx = flagSection.indexOf("/de");
            String after = flagSection.substring(flagIdx + "/de".length()).trim();
            int nextSlash = after.indexOf('/');
            String value = (nextSlash >= 0) ? after.substring(0, nextSlash).trim() : after.trim();

            if (value.isEmpty()) {
                ui.showEditUsage();
                return null;
            }
            newDescription = value;
            String before = flagSection.substring(0, flagIdx).trim();
            String remaining = (nextSlash >= 0) ? after.substring(nextSlash).trim() : "";
            flagSection = (before + " " + remaining).trim();
        }

        if (flagSection.contains("/da")) {
            int flagIdx = flagSection.indexOf("/da");
            String after = flagSection.substring(flagIdx + "/da".length()).trim();
            String[] tokens = after.split("\\s+", 2);

            if (tokens[0].isEmpty()) {
                ui.showEditUsage();
                return null;
            }
            newDate = parseDate(tokens[0], ui);
            if (newDate == null) {
                return null;
            }
            String before = flagSection.substring(0, flagIdx).trim();
            String remaining = tokens.length > 1 ? tokens[1].trim() : "";
            flagSection = (before + " " + remaining).trim();
        }

        if (flagSection.contains("/c")) {
            int flagIdx = flagSection.indexOf("/c");
            String after = flagSection.substring(flagIdx + "/c".length()).trim();
            int nextSlash = after.indexOf('/');
            String value = (nextSlash >= 0) ? after.substring(0, nextSlash).trim() : after.trim();

            if (value.isEmpty()) {
                ui.showEditUsage();
                return null;
            }
            newCategory = value;
            String before = flagSection.substring(0, flagIdx).trim();
            String remaining = (nextSlash >= 0) ? after.substring(nextSlash).trim() : "";
            flagSection = (before + " " + remaining).trim();
        }

        if (newAmount == null && newDescription == null && newCategory == null && newDate == null) {
            ui.showEditUsage();
            return null;
        }

        return new EditCommand(ui, editIndex, newAmount, newDescription, newCategory, newDate);
    }

    /**
     * Parses the argument string for the lend command and returns a LendCommand.
     * Expects the format: lend AMOUNT BORROWER_NAME [/da DATE].
     *
     * @param arguments The portion of user input after the lend keyword.
     * @param ui        The Ui instance used to display error or usage messages.
     * @return A fully constructed LendCommand, or null if the input is invalid.
     */
    private static Command parseLendCommand(String arguments, Ui ui) {
        if (arguments.isEmpty()) {
            ui.showLendUsage();
            return null;
        }

        String[] inputParts = arguments.split("\\s+", 2);
        if (inputParts.length < 2) {
            ui.showLendUsage();
            return null;
        }

        double amount;
        try {
            amount = Double.parseDouble(inputParts[0]);
        } catch (NumberFormatException e) {
            ui.showInvalidAmount();
            return null;
        }

        if (Double.isNaN(amount) || Double.isInfinite(amount)) {
            ui.showInvalidAmount();
            return null;
        }
        if (amount == 0) {
            ui.showZeroLoanAmountWarning();
            return null;
        }
        if (amount < 0) {
            ui.showInvalidAmount();
            return null;
        }

        String remainingText = inputParts[1].trim();

        LocalDate date = null;
        if (remainingText.contains("/da")) {
            int flagIndex = remainingText.indexOf("/da");
            String afterFlag = remainingText.substring(flagIndex + "/da".length()).trim();
            String[] tokens = afterFlag.split("\\s+", 2);

            if (tokens[0].isEmpty()) {
                ui.showLendUsage();
                return null;
            }
            date = parseDate(tokens[0], ui);
            if (date == null) {
                return null;
            }
            String beforeFlag = remainingText.substring(0, flagIndex).trim();
            String afterDate = tokens.length > 1 ? tokens[1].trim() : "";
            remainingText = (beforeFlag + " " + afterDate).trim();
        }

        String borrowerName = remainingText.trim();
        if (borrowerName.isEmpty() || borrowerName.startsWith("/")) {
            ui.showLendUsage();
            return null;
        }

        assert amount > 0 : "Amount should be positive after all validation";
        assert !borrowerName.isEmpty() : "Borrower name should not be empty after validation";

        return new LendCommand(ui, borrowerName, amount, date);
    }

    /**
     * Parses the argument string for the loans command.
     * Accepts no arguments (outstanding only) or /all (include repaid).
     *
     * @param arguments The portion of user input after the loans keyword.
     * @param ui        The Ui instance used to display error or usage messages.
     * @return A LoansCommand, or null if the argument is unrecognised.
     */
    private static Command parseLoansCommand(String arguments, Ui ui) {
        if (arguments.isEmpty()) {
            return new LoansCommand(ui, false);
        }
        if (arguments.equals("/all")) {
            return new LoansCommand(ui, true);
        }
        ui.showLoansUsage();
        return null;
    }

    /**
     * Parses the argument string for the repay command and returns a RepayCommand.
     * Supports:
     *   repay INDEX          (full repayment)
     *   repay INDEX AMOUNT   (partial repayment)
     *
     * @param arguments The portion of user input after the repay keyword.
     * @param ui        The Ui instance used to display error or usage messages.
     * @return A fully constructed RepayCommand, or null if the input is invalid.
     */
    private static Command parseRepayCommand(String arguments, Ui ui) {
        if (arguments.isEmpty()) {
            ui.showRepayUsage();
            return null;
        }

        String[] tokens = arguments.split("\\s+");
        if (tokens.length > 2) {
            ui.showRepayUsage();
            return null;
        }

        int index;
        try {
            index = Integer.parseInt(tokens[0]);
        } catch (NumberFormatException e) {
            ui.showInvalidIndexFormat();
            return null;
        }

        if (index <= 0) {
            ui.showInvalidRepayIndex();
            return null;
        }

        Double amount = null;
        if (tokens.length == 2) {
            try {
                amount = Double.parseDouble(tokens[1]);
            } catch (NumberFormatException e) {
                ui.showInvalidAmount();
                return null;
            }
            if (amount <= 0 || Double.isNaN(amount) || Double.isInfinite(amount)) {
                ui.showInvalidAmount();
                return null;
            }
        }

        assert index > 0 : "Repay index should be positive after validation";
        return new RepayCommand(ui, index, amount);
    }


    /**
     * Parses the argument string for the find command and returns a FindCommand.
     * Supports optional flags: /c CATEGORY, /dmin DATE, /dmax DATE,
     * /amin AMOUNT, /amax AMOUNT, /sort asc|desc.
     *
     * @param arguments The portion of user input after the find keyword.
     * @param ui        The Ui instance used to display error or usage messages.
     * @return A fully constructed FindCommand, or null if the input is invalid.
     */
    private static Command parseFindCommand(String arguments, Ui ui) {
        if (arguments.isEmpty()) {
            ui.showFindUsage();
            return null;
        }

        String workingStr = arguments;
        String categoryFilter = null;
        LocalDate dateMin = null;
        LocalDate dateMax = null;
        Double amountMin = null;
        Double amountMax = null;
        String sortOrder = null;

        if (workingStr.contains("/sort")) {
            int flagIdx = workingStr.indexOf("/sort");
            String after = workingStr.substring(flagIdx + "/sort".length()).trim();
            String[] tokens = after.split("\\s+", 2);
            if (tokens[0].equals("asc") || tokens[0].equals("desc")) {
                sortOrder = tokens[0];
            } else {
                ui.showFindUsage();
                return null;
            }
            String before = workingStr.substring(0, flagIdx).trim();
            String remaining = tokens.length > 1 ? tokens[1].trim() : "";
            workingStr = (before + " " + remaining).trim();
        }

        if (workingStr.contains("/amin")) {
            int flagIdx = workingStr.indexOf("/amin");
            String after = workingStr.substring(flagIdx + "/amin".length()).trim();
            String[] tokens = after.split("\\s+", 2);
            try {
                amountMin = Double.parseDouble(tokens[0]);
            } catch (NumberFormatException e) {
                ui.showInvalidAmount();
                return null;
            }
            String before = workingStr.substring(0, flagIdx).trim();
            String remaining = tokens.length > 1 ? tokens[1].trim() : "";
            workingStr = (before + " " + remaining).trim();
        }

        if (workingStr.contains("/amax")) {
            int flagIdx = workingStr.indexOf("/amax");
            String after = workingStr.substring(flagIdx + "/amax".length()).trim();
            String[] tokens = after.split("\\s+", 2);
            try {
                amountMax = Double.parseDouble(tokens[0]);
            } catch (NumberFormatException e) {
                ui.showInvalidAmount();
                return null;
            }
            String before = workingStr.substring(0, flagIdx).trim();
            String remaining = tokens.length > 1 ? tokens[1].trim() : "";
            workingStr = (before + " " + remaining).trim();
        }

        if (workingStr.contains("/dmin")) {
            int flagIdx = workingStr.indexOf("/dmin");
            String after = workingStr.substring(flagIdx + "/dmin".length()).trim();
            String[] tokens = after.split("\\s+", 2);
            dateMin = parseDate(tokens[0], ui);
            if (dateMin == null) {
                return null;
            }
            String before = workingStr.substring(0, flagIdx).trim();
            String remaining = tokens.length > 1 ? tokens[1].trim() : "";
            workingStr = (before + " " + remaining).trim();
        }

        if (workingStr.contains("/dmax")) {
            int flagIdx = workingStr.indexOf("/dmax");
            String after = workingStr.substring(flagIdx + "/dmax".length()).trim();
            String[] tokens = after.split("\\s+", 2);
            dateMax = parseDate(tokens[0], ui);
            if (dateMax == null) {
                return null;
            }
            String before = workingStr.substring(0, flagIdx).trim();
            String remaining = tokens.length > 1 ? tokens[1].trim() : "";
            workingStr = (before + " " + remaining).trim();
        }

        if (workingStr.contains("/c")) {
            int flagIdx = workingStr.indexOf("/c");
            String after = workingStr.substring(flagIdx + 2).trim();
            int nextSlash = after.indexOf('/');
            String value = (nextSlash >= 0)
                    ? after.substring(0, nextSlash).trim() : after.trim();
            if (value.isEmpty()) {
                ui.showFindUsage();
                return null;
            }
            categoryFilter = value;
            String before = workingStr.substring(0, flagIdx).trim();
            String remaining = (nextSlash >= 0) ? after.substring(nextSlash).trim() : "";
            workingStr = (before + " " + remaining).trim();
        }

        if (amountMin != null && amountMax != null && amountMin > amountMax) {
            ui.showInvalidAmountRange();
            return null;
        }

        if (dateMin != null && dateMax != null && dateMin.isAfter(dateMax)) {
            ui.showInvalidDateRange();
            return null;
        }

        String keyword = workingStr.trim();
        boolean hasAnyFilter = categoryFilter != null || dateMin != null
                || dateMax != null || amountMin != null || amountMax != null
                || sortOrder != null;

        if (keyword.isEmpty() && !hasAnyFilter) {
            ui.showFindUsage();
            return null;
        }

        return new FindCommand(ui, keyword, categoryFilter,
                dateMin, dateMax, amountMin, amountMax, sortOrder);
    }

    /**
     * Parses the argument string for the budget command and returns a BudgetCommand.
     * Supports:
     * budget
     * budget AMOUNT
     * budget YYYY-MM
     * budget YYYY-MM AMOUNT
     *
     * @param arguments The portion of user input after the budget keyword.
     * @param ui        The Ui instance used to display error or usage messages.
     * @return A fully constructed BudgetCommand, or null if the input is invalid.
     */
    private static Command parseBudgetCommand(String arguments, Ui ui) {
        YearMonth currentMonth = YearMonth.now();

        if (arguments.isEmpty()) {
            return new BudgetCommand(ui, currentMonth, null);
        }

        String[] tokens = arguments.split("\\s+");
        if (tokens.length > 2) {
            ui.showBudgetUsage();
            return null;
        }

        if (tokens.length == 1) {
            YearMonth parsedMonth = parseYearMonth(tokens[0]);
            if (parsedMonth != null) {
                return new BudgetCommand(ui, parsedMonth, null);
            }

            try {
                double budgetAmount = Double.parseDouble(tokens[0]);
                if (budgetAmount <= 0 || Double.isNaN(budgetAmount) || Double.isInfinite(budgetAmount)) {
                    ui.showInvalidBudget();
                    return null;
                }
                return new BudgetCommand(ui, currentMonth, budgetAmount);
            } catch (NumberFormatException e) {
                ui.showBudgetUsage();
                return null;
            }
        }

        YearMonth parsedMonth = parseYearMonth(tokens[0]);
        if (parsedMonth == null) {
            ui.showBudgetUsage();
            return null;
        }

        try {
            double budgetAmount = Double.parseDouble(tokens[1]);
            if (budgetAmount <= 0 || Double.isNaN(budgetAmount) || Double.isInfinite(budgetAmount)) {
                ui.showInvalidBudget();
                return null;
            }
            return new BudgetCommand(ui, parsedMonth, budgetAmount);
        } catch (NumberFormatException e) {
            ui.showInvalidBudget();
            return null;
        }
    }

    /**
     * Parses a month string strictly following the YYYY-MM format.
     *
     * @param monthStr The raw month token to parse.
     * @return The parsed YearMonth, or null if parsing failed.
     */
    private static YearMonth parseYearMonth(String monthStr) {
        try {
            return YearMonth.parse(monthStr, YEAR_MONTH_FORMAT);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Shows usage help for a specific command by routing to the matching Ui method.
     *
     * @param arguments The command name to show help for.
     * @param ui        The Ui instance used to display the usage message.
     * @return null always, since this only displays information.
     */
    private static Command parseHelpCommand(String arguments, Ui ui) {
        switch (arguments.toLowerCase()) {
        case "add":
            ui.showAddUsage();
            break;
        case "delete":
            ui.showDeleteUsage();
            break;
        case "edit":
            ui.showEditUsage();
            break;
        case "find":
            ui.showFindUsage();
            break;
        case "budget":
            ui.showBudgetUsage();
            break;
        case "sort":
            ui.showSortUsage();
            break;
        case "lend":
            ui.showLendUsage();
            break;
        case "loans":
            ui.showLoansUsage();
            break;
        case "repay":
            ui.showRepayUsage();
            break;
        default:
            ui.showUnknownCommand();
            break;
        }
        return null;
    }

    /**
     * Parses a date string strictly following the YYYY-MM-DD format.
     * Impossible calendar dates are also rejected.
     *
     * @param dateStr The raw date token to parse.
     * @param ui      The Ui instance used to display the error message on failure.
     * @return The parsed LocalDate, or null if parsing failed.
     */
    private static LocalDate parseDate(String dateStr, Ui ui) {
        try {
            return LocalDate.parse(dateStr, DATE_FORMAT);
        } catch (DateTimeParseException e) {
            ui.showInvalidDateFormat();
            return null;
        }
    }
}
