package com.example.Autoservis.bean;

import javax.persistence.*;

@Entity
@Table(name="Payouts")
public class Payouts {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "payout_id", updatable = false, nullable = false)
    private long payout_id;

    private String name;
    private String surname;
    private int amount;
    private int mechanic_id; //foreign key

    public long getPayout_id() {
        return payout_id;
    }

    public void setPayout_id(long payout_id) {
        this.payout_id = payout_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getMechanic_id() {
        return mechanic_id;
    }

    public void setMechanic_id(int mechanic_id) {
        this.mechanic_id = mechanic_id;
    }
}
