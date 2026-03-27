# Developer Guide

## Acknowledgements

{list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

## Design & implementation

{Describe the design and implementation of the product. Use UML diagrams and short code snippets where applicable.}

### Edit Expense Feature

The edit feature allows users to modify one or more fields of an existing expense using the `edit` command.

**How it works:**

The user provides a 1-based index followed by one or more optional flags:
- `/amount` to update the monetary value
- `/desc` to update the description
- `/c` to update the category
- `/d` to update the date (must follow `YYYY-MM-DD` format)

At least one flag must be supplied; omitted fields retain their existing values.

**Implementation:**

`Parser.parseEditCommand()` extracts the index and each flag from the input string sequentially.
Each flag is located by its keyword, its value is extracted up to the next `/` or end of input, and then stripped from the working string before the next flag is processed.
This allows flags to appear in any order without ambiguity.

Once all fields are parsed, an `EditCommand` is constructed with nullable fields for each of the four attributes.
In `EditCommand.execute()`, the existing `Expense` at the given index is retrieved, each non-null field replaces the corresponding existing value, and a new `Expense` object is created and written back via `ExpenseList.setExpense()`.

**Design considerations:**

`Expense` objects are immutable (all fields are `final`), so editing produces a new `Expense` rather than mutating the existing one.
An alternative considered was making `Expense` mutable, but immutability was preferred to avoid unintended side effects across the codebase.

### Category and Date Parsing (Add Command)

The `add` command supports two optional flags: `/c` for category and `/d` for date.

`Parser.parseAddCommand()` first extracts the mandatory amount, then strips `/d` and `/c` flags from the remaining input one at a time.
The date token is parsed with `ResolverStyle.STRICT` to reject impossible calendar dates such as `2026-02-30`.
Whatever text remains after both flags are removed becomes the description, which means the description does not need to appear in a fixed position relative to the flags.

If no category is supplied, `Expense` defaults to `"Others"`. If no date is supplied, it defaults to today's date via `LocalDate.now()`.

### Input Validation (Strict Commands)

The `list`, `help`, `exit`, and `total` commands do not accept any arguments.
If trailing text is detected after these keywords, the parser calls `ui.showUnknownCommand()` and returns `null`, preventing silent misinterpretation of user input such as `help something` or `exit now`.

### Budget Feature

The budget feature allows users to set a spending limit using the `budget` command.
The budget value is stored in `ExpenseList`, which is responsible for tracking total expenses and checking if the budget is exceeded.
When an expense is added through `AddCommand`, the system calls `isOverBudget()` to determine whether a warning should be displayed.
The budget is persisted in `Storage` as a special line and reloaded when the application starts.
This design keeps business logic within `ExpenseList`, ensuring separation of concerns between data handling and command execution.
An alternative approach considered was performing budget checks inside `AddCommand`, but this was avoided to maintain cleaner object-oriented design.

## Product scope

### Target user profile

* **Demographic:** University students (like those at NUS) and young professionals.
* **Habits:** Spends a lot of time on their computer/terminal, prefers typing over mouse interactions, and wants a fast, no-nonsense way to log daily expenses (like meals and transport).
* **Needs:** Needs a way to enforce a strict budget, categorize spending, and maintain data locally without relying on cloud services or slow mobile apps.

### Value proposition

SpendSwift solves the problem of friction in financial tracking. Most budgeting apps require navigating multiple menus and screens just to log a $5 coffee. SpendSwift allows power users to log, edit, and review their finances instantly using simple Command Line Interface (CLI) commands, keeping their hands on the keyboard and their focus unbroken.

## User Stories

|Version| As a ... | I want to ... | So that I can ...|
|--------|----------|---------------|------------------|
|v1.0|new user|see usage instructions|refer to them when I forget how to use the application|
|v1.0|user|add an expense with a description and amount|keep track of what I have spent|
|v1.0|user|delete an expense by index|remove entries I added by mistake|
|v2.0|user|assign a category and date to an expense|organise my spending history|
|v2.0|user|edit an existing expense|correct mistakes without deleting and re-adding entries|
|v2.0|user|find a to-do item by name|locate a to-do without having to go through the entire list|

## Non-Functional Requirements

1. **Performance:** The system should respond to any user command within 2 seconds.
2. **Portability:** The application must work seamlessly across Windows, macOS, and Linux environments, provided Java 11 or higher is installed.
3. **Data Integrity:** The application must safely persist data to a local text file (`data/expenses.txt`) and be able to recover or skip corrupted lines without crashing.
4. **Usability:** A user with average typing speed should be able to log a new expense faster than using a GUI-based mobile application.

## Glossary

* **CLI (Command Line Interface)** - A text-based user interface used to interact with the application by typing commands.
* **Architecture** - The overall structural design of the software, determining how different components (like Parser, Storage, and Commands) interact.
* **Persisted Data** - Information that is saved to the user's hard drive (in `expenses.txt`) so it is not lost when the application closes.

## Instructions for manual testing

Given below are instructions to test the app manually.

### Launch and Shutdown
1. **Initial launch:** Download the latest `.jar` file and copy it into an empty folder.
2. Open your terminal, navigate to the folder, and run `java -jar SpendSwift.jar`.
   * *Expected:* The welcome message appears, and a `data` folder is created in the same directory.
3. **Shutdown:** Type `exit` and press Enter.
   * *Expected:* The farewell message is shown and the application terminates.

### Testing the Add Command (v2.0 Features)
1. **Test adding with all parameters:**
   * Run: `add 5.50 Chicken Rice /c food /d 2026-03-24`
   * *Expected:* The expense is added. Typing `list` should show the expense with `[Cat: food]` and `[Date: Mar 24 2026]`.
2. **Test default parameters:**
   * Run: `add 2.00 Bus`
   * *Expected:* The expense is added. Typing `list` should show it defaults to `[Cat: Others]` and today's date.
3. **Test invalid date format:**
   * Run: `add 10.00 Movie /d 24-03-2026`
   * *Expected:* An error message prompts the user to use the `YYYY-MM-DD` format. The expense is *not* added.

### Testing the Budget Feature
1. **Setting a budget:**
   * Run: `budget 50`
   * *Expected:* A confirmation message states the budget is set to $50.00.
2. **Exceeding the budget:**
   * Run: `add 60.00 Textbook`
   * *Expected:* The expense is added, but the UI triggers a "Budget Exceeded" warning message.