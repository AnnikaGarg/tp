package seedu.duke;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * Parses raw user input strings into executable Command objects.
 * Both add and edit commands use /c for category and /da for date.
 * The edit command additionally uses /a for amount and /de for description.
 * The lend command uses /da for date.
 */
public class Parser {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);

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
            if (!arguments.isEmpty()) {
                ui.showUnknownCommand();
                return null;
            }
            return new ListCommand(ui);

        case "help":
            if (!arguments.isEmpty()) {
                ui.showUnknownCommand();
                return null;
            }
            return new HelpCommand(ui);

        case "exit":
            if (!arguments.isEmpty()) {
                ui.showUnknownCommand();
                return null;
            }
            return new ExitCommand(ui);

        case "add":
            return parseAddCommand(arguments, ui);

        case "delete":
            return parseDeleteCommand(arguments, ui);

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
            if (arguments.isEmpty()) {
                return new BudgetCommand(ui, null);
            }
            try {
                double budgetAmount = Double.parseDouble(arguments);
                if (budgetAmount <= 0) {
                    ui.showInvalidBudget();
                    return null;
                }
                return new BudgetCommand(ui, budgetAmount);
            } catch (NumberFormatException e) {
                ui.showInvalidBudget();
                return null;
            }

        case "sort":
            if (!arguments.equals("category") && !arguments.equals("date")) {
                ui.showSortUsage();
                return null;
            }
            return new SortCommand(ui, arguments);

        case "stats":
            if (!arguments.isEmpty()) {
                ui.showUnknownCommand();
                return null;
            }
            return new StatisticsCommand(ui);

        case "lend":
            return parseLendCommand(arguments, ui);

        case "loans":
            return parseLoansCommand(arguments, ui);

        case "repay":
            return parseRepayCommand(arguments, ui);

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

            if (Double.isNaN(amount) || Double.isInfinite(amount) || amount < 0) {
                ui.showInvalidAmount();
                return null;
            }
        } catch (NumberFormatException e) {
            ui.showInvalidAmount();
            return null;
        }

        String workingStr = firstSplit[1].trim();

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
            ui.showZeroAmountWarning();
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
     * Expects a single positive integer index with no trailing tokens.
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
        if (tokens.length > 1) {
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
            ui.showInvalidIndex();
            return null;
        }

        assert index > 0 : "Repay index should be positive after validation";
        return new RepayCommand(ui, index);
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
     * Parses a date string strictly following the YYYY-MM-DD format.
     * Impossible calendar dates are also rejected.
     * Calls showInvalidDateFormat and returns null on failure.
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
