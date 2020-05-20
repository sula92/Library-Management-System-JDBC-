package entity;

import java.time.LocalDate;
import java.sql.Date;

public class Return {

    String borrowId;
    Date returnedDate;


    public Return() {
    }

    public Return(String borrowId, Date returnedDate) {
        this.borrowId = borrowId;
        this.returnedDate = returnedDate;
    }


    public String getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(String borrowId) {
        this.borrowId = borrowId;
    }

    public Date getReturnedDate() {
        return returnedDate;
    }

    public void setReturnedDate(Date returnedDate) {
        this.returnedDate = returnedDate;
    }

    @Override
    public String toString() {
        return "Return{" +
                "borrowId='" + borrowId + '\'' +
                ", returnedDate=" + returnedDate +
                '}';
    }
}
