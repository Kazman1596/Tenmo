package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

public interface AccountDao {

    Account getAccountbyUserId(int userId);

    Account getAccountbyAccountId(int accountId);

    Account updateAccount(Account account);

}
