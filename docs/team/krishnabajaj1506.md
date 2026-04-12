# Project Portfolio Page - SpendSwift

## Overview
**SpendSwift** is a desktop application for managing personal finances, optimized for Command Line Interface (CLI) users. It allows university students to track expenses, manage budgets, and analyze spending habits through a fast, text-based interface.

## Summary of Contributions
This section summarizes my specific contributions to the project, focusing on core functionality related to sorting, statistics, budget UI, deletion logic, and the clear command.

* **Code Contributed:** [Repo Dashboard Link](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=krishnabajaj1506&breakdown=true)

* **Enhancements Implemented**:
  * **Sort Feature**: Implemented the ability to reorder the entire tracking list. Users can sort alphabetically by category, chronologically by date (newest first), or by amount (highest first). The sort argument is case-insensitive, so `sort DATE` and `sort date` both work.
    * *Justification:* Gives users a powerful tool for discovering duplicate expenses, analyzing spending trends, or quickly identifying their largest expenses.
    * *Highlights:* Engineered with an in-place `Collections.sort` leveraging static, chained Comparators (`Comparator.reverseOrder()`), guaranteeing seamless persistence of the newly sorted list and optimal efficiency. Added `BY_AMOUNT` comparator and case-insensitive argument parsing in the Parser.
  * **Statistics Feature**: Developed the `stats` command to display a yearly dashboard with month-by-month budget vs. spending breakdowns, visual ASCII progress bars, and per-category spending totals. Supports an optional year argument (e.g., `stats 2026`).
    * *Justification:* Quickly summarizing overall spending categories and monthly trends is the core value proposition of a budget tracker.
    * *Highlights:* The yearly dashboard renders ASCII progress bars showing budget utilisation per month, making it easy to spot over-spending at a glance.
  * **Budget Command UI**: Designed and implemented the entire user interface for the budget command, including month-specific budget support with comparison and offset display. Users can set budgets per month (`budget 2026-05 500`) and view remaining/exceeded amounts with visual indicators.
    * *Justification:* A one-size-fits-all budget is insufficient for real-world use. Monthly budgets with comparison views let users adapt their spending to each month's needs.
  * **Clear Command**: Implemented the `clear` command that permanently removes all expenses from the list. Added a safety confirmation prompt requiring users to type `confirm` before execution to prevent accidental data loss.
    * *Justification:* Users need a way to start fresh, but a destructive operation like clearing all data must be guarded against accidental invocation.
  * **Batch Deletion by Category and Date**: Extended the `delete` command to support batch deletion modes — `delete /c CATEGORY` removes all expenses in a given category, and `delete /da YYYY-MM-DD` removes all expenses for a specific date.
    * *Justification:* Deleting expenses one-by-one is tedious when a user wants to remove an entire category or day's worth of entries.
  * **Chronological Insertion & Deletion**: Refactored the fundamental way `ExpenseList` handles data. Expenses are now injected directly in purely chronological (newest-first) order. Also authored the strict `DeleteCommand` ensuring comprehensive bound-checking and error-handling against corrupted manual saves.
  * **Automated Unit Testing Environment**: Independently designed and authored extensive JUnit testing suites ensuring edge-case scenarios—such as chronological anomalies or out-of-bounds index deletion inputs—fail gracefully without an application crash.

* **Contributions to the UG**:
  * Authored the comprehensive usage and parameter explanation sections for the `sort`, `list`, `stats`, `delete`, `clear`, and `budget` commands.
  * Documented batch deletion by category (`delete /c Food`) and by date (`delete /da 2026-03-10`).
  * Documented the `clear` command with its confirmation prompt workflow.
  * Updated `sort` to include the `sort amount` option and noted case-insensitive argument support.
  * Updated `stats` format to `stats [YYYY]` with optional year argument.
  * Updated `list` format to `list [YYYY-MM]` with optional month filter.
  * Updated `budget` format to `budget [YYYY-MM] [AMOUNT]` with month-specific support.
  * Completely updated the Command Summary table to reflect all available commands and their latest formats.
  * Fixed Java version inconsistency — corrected FAQ to state Java 17 (was incorrectly stating Java 11).

* **Contributions to the DG**:
  * Authored the "Design & Implementation" and "Design Considerations" for five full chapters: **Sort Feature**, **Statistics Feature**, **Delete Feature**, **Clear Feature**, and **Forecast Feature**.
  * Drafted and embedded **4 UML Sequence Diagrams** (Delete, Sort, Statistics, Clear) using correct `instanceName:ClassName` notation, proper activation bars, and plain return labels — fixing all PE-D reported UML bugs.
  * Updated the Delete section to document all three deletion modes (by index, by category, by date).
  * Updated the Sort section to include `sort amount` and case-insensitive parsing.
  * Rewrote the Statistics section to accurately describe the yearly dashboard with month-by-month budget utilisation.
  * Added 18 missing user stories covering budget, sort, stats, lend/loans/repay, forecast, batch delete, clear, and monthly list.
  * Fixed Java version in Non-Functional Requirements from Java 11 to Java 17.
  * Wrote manual functional testing parameters for all core commands.

* **Contributions to Team-based Tasks**:
  * Fixed the main GitHub Pages landing page by replacing the `{Give product intro here}` placeholder with a proper product description.
  * Fixed the broken About Us page table formatting and corrected all GitHub profile URLs and portfolio link paths.
  * Standardized the repository's `.jar` build configurations during critical milestone releases.
  * Handled fixing cascading unit testing errors whenever overarching formatting updates were applied to expected outputs.

* **Review/Mentoring Contributions**:
  * Reviewed two major overarching PRs covering the `EditCommand` and `Parser` abstractions, specifically pointing out missing edge-case null handling scenarios that improved the robustness of the Parser class.
  * Held an offline pair-programming session to help a teammate debug complex Git Merge conflicts halfway through a milestone sprint.
  
* **Contributions Beyond the Project Team**:
  * Actively participated in the Canvas/Luminus Module forums, sharing a comprehensive explanation regarding a common Checkstyle configuration error affecting GitHub Actions.
  * Participated in the structured peer-testing phase, where I successfully identified and submitted a critical timezone parsing bug in another team's project, preventing them from losing marks during their final evaluation.

---

## Contributions to the User Guide (Extracts)

### Sorting expenses: `sort`
Sorts your recorded expenses. You can organize them alphabetically by category, chronologically by date (newest first), or by amount (highest first). The sort argument is case-insensitive.
*Note: When sorting by category, expenses within the same category will automatically fall back to being sorted by date (newest first).*

**Format:** `sort category` or `sort date` or `sort amount`

**Examples:**
* `sort category`
* `sort date`
* `sort amount`

### Clearing all expenses: `clear`
Permanently removes all expenses from your list. A confirmation prompt will appear before execution to prevent accidental data loss.

**Format:** `clear`

* You must type `confirm` when prompted to proceed. Typing anything else cancels the operation.

### Deleting an expense: `delete`
Removes an expense from your list by its index number. You can also batch-delete all expenses matching a category or date.

**Format:** `delete INDEX` or `delete /c CATEGORY` or `delete /da YYYY-MM-DD`

**Examples:**
* `delete 3` *(Removes the 3rd expense)*
* `delete /c Food` *(Removes all Food expenses)*
* `delete /da 2026-03-10` *(Removes all expenses from that date)*

---

## Contributions to the Developer Guide (Extracts)

### Clear Feature

The clear feature allows users to permanently remove all expenses from the list using the `clear` command.

**Implementation:**
`ClearCommand.execute()` operates by:
1. Displaying a confirmation prompt via `Ui.showClearConfirmationPrompt()`.
2. Reading the user's response via `Ui.getUserInput()`.
3. If the user types `confirm` (case-insensitive): recording the original list size, calling `ExpenseList.clearExpenses()`, and displaying a success message.
4. If the user types anything else: displaying a cancellation message and leaving the list untouched.

**Design Considerations:**
- **Why require confirmation?** Clearing all expenses is a destructive, irreversible operation. Requiring the user to type `confirm` prevents accidental data loss.
- **Why not use a simple yes/no?** Requiring the specific word `confirm` makes it harder to accidentally trigger by mistyping, providing a stronger safety net.

### Statistics Feature

The statistics feature provides a yearly dashboard with month-by-month budget vs. spending breakdowns using the `stats` command.

**Implementation:**
`StatisticsCommand.execute()` determines the target year (either from the user argument or the current date), then delegates the display to `Ui.showYearlyDashboard(expenseList, year)`. The `Ui` method retrieves all monthly budgets, computes per-month totals, and renders an ASCII table with visual progress bars showing budget utilisation.

**Design Considerations:**
- **Why delegate dashboard rendering to `Ui`?** The dashboard requires complex ASCII formatting. Keeping this in `Ui` follows the existing separation of concerns where all output formatting lives in the presentation layer.
- **Why accept an optional year argument?** Users may want to review historical spending data. Defaulting to the current year keeps the common case simple.
