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

            switch (commandWord) {
            case "list":
                printAllExpenses();
                break;
            case "help":
                printHelp();
                break;
            case "add":
                String[] parts = fullCommand.split("\\s+", 3);
                if (parts.length < 3) {
                    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    System.out.println("Usage: add <amount> <description>");
                    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    break;
                }

                try {
                    double amount = Double.parseDouble(parts[1]);
                    String description = parts[2];

                    AddExpense addCommand = new AddExpense(description, amount);
                    addCommand.execute(expenseList);
                    storage.save(expenseList);

                } catch (NumberFormatException e) {
                    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    System.out.println("Amount must be a valid number.");
                    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                }
                break;
            case "exit":
                isExit = true;
                storage.save(expenseList);
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                System.out.println("Bye. Hope to see you again soon!");
                System.out.print("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                break;
            default:
                System.out.println("Unknown command. Type 'help' to see available commands.");
                break;
            }
        }
        scanner.close();
    }

    /**
     * Prints the help menu showing all available commands and their formats.
     */
    private static void printHelp() {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("Here are the available commands:");
        System.out.println("  add AMOUNT DESCRIPTION  - Add a new expense");
        System.out.println("  list                    - List all expenses");
        System.out.println("  delete INDEX            - Delete an expense by index");
        System.out.println("  help                    - Show this help menu");
        System.out.println("  exit                    - Exit the application");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    /**
     * Prints all current expenses to the user
     */
    private void printAllExpenses() {
        if (expenseList.getSize() == 0) {
            System.out.println("Your expense list is currently empty.");
            return;
        }

        System.out.println("Here are your tracked expenses:");
        for (int i = 0; i < expenseList.getSize(); i++) {
            System.out.println((i + 1) + ". " + expenseList.getExpense(i).toString());
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
