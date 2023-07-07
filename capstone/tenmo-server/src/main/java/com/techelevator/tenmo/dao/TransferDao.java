package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.stereotype.Component;
import java.util.List;



public interface TransferDao {

    Transfer createTransfer(Transfer transfer);
    Transfer updateTransfer(Transfer transfer);
    Transfer getTransferByTransferId(int id);
    List<Transfer> getTransfersByAccountId(int userId);


}
