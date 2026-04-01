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
* `budget 100`: Sets a spending limit of $100.00.
* `exit`: Safely saves your data and exits the application.

---

## Notes about the Command Format

Before diving into the features, here are a few things to keep in mind regarding how SpendSwift reads your commands:

* Words in `UPPER_CASE` are the parameters to be supplied by the user.
    * *e.g., in `add AMOUNT DESCRIPTION`, `AMOUNT` is a parameter which can be used as `add 5.00 Coffee`.*
* Items in square brackets `[ ]` are optional.
    * *e.g., `[/c CATEGORY]` can be used as `/c Food` or can be left out entirely.*
* Parameters with flags (like `/c` or `/da`) can be typed in any order.
    * *e.g., if the command specifies `[/c CATEGORY] [/da DATE]`, typing `/da 2026-03-24 /c Food` is perfectly acceptable.*

---

## Notes on User Interaction

SpendSwift provides interactive feedback to guide users:

- Clear confirmation messages are shown after each command (e.g., adding or deleting expenses)
- Warning messages are displayed for invalid inputs or exceeded budgets
- Some commands (such as `add`) may trigger interactive prompts to request additional input

These interactions ensure that users receive immediate feedback and guidance while using the application.

---

## Features

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

**Examples:**
* `add 12.50 McDonald's Lunch /c Food /da 2026-03-24`
* `add 2.00 Bus Fare` *(triggers category prompt)*


### Editing an expense: `edit`
Edits an existing expense in your list. You only need to provide the flags for the fields you want to change, and SpendSwift handles the rest.

**Format:** `edit INDEX [/a NEW_AMOUNT] [/de NEW_DESC] [/c NEW_CATEGORY] [/da YYYY-MM-DD]`

**Example:**
* `edit 1 /a 15.00 /c Fast Food`


### Listing expenses: `list`
Shows a list of all your recorded expenses. By default, the list is maintained in chronological order with the newest expenses appearing first.

**Format:** `list`


### Calculating overall total: `total`
Displays the absolute sum of all expenses currently in your list for a quick snapshot of your spending.

**Format:** `total`


### Deleting an expense: `delete`
Removes an expense from your list by its index number.

**Format:** `delete INDEX`

**Example:**
* `delete 3` *(Removes the 3rd expense from your list)*


### Setting a budget: `budget`
Sets or views your spending budget.

**Format:** `budget AMOUNT` (to set)  
**Format:** `budget` (to view)

* `AMOUNT` must be a number greater than 0.
* Setting a budget overwrites any previously set budget.

**Behavior:**
- When a budget is set, SpendSwift tracks your total spending against it.
- If your total spending exceeds the budget, a warning message will be displayed.

**Viewing Budget:**
When using `budget` without arguments, SpendSwift shows:
- Current budget
- Total spent
- Remaining budget (or exceeded amount)

**Examples:**
* `budget 100`
* `budget`


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
Displays a per-category breakdown of total spending.

**Format:** `stats`


### Viewing help: `help`
Displays a summary of all available commands and their usage formats.

**Format:** `help`


### Sorting expenses: `sort`
Sorts your recorded expenses. You can organize them alphabetically by category, or chronologically by date (newest first). 
*Note: When sorting by category, expenses within the same category will automatically fall back to being sorted by date (newest first).*

**Format:** `sort category` or `sort date`

**Examples:**
* `sort category`
* `sort date`


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


### Exiting the program: `exit`
Exits the program and ensures all data is safely saved to your hard drive.

**Format:** `exit`

---

## FAQ

**Q: Where is my data saved?**
**A:** SpendSwift automatically creates a `data` folder in the same directory as your `.jar` file. Your expenses are saved inside `data/expenses.txt`.

**Q: How do I transfer my data to another computer?**
**A:** Simply install Java 11 on the new computer, download the `SpendSwift.jar` file, and copy your `data/expenses.txt` file into the same folder on the new machine. When you launch SpendSwift, it will automatically load your history!

---

## Command Summary

| Action | Format, Examples |
|--------|------------------|
| **Add** | `add AMOUNT DESCRIPTION [/c CATEGORY] [/da YYYY-MM-DD]` <br> e.g., `add 5.50 Coffee /c Food` |
| **Edit** | `edit INDEX [/a VAL] [/de DESC] [/c CAT] [/da DATE]` <br> e.g., `edit 1 /a 15.00` |
| **Delete**| `delete INDEX` <br> e.g., `delete 3` |
| **List** | `list` |
| **Find** | `find [KEYWORD] [/c CAT] [/dmin DATE] [/dmax DATE] [/amin AMT] [/amax AMT] [/sort asc\|desc]` <br> e.g., `find coffee /c Food /sort desc` |
| **Total**| `total` |
| **Budget**| `budget [AMOUNT]` <br> e.g., `budget 100` or `budget` |
| **Sort** | `sort category` or `sort date` |
| **Stats**| `stats` |
| **Lend** | `lend AMOUNT BORROWER [/da DATE]` <br> e.g., `lend 20.00 Alice` |
| **Repay**| `repay INDEX` <br> e.g., `repay 1` |
| **Loans**| `loans` OR `loans /all` |
| **Help** | `help` |
| **Exit** | `exit` |
