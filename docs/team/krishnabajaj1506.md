# Project Portfolio Page - SpendSwift

## Overview
**SpendSwift** is a fast, text-based personal finance tracker optimized for CLI users, allowing students to seamlessly manage budgets and analyze spending habits.

## Summary of Contributions

* **Code Contributed:** [Repo Dashboard Link](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=krishnabajaj1506&breakdown=true)

* **Enhancements Implemented**:
  * **Sort Feature**: Implemented `sort date`, `sort category`, and `sort amount` with case-insensitive parsing to bring robust data analysis capabilities to the CLI.
  * **Statistics Dashboard**: Engineered the `stats [YYYY]` command to render a comprehensive ASCII dashboard, calculating per-category totals and visualizing monthly budget utilization via dynamic progress bars.
  * **Budget UI**: Designed the `budget` interface with robust dynamic offset displays and explicit over-budget indicators, enhancing financial tracking clarity.
  * **Intelligent Deletion**: Extended the `delete` command to support batch deletion by category (`/c`) and exact date (`/da`). Refactored chronological data storage to ensure strict bounds checking.
  * **Clear Command**: Engineered a fail-safe `clear` command with a mandatory `confirm` validation loop to prevent accidental permanent data erasure.
  * **Welcome Screen**: Developed a professional ASCII art startup banner (`WelcomeBanner.java`).
  * **Testing**: Architected extensive JUnit testing suites targeting extreme edge-cases, chronological anomalies, and out-of-bounds validations.

* **Contributions to the UG**: 
  * Authored parameters and examples for `sort`, `stats`, `delete` (batch modes), `clear`, and `budget`. 
  * Completely updated the Command Summary tables and corrected system Java prerequisites.

* **Contributions to the DG**: 
  * Authored the comprehensive "Design & Implementation" and "Design Considerations" chapters for Sort, Statistics, Delete, Clear, and Forecast features.
  * Drafted and embedded 4 major UML Sequence Diagrams resolving comprehensive PE-D UML bugs. 
  * Authored 18 user stories and structured manual testing criteria.

* **Contributions to Team-based Tasks**: 
  * Corrected overall GitHub Pages formatting, About Us page links, and standardized the repository's `.jar` build configurations metrics. 
  * Fixed cascading errors in unit tests resulting from overarching UI layout updates.

* **Review/Mentoring**: 
  * Reviewed and identified critical null-handling flaws in `Parser`/`EditCommand` PRs. 
  * Mentored a teammate through complex Git merge conflict resolutions offline.

* **Contributions Beyond the Project Team**: 
  * Identified and reported a critical timezone parsing bug in another team's project during PE-D testing. 
  * Shared GitHub action checkstyle configuration solutions on the module forum.

---

## Contributions to the User Guide (Extracts)

### Sorting expenses: `sort`
Organizes your recorded expenses alphabetically by category, chronologically by date, or by amount (highest first). Case-insensitive.

**Format:** `sort category` | `sort date` | `sort amount`

### Deleting expenses: `delete`
Removes an expense by index, or batch-deletes all expenses matching a category or date.

**Examples:**
* `delete 3` *(Removes the 3rd expense)*
* `delete /c Food` *(Batch removes Food expenses)*
* `delete /da 2026-03-10` *(Batch removes expenses from this date)*

---

## Contributions to the Developer Guide (Extracts)

### Statistics Feature
Provides a yearly dashboard with month-by-month budget vs. spending breakdowns.

**Implementation:**
`StatisticsCommand.execute()` determines the target year, then delegates the display to `Ui.showYearlyDashboard(expenseList, year)`. The presentation layer retrieves all monthly budgets, computes per-month totals, and renders an ASCII table with visual progress bars showing precise budget utilisation.

**Design Considerations:**
- **Why delegate rendering to `Ui`?** The complex ASCII progress bar formatting firmly belongs in the presentation layer, preserving the system's separation of concerns.

### Clear Feature
Allows users to permanently remove all expenses using the `clear` command.

**Implementation:**
`ClearCommand.execute()` displays `Ui.showClearConfirmationPrompt()`. It reads `Ui.getUserInput()`. If the exact string `confirm` is typed, `ExpenseList.clearExpenses()` is invoked; otherwise, the destructive operation is blocked.

**Design Considerations:**
- **Why not use an immediate deletion?** Clearing all expenses is highly destructive. Requiring the explicit word `confirm` establishes a strict safety net against fast-typing errors.
