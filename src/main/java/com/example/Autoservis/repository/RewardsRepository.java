package com.example.Autoservis.repository;

import com.example.Autoservis.bean.Rewards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RewardsRepository extends JpaRepository<Rewards, Long> {
    Rewards findByNameAndSurname(String name, String surname);
}