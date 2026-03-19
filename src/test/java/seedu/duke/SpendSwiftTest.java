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

        assertTrue(output.contains("Usage: add <amount> <description>"));
        assertTrue(output.contains("Amount must be a valid number."));
        assertTrue(output.contains("Index must be a valid integer."));
        assertTrue(output.contains("Unknown command. Type 'help' to see available commands."));
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
