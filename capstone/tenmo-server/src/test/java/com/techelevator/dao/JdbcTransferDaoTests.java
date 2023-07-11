package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class JdbcTransferDaoTests extends BaseDaoTests {

    protected static final Transfer TRANSFER_1 = new Transfer(50.00, 2002, 1001, 111);

    private JdbcTransferDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTransferDao(jdbcTemplate);
    }


    //create Transfer
    @Test
    public void createTransfer_returns_transfer_with_id_and_expected_values() {

        //Arrange
        Transfer myTransfer = new Transfer();
        myTransfer.setTransferTypeId(13);
        myTransfer.setTransferStatusId(101);
        myTransfer.setAccountFromId(2002);
        myTransfer.setAccountToId(3003);

        //Act
        Transfer createdTransfer = sut.createTransfer(myTransfer);
        myTransfer.setTransferId(createdTransfer.getTransferId());

        //Assert
        assertTransfersMatch(myTransfer, createdTransfer);

        Transfer retrieved = sut.getTransferByTransferId(createdTransfer.getTransferId());
        assertTransfersMatch(myTransfer, retrieved);


    }

    //update Transfer
    @Test
    public void updated_transfer_has_expected_values_when_retrieved() {
        Transfer transferToUpdate = sut.getTransferByTransferId(2002);

        transferToUpdate.setTransferTypeId(130);
        transferToUpdate.setTransferStatusId(333);
        transferToUpdate.setAccountFromId(7000);
        transferToUpdate.setAccountToId(8000);

        sut.updateTransfer(transferToUpdate);

        Transfer retrievedTransfer = sut.getTransferByTransferId(1);
        assertTransfersMatch(transferToUpdate, retrievedTransfer);
    }


    //get Transfer by transfer id
    @Test
    public void getTransferByTransferId_given_invalid_user_id_returns_null() {
        Transfer actualTransfer = sut.getTransferByTransferId(-1);

        Assert.assertNull(actualTransfer);
    }

    @Test
    public void getTransferByTransferId_given_valid_user_id_returns_user() {
        Transfer actualTransfer = sut.getTransferByTransferId((TRANSFER_1.getTransferId()));

        Assert.assertEquals(TRANSFER_1, actualTransfer);
    }

    //get Transfer by account id
    @Test
    public void getTransferByAccountId_given_invalid_user_id_returns_null() {
        List<Transfer> actualTransfers = sut.getTransfersByAccountId(-1, 1);

        Assert.assertNull(actualTransfers);
    }

    @Test
    public void getTransferByAccountId_given_valid_userFrom_id_returns_user() {
        List <Transfer> transfersByUser = sut.getTransfersByAccountId(TRANSFER_1.getAccountFromId(), TRANSFER_1.getTransferStatusId());

        Assert.assertEquals(TRANSFER_1.getAccountFromId(), TRANSFER_1.getTransferStatusId());
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
