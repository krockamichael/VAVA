package com.example.Autoservis.repository;

import com.example.Autoservis.bean.Payouts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PayoutsRepository extends JpaRepository<Payouts, Long> {
    Payouts findByNameAndSurname(String name, String surname);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Payouts P WHERE P.mechanic_id = ?1 AND P.name = ?2 AND P.surname = ?3")
    void deletePayoutMechanic(int mechanic_id,String name, String surname);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Payouts P SET P.amount = ?1 WHERE P.mechanic_id = ?2 AND P.name = ?3 AND P.surname = ?4")
    void updatePayoutMechanic(int amount, int mechanic_id,String name, String surname);
}
