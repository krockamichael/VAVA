package com.example.Autoservis.services;

import com.example.Autoservis.bean.Cars;
import com.example.Autoservis.generic.GenericService;
import java.util.List;

public interface CarsService extends GenericService<Cars> {
    List<Cars> getCarsMaybe(String model, String brand, String vin);
}
