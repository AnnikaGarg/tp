package seedu.duke.command;

import seedu.duke.model.ExpenseList;
import seedu.duke.ui.Ui;

/**
 * Handles the logic for displaying the help menu.
 */
public class HelpCommand extends Command {

    /**
     * Constructs a HelpCommand with the specified Ui instance.
     *
     * @param ui The Ui object used to display messages.
     */
    HelpCommand(Ui ui) {
        super(ui);
        assert ui != null : "Ui cannot be null";
    }

    @Override
    public void execute(ExpenseList expenseList) {
        ui.showHelp();
    }
}
