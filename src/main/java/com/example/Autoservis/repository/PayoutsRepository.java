package com.example.Autoservis.repository;

import com.example.Autoservis.bean.Payouts;
import com.example.Autoservis.bean.Rewards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayoutsRepository extends JpaRepository<Payouts, Long> {
    Payouts findByNameAndSurname(String name, String surname);
}
