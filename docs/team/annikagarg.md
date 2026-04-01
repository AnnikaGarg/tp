# Project Portfolio Page - SpendSwift

## Overview
**SpendSwift** is a desktop application for managing personal finances, optimized for Command Line Interface (CLI) users. It allows university students to track daily expenses, enforce budgets, and analyze their spending habits through a fast, text-based interface.

## Summary of Contributions
This section summarizes my specific contributions to the project. My primary focus was implementing budget management, refining the UI interaction layer, and improving test coverage to ensure correctness and robustness of core features.

* **Code Contributed:** [Repo Dashboard Link](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=&tabAuthor=AnnikaGarg&sort=groupTitle%20dsc&sortWithin=title&since=2026-02-20T00%3A00%3A00&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=false&filteredFileName=&tabOpen=true&tabType=authorship&tabRepo=AY2526S2-CS2113-F14-1%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)

* **Enhancements Implemented**:
  * **Budget Management System (`BudgetCommand` + `ExpenseList`)**: Designed and implemented the budget tracking feature that allows users to set, monitor, and evaluate their spending against a defined limit.
    * *Justification:* Budgeting is a core requirement for university students managing limited finances. Without a budget system, expense tracking lacks actionable insight.
    * *Highlights:* Implemented validation logic to reject invalid budgets (zero/negative values) and integrated budget checks directly into the expense addition workflow. Centralised the budget state within `ExpenseList` to ensure consistency between expense totals and budget evaluation.
  * **UI Interaction Layer (`Ui` class)**: Structured and refined the `Ui` component to handle all user-facing input and output across the application.
    * *Justification:* Separating presentation logic from command logic improves maintainability and ensures consistent user experience across all commands.
    * *Highlights:* Designed a unified interface for displaying confirmations, warnings, and summaries (e.g., `showAddExpense`, `showBudgetSet`, `showBudgetExceededWarning`). Enabled interactive workflows (such as category selection) without coupling UI logic to business logic.
  * **Command Execution Testing & Coverage Improvements**: Expanded and strengthened the test suite to improve coverage of core command classes.
    * *Justification:* CLI applications rely heavily on command correctness; edge cases must be handled reliably to prevent runtime failures.
    * *Highlights:* Enhanced `AddCommandTest` and `BudgetCommandTest` to cover multiple execution paths, including edge cases such as zero values, repeated updates, and invalid inputs. Ensured correctness of both normal and failure scenarios, improving overall system robustness.

* **Contributions to the UG**:
  * Authored and refined the usage sections for the `add` and `budget` commands.
  * Improved clarity by documenting command formats, examples, and edge cases (e.g., invalid inputs and interactive prompts).
  * Added explanations for UI behaviour such as warnings and interactive category selection.

* **Contributions to the DG**:
  * Authored the "Design & Implementation" sections for the **Add Feature**, **Budget Feature**, and **Ui Component**.
  * Created and integrated UML diagrams, including:
    * Sequence diagram for `AddCommand`
    * Sequence diagram for `BudgetCommand`
    * Class/component diagram for the `Ui` component
  * Documented execution flow from parsing to command execution, with emphasis on separation of concerns and modular design.

* **Contributions to Team-based Tasks**:
  * Ensured consistency with Checkstyle requirements and resolved formatting issues across multiple files.
  * Assisted in debugging integration issues between command execution and UI output.
  * Helped maintain overall code quality during feature integration.

* **Review/Mentoring Contributions**:
  * Reviewed teammates’ pull requests, focusing on correctness, edge case handling, and adherence to coding standards.
  * Provided guidance on improving test coverage and structuring command-based logic.

---

---

## Contributions to the User Guide (Extracts)

### Adding an expense: `add`
Adds a new expense to your tracking list.

**Format:** `add AMOUNT DESCRIPTION [/c CATEGORY] [/da YYYY-MM-DD]`

* `AMOUNT` must be a valid number greater than 0.
* If `/da` (date) is not provided, the expense will default to the current date.
* If `/c` (category) is not provided, an interactive prompt will appear.

**Interactive Prompt:**
If no category is specified, SpendSwift will display a numbered list of available categories. You may:
- Enter a number to select an existing category
- Type a new category name to create one

---

## Contributions to the Developer Guide (Extracts)

### Budget Feature

The budget feature allows users to set a spending limit and monitor their spending against that limit using the `budget` command.

**How it works:**
The user may enter:
- `budget AMOUNT` to set a budget
- `budget` to view the current budget status