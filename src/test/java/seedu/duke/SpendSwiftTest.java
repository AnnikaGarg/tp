package seedu.duke;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class SpendSwiftTest {

    @TempDir
    Path tempDir;

    @Test
    public void run_exitCommand_printsWelcomeAndExitMessage() {
        String output = runWithInput("exit\n", tempDir);

        assertTrue(output.contains("Hello! I'm SpendSwift."));
        assertTrue(output.contains("Bye. Hope to see you again soon!"));
    }

    @Test
    public void run_addThenList_showsAddedExpense() {
        String output = runWithInput("add 5.50 Coffee\nlist\nexit\n", tempDir);

        assertTrue(output.contains("I've added this expense:"));
        assertTrue(output.contains("Coffee ($5.50)"));
        assertTrue(output.contains("Here are your tracked expenses:"));
    }

    @Test
    public void run_invalidCommands_showsValidationMessages() {
        String output = runWithInput("add\nadd abc Lunch\ndelete abc\nunknown\nexit\n", tempDir);

        assertTrue(output.contains("Usage: add <amount> [/c <category>] [/da <YYYY-MM-DD>] <description>"));
        assertTrue(output.contains("Amount must be a valid non-negative number."));
        assertTrue(output.contains("Index must be a valid integer."));
        assertTrue(output.contains("Unknown command. Type 'help' to see available commands."));
    }

    @Test
    public void run_persistenceAcrossRestart_expensesReloadedOnNextRun() {
        // First session: add an expense and exit (triggers save)
        runWithInput("add 8.00 Sushi\nexit\n", tempDir);

        // Second session: same data directory, expenses should be reloaded
        String output = runWithInput("list\nexit\n", tempDir);

        assertTrue(output.contains("Sushi"),
                "Expenses added in a previous session should be reloaded from disk");
        assertTrue(output.contains("$8.00"),
                "Reloaded expense should retain its original amount");
    }

    @Test
    public void run_deleteAfterAdd_listShowsRemainingExpenses() {
        String output = runWithInput("add 5.00 Coffee\nadd 10.00 Lunch\ndelete 1\nlist\nexit\n", tempDir);

        assertTrue(output.contains("I've removed this expense:"),
                "Delete confirmation should be shown");
        assertTrue(output.contains("Lunch"),
                "Remaining expense should still appear in list");
    }

    @Test
    public void run_blankLines_ignoredGracefully() {
        String output = runWithInput("\n\n\nadd 3.00 Tea\n\nexit\n", tempDir);

        assertTrue(output.contains("Tea"),
                "Blank lines should be ignored and valid commands still processed");
    }

    @Test
    public void run_deleteOutOfBoundsIndex_showsInvalidIndex() {
        String output = runWithInput("add 5.00 Coffee\ndelete 999\nexit\n", tempDir);

        assertTrue(output.contains("Invalid index"),
                "Deleting an out-of-bounds index should show invalid index error");
    }

    @Test
    public void run_addWithCategoryAndDate_expenseStoredCorrectly() {
        String output = runWithInput("add 12.50 /c Food /da 2026-03-24 Chicken Rice\nlist\nexit\n", tempDir);

        assertTrue(output.contains("Chicken Rice"),
                "Expense description should appear in list output");
        assertTrue(output.contains("Food"),
                "Category should appear in list output");
    }

    private String runWithInput(String input, Path workingDirectory) {
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;
        String originalUserDir = System.getProperty("user.dir");
        ByteArrayOutputStream outputBuffer = new ByteArrayOutputStream();

        try {
            System.setProperty("user.dir", workingDirectory.toString());
            System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
            System.setOut(new PrintStream(outputBuffer));

            new SpendSwift().run();
            return outputBuffer.toString(StandardCharsets.UTF_8);
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
            System.setProperty("user.dir", originalUserDir);
        }
    }
}
