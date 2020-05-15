package com.example.Autoservis.bean;

import javax.persistence.*;

@Entity
@Table(name="Mechanics")
public class Mechanics {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "mechanic_id", updatable = false, nullable = false)
    private int mechanicId;

    private String name;
    private String surname;
    private int user_id;  //foreign key

    public Mechanics(){}

    public Mechanics(String name, String surname)
    {
        this.name = name;
        this.surname = surname;
    }

    public int getMechanicId() {
        return mechanicId;
    }

    public void setMechanicId(int mechanic_id) {
        this.mechanicId = mechanic_id;
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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}