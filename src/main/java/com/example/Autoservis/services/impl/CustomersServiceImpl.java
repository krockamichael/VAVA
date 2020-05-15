package com.example.Autoservis.services.impl;

import com.example.Autoservis.bean.Components;
import com.example.Autoservis.bean.Customers;
import com.example.Autoservis.repository.CustomersRepository;
import com.example.Autoservis.services.CustomersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Service
public class CustomersServiceImpl implements CustomersService {
    @Autowired
    private CustomersRepository customersRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Customers findByNameAndSurname(String name, String surname) {
        return customersRepository.findByNameAndSurname(name, surname);
    }

    @Override
    public List<Customers> getCustomers(String name, String surname) {
        TypedQuery<Customers> query = entityManager.createQuery("SELECT c FROM Customers c WHERE c.name like ?1 AND c.surname like ?2", Customers.class);
        query.setParameter(1, name+"%");
        query.setParameter(2, surname+"%");
        return query.getResultList();
    }

    @Override
    public Customers findByCustomerId(long customer_id) {
        return customersRepository.findByCustomerId(customer_id);
    }

    @Override
    public Customers save(Customers entity) {
        return customersRepository.save(entity);
    }

    @Override
    public Customers update(Customers entity) {
        return customersRepository.save(entity);
    }

    @Override
    public void delete(Customers entity) {
        customersRepository.delete(entity);
    }

    @Override
    public void delete(Long id) {
        customersRepository.deleteById(id);
    }

    @Override
    public List<Customers> findAll() {
        return customersRepository.findAll();
    }
}
