# Project Portfolio Page — SpendSwift

## Overview
**SpendSwift** is a desktop application for managing personal finances, optimized for Command Line Interface (CLI) users. It allows university students to track expenses, manage budgets, and analyze spending habits through a fast, text-based interface.

## Summary of Contributions
This section summarizes my specific contributions to the project, including the user interface, monthly budget system, expense listing functionality, and technical documentation.

* **Code Contributed**: [View my code on RepoSense](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2026-02-20T00%3A00%3A00&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=AnnikaGarg&tabRepo=AY2526S2-CS2113-F14-1%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)

* **Feature**: **Centralised User Interface Component (`Ui`)**
  * **What it does**: Improved the shared `Ui` component that handles user-facing input and output in SpendSwift, including confirmation messages, warnings, usage hints, and interactive prompts.
  * **Highlights**: Centralised presentation logic so that formatting remains consistent across commands such as `budget`, `list`, and `help`, while command classes remain focused on execution logic.

* **Feature**: **Monthly Budget Management System (`budget`)**
  * **What it does**: Implemented and documented the `budget` feature, which allows users to set, update, and view monthly budgets for the current month or a specified month.
  * **Highlights**: Extended the system to support **month-specific budgets** instead of a single global budget, and handled both viewing and updating modes, including budget status display and warnings when spending exceeds the defined monthly limit.

* **Feature**: **Expense Listing System (`list`)**
  * **What it does**: Implemented and documented the `list` feature so users can either display all recorded expenses or filter expenses by a specific month.
  * **Highlights**: Kept the interface compact by supporting both full-list and month-filtered output through a single command, with validation for invalid month input and clear output through `Ui`.

* **Contributions to the UG**:
  * Authored the `list` and `budget` command sections, including command formats, rules, examples, and expected behaviour.
  * Contributed the **Notes on User Interaction** section covering confirmations, warnings, prompts, and read-only command behaviour.

* **Contributions to the DG**:
  * Authored the **Ui Component**, **List Feature**, and **Budget Feature** sections.
  * Documented the design rationale, parsing flow, command execution logic, and implementation considerations for these components.

---

## Contributions to the User Guide (Extracts)

### Listing expenses: `list`
**Format:** `list [YYYY-MM]`

Shows all recorded expenses, or only expenses from a specified month.

### Setting and viewing a monthly budget: `budget`
**Format:** `budget`, `budget AMOUNT`, `budget YYYY-MM`, `budget YYYY-MM AMOUNT`

Allows users to set, update, and view monthly budgets, and shows how much has been spent or exceeded.

---

## Contributions to the Developer Guide (Extracts)

### Ui Component
The `Ui` component centralises all user-facing input and output, keeping presentation logic separate from command execution logic.

### List Feature
`Parser.parseListCommand()` parses an optional `YYYY-MM` argument, while `ListCommand.execute()` displays either the full expense list or a month-filtered list.

### Budget Feature
`Parser.parseBudgetCommand()` supports both view and set/update modes. `BudgetCommand.execute()` stores month-specific budgets, retrieves monthly spending totals, and displays the corresponding budget status.