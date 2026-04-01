# Project Portfolio Page - SpendSwift

## Overview
**SpendSwift** is a desktop application for managing personal finances, optimized for Command Line Interface (CLI) users. It allows university students to track expenses, manage budgets, and analyze spending habits through a fast, text-based interface.

## Summary of Contributions
This section summarizes my specific contributions to the project. My primary focus was building the persistence layer, the search and filter system, the help feature, and driving defensive programming and test coverage across the codebase.

* **Code Contributed:** [Repo Dashboard Link](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=TrijalSrimal&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2026-02-20T00%3A00%3A00&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=TrijalSrimal&tabRepo=AY2526S2-CS2113-F14-1%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code~other&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)

* **Enhancements Implemented**:
    * **Storage & Persistence (`Storage` class)**: Designed and implemented the file-based persistence system that saves and loads expenses, loans, and budget data to `data/expenses.txt`.
        * *Justification:* Without persistence, every session starts from scratch — the application would be unusable for real budgeting.
        * *Highlights:* Engineered a pipe-delimited format (`AMOUNT | DATE | CATEGORY | DESCRIPTION`) with backward compatibility for v1.0 two-field saves. Added graceful handling of malformed or corrupt lines so the application never crashes on bad data. Extended the format to support budget and loan lines when those features were added by teammates.
    * **Find & Filter System (`FindCommand`)**: Built a comprehensive search and filter feature supporting keyword search, category filtering (`/c`), date range filtering (`/dmin`, `/dmax`), amount range filtering (`/amin`, `/amax`), and result sorting (`/sort asc|desc`).
        * *Justification:* As the expense list grows, users need a fast way to locate specific entries without scrolling through everything.
        * *Highlights:* All filters are independently optional and composable. The implementation uses four private predicate methods (`matchesCategory`, `matchesKeyword`, `matchesDateRange`, `matchesAmountRange`), keeping the main loop clean and making it trivial to add new filter dimensions in the future.
    * **Help Feature (`HelpCommand`)**: Authored the help command that displays all available commands with their flags and formats.
        * *Justification:* A CLI application needs an in-app reference so users don't have to leave the terminal to check documentation.
    * **Parser-Command Refactor**: Refactored `SpendSwift` from a monolithic switch-case loop into a clean `Parser` -> `Command.execute()` pattern, and introduced `shouldPersist()` to automatically trigger saves after state-changing commands.
        * *Justification:* The original architecture made it difficult for teammates to add new commands without touching the main loop. The refactored pattern lets each command be developed, tested, and merged independently.
    * **Defensive Programming**: Added assertions across all core classes (`Command`, `DeleteCommand`, `Expense`, `ExpenseList`, `Parser`, `Storage`) following CS2113 defensive programming standards.

* **Contributions to the UG**:
    * Authored the `find`, `delete`, `budget`, `stats`, and `help` command sections.
    * Expanded the Command Summary table to include all commands.

* **Contributions to the DG**:
    * Authored the "Design & Implementation" sections for the **Find / Filter Feature**, **Help Feature**, and **Storage & Persistence**.
    * Created and integrated 3 UML sequence diagrams (FindCommand, HelpCommand, Storage).
    * Authored manual testing instructions for the Find and Help commands.
    * Updated User Stories to cover find, filter, help, and persistence.

* **Contributions to Team-based Tasks**:
    * Drove test coverage from baseline to 89% line coverage / 79% branch coverage across the codebase.
    * Authored integration tests (`SpendSwiftTest`) verifying persistence across restarts, delete-after-add workflows, and edge cases.
    * Authored comprehensive test suites for `FindCommand`, `Storage`, `HelpCommand`, `ListCommand`, and `Parser`.

* **Review/Mentoring Contributions**:
    * Reviewed and merged PRs from teammates, ensuring changes were compatible with the storage persistence flow.

---

## Contributions to the User Guide (Extracts)

### Finding and filtering expenses: `find`
Searches your expense list using a keyword and/or filters. All filters are optional and can be combined freely.

**Format:** `find [KEYWORD] [/c CATEGORY] [/dmin DATE] [/dmax DATE] [/amin AMOUNT] [/amax AMOUNT] [/sort asc|desc]`

**Examples:**
* `find coffee` *(All expenses containing "coffee")*
* `find /c Food` *(All expenses in the Food category)*
* `find /amin 5 /amax 20 /sort asc` *(Expenses between $5-$20, cheapest first)*

---

## Contributions to the Developer Guide (Extracts)

### Find / Filter Feature

The find feature allows users to search and filter their expense list using a keyword and/or a combination of optional flags.

**Implementation:**
`Parser.parseFindCommand()` strips each recognised flag from the input string one at a time. A `FindCommand` is constructed with all seven parameters. During execution, each expense is tested against four private predicate methods — `matchesCategory()`, `matchesKeyword()`, `matchesDateRange()`, and `matchesAmountRange()` — each of which returns `true` when its corresponding filter is `null`.

**Design Considerations:**
- Extracting match logic into separate predicate methods keeps the main loop readable and makes it easy to add new filter dimensions without touching existing logic.
- Sorting only applies to the filtered result set, not the persisted list, preserving data integrity for a read-only operation.

### Storage & Persistence

The `Storage` class handles reading and writing all application data to `data/expenses.txt`. The pipe-delimited format supports three line types: expenses, budget, and loans. Backward compatibility with v1.0 two-field format ensures existing user data is never lost.

*(Full diagrams and details in the Developer Guide)*
