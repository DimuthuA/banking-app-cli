package org.example;

import javax.persistence.*;

@Entity
@Table(name = "transaction")
public class Transaction implements Printable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int transactionId;

    @Column(name = "sender_acc_id")
    private Integer senderAccountId;

    @Column(name = "recipient_acc_id")
    private Integer recipientAccountId;

    @Column(name = "amount")
    private double amount;

    @Column(name = "type")
    private String type;

    @Column(name = "status")
    private String status;

    public Transaction() {

    }

    public Transaction(Integer senderAccountId, Integer recipientAccountId, double amount, String type, String status) {
        this.senderAccountId = senderAccountId;
        this.recipientAccountId = recipientAccountId;
        this.amount = amount;
        this.type = type;
        this.status = status;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public Integer getSenderAccountId() {
        return senderAccountId;
    }

    public void setSenderAccountId(Integer senderAccountId) {
        this.senderAccountId = senderAccountId;
    }

    public Integer getRecipientAccountId() {
        return recipientAccountId;
    }

    public void setRecipientAccountId(Integer recipientAccountId) {
        this.recipientAccountId = recipientAccountId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void printDetails() {
        System.out.printf("""
                        
                        TRANSACTION DETAILS:
                            Transaction ID: %d
                            Sender Account ID: %s
                            Recipient Account ID: %s
                            Amount: %.2f
                            Transaction Type: %s
                            Transaction Status: %s%n""",
                this.getTransactionId(), this.getSenderAccountId(), this.getRecipientAccountId(), this.getAmount(),
                this.getType(), this.getStatus());
    }
}
