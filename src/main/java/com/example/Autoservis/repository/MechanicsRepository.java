package com.example.Autoservis.repository;

import com.example.Autoservis.bean.Mechanics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MechanicsRepository extends JpaRepository<Mechanics, Long> {
    Mechanics findByNameAndSurname(String name, String surname);
    Mechanics findByMechanicId(int mechanic_id);
}