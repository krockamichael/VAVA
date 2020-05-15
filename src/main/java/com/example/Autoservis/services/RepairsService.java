package com.example.Autoservis.services;

import com.example.Autoservis.bean.Repairs;
import com.example.Autoservis.generic.GenericService;
import org.springframework.data.jpa.repository.Query;

import java.sql.ResultSet;
import java.util.List;

public interface RepairsService extends GenericService<Repairs> {
    Repairs findByMechanicId (int mechanic_id);
    Repairs findByCarId(int car_id);
    int total(int mechanic_id);
    int AvgDate(int mechanic_id);
    String getCostSum(int car_id);
    int GetNumberOfR(int mechanic_id);
    List<Repairs> getWorkDetails(int mechnic_id);
}
