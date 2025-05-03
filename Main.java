import java.util.*;


public class Main {
    public static void main(String[] args) {
        ExpenseManager manager = new ExpenseManager();
        Scanner sc = new Scanner(System.in); // Use keyboard input


        System.out.println("Welcome to Expense Splitter!");


        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Add User");
            System.out.println("2. Add Transaction");
            System.out.println("3. Show Balances");
            System.out.println("4. Simplify Debts (Greedy)");
            System.out.println("5. Simplify Debts (Graph)");
            System.out.println("6. Exit");


            // Input validation
            if (!sc.hasNextInt()) {
                System.out.println("Error: Please enter a number between 1 and 6.");
                sc.next(); // consume invalid input
                continue;
            }


            int choice = sc.nextInt();
            sc.nextLine(); // consume leftover newline


            switch (choice) {
                case 1:
                    System.out.print("Enter User ID: ");
                    if (!sc.hasNextInt()) {
                        System.out.println("Error: User ID must be a number.");
                        sc.next();
                        break;
                    }
                    int id = sc.nextInt();
                    sc.nextLine(); // consume newline


                    if (manager.isUserExists(id)) {
                        System.out.println("Error: User ID " + id + " already exists.");
                        break;
                    }


                    System.out.print("Enter User Name: ");
                    String name = sc.nextLine();
                    if (name.trim().isEmpty()) {
                        System.out.println("Error: User name cannot be empty.");
                        break;
                    }


                    manager.addUser(new User(id, name));
                    System.out.println("User added successfully.");
                    break;


                case 2:
                    System.out.print("Enter Payer ID: ");
                    if (!sc.hasNextInt()) {
                        System.out.println("Error: Payer ID must be a number.");
                        sc.next();
                        break;
                    }
                    int payerId = sc.nextInt();


                    if (!manager.isUserExists(payerId)) {
                        System.out.println("Error: Payer ID " + payerId + " does not exist.");
                        break;
                    }


                    System.out.print("Enter Amount Paid: ");
                    if (!sc.hasNextDouble()) {
                        System.out.println("Error: Amount must be a number.");
                        sc.next();
                        break;
                    }
                    double amount = sc.nextDouble();


                    if (amount <= 0) {
                        System.out.println("Error: Amount must be positive.");
                        break;
                    }


                    System.out.print("Enter number of users involved: ");
                    if (!sc.hasNextInt()) {
                        System.out.println("Error: Number of users involved must be a number.");
                        sc.next();
                        break;
                    }
                    int n = sc.nextInt();


                    if (n <= 0) {
                        System.out.println("Error: Number of users involved must be at least 1.");
                        break;
                    }


                    List<Integer> sharedWith = new ArrayList<Integer>();
                    System.out.println("Enter User IDs who shared:");
                    boolean invalidUserFound = false;
                    for (int i = 0; i < n; i++) {
                        if (!sc.hasNextInt()) {
                            System.out.println("Error: User ID must be a number.");
                            sc.next();
                            invalidUserFound = true;
                            break;
                        }
                        int userId = sc.nextInt();
                        if (!manager.isUserExists(userId)) {
                            System.out.println("Error: User ID " + userId + " does not exist.");
                            invalidUserFound = true;
                            break;
                        }
                        sharedWith.add(userId);
                    }


                    if (invalidUserFound) {
                        break;
                    }


                    // Optional: prevent payer being the only one involved
                    if (sharedWith.size() == 1 && sharedWith.get(0) == payerId) {
                        System.out.println("Error: Transaction must involve at least one other user.");
                        break;
                    }


                    manager.addTransaction(new Transaction(payerId, amount, sharedWith));
                    System.out.println("Transaction recorded.");
                    break;


                case 3:
                    manager.printBalances();
                    break;


                case 4:
                    manager.simplifyDebtsGreedy();
                    break;


                case 5:
                    manager.simplifyDebtsGraph();
                    break;


                case 6:
                    System.out.println("Thank you for using Expense Splitter. Goodbye!");
                    sc.close();
                    return;


                default:
                    System.out.println("Error: Invalid choice. Please select a number between 1 and 6.");
            }
        }
    }
}
