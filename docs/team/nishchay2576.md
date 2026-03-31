# Project Portfolio Page - SpendSwift

## Overview
**SpendSwift** is a desktop application for managing personal finances, optimized for Command Line Interface (CLI) users. It allows university students to track daily expenses, enforce budgets, and analyze their spending habits through a fast, text-based interface.

## Summary of Contributions
This section summarizes my specific contributions to the project. My primary focus was architecting the core parsing engine, developing dynamic expense modification commands, and engineering an entirely independent loan tracking system to handle peer-to-peer debts.

* **Code Contributed:** [Repo Dashboard Link](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=Nishchay2576&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2026-02-20T00%3A00%3A00&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=Nishchay2576&tabRepo=AY2526S2-CS2113-F14-1%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code~other&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)

* **Enhancements Implemented**:
    * **Core Parsing Engine (`Parser` class)**: Engineered the centralized logic responsible for tokenizing raw user strings, extracting optional flags (e.g., `/c`, `/d`), and enforcing strict input validation across all commands.
        * *Justification:* A CLI application lives and dies by its parser. Hardcoded parameter positions are frustrating for fast typists.
        * *Highlights:* Built a robust string-stripping algorithm that allows users to input parameters in *any order*. This significantly lowered the friction for new users and required complex Regex and substring manipulation to ensure edge cases (like missing flags or extra spaces) were handled gracefully without throwing generic exceptions.
    * **Loan & Debt Tracking System**: Designed and implemented a secondary ledger specifically to track money lent to others and subsequent repayments.
        * *Justification:* University students frequently split bills or lend money to peers. Mixing these "temporary" debts into the main expense list ruins personal budgeting statistics and triggers false budget warnings.
        * *Highlights:* Implemented a dedicated `Loan` model and a suite of specialized commands (`LendCommand`, `RepayCommand`, `LoansCommand`). This required modifying the `Storage` class to handle multiple file persistence streams simultaneously, ensuring the debt ledger remains completely isolated from the primary expense list in memory and on disk.
    * **Expense Modification (`EditCommand`)**: Developed the ability to update specific fields of an existing expense (amount, description, category, or date) without needing to delete and recreate the entry.
        * *Justification:* Typing mistakes happen. Forcing a user to rewrite a long expense description just to fix a 50-cent typo is a terrible User Experience (UX).
        * *Highlights:* Utilized an immutable object pattern where editing creates a new `Expense` instance rather than mutating the existing one. This prevented unintended side-effects across the application state and required the Parser to dynamically determine which flags were present and which should retain their old values.
    * **Summary Queries (`ListCommand` & `TotalCommand`)**: Authored the primary read-only commands for users to view their chronological expense history and instantly calculate their absolute spending sum.

* **Contributions to the UG**:
    * Authored the comprehensive usage and parameter explanation sections for the `edit`, `list`, `total`, `lend`, `repay`, and `loans` commands.
    * Added the "Troubleshooting & Error Messages" section to proactively assist users with common CLI formatting mistakes.

* **Contributions to the DG**:
    * Authored the "Design & Implementation" and "Design Considerations" for the **Edit Expense Feature** and the **Loan Tracking System**.
    * Drafted technical documentation detailing how the `Parser` class strips flags independently of their index position.

* **Contributions to Team-based Tasks**:
    * Managed the resolution of merge conflicts involving the `Parser` when teammates added new commands that required parsing updates.

* **Review/Mentoring Contributions**:
    * Reviewed and approved PRs related to the `AddCommand` and `DeleteCommand`, specifically pointing out missing edge-case null handling scenarios that improved the robustness of the application.

---

---

## Contributions to the User Guide (Extracts)

### Editing an expense: `edit`
Edits an existing expense in your list. You only need to provide the flags for the fields you want to change.

**Format:** `edit INDEX [/a NEW_AMOUNT] [/de NEW_DESC] [/c NEW_CATEGORY] [/da YYYY-MM-DD]`

**Example:**
* `edit 1 /a 15.00 /c Fast Food`

### Tracking money lent: `lend`
Records money you have lent to someone else. These entries are kept in a separate list from your daily expenses so they do not skew your personal budget.

**Format:** `lend AMOUNT BORROWER_NAME [/da DATE]`

**Example:**
* `lend 10.00 Bob /da 2026-03-30`

---

## Contributions to the Developer Guide (Extracts)

### Edit Expense Feature

The edit feature allows users to modify one or more fields of an existing expense using the `edit` command.

**Implementation:**
`Parser.parseEditCommand()` extracts the index and each flag from the input string sequentially. Each flag is located by its keyword using `indexOf()`, its value is extracted up to the next `/` or the end of the input, and then it is stripped from the working string before the next flag is processed. This substring manipulation algorithm allows flags to appear in any order without ambiguity.

Once all fields are parsed, an `EditCommand` is constructed with nullable fields for each of the four attributes. In `EditCommand.execute()`, the existing `Expense` at the given index is retrieved, each non-null field replaces the corresponding existing value, and a new `Expense` object is created and written back via `ExpenseList.setExpense()`.

**Design considerations:**
`Expense` objects are immutable (all fields are `final`), so editing produces a new `Expense` rather than mutating the existing one. An alternative considered was making `Expense` mutable with setter methods, but immutability was preferred to avoid unintended side effects across the codebase.s are immutable (all fields are `final`), so editing produces a new `Expense` rather than mutating the existing one. An alternative considered was making `Expense` mutable, but immutability was preferred to avoid unintended side effects across the codebase.