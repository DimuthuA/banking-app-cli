package org.example;

import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

public class BankingSystem {

    public Account loadAccountByNumber(String accNumber) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hibernate");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Account account = null;

        try {
            entityManager.getTransaction().begin();

            entityManager.unwrap(Session.class).doWork(connection -> {
                try {
                    connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                } catch (SQLException e) {
                    System.out.println("An error occurred while trying to set the Isolation Level");
                }
            });

//            entityManager.createNativeQuery("SET TRANSACTION ISOLATION LEVEL SERIALIZABLE").executeUpdate();

            account = entityManager.createQuery(
                            "SELECT a FROM Account a WHERE a.accountNumber = :accNumber", Account.class)
                    .setParameter("accNumber", accNumber)
                    .getSingleResult();

            entityManager.getTransaction().commit();

        } catch (NoResultException e) {
            // if no such account is found in the database

        } finally {
            try {
                if (entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback();
                }
            } catch (Exception e) {
                System.out.println("\nException occurred while retrieving account details!");
            }

            try {
                if (entityManager.isOpen()) {
                    entityManager.close();
                }
            } catch (Exception e) {
                System.out.println("\nException occurred while attempting to close entityManager!");
            }

            try {
                if (entityManagerFactory.isOpen()) {
                    entityManagerFactory.close();
                }
            } catch (Exception e) {
                System.out.println("\nException occurred while attempting to close entityManagerFactory!");
            }

        }

        return account;
    }

    public Customer loadCustomerByNIC(String NIC) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hibernate");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Customer customer = null;

        try {
            entityManager.getTransaction().begin();

            entityManager.unwrap(Session.class).doWork(connection -> {
                try {
                    connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                } catch (SQLException e) {
                    System.out.println("An error occurred while trying to set the Isolation Level");
                }
            });

//            entityManager.createNativeQuery("SET TRANSACTION ISOLATION LEVEL SERIALIZABLE").executeUpdate();

            customer = entityManager.createQuery(
                            "SELECT a FROM Customer a WHERE a.NIC = :NIC", Customer.class)
                    .setParameter("NIC", NIC)
                    .getSingleResult();

            entityManager.getTransaction().commit();




        } catch (NoResultException e) {
            // if no such customer is found in the database

        } finally {
            try {
                if (entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback();
                }
            } catch (Exception e) {
                System.out.println("\nException occurred while retrieving customer details!");
            }

            try {
                if (entityManager.isOpen()) {
                    entityManager.close();
                }
            } catch (Exception e) {
                System.out.println("\nException occurred while attempting to close entityManager!");
            }

            try {
                if (entityManagerFactory.isOpen()) {
                    entityManagerFactory.close();
                }
            } catch (Exception e) {
                System.out.println("\nException occurred while attempting to close entityManagerFactory!");
            }

        }

        return customer;
    }

    public Account saveAccount(Account account) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hibernate");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Account mergedAccount = null;

        try {
            entityManager.getTransaction().begin();

            entityManager.unwrap(Session.class).doWork(connection -> {
                try {
                    connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                } catch (SQLException e) {
                    System.out.println("An error occurred while trying to set the Isolation Level");
                }
            });

//            entityManager.createNativeQuery("SET TRANSACTION ISOLATION LEVEL SERIALIZABLE").executeUpdate();

            mergedAccount = entityManager.merge(account);

            entityManager.getTransaction().commit();

            System.out.println("\nAccount details saved successfully!");

        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }

            System.out.println("\nException occurred while saving account details!");

        } finally {
            try {
                if (entityManager.isOpen()) {
                    entityManager.close();
                }
            } catch (Exception e) {
                System.out.println("\nException occurred while attempting to close entityManager!");
            }

            try {
                if (entityManagerFactory.isOpen()) {
                    entityManagerFactory.close();
                }
            } catch (Exception e) {
                System.out.println("\nException occurred while attempting to close entityManagerFactory!");
            }
        }

        return mergedAccount;
    }


    public Transaction saveTransaction(Transaction transaction) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hibernate");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Transaction mergedTransaction = null;

        try {
            entityManager.getTransaction().begin();

            entityManager.unwrap(Session.class).doWork(connection -> {
                try {
                    connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                } catch (SQLException e) {
                    System.out.println("An error occurred while trying to set the Isolation Level");
                }
            });

//            entityManager.createNativeQuery("SET TRANSACTION ISOLATION LEVEL SERIALIZABLE").executeUpdate();

            mergedTransaction = entityManager.merge(transaction);

            entityManager.getTransaction().commit();

            System.out.println("\nTransaction details saved successfully!");

        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }

            System.out.println("\nException occurred while saving transaction details!");

        } finally {
            try {
                if (entityManager.isOpen()) {
                    entityManager.close();
                }
            } catch (Exception e) {
                System.out.println("\nException occurred while attempting to close entityManager!");
            }

            try {
                if (entityManagerFactory.isOpen()) {
                    entityManagerFactory.close();
                }
            } catch (Exception e) {
                System.out.println("\nException occurred while attempting to close entityManagerFactory!");
            }
        }

        return mergedTransaction;
    }

    public void printDetails(Printable printableObject) {
        printableObject.printDetails();
    }

    public Transaction depositCash(Account account, double amount) {
        Transaction transaction = new Transaction(null, account.getAccountId(),
                amount, "DEPOSIT", "PENDING");

        if (Objects.equals(account.getType(), "FD")) {
            System.out.println("\nCannot deposit cash to an active Fixed-Deposit account!" +
                    "Renew the FD with the additional amount.");

            transaction.setStatus("FAILED");
        } else {
            account.deposit(amount);

            transaction.setStatus("COMPLETED");
        }
        return transaction;
    }

    public Transaction withdrawCash(Account account, double amount) {
        Transaction transaction = new Transaction(account.getAccountId(), null,
                amount, "WITHDRAW", "PENDING");

        if (Objects.equals(account.getType(), "FD")) {
            System.out.println("\nCannot withdraw cash from an active Fixed-Deposit account!" +
                    "Close the Fixed-Deposit first to withdraw.");

            transaction.setStatus("FAILED");
        } else {
            boolean result = account.withdraw(amount);

            if (result) {
                System.out.printf("""
                                
                                %.2f LKR successfully withdrawn from Account No: %s
                                """,
                        amount, account.getAccountNumber());

                transaction.setStatus("COMPLETED");
            } else {
                System.out.printf("""
                                
                                Couldn't withdraw cash from Account No: %s
                                """,
                        account.getAccountNumber());

                transaction.setStatus("FAILED");
            }
        }
        return transaction;
    }

    public Transaction transferCash(Account senderAccount, Account recipientAccount, double amount) {
        Transaction transaction = new Transaction(senderAccount.getAccountId(), recipientAccount.getAccountId(),
                amount, "TRANSFER", "PENDING");

        if (Objects.equals(senderAccount.getType(), "FD") || Objects.equals(recipientAccount.getType(), "FD")) {
            System.out.println("\nTransaction failed! Sender account or recipient account is a Fixed-Deposit.");

            transaction.setStatus("FAILED");
        } else {
            boolean result = senderAccount.withdraw(amount);

            if (result) {
                recipientAccount.deposit(amount);
                transaction.setStatus("COMPLETED");

                System.out.printf("""
                                
                                Transaction Successful! %.2f LKR transferred
                                    from Sender Account No: %s
                                    to Recipient Account No: %s
                                """,
                        amount, senderAccount.getAccountNumber(), recipientAccount.getAccountNumber());

            } else {
                transaction.setStatus("FAILED");

                System.out.printf("""
                                
                                Transaction Failed! Transfer request of amount %.2f LKR
                                    from Sender Account No: %s
                                    to Recipient Account No: %s
                                has FAILED to completes.
                                """,
                        amount, senderAccount.getAccountNumber(), recipientAccount.getAccountNumber());

            }

        }
        return transaction;
    }

    public Account createAccount(int customerId, String accountNo, String accountTypeId, String accountStatus,
                                 double accountBalance, double interestRate, double overdraftLimit) {
        Account newAccount;

        if (Objects.equals(accountTypeId, "001")) {
            newAccount = new SavingsAccount(customerId, accountNo, accountTypeId, accountStatus, accountBalance,
                    interestRate, overdraftLimit);
        } else if (Objects.equals(accountTypeId, "002")) {
            newAccount = new CurrentAccount(customerId, accountNo, accountTypeId, accountStatus, accountBalance,
                    interestRate, overdraftLimit);
        } else if (Objects.equals(accountTypeId, "003")) {
            newAccount = new FixedDepositAccount(customerId, accountNo, accountTypeId, accountStatus, accountBalance,
                    interestRate, overdraftLimit);
        } else {
            System.out.println("\nAccount creation failed! Invalid Account Type ID");
            return null;
        }


        System.out.printf("""
                                
                                Account No: %s successfully created!
                                """,
                newAccount.getAccountNumber());

        newAccount.printDetails();

        return newAccount;
    }

    public Customer createCustomer(String firstName, String lastName, String NIC) {
        return new Customer(firstName, lastName, NIC);
    }

}
