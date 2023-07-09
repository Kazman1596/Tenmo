package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;

import java.math.BigDecimal;
import java.util.Scanner;

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in);

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }
    public void createTransferBanner() {
        System.out.println("--------------------------------------------");
        System.out.println("Past Transfers");
        System.out.println("ID           From/To           Amount");
        System.out.println("--------------------------------------------");
    }
    public void createPendingTransferBanner() {
        System.out.println("--------------------------------------------");
        System.out.println("Pending Transfers");
        System.out.println("ID           From/To           Amount");
        System.out.println("--------------------------------------------");
    }

    public void createSendRequestTransferBanner() {
        System.out.println("--------------------------------------------");
        System.out.println("Users");
        System.out.println("ID           Username");
        System.out.println("--------------------------------------------");
    }

    public void createDetailBanner() {
        System.out.println("--------------------------------------------");
        System.out.println("Transfer Details");
        System.out.println("--------------------------------------------");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("0: Exit");
        System.out.println();
    }

    public int chooseUserFromList(User[] users) {
        for (User user : users) {
            System.out.println(user.getId() + "       " + user.getUsername());
        }
       return promptForInt("Please choose a user id -->");
    }

        public UserCredentials promptForCredentials () {
            String username = promptForString("Username: ");
            String password = promptForString("Password: ");
            return new UserCredentials(username, password);
        }

        public String promptForString (String prompt){
            System.out.print(prompt);
            return scanner.nextLine();
        }

        public int promptForInt (String prompt){
            System.out.print(prompt);
            while (true) {
                try {
                    return Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a number.");
                }
            }
        }

        public BigDecimal promptForBigDecimal (String prompt){
            System.out.print(prompt);
            while (true) {
                try {
                    //updated to only accept a positive number
                    BigDecimal amount = new BigDecimal(scanner.nextLine());
                    if (amount.compareTo(BigDecimal.ZERO) > 0) {
                        return amount;
                    } else {
                        System.out.println("Amount cannot be a number less than 0");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a decimal number.");
                }
            }
        }

        public void pause () {
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }

        public void printErrorMessage () {
            System.out.println("An error occurred. Check the log for details.");
        }

        public void promptForApproval() {
            System.out.println();
            System.out.println("1: Approve");
            System.out.println("2: Reject");
            System.out.println("0: Don't approve or reject");
            System.out.println();
        }
    }
