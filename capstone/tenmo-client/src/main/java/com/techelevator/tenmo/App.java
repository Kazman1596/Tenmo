package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;

import java.math.BigDecimal;
import java.util.List;

public class App {

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
                viewCurrentBalance(currentUser.getUser().getId());
            } else if (menuSelection == 2) {
                //TODO: NOT HARDCODED
                viewTransferHistory(2001);
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

	private void viewCurrentBalance(int id) {
        Account currentAccount = accountService.getAccountFromUserId(id);
        System.out.println(currentAccount.getBalance());

	}

	private void viewTransferHistory(int id) {
        Transfer[] transfers = transferService.getTransfersByUserId(id);
        System.out.println("--------------------------------------------");
        System.out.println("Transfers");
        System.out.println("ID           From/To           Amount");
        System.out.println("--------------------------------------------");

        //TODO: transfer.getAccountToId() should return username NOT id.
        for (Transfer transfer : transfers) {
            Account accountFrom = accountService.getAccount(transfer.getAccountFromId());
            Account accountTo = accountService.getAccount(transfer.getAccountToId());
            User userFrom = userService.getUserById(accountFrom.getUserId());
            User userTo = userService.getUserById(accountTo.getUserId());
            System.out.println(userFrom.getUsername() + "          " + userTo.getUsername() + "          " + transfer.getAmount());
        }
		
	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
		
	}

	private void sendBucks() {
		User user = consoleService.chooseFromList(getAllUsers());
        System.out.println(user.getUsername());
        BigDecimal amount = consoleService.promptForBigDecimal("Please type an amount to send.");

        Transfer transfer = new Transfer(2, accountService.getAccountFromUserId(user.getId()).getAccountId(),
                accountService.getAccountFromUserId(currentUser.getUser().getId()).getAccountId(), amount.doubleValue());

        try {
            transferService.

        }


	}

	private void requestBucks() {

        User userToRequest = consoleService.chooseFromList(getAllUsers());
        System.out.println(userToRequest.getUsername());
        BigDecimal amount = consoleService.promptForBigDecimal("Please type an amount to request.");

        Transfer transfer = new Transfer();
	}

    private User[] getAllUsers() {
        return userService.getUsers();
    }

}
