package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Balance {

    Double balance;

    public BigDecimal getBalance() {
        return BigDecimal.valueOf(balance);
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

}

