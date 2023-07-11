package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcTransferDaoTests extends BaseDaoTests {

    protected static final Transfer TRANSFER_1 = new Transfer(50.00, 2002, 1001, 111);

    Transfer testTransfer = new Transfer();
    private JdbcTransferDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTransferDao(jdbcTemplate);
    }

    @Test
    public void getTransferById_returns_null_when_id_not_found() {
        Transfer transfer = sut.getTransferByTransferId(99);
        Assert.assertNull(transfer);
    }

    @Test
    public void getTransferByTransferId_given_invalid_user_id_returns_null() {
        Transfer actualTransfer = sut.getTransferByTransferId(-1);

        Assert.assertNull(actualTransfer);
    }

    private void assertTransfersMatch(Transfer expected, Transfer actual) {
        Assert.assertEquals(expected.getAccountFromId(), actual.getAccountFromId());
        Assert.assertEquals(expected.getAccountToId(), actual.getAccountToId());
        Assert.assertEquals(expected.getAmount(), actual.getAmount());
        Assert.assertEquals(expected.getTransferId(), actual.getTransferId());
        Assert.assertEquals(expected.getTransferStatusId(), actual.getTransferStatusId());
        Assert.assertEquals(expected.getTransferTypeId(), actual.getTransferTypeId());
    }

}
