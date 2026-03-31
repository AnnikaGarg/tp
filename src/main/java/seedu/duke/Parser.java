package seedu.duke;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * Parses raw user input strings into executable Command objects.
 * Both add and edit commands use /c for category and /da for date.
 * The edit command additionally uses /a for amount and /de for description.
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
            if (arguments.isEmpty()) {
                ui.showFindUsage();
                return null;
            }
            return new FindCommand(ui, arguments);

        case "budget":
            if (arguments.isEmpty()) {
                ui.showBudgetUsage();
                return null;
            }
            try {
                double budgetAmount = Double.parseDouble(arguments);
                if (budgetAmount < 0) {
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

