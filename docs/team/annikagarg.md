## Contributions to the Developer Guide (Extracts)

### Ui Component

The `Ui` component centralises all user-facing input and output in SpendSwift. It is responsible for displaying confirmation messages, warnings, usage hints, summaries, and interactive prompts.

Unlike the business logic classes, `Ui` does not modify application state. Instead, command classes delegate user interaction responsibilities to it. For example, `AddCommand` uses `Ui` to display success messages and category prompts, while `BudgetCommand` uses it to show budget confirmations, status displays, and warning messages.

### Budget Feature

The budget feature allows users to set, update, and view a **monthly spending budget** using the `budget` command.

**How it works:**
The user may enter:
- `budget AMOUNT` to set or update the budget for the current month
- `budget YYYY-MM AMOUNT` to set or update the budget for a specific month
- `budget YYYY-MM` to view the budget status for a specific month
- `budget` to view the budget status for the current month

### List Feature

The list feature allows users to display either all recorded expenses or only the expenses from a specific month using the `list` command.

**How it works:**
The user may enter:
- `list`
- `list YYYY-MM`