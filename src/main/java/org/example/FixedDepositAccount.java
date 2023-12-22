package org.example;

import javax.persistence.*;

@Entity
@DiscriminatorValue("003")
public class FixedDepositAccount extends Account {

//    private int term;


    public FixedDepositAccount() {

    }

    public FixedDepositAccount(int customerId, String accountNumber, String accountTypeId, String accountStatus,
                          double accountBalance, double interestRate, double overdraftLimit) {
        super(customerId, accountNumber, accountTypeId, accountStatus,
                accountBalance, interestRate, overdraftLimit);
    }

    public double calculateBalance() {
        int TERM = 12;
        return this.getAccountBalance() * (1 + ((this.getInterestRate() / 100) * TERM / 12));
    }

    public Account closeFixedDeposit() {
        double newBalance = this.calculateBalance();
        Account newAccount = new SavingsAccount(this.getCustomerId(), this.getAccountNumber(),
                this.getAccountTypeId(), "ACTIVE", newBalance, 8, 0);

        this.setAccountStatus("CLOSED");
        return newAccount;
    }

    public void renewFixedDeposit(double additionalAmount) {
        this.setAccountBalance(this.calculateBalance() + additionalAmount);
    }

    @Override
    public String getType() {
        return "FD";
    }

    @Override
    public void deposit(double amount) {
        System.out.println("\nRenewing the Fixed Deposit with the additional amount, for another term.");
        this.renewFixedDeposit(amount);
    }

//    @Override
//    public Account withdraw(double amount) {
//        System.out.println("\nClosing the Fixed Deposit, and transferring the amount to a new Savings Account.");
//
//        return this.closeFixedDeposit();
//    }
}
