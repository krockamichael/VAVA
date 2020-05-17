package com.example.Autoservis.bean;

import javax.persistence.*;

@Entity
@Table(name="Customers")
public class Customers {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "customer_id", updatable = false, nullable = false)
    private long customerId;
    private String name;
    private String surname;
    private String email;
    private String phone_number;
    private String id;

    public Customers(){}

    public Customers(String name, String surname, String id, int ElementID) {
        this.name = name;
        this.surname = surname;
        this.id = id;
        this.customerId = ElementID;
    }

    public Customers(String name, String surname, String email, String phone_number, String id) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone_number = phone_number;
        this.id = id;
    }

    //@OneToOne(mappedBy = "customerModel")
    //public CarModel getCarModel() {
    //  return carModel;
    //}

    //public void setCarModel(CarModel carModel) {
    //  this.carModel = carModel;
    //}

    public void setCustomerId(long customer_id) {
        this.customerId = customer_id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCustomerId(){
        return customerId;
    }
}