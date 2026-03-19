package seedu.duke;

/**
 * Parses user input into meaningful commands.
 */
public class Parser {

    /**
     * Parses the full command string from the user and returns the appropriate Command object.
     *
     * @param fullCommand The raw string entered by the user.
     * @param ui The UI instance to pass to commands or use for error messages.
     * @return The corresponding Command object, or null if invalid.
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

        switch (commandWord) {
        case "list":
            return new ListCommand(ui);

        case "help":
            return new HelpCommand(ui);

        case "exit":
            return new ExitCommand(ui);

        case "add":
            if (arguments.isEmpty()) {
                ui.showAddUsage();
                return null;
            }

            String[] addParts = arguments.split("\\s+", 2);
            if (addParts.length < 2) {
                ui.showAddUsage();
                return null;
            }

            try {
                double amount = Double.parseDouble(addParts[0]);
                String description = addParts[1];
                if (Double.isNaN(amount) || Double.isInfinite(amount) || amount < 0) {
                    ui.showInvalidAmount();
                    return null;
                }
                if (description.isEmpty()) {
                    ui.showAddUsage();
                    return null;
                }
                return new AddCommand(ui, description, amount);
            } catch (NumberFormatException e) {
                ui.showInvalidAmount();
                return null;
            }

        case "delete":
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
                return new DeleteCommand(ui, index);
            } catch (NumberFormatException e) {
                ui.showInvalidIndexFormat();
                return null;
            }

        default:
            ui.showUnknownCommand();
            return null;
        }
    }
}
