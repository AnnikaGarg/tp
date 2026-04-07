package seedu.duke;

import seedu.duke.command.Command;
import seedu.duke.model.ExpenseList;
import seedu.duke.parser.Parser;
import seedu.duke.storage.Storage;
import seedu.duke.ui.Ui;

/**
 * The main entry point for the SpendSwift application.
 * Initializes the application and starts the interaction loop with the user.
 */
public class SpendSwift {
    private static final String DATA_FILE_PATH = "data/expenses.txt";
    private ExpenseList expenseList;
    private Storage storage;
    private Ui ui;

    /**
     * Constructs a SpendSwift instance and initializes the core components.
     * Loads any previously saved expenses from disk.
     */
    public SpendSwift() {
        expenseList = new ExpenseList();
        ui = new Ui();
        storage = new Storage(DATA_FILE_PATH, ui);
        storage.load(expenseList);
    }

    /**
     * Runs the main loop of the application.
     * Continuously reads user input and handles the "exit" command.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;

        while (!isExit) {
            try {
                // Read input exclusively through the single Ui Scanner
                String fullCommand = ui.getUserInput();

                if (fullCommand.isEmpty()) {
                    continue;
                }

                Command command = Parser.parse(fullCommand, ui);
                if (command == null) {
                    continue;
                }

                command.execute(expenseList);
                if (command.shouldPersist()) {
                    storage.save(expenseList);
                }
                isExit = command.isExit();

            } catch (java.util.NoSuchElementException e) {
                // Safety net: If an automated test script runs out of lines without typing "exit",
                // this safely breaks the loop instead of crashing the build.
                break;
            }
        }
    }

    /**
     * The main method that launches the SpendSwift application.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        new SpendSwift().run();
    }
}
