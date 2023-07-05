package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

public interface TransferDao {

    Transfer getTransferByTransferId(int id);
    Transfer getAccountFromIdFromTransferId(int id);
    Transfer getAccountToIdFromTransferId(int id);

}
