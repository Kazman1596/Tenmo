package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {

    int accountId;
    Double balance;
    int userId;

    public double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public int getAccountId() {
        return accountId;
    }

    public int getUserId() {
        return userId;
    }

}
