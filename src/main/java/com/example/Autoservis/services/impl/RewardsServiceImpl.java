package com.example.Autoservis.services.impl;

import com.example.Autoservis.bean.Rewards;
import com.example.Autoservis.repository.RewardsRepository;
import com.example.Autoservis.services.RewardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RewardsServiceImpl implements RewardsService {

    @Autowired
    private RewardsRepository rewardsRepository;

    @Override
    public Rewards findByNameAndSurname(String name, String surname) {
        return rewardsRepository.findByNameAndSurname(name, surname);
    }

    @Override
    public Rewards save(Rewards entity) {
        return rewardsRepository.save(entity);
    }

    @Override
    public Rewards update(Rewards entity) {
        return rewardsRepository.save(entity);
    }

    @Override
    public void delete(Rewards entity) {
        rewardsRepository.delete(entity);
    }

    @Override
    public void delete(Long id) {
        rewardsRepository.deleteById(id);
    }

    @Override
    public List<Rewards> findAll() {
        return rewardsRepository.findAll();
    }
}
