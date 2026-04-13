# SpendSwift - User Guide

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

Before diving into the features, here are a few things to keep in mind regarding how SpendSwift reads your commands:

* Words in `UPPER_CASE` are the parameters to be supplied by the user.
    * *e.g., in `add AMOUNT DESCRIPTION`, `AMOUNT` is a parameter which can be used as `add 5.00 Coffee`.*
* Items in square brackets `[ ]` are optional.
    * *e.g., `[/c CATEGORY]` can be used as `/c Food` or can be left out entirely.*
* **Restricted Characters:** The pipe character (`|`) is strictly reserved for internal data saving. Do not use `|` in your descriptions or categories.
* **Maximum Values:** To prevent floating-point precision errors, the maximum allowable expense amount is $999,999,999,999.99 (Just under 1 Trillion).
* **No Duplicate Flags:** You may only use a specific flag (like `/c` or `/da`) once per command.

---

## Notes on User Interaction

SpendSwift provides interactive feedback to guide users:

- Confirmation messages are shown after successful state-changing commands such as adding, editing, deleting, and setting or updating budgets.
- Warning messages are displayed for invalid inputs and when a monthly budget has been exceeded.
- Some commands (such as `add`) may trigger interactive prompts to request additional input.
- Read-only commands such as `budget` and `budget YYYY-MM` display the current budget status without changing saved data.

These interactions ensure that users receive immediate feedback and guidance while using the application.

---

## Features

### Adding an expense: `add`
Adds a new expense to your tracking list.

**Format:** `add AMOUNT [/c CATEGORY] [/da YYYY-MM-DD] DESCRIPTION`

* `AMOUNT` must be a valid number greater than 0.
* If `/da` (date) is not provided, the expense will default to the current date.
* If `/c` (category) is not provided, an interactive prompt will appear.

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

**Example:**
* `edit 1 /a 15.00 /c Fast Food`


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
- If more than 2 decimal places are entered, the displayed value is rounded accordingly.

**Invalid input handling:**
- If the month argument is invalid, SpendSwift shows the budget usage message.
- If the command contains too many arguments, SpendSwift shows the budget usage message.
- If an amount is provided in a valid budget-setting form but is invalid, SpendSwift shows: `Budget must be a number greater then 0.`
- If `AMOUNT` is not greater than 0, SpendSwift shows: `Budget must be a number greater then 0.`

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
- Budget values shown in confirmations and warnings are formatted to 2 decimal places.

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
* `/amin` and `/amax` filter by amount range (inclusive).
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
Displays a summary of all available commands and their usage formats.

**Format:** `help`


### Sorting expenses: `sort`
Sorts your recorded expenses. You can organize them alphabetically by category, chronologically by date (newest first), or by amount (highest first). The sort argument is case-insensitive.
*Note: When sorting by category, expenses within the same category will automatically fall back to being sorted by date (newest first).*

**Format:** `sort category` or `sort date` or `sort amount`

**Examples:**
* `sort category`
* `sort date`
* `sort amount`


### Tracking money lent: `lend`
Records money you have lent to someone else. These entries are kept in a separate loan ledger from your daily expenses so they do not skew your personal budget and statistics.

**Format:** `lend AMOUNT BORROWER_NAME [/da DATE]`

**Example:**
* `lend 10.00 Bob /da 2026-03-30`


### Recording a repayment: `repay`
Updates your loan ledger when someone pays you back, marking the debt as settled. The index you provide corresponds to the list of *outstanding* loans.

**Format:** `repay INDEX`

**Example:**
* `repay 1`


### Viewing all loans: `loans`
Displays a comprehensive list of all outstanding debts and money currently lent out. You can also view past settled debts using the `/all` flag.

**Format:** `loans` OR `loans /all`

### Forecasting spending: `forecast`
Predicts your end-of-month total spending based on your current daily habits. If you have a budget set, it will warn you if you are on track to exceed it.

**Format:** `forecast`

* Calculates your average daily spend (burn rate) for the current month.
* Projects your total expenses for the final day of the month.
* Note: This command does not accept any additional trailing text or flags.


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
| **Help** | `help` |
| **Exit** | `exit` |
