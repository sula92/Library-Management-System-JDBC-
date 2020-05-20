package entity;

import java.sql.Date;

public class Fee {

    String borrowId;
    int amount;

    public Fee() {
    }

    public Fee(String borrowId, int amount) {
        this.borrowId = borrowId;
        this.amount = amount;
    }

    public String getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(String borrowId) {
        this.borrowId = borrowId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Fee{" +
                "borrowId='" + borrowId + '\'' +
                ", amount=" + amount +
                '}';
    }
}
