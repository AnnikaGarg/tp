package seedu.duke;

import org.junit.jupiter.api.Test;
import seedu.duke.ui.Ui;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ParserTest {
    private final Ui ui = new Ui();

    @Test
    public void parse_strictCommandWithArguments_returnsNull() {
        Command command = Parser.parse("exit s", ui);
        assertNull(command, "Parser should return null when strict commands have extra arguments");
    }

    @Test
    public void parse_addCommandWithCategoryAndDate_returnsCommand() {
        Command command = Parser.parse("add 10.00 Lunch /c food /da 2026-03-24", ui);
        assertNotNull(command, "Parser should successfully parse complete v2.0 add commands");
    }

    @Test
    public void parse_listCommand_returnsListCommand() {
        Command command = Parser.parse("list", ui);
        assertTrue(command instanceof ListCommand, "Parser should return a ListCommand object");
    }

    @Test
    public void parse_exitCommand_returnsExitCommand() {
        Command command = Parser.parse("exit", ui);
        assertTrue(command instanceof ExitCommand, "Parser should return an ExitCommand object");
    }

    @Test
    public void parse_invalidCommandWord_returnsNull() {
        Command command = Parser.parse("jump", ui);
        assertNull(command, "Parser should return null for unknown commands");
    }

    @Test
    public void parse_addCommandMissingArguments_returnsNull() {
        Command command = Parser.parse("add", ui);
        assertNull(command, "Parser should return null when add arguments are missing");
    }

    @Test
    public void parse_addCommandValidArguments_returnsCommand() {
        Command command = Parser.parse("add 5.50 Coffee", ui);
        assertNotNull(command, "Parser should return a Command object for valid add inputs");
        assertFalse(command.isExit(), "Add command should not trigger exit");
    }

    @Test
    public void parse_deleteCommandInvalidNumber_returnsNull() {
        Command command = Parser.parse("delete abc", ui);
        assertNull(command, "Parser should return null if index is not a number");
    }

    @Test
    public void parse_addCommandNegativeAmount_returnsNull() {
        Command command = Parser.parse("add -3.50 Coffee", ui);
        assertNull(command, "Parser should return null when amount is negative");
    }

    @Test
    public void parse_deleteCommandZeroIndex_returnsNull() {
        Command command = Parser.parse("delete 0", ui);
        assertNull(command, "Parser should return null when index is zero");
    }

    @Test
    public void parse_nullCommand_returnsNull() {
        Command command = Parser.parse(null, ui);
        assertNull(command, "Parser should return null for null commands");
    }

    @Test
    public void parse_nullUi_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> Parser.parse("list", null));
    }

    @Test
    public void parse_findCommandValidKeyword_returnsFindCommand() {
        Command command = Parser.parse("find coffee", ui);
        assertTrue(command instanceof FindCommand, "Parser should return a FindCommand for valid find input");
    }

    @Test
    public void parse_findCommandNoKeyword_returnsNull() {
        Command command = Parser.parse("find", ui);
        assertNull(command, "Parser should return null when find has no keyword");
    }

    @Test
    public void parse_whitespaceOnly_returnsNull() {
        assertNull(Parser.parse("   ", ui));
    }

    @Test
    public void parse_helpCommand_returnsHelpCommand() {
        assertTrue(Parser.parse("help", ui) instanceof HelpCommand);
    }

    @Test
    public void parse_totalCommand_returnsTotalCommand() {
        assertTrue(Parser.parse("total", ui) instanceof TotalCommand);
    }

    @Test
    public void parse_statsCommand_returnsStatisticsCommand() {
        assertTrue(Parser.parse("stats", ui) instanceof StatisticsCommand);
    }

    @Test
    public void parse_sortCategoryCommand_returnsSortCommand() {
        assertTrue(Parser.parse("sort category", ui) instanceof SortCommand);
    }

    @Test
    public void parse_sortDateCommand_returnsSortCommand() {
        assertTrue(Parser.parse("sort date", ui) instanceof SortCommand);
    }

    @Test
    public void parse_sortInvalidArg_returnsNull() {
        assertNull(Parser.parse("sort invalid", ui));
    }

    @Test
    public void parse_deleteValidIndex_returnsDeleteCommand() {
        assertTrue(Parser.parse("delete 1", ui) instanceof DeleteCommand);
    }

    @Test
    public void parse_budgetNoAmount_returnsBudgetCommand() {
        assertTrue(Parser.parse("budget", ui) instanceof BudgetCommand);
    }

    @Test
    public void parse_budgetValidAmount_returnsBudgetCommand() {
        assertTrue(Parser.parse("budget 50.00", ui) instanceof BudgetCommand);
    }

    @Test
    public void parse_budgetNegativeAmount_returnsNull() {
        assertNull(Parser.parse("budget -5", ui));
    }

    @Test
    public void parse_budgetNonNumericAmount_returnsNull() {
        assertNull(Parser.parse("budget abc", ui));
    }

    @Test
    public void parse_addCommandCategoryWithoutFollowingFlag_returnsAddCommand() {
        // covers nextSlash < 0 in /c block (no flag after category value)
        assertNotNull(Parser.parse("add 5.00 Coffee /c Food", ui));
    }

    @Test
    public void parse_addCommandDateBeforeDescription_returnsAddCommand() {
        // covers tokens.length > 1 in /da block (description text follows the date)
        assertNotNull(Parser.parse("add 5.00 /da 2026-04-01 Coffee", ui));
    }

    @Test
    public void parse_addCommandEmptyCategoryValue_returnsNull() {
        // /c is present but its value is empty because /da immediately follows
        assertNull(Parser.parse("add 5.00 /c /da 2026-04-01 Coffee", ui));
    }

    @Test
    public void parse_addCommandEmptyDateValue_returnsNull() {
        // /da is present but nothing follows it
        assertNull(Parser.parse("add 5.00 Coffee /da", ui));
    }

    @Test
    public void parse_addCommandInvalidDate_returnsNull() {
        assertNull(Parser.parse("add 5.00 Coffee /da baddate", ui));
    }

    @Test
    public void parse_addCommandNoDescriptionAfterFlagRemoval_returnsNull() {
        // description is empty once /c and /da tokens are stripped out
        assertNull(Parser.parse("add 5.00 /c Food /da 2026-04-01", ui));
    }

    @Test
    public void parse_addCommandZeroAmount_returnsNull() {
        assertNull(Parser.parse("add 0 Coffee", ui));
    }

    @Test
    public void parse_addCommandSingleToken_returnsNull() {
        // amount provided but no description token at all
        assertNull(Parser.parse("add 5.00", ui));
    }

    @Test
    public void parse_editCommandAmountOnly_returnsEditCommand() {
        // covers /a block success path + tokens.length == 1 in /a (no trailing text)
        assertTrue(Parser.parse("edit 1 /a 5.00", ui) instanceof EditCommand);
    }

    @Test
    public void parse_editCommandDescriptionOnly_returnsEditCommand() {
        // covers /de block + nextSlash < 0 in /de (no flag after description)
        assertTrue(Parser.parse("edit 1 /de New Lunch", ui) instanceof EditCommand);
    }

    @Test
    public void parse_editCommandCategoryOnly_returnsEditCommand() {
        // covers /c block + nextSlash < 0 in /c
        assertTrue(Parser.parse("edit 1 /c Food", ui) instanceof EditCommand);
    }

    @Test
    public void parse_editCommandDateOnly_returnsEditCommand() {
        // covers /da block + tokens.length == 1 in /da
        assertTrue(Parser.parse("edit 1 /da 2026-04-01", ui) instanceof EditCommand);
    }

    @Test
    public void parse_editCommandDescAndCategory_returnsEditCommand() {
        // covers nextSlash >= 0 in /de (description is followed by /c flag)
        assertTrue(Parser.parse("edit 1 /de Lunch /c Food", ui) instanceof EditCommand);
    }

    @Test
    public void parse_editCommandDateAndCategory_returnsEditCommand() {
        // covers tokens.length > 1 in /da (/c text follows the date)
        assertTrue(Parser.parse("edit 1 /da 2026-04-01 /c Food", ui) instanceof EditCommand);
    }

    @Test
    public void parse_editCommandAllFourFlags_returnsEditCommand() {
        // covers tokens.length > 1 in /a and nextSlash >= 0 in /de simultaneously
        assertTrue(Parser.parse("edit 1 /a 5.00 /de Lunch /c Food /da 2026-04-01", ui)
                instanceof EditCommand);
    }

    @Test
    public void parse_editCommandZeroIndex_returnsNull() {
        assertNull(Parser.parse("edit 0 /a 5.00", ui));
    }

    @Test
    public void parse_editCommandNonNumericIndex_returnsNull() {
        assertNull(Parser.parse("edit abc /a 5.00", ui));
    }

    @Test
    public void parse_editCommandNoFlags_returnsNull() {
        // flagSection is empty after the index — no flags provided at all
        assertNull(Parser.parse("edit 1", ui));
    }

    @Test
    public void parse_editCommandEmptyAmountValue_returnsNull() {
        // /a present but no value follows it (tokens[0].isEmpty())
        assertNull(Parser.parse("edit 1 /a", ui));
    }

    @Test
    public void parse_editCommandNegativeAmount_returnsNull() {
        assertNull(Parser.parse("edit 1 /a -5.00", ui));
    }

    @Test
    public void parse_editCommandInvalidAmountString_returnsNull() {
        assertNull(Parser.parse("edit 1 /a abc", ui));
    }

    @Test
    public void parse_editCommandEmptyDescriptionValue_returnsNull() {
        // /de present but no value follows it
        assertNull(Parser.parse("edit 1 /de", ui));
    }

    @Test
    public void parse_editCommandInvalidDate_returnsNull() {
        assertNull(Parser.parse("edit 1 /da baddate", ui));
    }

    @Test
    public void parse_editCommandEmptyDateValue_returnsNull() {
        // /da present but no value follows it (tokens[0].isEmpty())
        assertNull(Parser.parse("edit 1 /da", ui));
    }

    @Test
    public void parse_editCommandEmptyCategoryValue_returnsNull() {
        // /c present but no value follows it
        assertNull(Parser.parse("edit 1 /c", ui));
    }

    @Test
    public void parse_editCommandUnrecognizedFlag_returnsNull() {
        // no recognized flag → all four fields remain null
        assertNull(Parser.parse("edit 1 /x garbage", ui));
    }

    @Test
    public void parse_lendCommandValid_returnsLendCommand() {
        assertTrue(Parser.parse("lend 20.00 John", ui) instanceof LendCommand);
    }

    @Test
    public void parse_lendCommandWithDate_returnsLendCommand() {
        assertTrue(Parser.parse("lend 20.00 John /da 2026-04-01", ui) instanceof LendCommand);
    }

    @Test
    public void parse_lendCommandNoArguments_returnsNull() {
        assertNull(Parser.parse("lend", ui));
    }

    @Test
    public void parse_lendCommandAmountOnly_returnsNull() {
        assertNull(Parser.parse("lend 20.00", ui));
    }

    @Test
    public void parse_lendCommandInvalidAmount_returnsNull() {
        assertNull(Parser.parse("lend abc John", ui));
    }

    @Test
    public void parse_lendCommandZeroAmount_returnsNull() {
        assertNull(Parser.parse("lend 0 John", ui));
    }

    @Test
    public void parse_lendCommandNegativeAmount_returnsNull() {
        assertNull(Parser.parse("lend -5.00 John", ui));
    }

    @Test
    public void parse_lendCommandInvalidDate_returnsNull() {
        assertNull(Parser.parse("lend 20.00 John /da notadate", ui));
    }

    @Test
    public void parse_lendCommandBorrowerStartsWithSlash_returnsNull() {
        assertNull(Parser.parse("lend 20.00 /unknownflag", ui));
    }

    @Test
    public void parse_loansCommand_returnsLoansCommand() {
        assertTrue(Parser.parse("loans", ui) instanceof LoansCommand);
    }

    @Test
    public void parse_loansCommandAll_returnsLoansCommand() {
        assertTrue(Parser.parse("loans /all", ui) instanceof LoansCommand);
    }

    @Test
    public void parse_loansCommandInvalidArg_returnsNull() {
        assertNull(Parser.parse("loans xyz", ui));
    }

    @Test
    public void parse_loansCommandSimilarName_returnsNull() {
        assertNull(Parser.parse("loansifajo", ui));
    }

    @Test
    public void parse_repayCommandValid_returnsRepayCommand() {
        assertTrue(Parser.parse("repay 1", ui) instanceof RepayCommand);
    }

    @Test
    public void parse_repayCommandNoArguments_returnsNull() {
        assertNull(Parser.parse("repay", ui));
    }

    @Test
    public void parse_repayCommandInvalidIndex_returnsNull() {
        assertNull(Parser.parse("repay abc", ui));
    }

    @Test
    public void parse_repayCommandZeroIndex_returnsNull() {
        assertNull(Parser.parse("repay 0", ui));
    }

    @Test
    public void parse_repayCommandNegativeIndex_returnsNull() {
        assertNull(Parser.parse("repay -1", ui));
    }

    @Test
    public void parse_repayCommandExtraTokens_returnsNull() {
        assertNull(Parser.parse("repay 1 extra", ui));
    }

    @Test
    public void parse_findCommandWithCategory_returnsFindCommand() {
        Command command = Parser.parse("find /c Food", ui);
        assertTrue(command instanceof FindCommand);
    }

    @Test
    public void parse_findCommandKeywordAndCategory_returnsFindCommand() {
        Command command = Parser.parse("find coffee /c Food", ui);
        assertTrue(command instanceof FindCommand);
    }

    @Test
    public void parse_findCommandEmptyCategoryValue_returnsNull() {
        assertNull(Parser.parse("find /c", ui));
    }

    @Test
    public void parse_findCommandDateMin_returnsFindCommand() {
        assertTrue(Parser.parse("find /dmin 2026-01-01", ui) instanceof FindCommand);
    }

    @Test
    public void parse_findCommandDateMax_returnsFindCommand() {
        assertTrue(Parser.parse("find /dmax 2026-12-31", ui) instanceof FindCommand);
    }

    @Test
    public void parse_findCommandDateRange_returnsFindCommand() {
        assertTrue(Parser.parse("find /dmin 2026-01-01 /dmax 2026-06-30", ui)
                instanceof FindCommand);
    }

    @Test
    public void parse_findCommandInvalidDateMin_returnsNull() {
        assertNull(Parser.parse("find /dmin baddate", ui));
    }

    @Test
    public void parse_findCommandAmountMin_returnsFindCommand() {
        assertTrue(Parser.parse("find /amin 5.00", ui) instanceof FindCommand);
    }

    @Test
    public void parse_findCommandAmountMax_returnsFindCommand() {
        assertTrue(Parser.parse("find /amax 20.00", ui) instanceof FindCommand);
    }

    @Test
    public void parse_findCommandAmountRange_returnsFindCommand() {
        assertTrue(Parser.parse("find /amin 5 /amax 20", ui) instanceof FindCommand);
    }

    @Test
    public void parse_findCommandInvalidAmountMin_returnsNull() {
        assertNull(Parser.parse("find /amin abc", ui));
    }

    @Test
    public void parse_findCommandInvalidAmountMax_returnsNull() {
        assertNull(Parser.parse("find /amax xyz", ui));
    }

    @Test
    public void parse_findCommandSortAsc_returnsFindCommand() {
        assertTrue(Parser.parse("find /c Food /sort asc", ui) instanceof FindCommand);
    }

    @Test
    public void parse_findCommandSortDesc_returnsFindCommand() {
        assertTrue(Parser.parse("find /c Food /sort desc", ui) instanceof FindCommand);
    }

    @Test
    public void parse_findCommandSortInvalid_returnsNull() {
        assertNull(Parser.parse("find /c Food /sort sideways", ui));
    }

    @Test
    public void parse_findCommandAllFilters_returnsFindCommand() {
        assertTrue(Parser.parse(
                "find lunch /c Food /dmin 2026-01-01 /dmax 2026-12-31 /amin 1 /amax 50 /sort asc",
                ui) instanceof FindCommand);
    }

    @Test
    public void parse_emptyString_returnsNull() {
        assertNull(Parser.parse("", ui));
    }

    @Test
    public void parse_listWithArgs_returnsNull() {
        assertNull(Parser.parse("list extra", ui));
    }

    @Test
    public void parse_helpWithArgs_returnsNull() {
        assertNull(Parser.parse("help me", ui));
    }

    @Test
    public void parse_totalWithArgs_returnsNull() {
        assertNull(Parser.parse("total stuff", ui));
    }

    @Test
    public void parse_exitWithArgs_returnsNull() {
        assertNull(Parser.parse("exit now", ui));
    }

    @Test
    public void parse_deleteNegativeIndex_returnsNull() {
        assertNull(Parser.parse("delete -1", ui));
    }

    @Test
    public void parse_addCommandNanAmount_returnsNull() {
        assertNull(Parser.parse("add NaN Coffee", ui));
    }

    @Test
    public void parse_addCommandInfinityAmount_returnsNull() {
        assertNull(Parser.parse("add Infinity Coffee", ui));
    }

    @Test
    public void parse_editCommandEmptyCategoryInMiddle_returnsNull() {
        assertNull(Parser.parse("edit 1 /c /da 2026-04-01", ui));
    }

    @Test
    public void parse_lendCommandEmptyDateValue_returnsNull() {
        assertNull(Parser.parse("lend 20.00 John /da", ui));
    }

    @Test
    public void parse_lendCommandInfinityAmount_returnsNull() {
        assertNull(Parser.parse("lend Infinity John", ui));
    }

    @Test
    public void parse_lendCommandNanAmount_returnsNull() {
        assertNull(Parser.parse("lend NaN John", ui));
    }

    @Test
    public void parse_lendCommandDateBeforeBorrower_returnsLendCommand() {
        assertTrue(Parser.parse("lend 20.00 /da 2026-04-01 John", ui) instanceof LendCommand);
    }
}
