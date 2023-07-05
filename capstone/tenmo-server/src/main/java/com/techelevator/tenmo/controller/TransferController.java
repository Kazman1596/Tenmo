package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.annotation.HttpConstraint;

@RestController
@RequestMapping("/transfer")
public class TransferController {

    public TransferDao transferDao;

    public TransferController(TransferDao transferDao) {
        this.transferDao = transferDao;
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Transfer getTransferByTransferId(@PathVariable int transferId) {
        Transfer transfer = transferDao.getTransferByTransferId(transferId);
        if (transfer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "rieughdfhglsdfjsklfds");
        } else {
            return transfer;
        }
    }
}
