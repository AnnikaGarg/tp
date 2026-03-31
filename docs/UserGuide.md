# SpendSwift - User Guide

## Introduction

**SpendSwift** is a desktop app for university students for managing personal finances, optimized for use via a Command Line Interface (CLI). If you can type fast, SpendSwift can log your daily expenses, track your budget, and generate spending statistics faster than traditional GUI-based mobile apps.

Whether you are a university student tracking daily meals or managing a strict budget, SpendSwift keeps your hands on the keyboard and your focus unbroken.

---

## Quick Start

1. Ensure you have **Java 11** or above installed on your Computer.
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

## Features

### Adding an expense: `add`
Adds a new expense to your tracking list.

**Format:** `add AMOUNT DESCRIPTION [/c CATEGORY] [/da YYYY-MM-DD]`

* The `AMOUNT` must be a valid, positive number.
* If the `/da` (date) flag is omitted, the expense defaults to today's date.
* **Interactive Prompt:** If the `/c` (category) flag is omitted, the application will pause and display a numbered list of all your existing categories. You can type a number to select an existing category, or type a new word to create a brand-new category on the fly!

**Examples:**
* `add 12.50 McDonald's Lunch /c Food /da 2026-03-24`
* `add 2.00 Bus Fare` *(Triggers the interactive category prompt)*


### Listing expenses: `list`
Shows a list of all your recorded expenses. By default, the list is maintained in chronological order with the newest expenses appearing first.

**Format:** `list`


### Sorting expenses: `sort`
Sorts your recorded expenses. You can organize them alphabetically by category, or chronologically by date (newest first). 
*Note: When sorting by category, expenses within the same category will automatically fall back to being sorted by date (newest first).*

**Format:** `sort category` or `sort date`

**Examples:**
* `sort category`
* `sort date`


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
| **List** | `list` |
| **Sort** | `sort category` or `sort date` |
| **Exit** | `exit` |