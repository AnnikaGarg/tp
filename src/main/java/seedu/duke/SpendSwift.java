package seedu.duke;

import java.util.Scanner;

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
        try (Scanner scanner = new Scanner(System.in)) {
            boolean isExit = false;
            while (!isExit && scanner.hasNextLine()) {
                String fullCommand = scanner.nextLine().trim();
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
