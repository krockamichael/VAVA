package com.example.Autoservis.bean;

import javax.persistence.*;

@Entity
@Table(name="Rewards")
public class Rewards {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "reward_id", updatable = false, nullable = false)
    private long reward_id;

    private String name;
    private String surname;
    private int amount;
    private String reason;
    private int mechanic_id; //foreign key

    public long getReward_id() {
        return reward_id;
    }

    public void setReward_id(long reward_id) {
        this.reward_id = reward_id;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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
