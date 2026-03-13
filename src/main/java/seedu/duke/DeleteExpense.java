package seedu.duke;

public class DeleteExpense {
    private int index;

    public DeleteExpense(int index) {
        this.index = index;
    }

    public void execute(ExpenseList expenseList) {
        try {
            // Convert 1-based user input to 0-based index
            Expense removed = expenseList.deleteExpense(index - 1);

            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("Noted. I've removed this expense:");
            System.out.println("  " + removed);
            System.out.println("Now you have " + expenseList.getSize() + " expenses in the list.");
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        } catch (IndexOutOfBoundsException e) {
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("Invalid index! Use 'list' to see valid numbers.");
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        }
    }
}