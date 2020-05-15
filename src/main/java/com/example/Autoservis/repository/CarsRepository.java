package com.example.Autoservis.repository;

import com.example.Autoservis.bean.Cars;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;

@Repository
public interface CarsRepository extends JpaRepository<Cars, Long> {
    Cars findByModelAndBrandAndVin(String model, String brand, String vin);
}