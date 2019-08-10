package com.bcabuddies.moneymanagement.Model;

import java.util.Date;

public class TransactionModel extends TransactionModelID {
    private Date time;
    private String type, interest, amount, userID;
    private boolean isAmt, isInt;

    TransactionModel() {
    }

    public TransactionModel(Date time, String type, String interest, String amount, String userID, boolean isAmt, boolean isInt) {
        this.time = time;
        this.type = type;
        this.interest = interest;
        this.amount = amount;
        this.userID = userID;
        this.isAmt = isAmt;
        this.isInt = isInt;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public boolean isAmt() {
        return isAmt;
    }

    public void setAmt(boolean amt) {
        isAmt = amt;
    }

    public boolean isInt() {
        return isInt;
    }

    public void setInt(boolean anInt) {
        isInt = anInt;
    }
}
