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

    /**
     * Constructs a SpendSwift instance and initializes the core components.
     * Loads any previously saved expenses from disk.
     */
    public SpendSwift() {
        expenseList = new ExpenseList();
        storage = new Storage(DATA_FILE_PATH);
        storage.load(expenseList);
    }

    /**
     * Runs the main loop of the application.
     * Continuously reads user input and handles the "exit" command.
     */
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hello! I'm SpendSwift.");
        System.out.println("What expenses are we tracking today?");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        boolean isExit = false;
        while (!isExit && scanner.hasNextLine()) {
            String fullCommand = scanner.nextLine().trim();
            if (fullCommand.isEmpty()) {
                continue;
            }

            String commandWord = fullCommand.split(" ")[0].toLowerCase();

            if (commandWord.equals("exit")) {
                isExit = true;
                storage.save(expenseList);
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                System.out.println("Bye. Hope to see you again soon!");
                System.out.print("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            } else {
                // The rest of the team will add their commands here!
                System.out.println("Command not recognized yet! Team is working on it.");
            }
        }
        scanner.close();
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
