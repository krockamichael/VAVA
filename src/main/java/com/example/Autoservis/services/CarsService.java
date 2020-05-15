package com.example.Autoservis.services;

import com.example.Autoservis.bean.Cars;
import com.example.Autoservis.generic.GenericService;

import java.sql.ResultSet;
import java.util.List;

public interface CarsService extends GenericService<Cars> {
    Cars findByModelAndBrandAndVin(String model, String brand, String vin);
    long getID(String model, String brand ,String vin);
    List<Cars> getCarsmaybe(String model, String brand, String vin);
}
