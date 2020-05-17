package com.example.Autoservis.bean;

import javax.persistence.*;

@Entity
@Table(name="Cars")
public class Cars {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "car_id", updatable = false, nullable = false)
    private long car_id;
    private String model;
    private String brand;
    private String vin;
    private String fuel;
    private int owner_id;

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public Cars(){}

    public Cars(String Model, String Type, String VIN, int carId) {
        this.model = Model;
        this.brand = Type;
        this.vin = VIN;
        this.car_id = carId;
    }

    public Cars(String model, String brand, String VIN, String fuel, int owner_id) {
        this.model = model;
        this.brand = brand;
        this.vin = VIN;
        this.fuel = fuel;
        this.owner_id = owner_id;
    }

    public long getCar_id(){
        return car_id;
    }

    public void setCar_id(long id){
        this.car_id = id;
    }

    public String getModel(){
        return model;
    }

    public void setModel(String model){
        this.model = model;
    }

    public String getBrand(){
        return brand;
    }

    public void setBrand(String brand){
        this.brand = brand;
    }

    public String getVin(){
        return vin;
    }

    public void setVin(String vin){
        this.vin = vin;
    }

    public String getFuel(){
        return fuel;
    }

    public void setFuel(String fuel){
        this.fuel = fuel;
    }
}