package com.example.Autoservis.repository;

import com.example.Autoservis.bean.Components;
import com.example.Autoservis.bean.Customers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomersRepository extends JpaRepository<Customers, Long> {
    Customers findByNameAndSurname(String name, String surname);
    Customers findByCustomerId(long customer_id);
}