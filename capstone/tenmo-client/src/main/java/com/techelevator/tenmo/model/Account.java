package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {

    int accountId;
    BigDecimal balance;
    int userId;

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public int getAccountId() {
        return accountId;
    }

    public int getUserId() {
        return userId;
    }

}
