package com.example.Autoservis.bean;

import javax.persistence.*;

@Entity
@Table(name="Components")
public class Components {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "component_id", updatable = false, nullable = false)
    private long componentId;
    private double cost;
    @Column(name = "car_type")
    private String carType;
    private String name;
    private int amount;

    public Components(){}

    public Components(String Component, String CarType, int Amount, int ID) {
        this.name = Component;
        this.carType = CarType;
        this.amount = Amount;
        this.componentId = ID;
    }

    public Components(String name, String carType, int amount, double cost) {
        this.name = name;
        this.carType = carType;
        this.amount = amount;
        this.cost = cost;
    }

    public long getComponentId() {
        return componentId;
    }

    public void setComponentId(long componentId) {
        this.componentId = componentId;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}