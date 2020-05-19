package com.example.Autoservis.repository;

import com.example.Autoservis.bean.Cars;
import com.example.Autoservis.bean.Repairs;
import com.example.Autoservis.bean.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;

@Repository
public interface RepairsRepository extends JpaRepository<Repairs, Long> {
    Repairs findByMechanicId(int mechanic_id);
    Repairs findByCarId(int car_id);

    @Query(value = "SELECT SUM(R.end_day - R.start_day + 1) FROM Repairs R JOIN Mechanics M on M.mechanicId = R.mechanicId WHERE M.mechanicId = ?1")
    String total(int mechanic_id);

    @Query(value = "SELECT AVG(R.end_day - R.start_day + 1) FROM Repairs R JOIN Mechanics M on M.mechanicId = R.mechanicId WHERE M.mechanicId = ?1")
    String avgDate(int mechanic_id);

    @Query(value = "SELECT SUM(R.cost) AS RepairCost,SUM(Cs.cost) AS ComponentCost FROM Repairs R INNER JOIN Component C on R.repair_id = C.repair_id INNER JOIN Components Cs on C.components_id = Cs.componentId WHERE R.carId = ?1")
    String getCostSum(int car_id);


    @Query(value = "SELECT COUNT(R.repair_id) FROM Repairs R JOIN Mechanics M on M.mechanicId = R.mechanicId WHERE M.mechanicId = ?1")
    String getNumberOfR(int mechanic_id);
}
