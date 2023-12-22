package org.example;

import javax.persistence.*;

@Entity
@Table(name = "customer")
public class Customer implements Printable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int customerId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "nic")
    private String NIC;

    public Customer() {

    }

    public Customer(String firstName, String lastName, String NIC) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.NIC = NIC;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNIC() {
        return NIC;
    }

    public void setNIC(String NIC) {
        this.NIC = NIC;
    }


    public void printDetails() {
        System.out.printf("""
                            
                            CUSTOMER DETAILS:
                                Customer ID: %d
                                First Name: %s
                                Last Name: %s
                                NIC: %s%n""",
                this.getCustomerId(), this.getFirstName(), this.getLastName(), this.getNIC());
    }
}
