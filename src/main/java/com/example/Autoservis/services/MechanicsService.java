package com.example.Autoservis.services;

import com.example.Autoservis.bean.Mechanics;
import com.example.Autoservis.generic.GenericService;
import java.util.List;

public interface MechanicsService extends GenericService<Mechanics>  {
    Mechanics findByNameAndSurname(String name, String surname);
    Mechanics findByMechanicId(int mechanic_id);
    List<Mechanics> getMechanics(String name, String surname);
}