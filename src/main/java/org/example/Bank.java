package org.example;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Bank {

    // Choice 1
    public static int handleChoice1Input(Scanner scanner) {
        while (true) {
            System.out.print("""
    
                        # Select an option -->
                            1 - Access an account
                            2 - Show customer details
                            3 - Terminate program
                            
                            ENTER:\s""");

            try {
                int choice1 = scanner.nextInt();
                scanner.nextLine();

                return choice1;

            } catch (InputMismatchException e) {
                System.out.println("\nInvalid input! Please enter a valid integer.");
                scanner.nextLine();
            }
        }
    }

    // Choice 2
    public static int handleChoice2Input(Scanner scanner) {
        while (true) {
            System.out.print("""

                            # Select an option -->
                                1 - Show account details
                                2 - Deposit cash
                                3 - Withdraw cash
                                4 - Transfer cash
                                5 - Go Back
                                
                                ENTER:\s""");

            try {
                int choice2 = scanner.nextInt();
                scanner.nextLine();

                return choice2;

            } catch(InputMismatchException e) {
                System.out.println("\nInvalid input! Please enter a valid integer.");
                scanner.nextLine();
            }
        }
    }

    // Choice 1 : Option 2
    public static void handleShowCustomerDetails(BankingSystem bankingSystem, Scanner scanner) {
        System.out.print("\n# Enter NIC: ");
        String nic = scanner.next().strip();

        Customer loadedCustomer = bankingSystem.loadCustomerByNIC(nic);
        if (loadedCustomer != null) {
            bankingSystem.printDetails(loadedCustomer);
        } else {
            System.out.println("\nNo customer found with NIC: " + nic);
        }
    }

    // Choice 1 : Option 1
    public static String handleAccountNumberInput(Scanner scanner, String partialString) {
        System.out.printf("\n# Enter %s account number: ", partialString);
        return scanner.next().strip();
    }

    // Choice 1 : Option 1
    public static Account handleAccessAccount(BankingSystem bankingSystem, String accNumber) {
        return bankingSystem.loadAccountByNumber(accNumber);

    }

    // Choice 1 : Option 1 -> Choice 2 : Option 2, 3, 4
    public static double handleAmountInput(Scanner scanner, String transactionType) {
        while (true) {
            System.out.printf("\nEnter amount to %s: ", transactionType);

            try {
                return Double.parseDouble(scanner.next());

            } catch(NumberFormatException e) {
                System.out.println("\nInvalid input! Please enter a valid amount.");
            }
        }
    }

    // Choice 1 : Option 1 -> Choice 2 : Option 2, 3, 4
    public static void handlePrintDetails(BankingSystem bankingSystem, Printable... printableObjects) {
        for (Printable printable : printableObjects) {
          bankingSystem.printDetails(printable);
        }
    }

    // Choice 1 : Option 1 -> Choice 2 : Option 2
    public static void handleDepositCash(BankingSystem bankingSystem, Scanner scanner, Account loadedAccount) {
        double amount = handleAmountInput(scanner, "deposit");

        Transaction depositTransaction = bankingSystem.depositCash(loadedAccount, amount);

        Transaction mergedTransaction = bankingSystem.saveTransaction(depositTransaction);
        Account mergedAccount = bankingSystem.saveAccount(loadedAccount);

        System.out.print("\nDetails after the deposit:");
        handlePrintDetails(bankingSystem, mergedAccount, mergedTransaction);

    }

    // Choice 1 : Option 1 -> Choice 2 : Option 3
    public static void handleWithdrawCash(BankingSystem bankingSystem, Scanner scanner, Account loadedAccount) {
        double amount = handleAmountInput(scanner, "withdraw");

        Transaction withdrawTransaction = bankingSystem.withdrawCash(loadedAccount, amount);

        Transaction mergedTransaction = bankingSystem.saveTransaction(withdrawTransaction);
        Account mergedAccount = bankingSystem.saveAccount(loadedAccount);

        System.out.println("\nDetails after the withdrawal:");
        handlePrintDetails(bankingSystem, mergedAccount, mergedTransaction);
    }

    // Choice 1 : Option 1 -> Choice 2 : Option 4
    public static void handleTransferCash(BankingSystem bankingSystem, Scanner scanner, Account loadedAccount) {
        String recipientAccNumber = handleAccountNumberInput(scanner, "recipient's");
        Account recipientAccount = handleAccessAccount(bankingSystem, recipientAccNumber);
        double amount = handleAmountInput(scanner, "transfer");

        Transaction transferTransaction = bankingSystem.transferCash(loadedAccount, recipientAccount, amount);

        Transaction mergedTransaction = bankingSystem.saveTransaction(transferTransaction);
        bankingSystem.saveAccount(loadedAccount);
        bankingSystem.saveAccount(recipientAccount);

        System.out.println("\nDetails after the transfer:");
        handlePrintDetails(bankingSystem, loadedAccount, recipientAccount, mergedTransaction);
    }


    public static void main(String[] args) {

        BankingSystem bankingSystem = new BankingSystem();
        Scanner scanner = new Scanner(System.in);

        while (true) {
           int choice1 = handleChoice1Input(scanner);

           switch (choice1) {
               case 3:
                   System.out.println("\nTerminating application...");
                   break;
               case 2:
                   handleShowCustomerDetails(bankingSystem, scanner);
                   break;
               case 1:
                   boolean goBack = false;

                   String accNumber = handleAccountNumberInput(scanner, "the");
                   Account loadedAccount = handleAccessAccount(bankingSystem, accNumber);

                   if (loadedAccount == null) {
                       System.out.println("\nNo account found with account number: " + accNumber);
                       goBack = true;
                   }

                   while (!goBack) {
                       int choice2 = handleChoice2Input(scanner);

                       switch (choice2) {
                           case 1:
                               handlePrintDetails(bankingSystem, loadedAccount);
                               break;
                           case 2:
                               handleDepositCash(bankingSystem, scanner, loadedAccount);
                               break;
                           case 3:
                               handleWithdrawCash(bankingSystem, scanner, loadedAccount);
                               break;
                           case 4:
                               handleTransferCash(bankingSystem, scanner, loadedAccount);
                               break;
                           case 5:
                               goBack = true;
                               break;
                           default:
                               System.out.println("\nInvalid Input! Please select your choice and enter the relevant number.");
                               break;
                       }
                   }
                   break;
               default:
                   System.out.println("\nInvalid Input! Please select your choice and enter the relevant number.");
                   break;
           }

            if (choice1 == 3) break;

        }
    }
}