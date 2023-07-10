package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {
    private int transferId;
    private int transferTypeId;
    private int transferStatusId = 1;
    private int accountToId;
    private int accountFromId;
    private BigDecimal amount;

    public Transfer() {};
    public Transfer(int transferTypeId, int accountToId, int accountFromId, BigDecimal amount) {
        this.transferTypeId = transferTypeId;
        this.accountToId = accountToId;
        this.accountFromId = accountFromId;
        this.amount = amount;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public int getAccountToId() {
        return accountToId;
    }

    public void setAccountToId(int accountToId) {
        this.accountToId = accountToId;
    }

    public int getAccountFromId() {
        return accountFromId;
    }

    public void setAccountFromId(int accountFromId) {
        this.accountFromId = accountFromId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTypeString() {
        String type = "";
        if (transferTypeId == 1){
            type = "Request";
        }
        if (transferTypeId == 2){
            type = "Send";
        }
        return type;
    }

    public String getStatusString() {
        String status = "";
        if (transferStatusId == 1) {
            status = "Pending";
        }
        if (transferStatusId == 2) {
            status = "Approved";
        }
        if (transferStatusId == 3) {
            status = "Rejected";
        }
        return status;
    }

}
