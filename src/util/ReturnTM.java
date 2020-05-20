package util;

import java.util.Date;

public class ReturnTM {

   String borrowId;
   Date borrowDate;
   Date returnedDate;
   String fine;
   double amount;

    public ReturnTM() {
    }

    public ReturnTM(String borrowId, Date borrowDate, Date returnedDate, String fine, double amount) {
        this.borrowId = borrowId;
        this.borrowDate = borrowDate;
        this.returnedDate = returnedDate;
        this.fine = fine;
        this.amount = amount;
    }

    public String getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(String borrowId) {
        this.borrowId = borrowId;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    public Date getReturnedDate() {
        return returnedDate;
    }

    public void setReturnedDate(Date returnedDate) {
        this.returnedDate = returnedDate;
    }

    public String getFine() {
        return fine;
    }

    public void setFine(String fine) {
        this.fine = fine;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "ReturnTM{" +
                "borrowId='" + borrowId + '\'' +
                ", borrowDate=" + borrowDate +
                ", returnedDate=" + returnedDate +
                ", fine='" + fine + '\'' +
                ", amount=" + amount +
                '}';
    }
}
