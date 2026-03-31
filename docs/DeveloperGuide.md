# Developer Guide

## Acknowledgements

{list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

## Design & implementation

### Delete Feature

The delete feature allows users to remove an existing expense from their tracking list by providing its 1-based index (e.g., `delete 1`).

**How it works:**

The user types `delete` followed by a single positive integer representing the expense's index.

**Implementation:**

`Parser.parseDeleteCommand()` splits the input string. It first verifies there is exactly one argument and then attempts to parse it into an integer. If the argument is missing, non-numeric, or zero/negative, it catches the parsing issues (or returns `null`) and tells `Ui` to show an invalid index message.

A valid integer index results in the instantiation of a `DeleteCommand`.

Below is the sequence of interactions when the user enters a valid command like `delete 1`:

*Figure 1: Sequence Diagram detailing the Delete feature execution.*
![Sequence Diagram for Delete Command](images/delete-expense-diagram.png)

`DeleteCommand.execute()` operates by:
1. Validating that the given index is greater than `0`.
2. Attempting to call `ExpenseList.deleteExpense(index - 1)`. 
3. Catching an `IndexOutOfBoundsException` if the index given is larger than the actual list's bounds, showing an error via the `Ui`.
4. Successfully removing the item and showing a success message via `Ui.showDeleteExpense()`.

Because deletion permanently removes persisted data, `DeleteCommand.shouldPersist()` returns `true`, triggering a file save sequentially.

### Edit Expense Feature

The edit feature allows users to modify one or more fields of an existing expense using the `edit` command.

**How it works:**
The user provides a 1-based index followed by one or more optional flags:
- `/a` to update the monetary value
- `/de` to update the description
- `/c` to update the category
- `/da` to update the date (must follow `YYYY-MM-DD` format)

At least one flag must be supplied; omitted fields retain their existing values.

**Implementation:**
`Parser.parseEditCommand()` extracts the index and each flag from the input string sequentially. Each flag is located by its keyword using `indexOf()`, its value is extracted up to the next `/` or the end of the input, and then it is stripped from the working string before the next flag is processed. This substring manipulation algorithm allows flags to appear in any order without ambiguity.

Once all fields are parsed, an `EditCommand` is constructed with nullable fields for each of the four attributes.

Below is the sequence of interactions when the user enters a valid command like `edit 1 /a 15.0`:

*Figure 2: Sequence Diagram detailing the Edit feature execution.*
![Sequence Diagram for Edit Command](images/edit-logic-diagram.png)

In `EditCommand.execute()`, the existing `Expense` at the given index is retrieved, each non-null field replaces the corresponding existing value, and a new `Expense` object is created and written back via `ExpenseList.setExpense()`.

**Design considerations:**
`Expense` objects are immutable (all fields are `final`), so editing produces a new `Expense` rather than mutating the existing one. An alternative considered was making `Expense` mutable with setter methods, but immutability was preferred to avoid unintended side effects across the codebase.

### Category and Date Parsing

Commands like `add` and `edit` support optional flags such as `/c` for category and `/da` for date.

`Parser` implements a robust stripping algorithm. For example, in `Parser.parseAddCommand()`, it first extracts the mandatory amount, then strips the `/da` and `/c` flags from the remaining input one at a time. The date token is parsed with `ResolverStyle.STRICT` to reject impossible calendar dates such as `2026-02-30`. Whatever text remains after the specified flags are removed becomes the description, which means the description does not need to appear in a fixed position relative to the flags.

### Loan Tracking System

The Loan Tracking System allows users to manage debts separately from their primary expenses.

**Implementation:**
The system is centered around the `Loan` class, which extends the `Expense` class to reuse validation logic but introduces a `borrowerName` and an `isRepaid` boolean flag.

The ledger is managed by three specific commands:

1. **LendCommand**: Instantiates a `Loan` object and adds it to the internal `ArrayList<Loan>` managed by `ExpenseList`.

Below is the sequence of interactions when the user enters a valid command like `lend 20 Alice`:

*Figure 3: Sequence Diagram detailing the Lend feature execution.*
![Sequence Diagram for Lend Command](images/loan-logic-diagram.png)

2. **LoansCommand**: Queries the `Ui` to display the current outstanding balance. It handles the `/all` flag to show both outstanding and settled debts.
3. **RepayCommand**: Rather than using an absolute index of the entire loan array, `RepayCommand` fetches a filtered list via `ExpenseList.getOutstandingLoans()`. The user's 1-based index is mapped to this dynamic list, and the selected loan is marked as repaid.

**Storage Integration:**
To persist this parallel data structure, the `Storage` class was modified to support multiple data types in a single file. Loan entries are prefixed with the `LOAN |` marker (e.g., `LOAN | 20.0 | 2026-04-01 | Alice | false`). During `load()`, the `Storage` class identifies this prefix, parses the loan using `parseLoanLine()`, and routes it to `ExpenseList.addLoan()` rather than the standard expense list.

**Design Consideration:**
- **Separate Ledgers**: We chose to keep loans in a separate list rather than the main `ExpenseList` to prevent temporary debt from skewing the "Statistics" and "Budget" features, which are intended strictly for personal spending analysis.
- **Dynamic Repayment Indexing**: By mapping the `repay INDEX` to the *outstanding* loans list rather than the full historical list, we vastly improved the UX, preventing the user from having to manually count past, settled debts.

### Interactive Category Selection

**Overview**
The application features a dynamic, interactive category selection mechanism. When a user attempts to add an expense without explicitly providing a category flag (`/c`), the application gracefully pauses execution, displays a numbered list of available categories, and prompts the user to select an existing category or input a new one.

This feature ensures that users do not accidentally pollute a default "Others" category due to forgetfulness, maintaining the integrity of their financial tracking while providing a seamless User Experience (UX).

**Implementation**
The interactive category selection mechanism is primarily orchestrated by the `AddCommand` class, acting as the controller. It interacts heavily with the `Ui` class for presentation and the `ExpenseList` class for state management.

To ensure the architecture remains decoupled and follows the Single Responsibility Principle, the execution flow is broken down into three distinct phases: **UI Interaction**, **Category Resolution**, and **Expense Finalization**.

#### Phase 1: The UI Interaction Prompt
When `AddCommand#execute(ExpenseList)` is invoked, it first evaluates the `category` field parsed from the user's initial input. If this field is `null`, the command intercepts the normal execution flow to query the user.

1. `AddCommand` fetches the current master list of categories from `ExpenseList`.
2. It passes this list to `Ui#showCategoryPrompt()`, which formats and prints a numbered list to the terminal.
3. `AddCommand` then suspends execution by calling `Ui#getUserInput()`, waiting for the user to type their selection.

*Figure 4: Sequence Diagram detailing the UI Interaction phase.*
![Phase 1 Sequence Diagram](images/interactive-category-phase1.png)

#### Phase 2: Dynamic Category Resolution
Once the user provides an input string, `AddCommand` must determine if the user typed a number (selecting an existing category) or a word (creating a brand new category).

If the user types a new category name (e.g., "Snacks"), `AddCommand` delegates the formatting and storage to `ExpenseList`. The `ExpenseList#addCategory()` method formats the string to Title Case (e.g., "snacks" -> "Snacks") and dynamically inserts it into the master list just above the "Others" category. This ensures "Others" always remains safely at the bottom of the user's UI prompt.

*Figure 5: Sequence Diagram detailing the parsing and dynamic storage of a new category.*
![Phase 2 Sequence Diagram](images/interactive-category-phase2.png)

#### Phase 3: Expense Finalization & Budget Checking
With the category definitively resolved (either extracted from the numbered list or dynamically created), the `AddCommand` proceeds to finalize the data mutation.

1. A new `Expense` object is instantiated with the resolved category.
2. The object is appended to the `ExpenseList` via `addExpense()`.
3. `Ui#showAddExpense()` is called to print the success confirmation.
4. Finally, `AddCommand` queries `ExpenseList#isOverBudget()`. If the new expense pushes the total over the user's defined limit, it triggers a warning message via the `Ui`.

*Figure 6: Sequence Diagram detailing the final object creation and budget validation.*
![Phase 3 Sequence Diagram](images/interactive-category-phase3.png)


**Design Considerations: Why it was implemented this way**
* **Strict Decoupling (Single Responsibility Principle):** The `AddCommand` acts strictly as an orchestrator. The `Ui` class knows nothing about how categories are saved, and the `ExpenseList` class knows nothing about `Scanner` inputs. They never communicate directly, which makes the codebase highly testable and modular.
* **The Open-Closed Principle for Categories:** Instead of hardcoding categories inside a Java `Enum` (which would require a code rewrite to add new ones), storing a dynamic `ArrayList<String>` inside `ExpenseList` allows the application to grow with the user's personalized spending habits.

**Alternatives Considered**
* **Alternative 1 (Strict Formatting Validation):** * *Design:* Throw an `IllegalArgumentException` in the `Parser` if the `/c` flag is missing, forcing the user to retype the entire command.
   * *Pros:* Very easy to implement. Keeps `AddCommand` execution strictly linear without needing pauses.
   * *Cons:* Creates a highly frustrating User Experience (UX). Power users typing quickly will constantly hit validation errors for forgetting a simple flag.
* **Alternative 2 (Silent Defaulting):** * *Design:* Automatically assign the expense to an "Others" category without prompting the user.
   * *Pros:* Immediate execution; keeps the `AddCommand` logic simple.
   * *Cons:* Leads to messy, inaccurate financial tracking. Users end up with the majority of their expenses dumped into a useless "Others" category, completely defeating the purpose of a budgeting application. The interactive prompt forces accurate categorization without making the user retype their description and amount.

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

*Figure 7: Sequence Diagram detailing the Sort feature execution.*
![SortCommand Sequence Diagram](images/sort-uml.png)

`SortCommand` delegates the actual reordering to `ExpenseList.sortExpenses(Comparator)`, which calls `java.util.Collections.sort(expenses, comparator)` in place. Two static `Comparator<Expense>` constants are pre-defined in `SortCommand`:

- `BY_CATEGORY` — uses `String.CASE_INSENSITIVE_ORDER` on `Expense.getCategory()`, with a fallback to sort by date (newest first) using `.thenComparing(Expense::getDate, Comparator.reverseOrder())`.
- `BY_DATE` — uses the reverse natural order of `LocalDate` via `Expense.getDate()` so the newest expenses appear first.

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

*Figure 8: Sequence Diagram detailing the Statistics feature execution.*
![StatisticsCommand Sequence Diagram](images/statistics-uml.png)

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
   * Run: `add 5.50 Chicken Rice /c food /da 2026-03-24`
   * *Expected:* The expense is added. Typing `list` should show the expense with `[Cat: food]` and `[Date: Mar 24 2026]`.
2. **Test default parameters:**
   * Run: `add 2.00 Bus`
   * *Expected:* The expense is added. Typing `list` should show it defaults to `[Cat: Others]` and today's date.
3. **Test invalid date format:**
   * Run: `add 10.00 Movie /da 24-03-2026`
   * *Expected:* An error message prompts the user to use the `YYYY-MM-DD` format. The expense is *not* added.

### Testing the Budget Feature
1. **Setting a budget:**
   * Run: `budget 50`
   * *Expected:* A confirmation message states the budget is set to $50.00.
2. **Exceeding the budget:**
   * Run: `add 60.00 Textbook`
   * *Expected:* The expense is added, but the UI triggers a "Budget Exceeded" warning message.
