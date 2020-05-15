package com.example.Autoservis.bean;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name="Repairs")
public class Repairs {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "repair_id", updatable = false, nullable = false)
    private long repair_id;

    private double cost;
    private String repair;
    private Date start_day;
    private Date end_day;
    private int mechanicId; //foreign key
    private int carId;   //foreign key

    public void setCost(double cost) {
        this.cost = cost;
    }

    @javax.persistence.Transient
    protected String mechanic_name;

    @javax.persistence.Transient
    protected int days;

    public String getMechanic_name() {
        return mechanic_name;
    }

    public void setMechanic_name(String mechanic_name) {
        this.mechanic_name = mechanic_name;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public Repairs()
    {
        //      this.days = day;
    }

    public Repairs(String repairT, Date startD, Date endD, int day)
    {
        this.repair = repairT;
        this.start_day = startD;
        this.end_day = endD;
        this.days = day;
    }

    public Repairs(String repairT, Date startD, Date endD, String MechanicName, Double Cost){
        this.repair = repairT;
        this.start_day = startD;
        this.end_day = endD;
        this.mechanic_name = MechanicName;
        this.cost = Cost;

    }

    public long getRepair_id() {
        return repair_id;
    }

    public void setRepair_id(long repair_id) {
        this.repair_id = repair_id;
    }

    public double getCost() {
        return cost;
    }

    public String getRepair() {
        return repair;
    }

    public void setRepair(String repair) {
        this.repair = repair;
    }

    public String getStart_day() {
        return String.valueOf(start_day);
    }

    public void setStart_day(Date start_day) { this.start_day = start_day; }

    public String getEnd_day() {
        return String.valueOf(end_day);
    }

    public void setEnd_day(Date end_day) {
        this.end_day = end_day;
    }

    public int getMechanicId() {
        return mechanicId;
    }

    public void setMechanicId(int mechanic_id) {
        this.mechanicId = mechanic_id;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int car_id) {
        this.carId = car_id;
    }
}