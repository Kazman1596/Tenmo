package com.techelevator.tenmo;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.tenmo.services.UserService;

import java.math.BigDecimal;

public class TransferMenu {
    AccountService accountService = new AccountService();
    ConsoleService consoleService = new ConsoleService();
    UserService userService = new UserService();
    TransferService transferService = new TransferService();
    private final Account currentAccount;

    public TransferMenu(Account currentAccount) {
        this.currentAccount = currentAccount;
    }

    public void transferMenuPrompt(Transfer transfer) {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.promptForApproval();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) { //Approve
                updateTransfer(transfer, true);
                break;
            } else if (menuSelection == 2) { //Reject
                updateTransfer(transfer, false);
                break;
            } else if (menuSelection == 0) { //Do Nothing
                break;
            }
        }
    }

    public Transfer chooseTransferFromList(Transfer[] transfers) {
        String toFrom = "";

        while(true) {
            for (Transfer transfer : transfers) {
                Account accountFrom = accountService.getAccount(transfer.getAccountFromId());
                Account accountTo = accountService.getAccount(transfer.getAccountToId());

                User userFrom = userService.getUserById(accountFrom.getUserId());
                User userTo = userService.getUserById(accountTo.getUserId());

                if (transfer.getAccountFromId() == currentAccount.getAccountId()) {
                    toFrom = "To: " + userTo.getUsername();
                } else {
                    toFrom = "From: " + userFrom.getUsername();
                }

                System.out.println(transfer.getTransferId() + "       " + toFrom + "        $" + transfer.getAmount());
            }
            int transferIdRequested = consoleService.promptForInt("Please choose a Transfer ID -->");
            Transfer transfer = transferService.getTransferByTransferId(transferIdRequested);
            if (transfer != null){
                return transfer;
            } else {
                System.out.println("Transfer was not found");
            }
        }
    }

    public Transfer transferPrompt(int transferTypeId) {
        while(true) {
            //Menu to choose
            int userIdRequested = consoleService.chooseUserFromList(getAllUsers());
            try{
                //See if choice is valid
                User userTo = userService.getUserById(userIdRequested);
                int userToId = userTo.getId();
                int currentUserId = currentAccount.getUserId();
                if (currentUserId == userToId) {
                    System.out.println("Cannot send/request money from yourself!");
                } else {
                    //TODO: Change things for Big Decimal? (If you have time)
                    BigDecimal amount = consoleService.promptForBigDecimal("Please type an amount to/from " + userTo.getUsername() + ": ");
                    Account accountFrom = accountService.getAccountFromUserId(currentUserId);
                    Account accountTo = accountService.getAccountFromUserId(userToId);

                    return new Transfer(transferTypeId, accountTo.getAccountId(),
                            accountFrom.getAccountId(), amount.doubleValue());
                }
            } catch (NullPointerException ex) {
                System.out.println("The user was not found");
            }
        }
    }

    public void updateTransfer(Transfer transfer, boolean isApproved) {
        Account accountFrom = accountService.getAccount(transfer.getAccountFromId());

        if (isApproved) {
            if (currentAccount.getBalance() > transfer.getAmount()) {
                // update transfer status
                transfer.setTransferStatusId(2);
                transferService.updateTransfer(transfer);
                System.out.println("Transfer approved.");

                //decrement our account
                currentAccount.setBalance(currentAccount.getBalance() - transfer.getAmount());
                accountService.updateAccount(currentAccount);
                System.out.println("Your new account balance is " + currentAccount.getBalance());

                //increase their account
                accountFrom.setBalance(accountFrom.getBalance() + transfer.getAmount());
                accountService.updateAccount(accountFrom);
            } else {
                System.out.println("Insufficient funds.");
            }
        } else {
            transfer.setTransferStatusId(3);
            transferService.updateTransfer(transfer);
            System.out.println("Transfer has been rejected.");
        }
    }

    private User[] getAllUsers() {
        return userService.getUsers();
    }
}
