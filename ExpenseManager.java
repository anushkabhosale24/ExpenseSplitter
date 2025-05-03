import java.util.*;


public class ExpenseManager {
    private Map<Integer, User> users = new HashMap<>();
    private Map<Integer, Double> netBalance = new HashMap<>();


    // Add a new user
    public void addUser(User user) {
        users.put(user.getId(), user);
        netBalance.put(user.getId(), 0.0);
    }


    // Check if a user exists
    public boolean isUserExists(int userId) {
        return users.containsKey(userId);
    }


    // Add a new transaction and update balances
    public void addTransaction(Transaction transaction) {
        double splitAmount = transaction.getAmount() / transaction.getSharedWith().size();
        int payerId = transaction.getPaidBy();


        for (int userId : transaction.getSharedWith()) {
            if (userId == payerId) continue; // Payer doesn't owe themselves
            netBalance.put(userId, netBalance.getOrDefault(userId, 0.0) - splitAmount);
            netBalance.put(payerId, netBalance.getOrDefault(payerId, 0.0) + splitAmount);
        }
    }


    // Print all users' current balances
    public void printBalances() {
        System.out.println("\n---- Net Balances ----");
        for (Map.Entry<Integer, Double> entry : netBalance.entrySet()) {
            String name = users.get(entry.getKey()).getName();
            double balance = entry.getValue();
            System.out.println(name + ": Rs." + String.format("%.2f", balance));
        }
    }


    // Greedy method to simplify debts
    public void simplifyDebtsGreedy() {
        PriorityQueue<Map.Entry<Integer, Double>> debtors = new PriorityQueue<>(Comparator.comparingDouble(Map.Entry::getValue));
        PriorityQueue<Map.Entry<Integer, Double>> creditors = new PriorityQueue<>((a, b) -> Double.compare(b.getValue(), a.getValue()));


        for (Map.Entry<Integer, Double> entry : netBalance.entrySet()) {
            double balance = entry.getValue();
            if (Math.abs(balance) < 1e-6) continue;


            if (balance < 0) {
                debtors.add(new AbstractMap.SimpleEntry<>(entry.getKey(), balance));
            } else {
                creditors.add(new AbstractMap.SimpleEntry<>(entry.getKey(), balance));
            }
        }


        System.out.println("\n---- Simplified Payments (Greedy) ----");
        while (!debtors.isEmpty() && !creditors.isEmpty()) {
            Map.Entry<Integer, Double> debtor = debtors.poll();
            Map.Entry<Integer, Double> creditor = creditors.poll();


            double amount = Math.min(-debtor.getValue(), creditor.getValue());
            int from = debtor.getKey();
            int to = creditor.getKey();


            System.out.println(users.get(from).getName() + " pays ₹" + String.format("%.2f", amount) + " to " + users.get(to).getName());


            double newDebtorBalance = debtor.getValue() + amount;
            double newCreditorBalance = creditor.getValue() - amount;


            if (Math.abs(newDebtorBalance) > 1e-6)
                debtors.add(new AbstractMap.SimpleEntry<>(from, newDebtorBalance));
            if (Math.abs(newCreditorBalance) > 1e-6)
                creditors.add(new AbstractMap.SimpleEntry<>(to, newCreditorBalance));
        }
    }


    // Graph-based method to simplify debts (Min Cash Flow)
    public void simplifyDebtsGraph() {
        System.out.println("\n---- Simplified Payments (Graph) ----");
        int maxId = 0;
        for (Integer id : users.keySet()) {
            if (id > maxId) maxId = id;
        }


        double[] balanceArr = new double[maxId + 1]; // Proper sizing


        for (Map.Entry<Integer, Double> entry : netBalance.entrySet()) {
            int userId = entry.getKey();
            balanceArr[userId] = entry.getValue();
        }


        minCashFlow(balanceArr);
    }


    private int getMaxCreditor(double[] balance) {
        int maxIndex = 0;
        for (int i = 1; i < balance.length; i++) {
            if (balance[i] > balance[maxIndex]) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }


    private int getMaxDebtor(double[] balance) {
        int minIndex = 0;
        for (int i = 1; i < balance.length; i++) {
            if (balance[i] < balance[minIndex]) {
                minIndex = i;
            }
        }
        return minIndex;
    }


    // Recursive function to minimize cash flow
    private void minCashFlow(double[] balance) {
        int maxCredit = getMaxCreditor(balance);
        int maxDebit = getMaxDebtor(balance);


        if (Math.abs(balance[maxCredit]) < 1e-6 && Math.abs(balance[maxDebit]) < 1e-6)
            return;


        double minAmount = Math.min(-balance[maxDebit], balance[maxCredit]);
        balance[maxCredit] -= minAmount;
        balance[maxDebit] += minAmount;


        if (users.containsKey(maxDebit) && users.containsKey(maxCredit)) {
            System.out.println(users.get(maxDebit).getName() + " pays Rs." + String.format("%.2f", minAmount) + " to " + users.get(maxCredit).getName());
        }


        minCashFlow(balance);
    }
}