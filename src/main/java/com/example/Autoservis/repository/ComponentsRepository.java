package com.example.Autoservis.repository;

import com.example.Autoservis.bean.Components;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComponentsRepository extends JpaRepository<Components, Long> {
    Components findByNameAndCarType(String name, String carType);
    Components findByComponentId(long component_id);
}