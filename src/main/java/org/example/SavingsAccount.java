package org.example;

import javax.persistence.*;

@Entity
@DiscriminatorValue("001")
public class SavingsAccount extends Account {

    public SavingsAccount() {

    }

    public SavingsAccount(int customerId, String accountNumber, String accountTypeId, String accountStatus,
                          double accountBalance, double interestRate, double overdraftLimit) {
        super(customerId, accountNumber, accountTypeId, accountStatus,
                accountBalance, interestRate, overdraftLimit);
    }


    public double getMinimumBalance() {
        return 2000;
    }

    @Override
    public String getType() {
        return "SAVINGS";
    }

    @Override
    public boolean withdraw(double amount) {
        double balance = this.getAccountBalance();
        if((balance - amount) > this.getMinimumBalance()) {
            this.setAccountBalance(balance - amount);
            return true;
        } else {
            System.out.println("\nInsufficient account balance available.");
            return false;
        }
    }
}
