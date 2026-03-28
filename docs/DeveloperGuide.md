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

---

### Sort Feature

The sort feature allows users to reorder their expense list either **alphabetically by category** or **chronologically by date** using the command `sort category` or `sort date`.

**How it works:**

The user types `sort` followed by exactly one criterion — `category` or `date`. Any other argument causes the parser to show a usage hint and return `null` without creating a command.

**Implementation:**

Below is the sequence of interactions when the user enters `sort category`:

```
@startuml
actor User
participant "Parser" as P
participant "SortCommand" as SC
participant "ExpenseList" as EL
participant "Ui" as UI

User -> P : parse("sort category", ui)
P -> P : commandWord = "sort"
P -> P : arguments = "category"
P -> SC : new SortCommand(ui, "category")
SC --> P : sortCommand
P --> User : sortCommand

User -> SC : execute(expenseList)
SC -> EL : sortExpenses(BY_CATEGORY)
EL -> EL : expenses.sort(comparator)
EL --> SC : (sorted in place)
SC -> UI : showSorted(expenseList, "category")
UI --> SC : (list printed)
SC --> User : (done)
@enduml
```

`SortCommand` delegates the actual reordering to `ExpenseList.sortExpenses(Comparator)`, which calls `ArrayList.sort()` in place. Two static `Comparator<Expense>` constants are pre-defined in `SortCommand`:

- `BY_CATEGORY` — uses `String.CASE_INSENSITIVE_ORDER` on `Expense.getCategory()`.
- `BY_DATE` — uses the natural order of `LocalDate` via `Expense.getDate()`.

Because the sort modifies the list order that is persisted to file, `SortCommand.shouldPersist()` returns `true`, triggering a save after execution.

**Design Considerations:**

- **Why sort in place?** Mutating the list directly ensures that the sorted order is reflected in subsequent `list` commands and is saved to disk without extra copying.
- **Why static Comparators?** Declaring them as `public static final` fields on `SortCommand` makes them easily reusable and testable in isolation, without coupling the comparator logic to any single instance.
- **Alternative considered:** Returning a new sorted list and replacing the existing one. This was rejected because it would require `ExpenseList` to expose a method for replacing all its contents, adding unnecessary surface area to the API.

---

### Statistics Feature

The statistics feature provides a per-category breakdown of total spending using the `stats` command.

**How it works:**

The user types `stats` with no arguments. Trailing text is not allowed; if any arguments are detected, the parser shows an unknown-command message and returns `null`.

**Implementation:**

Below is the sequence of interactions when the user enters `stats`:

```
@startuml
actor User
participant "Parser" as P
participant "StatisticsCommand" as STC
participant "ExpenseList" as EL
participant "Ui" as UI

User -> P : parse("stats", ui)
P -> P : commandWord = "stats"
P -> P : arguments = "" (empty)
P -> STC : new StatisticsCommand(ui)
STC --> P : statsCommand
P --> User : statsCommand

User -> STC : execute(expenseList)
STC -> STC : totals = new LinkedHashMap<>()
loop for each expense in expenseList
    STC -> EL : getExpense(i)
    EL --> STC : expense
    STC -> STC : totals.merge(category, amount)
end
STC -> UI : showStatistics(totals, count)
UI --> STC : (stats printed)
STC --> User : (done)
@enduml
```

`StatisticsCommand.execute()` iterates over every expense in the list and accumulates per-category totals into a `LinkedHashMap<String, Double>`. Using a `LinkedHashMap` preserves the **insertion order**, so categories are printed in the order they first appear in the list — giving the output a predictable, intuitive feel.

The final map and expense count are then passed to `Ui.showStatistics()`, which formats each category-total pair as `CategoryName: $X.XX`.

Because `stats` is a read-only query, `StatisticsCommand.shouldPersist()` returns `false` — no file write is triggered.

**Design Considerations:**

- **Why `LinkedHashMap` instead of `HashMap`?** A plain `HashMap` has non-deterministic iteration order, which would cause the printed output to vary between runs. `LinkedHashMap` maintains insertion order at negligible extra cost.
- **Why `TreeMap` was not used?** `TreeMap` would sort categories alphabetically, which is a different concern from counting. Keeping the order user-defined (insertion order) is more intuitive for the `stats` command.
- **Alternative considered:** Computing statistics inside `Ui` itself. This was rejected because it would embed business logic in the presentation layer, violating the separation-of-concerns principle.

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