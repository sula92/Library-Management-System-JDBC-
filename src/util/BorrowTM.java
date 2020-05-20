package util;

import javafx.scene.control.Button;

import java.util.Date;

public class BorrowTM {

    String borrowId;
    Date borrowedDate;
    String bookId;
    String cusId;
    Button button;


    public BorrowTM(BorrowTM borrowTM) {
    }

    public BorrowTM(String borrowId, Date borrowedDate, String bookId, String cusId,Button button) {
        this.borrowId = borrowId;
        this.borrowedDate = borrowedDate;
        this.bookId = bookId;
        this.cusId = cusId;
        this.button=button;
    }

    public String getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(String borrowId) {
        this.borrowId = borrowId;
    }

    public Date getBorrowedDate() {
        return borrowedDate;
    }

    public void setBorrowedDate(Date borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    @Override
    public String toString() {
        return "BorrowTM{" +
                "borrowId='" + borrowId + '\'' +
                ", borrowedDate=" + borrowedDate +
                ", bookId='" + bookId + '\'' +
                ", cusId='" + cusId + '\'' +
                '}';
    }
}
