package org.example;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Bank {

    // Options under Choice 1
    private static final int OPTION_ACCESS_ACCOUNT = 1;
    private static final int OPTION_SHOW_CUSTOMER_DETAILS = 2;
    private static final int OPTION_TERMINATE_APPLICATION = 3;

    // Options under Choice 2
    private static final int OPTION_SHOW_ACCOUNT_DETAILS = 1;
    private static final int OPTION_DEPOSIT_CASH = 2;
    private static final int OPTION_WITHDRAW_CASH = 3;
    private static final int OPTION_TRANSFER_CASH = 4;
    private static final int OPTION_GO_BACK = 5;

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            BankingSystem bankingSystem = new BankingSystem();

            while (true) {
                int choice1 = handleChoice1Input(scanner);

                switch (choice1) {
                    case OPTION_ACCESS_ACCOUNT:
                        handleOptionAccessAccount(bankingSystem, scanner);
                        break;
                    case OPTION_SHOW_CUSTOMER_DETAILS:
                        handleOptionShowCustomerDetails(bankingSystem, scanner);
                        break;
                    case OPTION_TERMINATE_APPLICATION:
                        System.out.println("\nTerminating application...");
                        return;
                    default:
                        System.out.println("\nInvalid Input! Please select your choice and enter the relevant number.");
                        break;
                }
            }
        }
    }

    // Choice 1 - prompt and recording input
    private static int handleChoice1Input(Scanner scanner) {
        String promptMessage = """

                    # Select an option -->
                        1 - Access an account
                        2 - Show customer details
                        3 - Terminate application
                        
                        ENTER:\s""";

        return getValidIntegerInput(scanner, promptMessage);
    }

    // Choice 2 - prompt and recording input
    private static int handleChoice2Input(Scanner scanner) {
        String promptMessage = """

                        # Select an option -->
                            1 - Show account details
                            2 - Deposit cash
                            3 - Withdraw cash
                            4 - Transfer cash
                            5 - Go Back
                            
                            ENTER:\s""";

        return getValidIntegerInput(scanner, promptMessage);
    }

    // inside handleChoice1Input() and handleChoice2Input()
    private static int getValidIntegerInput(Scanner scanner, String promptMessage) {
        while (true) {
            System.out.print(promptMessage);
            try {
                int input = scanner.nextInt();
                scanner.nextLine();
                return input;

            } catch (InputMismatchException e) {
                System.out.println("\nInvalid input! Please enter a valid integer.");
                scanner.nextLine();  // Consume the invalid input
            }
        }
    }

    // Choice 1: OPTION_ACCESS_ACCOUNT
    private static void handleOptionAccessAccount(BankingSystem bankingSystem, Scanner scanner) {
        String accNumber = handleAccountNumberInput(scanner, "the");
        Account loadedAccount = bankingSystem.loadAccountByNumber(accNumber);

        if (loadedAccount == null) {
            System.out.println("\nNo account found with account number: " + accNumber);
            return;
        }

        handleAccountOperationsMenu(bankingSystem, scanner, loadedAccount);
    }

    // inside handleOptionAccessAccount()
    private static void handleAccountOperationsMenu(BankingSystem bankingSystem, Scanner scanner, Account loadedAccount) {
        boolean goBack = false;

        while (!goBack) {
            int choice2 = handleChoice2Input(scanner);

            switch (choice2) {
                case OPTION_SHOW_ACCOUNT_DETAILS:
                    handlePrintDetails(bankingSystem, loadedAccount);
                    break;
                case OPTION_DEPOSIT_CASH:
                    handleDepositCash(bankingSystem, scanner, loadedAccount);
                    break;
                case OPTION_WITHDRAW_CASH:
                    handleWithdrawCash(bankingSystem, scanner, loadedAccount);
                    break;
                case OPTION_TRANSFER_CASH:
                    handleTransferCash(bankingSystem, scanner, loadedAccount);
                    break;
                case OPTION_GO_BACK:
                    goBack = true;
                    break;
                default:
                    System.out.println("\nInvalid Input! Please select your choice and enter the relevant number.");
                    break;
            }
        }
    }

    // Choice 1 : OPTION_SHOW_CUSTOMER_DETAILS
    private static void handleOptionShowCustomerDetails(BankingSystem bankingSystem, Scanner scanner) {
        System.out.print("\n# Enter NIC: ");
        String nic = scanner.next().strip();

        Customer loadedCustomer = bankingSystem.loadCustomerByNIC(nic);
        if (loadedCustomer != null) {
            bankingSystem.printDetails(loadedCustomer);
        } else {
            System.out.println("\nNo customer found with NIC: " + nic);
        }
    }

    // Choice 1 : OPTION_ACCESS_ACCOUNT -> Choice 2 : OPTION_SHOW_ACCOUNT_DETAILS
    // and inside handleDepositCash(), handleWithdrawCash(), handleTransferCash()
    private static void handlePrintDetails(BankingSystem bankingSystem, Printable... printableObjects) {
        for (Printable printableObj : printableObjects) {
            if (printableObj == null) {
                System.out.println("\nCannot print the details of 'Object' since it is null.");
            } else {
                bankingSystem.printDetails(printableObj);
            }
        }
    }

    // Choice 1 : OPTION_ACCESS_ACCOUNT -> Choice 2 : OPTION_DEPOSIT_CASH
    private static void handleDepositCash(BankingSystem bankingSystem, Scanner scanner, Account loadedAccount) {
        double amount = handleAmountInput(scanner, "deposit");

        Transaction depositTransaction = bankingSystem.depositCash(loadedAccount, amount);

        Transaction mergedTransaction = bankingSystem.saveTransaction(depositTransaction);
        Account mergedAccount = bankingSystem.saveAccount(loadedAccount);

        System.out.print("\nDetails after the deposit:");
        handlePrintDetails(bankingSystem, mergedAccount, mergedTransaction);

    }

    // Choice 1 : OPTION_ACCESS_ACCOUNT -> Choice 2 : OPTION_WITHDRAW_CASH
    private static void handleWithdrawCash(BankingSystem bankingSystem, Scanner scanner, Account loadedAccount) {
        double amount = handleAmountInput(scanner, "withdraw");

        Transaction withdrawTransaction = bankingSystem.withdrawCash(loadedAccount, amount);

        Transaction mergedTransaction = bankingSystem.saveTransaction(withdrawTransaction);
        Account mergedAccount = bankingSystem.saveAccount(loadedAccount);

        System.out.println("\nDetails after the withdrawal:");
        handlePrintDetails(bankingSystem, mergedAccount, mergedTransaction);
    }

    // Choice 1 : OPTION_ACCESS_ACCOUNT -> Choice 2 : OPTION_TRANSFER_CASH
    private static void handleTransferCash(BankingSystem bankingSystem, Scanner scanner, Account loadedAccount) {
        String recipientAccNumber = handleAccountNumberInput(scanner, "recipient's");
        Account recipientAccount = bankingSystem.loadAccountByNumber(recipientAccNumber);
        double amount = handleAmountInput(scanner, "transfer");

        Transaction transferTransaction = bankingSystem.transferCash(loadedAccount, recipientAccount, amount);

        Transaction mergedTransaction = bankingSystem.saveTransaction(transferTransaction);
        bankingSystem.saveAccount(loadedAccount);
        bankingSystem.saveAccount(recipientAccount);

        System.out.println("\nDetails after the transfer:");
        handlePrintDetails(bankingSystem, loadedAccount, recipientAccount, mergedTransaction);
    }

    // inside handleOptionAccessAccount() and handleTransferCash()
    private static String handleAccountNumberInput(Scanner scanner, String partialString) {
        System.out.printf("\n# Enter %s account number: ", partialString);
        return scanner.next().strip();
    }

    // inside handleDepositCash(), handleWithdrawCash(), handleTransferCash()
    private static double handleAmountInput(Scanner scanner, String transactionType) {
        while (true) {
            System.out.printf("\nEnter amount to %s: ", transactionType);

            try {
                double amount = Double.parseDouble(scanner.next());
                if (amount <= 0) {
                    System.out.println("\nEntered amount is not a positive value! Please try again.");
                } else {
                    return amount;
                }
            } catch(NumberFormatException e) {
                System.out.println("\nInvalid input! Please enter a valid amount.");
            }
        }
    }
}