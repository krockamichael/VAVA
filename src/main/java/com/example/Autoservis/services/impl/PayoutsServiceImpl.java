package com.example.Autoservis.services.impl;

import com.example.Autoservis.bean.Payouts;
import com.example.Autoservis.repository.PayoutsRepository;
import com.example.Autoservis.services.PayoutsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PayoutsServiceImpl implements PayoutsService {

    @Autowired
    private PayoutsRepository payoutsRepository;

    @Override
    public Payouts findByNameAndSurname(String name, String surname) {
        return payoutsRepository.findByNameAndSurname(name, surname);
    }

    @Override
    public void deletePayoutMechanic(int mechanic_id, String name, String surname) {
        payoutsRepository.deletePayoutMechanic(mechanic_id, name, surname);
    }

    @Override
    public void updatePayoutMechanic(int amount, int mechanic_id, String name, String surname) {
        payoutsRepository.updatePayoutMechanic(amount, mechanic_id, name, surname);
    }

    @Override
    public Payouts save(Payouts entity) {
        return payoutsRepository.save(entity);
    }

    @Override
    public Payouts update(Payouts entity) {
        return payoutsRepository.save(entity);
    }

    @Override
    public void delete(Payouts entity) {
        payoutsRepository.delete(entity);
    }

    @Override
    public void delete(Long id) {
        payoutsRepository.deleteById(id);
    }

    @Override
    public List<Payouts> findAll() {
        return payoutsRepository.findAll();
    }
}
