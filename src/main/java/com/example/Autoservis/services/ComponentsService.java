package com.example.Autoservis.services;

import com.example.Autoservis.bean.Cars;
import com.example.Autoservis.bean.Components;
import com.example.Autoservis.generic.GenericService;

import java.util.List;

public interface ComponentsService extends GenericService<Components> {
    Components findByNameAndCarType(String name, String carType);
    List<Components> getComponents(String name, String car_type);
    Components findByComponentId(long component_id);
}
