package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.stereotype.Component;



public interface TransferDao {

    Transfer getTransferByTransferId(int id);
    Transfer getAccountFromIdFromTransferId(int id);
    Transfer getAccountToIdFromTransferId(int id);
    String getTransferStatusByTransferId(int id);
    String getTransferTypeByTransferId(int id);

}
