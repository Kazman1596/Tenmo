package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.annotation.HttpConstraint;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/transfer")
public class TransferController {

    @Autowired
    public TransferDao transferDao;

    @RequestMapping(path = "/user/{id}", method = RequestMethod.GET)
    public List<Transfer> getTransfersByUserId(@PathVariable int id) {
        List<Transfer> transfers = transferDao.getTransfersByUserId(id);
        if (transfers.size() == 0) {
            System.out.println("No transfers yet!");
        }
        return transfers;
    }

    @RequestMapping(path = "/{transferId}", method = RequestMethod.GET)
    public Transfer getTransferByTransferId(@PathVariable int transferId) {
        Transfer transfer = transferDao.getTransferByTransferId(transferId);
        if (transfer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such transfer.");
        } else {
            return transfer;
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "", method = RequestMethod.POST)
    public Transfer create(@Valid @RequestBody Transfer transfer) {
        return transferDao.createTransfer(transfer);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public Transfer update(@PathVariable int id, @Valid @RequestBody Transfer transfer ) {
        try{
            transfer.setTransferId(id);
            return transferDao.updateTransfer(transfer);
        } catch (DaoException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "We could not find the transfer");
        }
    }

}
