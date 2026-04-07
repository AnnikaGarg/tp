package seedu.duke.command;

import org.junit.jupiter.api.Test;
import seedu.duke.model.ExpenseList;
import seedu.duke.ui.Ui;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HelpCommandTest {

    @Test
    public void execute_helpCommand_executesWithoutException() {
        Ui ui = new Ui();
        ExpenseList expenseList = new ExpenseList();
        HelpCommand helpCommand = new HelpCommand(ui);

        assertDoesNotThrow(() -> helpCommand.execute(expenseList),
                "HelpCommand should execute successfully without throwing errors");
    }

    @Test
    public void execute_helpCommand_outputContainsAllCommands() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));

        new HelpCommand(new Ui()).execute(new ExpenseList());

        System.setOut(original);
        String output = out.toString();

        assertTrue(output.contains("add"), "Help output should list add command");
        assertTrue(output.contains("list"), "Help output should list list command");
        assertTrue(output.contains("delete"), "Help output should list delete command");
        assertTrue(output.contains("edit"), "Help output should list edit command");
        assertTrue(output.contains("budget"), "Help output should list budget command");
        assertTrue(output.contains("total"), "Help output should list total command");
        assertTrue(output.contains("exit"), "Help output should list exit command");
    }
}
