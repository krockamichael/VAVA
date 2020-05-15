package com.example.Autoservis.repository;

import com.example.Autoservis.bean.Rewards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RewardsRepository extends JpaRepository<Rewards, Long> {
    Rewards findByNameAndSurname(String name, String surname);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Rewards R WHERE R.mechanic_id = ?1 AND R.name = ?2 AND R.surname = ?3")
    void DeleteRewardMechanic(int mechanic_id,String name, String surname);
}