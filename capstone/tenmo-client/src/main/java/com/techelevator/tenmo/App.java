package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;

import java.io.FilterOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";
    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final AccountService accountService = new AccountService();
    private final TransferService transferService = new TransferService();
    private final UserService userService = new UserService();
    private AuthenticatedUser currentUser;
    private Account currentAccount;
    private TransferMenu transferMenu;

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
        } else {
            currentAccount = accountService.getAccountFromUserId(currentUser.getUser().getId());
            transferMenu = new TransferMenu(currentAccount);
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
        System.out.println("$ " + currentAccount.getBalance());
	}

	private void viewTransferHistory() {
        Transfer[] transfers = transferService.getTransfersByAccountId(currentAccount.getAccountId());
        consoleService.createTransferBanner();
        Transfer transfer = transferMenu.chooseTransferFromList(transfers);
        getTransferDetails(transfer);
	}

	private void viewPendingRequests() {
        Transfer[] pendingTransfers = transferService.getPendingTransfers(currentAccount.getAccountId(), 1);
        consoleService.createPendingTransferBanner();
        if (pendingTransfers.length != 0) {
            Transfer transfer = transferMenu.chooseTransferFromList(pendingTransfers);
            getTransferDetails(transfer);
            transferMenu.transferMenuPrompt(transfer);
        } else {
            System.out.println("Looks like you don't owe anyone money...");
        }
	}

	private void sendBucks() {
        consoleService.createSendRequestTransferBanner();

        //Creating transfer
		Transfer transfer = transferMenu.transferPrompt(2);
        transfer.setTransferStatusId(2);

        //Account involved aside from currentUser
        Account accountTo = accountService.getAccount(transfer.getAccountToId());

        //Checking for balance and updating balances
        //transfer.getAmount() < currentAccount.getBalance()
        if (transfer.getAmount().compareTo(currentAccount.getBalance()) < 0) {
            //Setting new balances
            accountTo.setBalance(accountTo.getBalance().add(transfer.getAmount()));
            currentAccount.setBalance(currentAccount.getBalance().subtract(transfer.getAmount()));
            //Updating database
            transferService.createTransfer(transfer);
            accountService.updateAccount(accountTo);
            accountService.updateAccount(currentAccount);

            System.out.println();
            System.out.println("Successfully sent TEBucks!");
            System.out.println("Your new balance is $" + currentAccount.getBalance());
        } else {
            System.out.println("Amount exceeds available account balance.");
        }

	}

	private void requestBucks() {
        consoleService.createSendRequestTransferBanner();
        Transfer transfer = transferMenu.transferPrompt(1);
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

    private Transfer transferPrompt(int transferTypeId) {
        while(true) {
            //Menu to choose
            int userIdRequested = consoleService.chooseUserFromList(getAllUsers());
            try{
                //See if choice is valid
                User userTo = userService.getUserById(userIdRequested);
                int userToId = userTo.getId();
                int userFromId = currentUser.getUser().getId();
                if (userFromId == userToId) {
                    System.out.println("Cannot send/request money from yourself!");
                } else {
                    //TODO: Reword
                    BigDecimal amount = consoleService.promptForBigDecimal("Please type an amount to/from " + userTo.getUsername() + ": ");
                    Account accountFrom = accountService.getAccountFromUserId(userFromId);
                    Account accountTo = accountService.getAccountFromUserId(userToId);

                    return new Transfer(transferTypeId, accountTo.getAccountId(),
                            accountFrom.getAccountId(), amount.doubleValue());
                }
            } catch (NullPointerException ex) {
                System.out.println("The user was not found!!!!!!");
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
            Transfer transfer = transferService.getTransferByTransferId(transferIdRequested);
            if (transfer != null){
                return transfer;
            } else {
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

        consoleService.createDetailBanner();
        System.out.println("Id: " + transfer.getTransferId());
        System.out.println("From: " + userFrom.getUsername());
        System.out.println("To: " + userTo.getUsername());
        System.out.println("Type: " + transfer.getTypeString());
        System.out.println("Status: " + transfer.getStatusString());
        System.out.println("Amount: $" + transfer.getAmount());
    }

}
