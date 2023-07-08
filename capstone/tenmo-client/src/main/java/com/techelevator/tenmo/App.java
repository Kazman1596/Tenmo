package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;

import java.io.FilterOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class App {

    //TODO: Integration tests on Server side

    private static final String API_BASE_URL = "http://localhost:8080/";
    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final AccountService accountService = new AccountService();
    private final TransferService transferService = new TransferService();
    private final UserService userService = new UserService();
    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
        int userId = currentUser.getUser().getId();
        Account currentAccount = accountService.getAccountFromUserId(userId);
        System.out.println("$ " + currentAccount.getBalance());

	}

	private void viewTransferHistory() {
        Account userAccount = accountService.getAccountFromUserId(currentUser.getUser().getId());
        Transfer[] transfers = transferService.getTransfersByAccountId(userAccount.getAccountId());
        createTransferBanner();
        Transfer transfer = chooseTransferFromList(transfers);
        getTransferDetails(transfer);
		
	}

	private void viewPendingRequests() {
        Account userAccount = accountService.getAccountFromUserId(currentUser.getUser().getId());
        Transfer[] pendingTransfers = transferService.getPendingTransfers(userAccount.getAccountId(), 1);
        createTransferBanner();
        Transfer transfer = chooseTransferFromList(pendingTransfers);
        getTransferDetails(transfer);
        transferMenu(transfer);
	}
//TODO: add spacing between deets and menu
    private void transferMenu(Transfer transfer) {
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

	private void sendBucks() {
        createSendRequestTransferBanner();
		Transfer transfer = transferPrompt(2);
        if (transfer.getAmount() < accountService.getAccount(transfer.getAccountFromId()).getBalance()) {
            transferService.createTransfer(transfer);
            System.out.println();
            System.out.println("Successfully sent TEBucks!");
        } else {
            System.out.println("Amount exceeds available account balance.");
        }

	}

	private void requestBucks() {
        createSendRequestTransferBanner();
        Transfer transfer = transferPrompt(1);
        transferService.createTransfer(transfer);
        System.out.println();
        System.out.println("Successfully requested TEBucks!");
	}

    private User[] getAllUsers() {
        return userService.getUsers();
    }

    private void getStringForTransfer(Transfer transfer) {

        Account accountFrom = accountService.getAccount(transfer.getAccountFromId());
        Account accountTo = accountService.getAccount(transfer.getAccountToId());
        User userFrom = userService.getUserById(accountFrom.getUserId());
        User userTo = userService.getUserById(accountTo.getUserId());
        System.out.println(userFrom.getUsername() + "          " + userTo.getUsername() + "          " + transfer.getAmount());

    }

    //TODO: This is disgusting. Make prettier.
    private Transfer transferPrompt(int transferTypeId) {
        while(true) {
            int userIdRequested = consoleService.chooseUserFromList(getAllUsers());
            try{
                User userTo = userService.getUserById(userIdRequested);
                int userToId = userTo.getId();
                int userFromId = currentUser.getUser().getId();
                if (userFromId == userToId) {
                    System.out.println("Cannot send/request money from yourself!");
                } else {
                    BigDecimal amount = consoleService.promptForBigDecimal("Please type an amount to request to " + userTo.getUsername() + ": ");
                    Account accountFrom = accountService.getAccountFromUserId(userFromId);
                    Account accountTo = accountService.getAccountFromUserId(userToId);

                    return new Transfer(transferTypeId, accountTo.getAccountId(),
                            accountFrom.getAccountId(), amount.doubleValue());
                }
            } catch (NullPointerException ex) {
                System.out.println("The user was not found");
            }
        }
    }
//TODO: DOes transfer menu need to be 0, 1 2,
    //TODO: APproved xfers showing up in view pendings still
    public Transfer chooseTransferFromList(Transfer[] transfers) {
        String toFrom = "";
        Account currentAccount = accountService.getAccountFromUserId(currentUser.getUser().getId());
        Account otherAccount = null;

        while(true) {
            for (Transfer transfer : transfers) {

                otherAccount = accountService.getAccount(transfer.getAccountFromId());

                if (transfer.getAccountFromId() == currentAccount.getAccountId()) {
                    toFrom = "To:";
                } else {
                    toFrom = "From:";
                }
                User user = userService.getUserById(currentAccount.getUserId());
                User userFrom = userService.getUserById(otherAccount.getUserId());

                System.out.println(transfer.getTransferId() + "    " + toFrom + " " + userFrom.getUsername() + "     $" + transfer.getAmount());
            }
            int transferIdRequested = consoleService.promptForInt("Please choose a Transfer ID -->");
            //TODO: If transfer doesn't exist, try/catch does nothing
            try{
                Transfer transfer = transferService.getTransferByTransferId(transferIdRequested);
                return transfer;
            } catch (NullPointerException e) {
                System.out.println("Transfer was not found");
            }
        }

    }

    private void createTransferBanner() {
        System.out.println("--------------------------------------------");
        System.out.println("Transfers");
        System.out.println("ID           From/To           Amount");
        System.out.println("--------------------------------------------");
    }

    private void createSendRequestTransferBanner() {
        System.out.println("--------------------------------------------");
        System.out.println("Users");
        System.out.println("ID           Username");
        System.out.println("--------------------------------------------");
    }

    private void createDetailBanner() {
        System.out.println("--------------------------------------------");
        System.out.println("Transfer Details");
        System.out.println("--------------------------------------------");
    }

    private void getTransferDetails(Transfer transfer) {
        //Get accounts
        Account accountFrom = accountService.getAccount(transfer.getAccountFromId());
        Account accountTo = accountService.getAccount(transfer.getAccountToId());

        //Get users
        User userFrom = userService.getUserById(accountFrom.getUserId());
        User userTo = userService.getUserById(accountTo.getUserId());

        createDetailBanner();
        System.out.println("Id: " + transfer.getTransferId());
        System.out.println("From: " + userFrom.getUsername());
        System.out.println("To: " + userTo.getUsername());
        System.out.println("Type: " + transfer.getTypeString());
        System.out.println("Status: " + transfer.getStatusString());
        System.out.println("Amount: $" + transfer.getAmount());
    }

    private void approveRejectTransfer(Transfer transfer) {
        //TODO: transfer has to update
        //TODO: "Transfer approved/rejected"
        //TODO: Update Balance (account.setBalance() && updateAccount()) to reflect approval based on to/from
        //TODO: "Your new balance is..."
        //transfer.setTransferStatusId();
    }

    private void updateTransfer(Transfer transfer, boolean isApproved) {
        //TODO: If I approve a request for money and have sufficient funds, then update transfer status ID and subtract from my account balance
        // Approvals are always initiated by the accountFrom (user who is Sending the money)
        // Send Money needs to be updated
        Account accountFrom = accountService.getAccount(transfer.getAccountFromId());
        Account accountTo = accountService.getAccount(transfer.getAccountToId()); //Needed in order to increment their balance
        Account currentAccount = accountService.getAccountFromUserId(currentUser.getUser().getId());

        if (isApproved) {
            //TODO: don't allow users to approve (or even see) their own pending requests.
            if (currentAccount.getBalance() > transfer.getAmount()) {

                // update transfer status
                transfer.setTransferStatusId(2);
                transferService.updateTransfer(transfer);
                System.out.println("Transfer approved.");

                //decrement our account
                accountTo.setBalance(accountTo.getBalance() - transfer.getAmount());
                accountService.updateAccount(accountTo);
                System.out.println("Your new account balance is " + accountTo.getBalance());

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
}
