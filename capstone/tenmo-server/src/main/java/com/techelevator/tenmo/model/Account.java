package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {

    private double balance;
    private int accountId;
    private int userId;

    public Account(double v, int i, int i1) {
    }

    public Account() {

    }

    public BigDecimal getBalance() {
        return BigDecimal.valueOf(balance);
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public int getAccountId(){
       return accountId;
    }

    public int getUserId() {
        return userId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}