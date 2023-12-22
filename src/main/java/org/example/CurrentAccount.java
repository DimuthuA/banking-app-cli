package org.example;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("002")
public class CurrentAccount extends Account {


    public CurrentAccount() {

    }

    public CurrentAccount(int customerId, String accountNumber, String accountTypeId, String accountStatus,
                          double accountBalance, double interestRate, double overdraftLimit) {
        super(customerId, accountNumber, accountTypeId, accountStatus,
                accountBalance, interestRate, overdraftLimit);
    }

    @Override
    public String getType() {
        return "CURRENT";
    }

    @Override
    public boolean withdraw(double amount) {
        double balance = this.getAccountBalance();
        if ((balance + this.getOverdraftLimit() - amount) > 0) {
            this.setAccountBalance(balance - amount);
            return true;
        } else {
            System.out.println("\nWithdrawal exceeds the overdraft limit.");
            return false;
        }
    }
}
