package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.Assert.assertEquals;


public class JdbcAccountDaoTests extends BaseDaoTests {

    private JdbcAccountDao sut;
    private Account testAccount;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDao(jdbcTemplate);
    }

    @Test
    public void getAccountbyAccountId_given_invalid_user_id_returns_null() {
        Account actualAccount = sut.getAccountbyAccountId(-1);

        Assert.assertNull(actualAccount);
    }
}
