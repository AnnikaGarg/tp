# Project Portfolio Page — SpendSwift

## Overview
**SpendSwift** is a desktop application for managing personal finances, optimized for Command Line Interface (CLI) users. It allows university students to track expenses, manage budgets, and analyze spending habits through a fast, text-based interface.

## Summary of Contributions
This section summarizes my specific contributions to the project, including the core architecture, interactive features, and technical documentation.

* **Code Contributed**: https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2026-02-20T00%3A00%3A00&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=aruyadav13&tabRepo=AY2526S2-CS2113-F14-1%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code~other&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false

* **Feature**: **Interactive Category Selection**
    * **What it does**: Allows the user to add an expense without a category flag; the system then pauses to provide an interactive, numbered list of existing categories for selection or allows for the creation of a new one.
    * **Justification**: This feature significantly improves the user experience by preventing "accidental" categorization into a default category and allows for a more dynamic, personalized data set.
    * **Highlights**: This required managing application state during the execution phase of a command, ensuring that the `Ui` and `Model` remained decoupled while waiting for asynchronous user input.

* **Feature**: **Core Architecture & Command Dispatching**
    * **What it does**: Built the foundational skeleton for `SpendSwift`, including the `Parser` logic and the `Command` execution pattern.
    * **Justification**: This established the internal "language" of the app, enabling teammates to build modular features (like `edit`, `total`, or `stats`) that plugged directly into a stable execution loop.

* **Feature**: **Predictive Spending Forecast**
  * **What it does**: Analyzes current monthly spending, calculates the daily burn rate, and projects the total end-of-month expenditure. It dynamically warns the user if they are trending to exceed their monthly budget.
 
* **Feature**: **Strict Data Validation & Unit Testing**
    * **What it does**: Implemented rigorous validation for `Expense` attributes (blocking zero/negative amounts and future-dated entries) and authored a comprehensive test suite.
    * **Highlights**: Achieved **100% Line and Method coverage** for the `Expense` and `ExpenseList` classes, ensuring the "Model" of our application is bug-free and handles all edge cases (nulls, infinite doubles, etc.).

* **Contributions to Team Tasks**:
    * Managed the resolution of global `Scanner` bugs to ensure CI/CD stability.
    * Maintained the `data/` persistence logic to handle corrupted save files gracefully.
    * Standardized the Checkstyle configuration for the team to ensure consistent code quality.

* **Contributions to the UG**:
    * Authored the `add`, and `exit` command sections.
    * Created the "Command Summary" cheat sheet for quick user reference.

* **Contributions to the DG**:
    * Authored the implementation details for the **Interactive Category Selection** feature.
    * Created and integrated UML sequence diagrams for three distinct execution phases (UI Interaction, Category Resolution, and Finalization).

---

## Contributions to the User Guide (Extracts)

### Adding an expense: `add`
Adds a new expense to your tracking list.

**Format:** `add AMOUNT DESCRIPTION [/c CATEGORY] [/da YYYY-MM-DD]`

* **Interactive Prompt:** If the `/c` (category) flag is omitted, the application will pause and display a numbered list of all your existing categories. You can type a number to select an existing category, or type a new word to create a brand-new category on the fly!

---

## Contributions to the Developer Guide (Extracts)

### Interactive Category Selection
**Implementation**
The interactive category selection mechanism is primarily orchestrated by the `AddCommand` class, acting as the controller. It interacts with the `Ui` class for presentation and the `ExpenseList` class for state management.

To ensure the architecture remains decoupled, the execution flow is broken down into three distinct phases: **UI Interaction**, **Category Resolution**, and **Expense Finalization**.

#### Phase 1: The UI Interaction Prompt
When `AddCommand#execute(ExpenseList)` is invoked, it evaluates the `category` field. If this field is `null`, the command intercepts the normal execution flow.

1. `AddCommand` fetches the current master list of categories from `ExpenseList`.
2. It invokes `Ui#showCategoryPrompt()`, which formats the terminal output.
3. It suspends execution via `Ui#getUserInput()`, waiting for the user's selection.

*(Images omitted for page limit constraints)*