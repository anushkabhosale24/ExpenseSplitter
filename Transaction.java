import java.util.List;

public class Transaction {
    private int paidBy;
    private double amount;
    private List<Integer> sharedWith;

    public Transaction(int paidBy, double amount, List<Integer> sharedWith) {
        this.paidBy = paidBy;
        this.amount = amount;
        this.sharedWith = sharedWith;
    }

    public int getPaidBy() {
        return paidBy;
    }

    public double getAmount() {
        return amount;
    }

    public List<Integer> getSharedWith() {
        return sharedWith;
    }
}
