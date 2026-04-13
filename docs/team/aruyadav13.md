# Project Portfolio Page — SpendSwift

## Overview
**SpendSwift** is a desktop application for managing personal finances, optimized for Command Line Interface (CLI) users. It allows university students to track expenses, manage budgets, and analyze spending habits through a fast, text-based interface.

## Summary of Contributions
This section summarizes my specific contributions to the project, including the core architecture, interactive features, and technical documentation.

* **Code Contributed**: [View my code on RepoSense](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2026-02-20T00%3A00%3A00&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=aruyadav13&tabRepo=AY2526S2-CS2113-F14-1%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code~other&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)

* **Feature**: **Core Architecture & Data Models**
  * **What it does**: Set up the initial repository and engineered the foundational `Parser` logic and `Command` execution loop. Built the central `Expense` object and the `ExpenseList` state manager to handle budget tracking and chronological sorting.
  * **Highlights**: Implemented rigorous validation for attributes (blocking negative amounts and future-dated entries) and authored a comprehensive test suite achieving **90% coverage** for core models.

* **Feature**: **Comprehensive Expense Creation Engine (`AddCommand`)**
  * **What it does**: Drives the core functionality of logging expenses, featuring a robust parsing engine that extracts optional date (`/da`) and category (`/c`) flags using dynamic string manipulation.
  * **Interactive Fallback**: If the user omits the category flag, the system pauses execution to provide an interactive, numbered list of existing categories for selection or on-the-fly creation.

* **Feature**: **Predictive Spending Forecast (`ForecastCommand`)**
  * **What it does**: Analyzes current monthly spending, calculates the daily burn rate, and projects the total end-of-month expenditure. It dynamically warns the user if they are trending to exceed their monthly budget.

* **Contributions to Team Tasks**:
  * Maintained CI/CD stability by resolving global `Scanner` and storage persistence bugs.
  * Resolved critical Practical Exam (PE) bugs by implementing strict trailing-argument validation and capping floating-point limits to 1 Trillion to prevent memory overflow.

* **Contributions to the UG**:
  * Authored the `add` (including the interactive category prompt), `forecast`, and `exit` command sections, alongside the Command Summary cheat sheet.

* **Contributions to the DG**:
  * Authored the implementation details and design considerations for the **`AddCommand`** (parsing and interactive category selection) and **`ForecastCommand`** features.
  * Created and integrated UML sequence diagrams detailing execution logic and budget validation phases.

---

## Contributions to the User Guide (Extracts)

### Adding an expense: `add`
Adds a new expense to your tracking list.

**Format:** `add AMOUNT [/c CATEGORY] [/da YYYY-MM-DD] DESCRIPTION`

* **Flexible Input:** The `/c` and `/da` flags can be supplied in any order.
* **Interactive Prompt:** If the `/c` (category) flag is omitted, the application will pause and display a numbered list of all your existing categories. You can type a number to select an existing category, or type a new word to create a brand-new category on the fly.

### Forecasting spending: `forecast`
Predicts your end-of-month total spending based on your current daily habits.

**Format:** `forecast`
* Calculates your average daily spend (burn rate) for the current month and projects your total expenses for the final day of the month. Warns you if you are on track to exceed your set budget.

---

## Contributions to the Developer Guide (Extracts)

### Expense Creation (`AddCommand`) & Interactive Category Selection
**Implementation:** The `add` feature execution is decoupled into two phases to maintain strict separation between string interpretation and system state mutation:
1. **Parsing Phase:** `Parser.parseAddCommand()` extracts the mandatory amount, then dynamically strips the optional `/c` and `/da` flags from the remaining input.
2. **Execution Phase:** `AddCommand.execute()` intercepts the standard execution flow if a category is missing, triggering a suspended terminal prompt via `Ui` to resolve the category interactively. It then instantiates the `Expense` object and verifies budget constraints.

### Predictive Spending Forecast
**Implementation:** The `forecast` feature calculates predictive spending without mutating application state (`shouldPersist()` returns `false`). It divides the current month's total spending by the current day of the month to establish a `dailyBurnRate`, and multiplies this by `lengthOfMonth()`. It includes defensive logic to treat the 0th index as `day 1` to prevent `ArithmeticException` (division by zero) at the exact start of a new month.