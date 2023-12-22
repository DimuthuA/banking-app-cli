package org.example;

import javax.persistence.*;


@Entity
@Table(name = "account")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "account_type_id", discriminatorType = DiscriminatorType.STRING)
public abstract class Account implements Printable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int accountId;

    @Column(name = "customer_id")
    private int customerId;

    @Column(name = "account_no")
    private String accountNumber;

    @Column(name = "account_type_id", insertable = false, updatable = false)
    private String accountTypeId;

    @Column(name = "account_status")
    private String accountStatus;

    @Column(name = "current_balance")
    private double accountBalance;

    @Column(name = "interest_rate")
    private double interestRate;

    @Column(name = "overdraft_limit")
    private double overdraftLimit;

    public Account() {
    }

    public Account(int customerId, String accountNumber, String accountTypeId, String accountStatus,
                   double accountBalance, double interestRate, double overdraftLimit) {
        this.customerId = customerId;
        this.accountNumber = accountNumber;
        this.accountTypeId = accountTypeId;
        this.accountStatus = accountStatus;
        this.accountBalance = accountBalance;
        this.interestRate = interestRate;
        this.overdraftLimit = overdraftLimit;
    }

    public int getAccountId() {
        return accountId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountTypeId() {
        return accountTypeId;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(double overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }


    public abstract String getType();

    public void printDetails() {
        System.out.printf("""
                        
                        ACCOUNT DETAILS:
                            Account ID: %d
                            Customer ID: %d
                            Account Number: %s
                            Account Type: %s
                            Account Status: %s
                            Current Balance: %.2f
                            Interest Rate: %.2f
                            Overdraft Limit: %.2f%n""",
                this.getAccountId(), this.getCustomerId(), this.getAccountNumber(), this.getType(),
                this.getAccountStatus(), this.getAccountBalance(), this.getInterestRate(),
                this.getOverdraftLimit());
    }

    public double calculateInterest() {
        return (this.getAccountBalance() * this.getInterestRate());
    }

    public void deposit(double amount) {
        double balance = this.getAccountBalance();
        this.setAccountBalance(balance + amount);
    }

    public boolean withdraw(double amount) {

        double balance = this.getAccountBalance();
        if (balance > amount) {
            this.setAccountBalance(balance - amount);
            return true;
        } else {
            return false;
        }
    }
}

/*
    @Transient is used to indicate that a field or method should not be persisted or stored in the database.
    In other words, it tells Hibernate to ignore the annotated element during the process of database mapping
    and persistence.
*/


