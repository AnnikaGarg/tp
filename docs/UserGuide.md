# SpendSwift - User Guide

## Table of Contents
* [Introduction](#introduction)
* [Quick Start](#quick-start)
* [Notes about the Command Format](#notes-about-the-command-format)
* [Notes on User Interaction](#notes-on-user-interaction)
* [Features](#features)
  * [Adding an expense: `add`](#adding-an-expense-add)
  * [Editing an expense: `edit`](#editing-an-expense-edit)
  * [Listing expenses: `list`](#listing-expenses-list)
  * [Calculating overall total: `total`](#calculating-overall-total-total)
  * [Deleting an expense: `delete`](#deleting-an-expense-delete)
  * [Setting and viewing a monthly budget: `budget`](#setting-and-viewing-a-monthly-budget-budget)
  * [Finding and filtering expenses: `find`](#finding-and-filtering-expenses-find)
  * [Viewing statistics: `stats`](#viewing-statistics-stats)
  * [Viewing help: `help`](#viewing-help-help)
  * [Sorting expenses: `sort`](#sorting-expenses-sort)
  * [Tracking money lent: `lend`](#tracking-money-lent-lend)
  * [Recording a repayment: `repay`](#recording-a-repayment-repay)
  * [Viewing all loans: `loans`](#viewing-all-loans-loans)
  * [Forecasting spending: `forecast`](#forecasting-spending-forecast)
  * [Clearing all expenses: `clear`](#clearing-all-expenses-clear)
  * [Exiting the program: `exit`](#exiting-the-program-exit)
* [FAQ](#faq)
* [Command Summary](#command-summary)

<div style="page-break-after: always;"></div>

## Introduction

**SpendSwift** is a desktop app for university students for managing personal finances, optimized for use via a Command Line Interface (CLI). If you can type fast, SpendSwift can log your daily expenses, track your budget, and generate spending statistics faster than traditional GUI-based mobile apps.

Whether you are a university student tracking daily meals or managing a strict budget, SpendSwift keeps your hands on the keyboard and your focus unbroken.

---

## Quick Start

1. Ensure you have **Java 17** installed on your Computer.
2. Download the latest `SpendSwift.jar` from our GitHub releases page.
3. Copy the file to an empty folder where you want to store your data.
4. Open a command terminal, navigate to the folder, and run the application using the `java -jar SpendSwift.jar` command.
5. The SpendSwift welcome message will appear. Type the command in the command box and press Enter to execute it.

**Try these example commands to get started:**
* `help`: Displays a quick summary of all available commands.
* `add 5.50 Chicken Rice`: Adds a $5.50 expense and prompts you to select a category.
* `list`: Shows a list of all your recorded expenses.
* `budget 100`: Sets the current month's budget to $100.00.
* `exit`: Safely saves your data and exits the application.

---

## Notes about the Command Format

Before diving into the features, here are the strict rules regarding how SpendSwift interprets your commands. **Please keep these in mind to ensure smooth operation:**

* **Parameters:** Words in `UPPER_CASE` are the parameters to be supplied by the user.
  * *e.g., in `add AMOUNT DESCRIPTION`, `AMOUNT` is a parameter which can be used as `add 5.00 Coffee`.*
* **Optional Fields:** Items in square brackets `[ ]` are optional.
  * *e.g., `[/c CATEGORY]` can be used as `/c Food` or can be left out entirely.*
* **Case Insensitivity:** Command keywords are case-insensitive. Typing `add`, `ADD`, or `aDd` will all execute the same command. Sorting arguments (like `date` or `amount`) are also case-insensitive.
* **Restricted Characters:** * The pipe character (`|`) is strictly reserved for internal data saving.
  * **Do not use the forward-slash (`/`)** inside your descriptions, categories, or borrower names. SpendSwift uses `/` exclusively to detect command flags (like `/c` or `/da`). Using extra slashes in your text will cause parsing errors.
* **Amount Limits:** To maintain system stability and prevent floating-point precision loss, all monetary amounts (expenses, budgets, and loans) must be strictly greater than `$0.00` and strictly less than `$1,000,000,000,000.00` (One Trillion).
* **Date Limits:** All dates must be valid calendar dates strictly between the years `2000` and `2100`.
* **Flag Usage:** Flags (like `/c` or `/da`) can be provided in **any order**. However, you may only use a specific flag **once** per command.
* **Strict Commands:** Commands that do not require parameters (like `total`, `forecast`, `clear`, and `exit`) are strictly validated. Adding any extra text or characters after these keywords will cause the command to be rejected.

---

## Notes on User Interaction

SpendSwift provides interactive feedback to guide users:

- Confirmation messages are shown after successful state-changing commands such as adding, editing, deleting, and setting or updating budgets.
- Warning messages are displayed for invalid inputs, missing fields, and when a monthly budget has been exceeded.
- Some commands (such as `add`) may trigger interactive prompts to request additional input.
- Read-only commands such as `budget` and `budget YYYY-MM` display the current budget status without changing saved data.

These interactions ensure that you receive immediate feedback and guidance while using the application.

---

## Features

### Adding an expense: `add`
Adds a new expense to your tracking list.

**Format:** `add AMOUNT [/c CATEGORY] [/da YYYY-MM-DD] DESCRIPTION`

* `AMOUNT` must be a valid number greater than 0 and less than 1 Trillion.
* If `/da` (date) is not provided, the expense will default to the current date.
* If `/c` (category) is not provided, an interactive prompt will appear.
* **Do not** use extra `/` or `|` characters in your description.

**Interactive Prompt:**
If no category is specified, SpendSwift will display a numbered list of available categories. You may:
- Enter a number to select an existing category
- Type a new category name to create one

**Examples:**
* `add 12.50 McDonald's Lunch /c Food /da 2026-03-24`
* `add 2.00 Bus Fare` *(triggers category prompt)*


### Editing an expense: `edit`
Edits an existing expense in your list. You only need to provide the flags for the fields you want to change, and SpendSwift handles the rest.

**Format:** `edit INDEX [/a NEW_AMOUNT] [/de NEW_DESC] [/c NEW_CATEGORY] [/da YYYY-MM-DD]`

**Rules:**
* At least one flag must be provided.
* Flags can be provided in **any order**.
* **Do not** use extra `/` characters in your new description or category, as it will confuse the parser.

**Examples of Combinations:**
* `edit 1 /a 15.00 /c Food` *(Updates the amount to $15.00 and category to Food)*
* `edit 2 /de Chicken Noodles` *(Updates only the description of the 2nd expense)*
* `edit 3 /da 2026-04-10 /a 5.50 /de Lunch` *(Updates the date, amount, and description simultaneously. Notice the order of flags does not matter)*


### Listing expenses: `list`
Shows your recorded expenses. You may list all expenses or only the expenses for a specific month.

**Format:** `list [YYYY-MM]`

**Rules:**
- If no month is provided, SpendSwift shows all recorded expenses.
- If `YYYY-MM` is provided, SpendSwift shows only the expenses recorded in that month.
- `YYYY-MM` must be a valid month in `YYYY-MM` format.
- The `list` command accepts at most one optional `YYYY-MM` argument.

**Examples:**
* `list` *(Shows all expenses)*
* `list 2026-03` *(Shows only expenses from March 2026)*


### Calculating overall total: `total`
Displays the absolute sum of all expenses currently in your list for a quick snapshot of your spending.

**Format:** `total`

*Note on extremely large numbers: SpendSwift caps individual expenses at just under 1 Trillion dollars. While you can technically accumulate astronomical totals across thousands of entries, doing so may eventually result in slight floating-point precision loss at the decimal level. We assume regular personal budgeting will not reasonably reach these limits!*


### Deleting an expense: `delete`
Removes an expense from your list by its index number. You can also batch-delete all expenses matching a category or date.

**Format:** `delete INDEX` or `delete /c CATEGORY` or `delete /da YYYY-MM-DD`

**Examples:**
* `delete 3` *(Removes the 3rd expense from your list)*
* `delete /c Food` *(Removes all expenses in the Food category)*
* `delete /da 2026-03-10` *(Removes all expenses from March 10, 2026)*


### Setting and viewing a monthly budget: `budget`
Sets, updates, or views a monthly spending budget.

**Format:**
- `budget`
- `budget AMOUNT`
- `budget YYYY-MM`
- `budget YYYY-MM AMOUNT`

**What each format does:**
- `budget`  
  Shows the budget details for the **current month**.
- `budget AMOUNT`  
  Sets or updates the budget for the **current month**.
- `budget YYYY-MM`  
  Shows the budget details for the specified month.
- `budget YYYY-MM AMOUNT`  
  Sets or updates the budget for the specified month.

**Rules:**
- `AMOUNT` must be a valid number greater than 0.
- `YYYY-MM` must be a valid month in `YYYY-MM` format.
- The command accepts at most two arguments after `budget`.
- If a budget already exists for that month, setting a new one overwrites the old value.
- Budgets are tracked separately for each month.
- Budget amounts are displayed to 2 decimal places in user-facing messages.

**When viewing a budget:**
- If a budget has been set for that month, SpendSwift shows:
  - the budget amount
  - the total amount spent in that month
  - the remaining budget, or how much the budget has been exceeded by
- If no budget has been set for that month, SpendSwift tells you that no budget exists for that month.

**When setting a budget:**
- SpendSwift sets the budget for the target month.
- If a budget already existed for that month, SpendSwift shows an update message with the old and new values.
- If spending for that month already exceeds the newly set budget, SpendSwift immediately shows a budget-exceeded warning.

**Examples:**
* `budget` *(Shows the current month's budget details)*
* `budget 100` *(Sets the current month's budget to $100.00)*
* `budget 2026-05` *(Shows May 2026's budget details)*
* `budget 2026-05 500` *(Sets May 2026's budget to $500.00)*


### Finding and filtering expenses: `find`
Searches your expense list using a keyword and/or filters. All filters are optional and can be combined freely.

**Format:** `find [KEYWORD] [/c CATEGORY] [/dmin DATE] [/dmax DATE] [/amin AMOUNT] [/amax AMOUNT] [/sort asc|desc]`

* `KEYWORD` searches across both description and category (case-insensitive).
* `/c CATEGORY` filters by exact category match.
* `/dmin` and `/dmax` filter by date range (inclusive, YYYY-MM-DD format).
* `/amin` and `/amax` filter by amount range (inclusive). Minimum must not exceed maximum.
* `/dmin` must not be after `/dmax`. Reversed ranges are rejected with an error message.
* `/sort asc` or `/sort desc` sorts results by amount.

**Examples:**
* `find coffee` *(All expenses containing "coffee")*
* `find /c Food` *(All expenses in the Food category)*
* `find /dmin 2026-01-01 /dmax 2026-03-31` *(Q1 expenses)*
* `find /amin 5 /amax 20 /sort asc` *(Expenses between $5-$20, cheapest first)*
* `find lunch /c Food /sort desc` *("lunch" in Food, most expensive first)*


### Viewing statistics: `stats`
Displays a yearly dashboard with month-by-month budget vs. spending breakdown, visual progress bars, and per-category spending totals.

**Format:** `stats [YYYY]`

* If no year is provided, defaults to the current year.

**Examples:**
* `stats` *(Shows dashboard for current year)*
* `stats 2026` *(Shows dashboard for 2026)*


### Viewing help: `help`
Displays a summary of all available commands and their usage formats. You can also get usage help for a specific command.

**Format:** `help` or `help COMMAND`

**Examples:**
* `help` *(Shows full help menu)*
* `help add` *(Shows usage for the add command)*
* `help find` *(Shows usage for the find command)*


### Sorting expenses: `sort`
Sorts your recorded expenses. You can organize them alphabetically by category, chronologically by date (newest first), or by amount (highest first). The sort argument is case-insensitive.
*Note: When sorting by category, expenses within the same category will automatically fall back to being sorted by date (newest first).*

**Format:** `sort category` or `sort date` or `sort amount`

**Examples:**
* `sort CATEGORY`
* `sort date`
* `sort amount`


### Tracking money lent: `lend`
Records money you have lent to someone else. These entries are kept in a separate loan ledger from your daily expenses so they do not skew your personal budget and statistics.

**Format:** `lend AMOUNT BORROWER_NAME [/da DATE]`

**Example:**
* `lend 10.00 Bob /da 2026-03-30`


### Recording a repayment: `repay`
Updates your loan ledger when someone pays you back, marking the debt as settled. The index you provide corresponds to the list of *outstanding* loans.

**Format:** `repay INDEX [AMOUNT]`

**Example:**
* `repay 1` *(Records a full repayment for the 1st outstanding loan)*
* `repay 1 5.00` *(Records a partial repayment of $5.00 for the 1st outstanding loan)*


### Viewing all loans: `loans`
Displays a comprehensive list of all outstanding debts and money currently lent out. You can also view past settled debts using the `/all` flag.

**Format:** `loans` OR `loans /all`

### Forecasting spending: `forecast`
Predicts your end-of-month total spending based on your current daily habits. If you have a budget set, it will warn you if you are on track to exceed it.

**Format:** `forecast`

* Calculates your average daily spend (burn rate) for the current month.
* Projects your total expenses for the final day of the month.
* Note: This command strictly accepts no additional parameters.


### Clearing all expenses: `clear`
Permanently removes all expenses from your list. A confirmation prompt will appear before execution to prevent accidental data loss.

**Format:** `clear`

* You must type `confirm` when prompted to proceed. Typing anything else cancels the operation.


### Exiting the program: `exit`
Exits the program and ensures all data is safely saved to your hard drive.

**Format:** `exit`

---

## FAQ

**Q: Where is my data saved?**
**A:** SpendSwift automatically creates a `data` folder in the same directory as your `.jar` file. Your expenses are saved inside `data/expenses.txt`.

**Q: How do I transfer my data to another computer?**
**A:** Simply install Java 17 on the new computer, download the `SpendSwift.jar` file, and copy your `data/expenses.txt` file into the same folder on the new machine. When you launch SpendSwift, it will automatically load your history!

---

## Command Summary

| Action | Format, Examples |
|--------|------------------|
| **Add** | `add AMOUNT DESCRIPTION [/c CATEGORY] [/da YYYY-MM-DD]` <br> e.g., `add 5.50 Coffee /c Food` |
| **Edit** | `edit INDEX [/a VAL] [/de DESC] [/c CAT] [/da DATE]` <br> e.g., `edit 1 /a 15.00` |
| **Delete** | `delete INDEX` or `delete /c CAT` or `delete /da DATE` <br> e.g., `delete 3`, `delete /c Food` |
| **Clear** | `clear` (requires typing `confirm`) |
| **List** | `list [YYYY-MM]` <br> e.g., `list`, `list 2026-03` |
| **Find** | `find [KEYWORD] [/c CAT] [/dmin DATE] [/dmax DATE] [/amin AMT] [/amax AMT] [/sort asc\|desc]` <br> e.g., `find coffee /c Food /sort desc` |
| **Total** | `total` |
| **Budget** | `budget`, `budget AMOUNT`, `budget YYYY-MM`, or `budget YYYY-MM AMOUNT` <br> e.g., `budget`, `budget 100`, `budget 2026-05`, `budget 2026-05 500` |
| **Sort** | `sort category` or `sort date` or `sort amount` |
| **Stats** | `stats [YYYY]` <br> e.g., `stats` or `stats 2026` |
| **Lend** | `lend AMOUNT BORROWER [/da DATE]` <br> e.g., `lend 20.00 Alice` |
| **Repay** | `repay INDEX [AMOUNT]` <br> e.g., `repay 1` or `repay 1 10.00` |
| **Loans** | `loans` or `loans /all` |
| **Forecast** | `forecast` |
| **Help** | `help` or `help COMMAND` <br> e.g., `help add` |
| **Exit** | `exit` |
