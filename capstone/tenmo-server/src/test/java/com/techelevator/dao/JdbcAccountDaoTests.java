package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcAccountDaoTests extends BaseDaoTests {
    protected static final Account ACCOUNT_1 = new Account();

    private JdbcAccountDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDao(jdbcTemplate);
    }

    // get account by user id
    @Test
    public void getAccountbyUserId_given_invalid_user_id_returns_null() {
        Account actualAccount = sut.getAccountbyUserId(-1);

        Assert.assertNull(actualAccount);
    }

    @Test //TODO: Need user id
    public void getAccountbyUserById_given_valid_user_id_returns_user() {
        Account actualUser = sut.getAccountbyUserId(ACCOUNT_1.getUserId());

        Assert.assertEquals(ACCOUNT_1, actualUser);
    }


    // get account by account id
    @Test
    public void getAccountbyAccountId_given_invalid_user_id_returns_null() {
        Account actualAccount = sut.getAccountbyAccountId(-1);

        Assert.assertNull(actualAccount);
    }


    // update account
    @Test
    public void updated_account_has_expected_values_when_retrieved() {
        Account accountToUpdate = sut.getAccountbyAccountId(2002);

        accountToUpdate.setBalance(300.00);

        sut.updateAccount(accountToUpdate);

        Account retrievedAccount = sut.getAccountbyUserId(1);
        assertAccountsMatch(accountToUpdate, retrievedAccount);
    }

    private void assertAccountsMatch(Account expected, Account actual) {
        Assert.assertEquals(expected.getAccountId(), actual.getAccountId());
        Assert.assertEquals(expected.getBalance(), actual.getBalance());
        Assert.assertEquals(expected.getUserId(), actual.getUserId());
    }
}
