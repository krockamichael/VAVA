package com.example.Autoservis.services;

import com.example.Autoservis.bean.Rewards;
import com.example.Autoservis.generic.GenericService;

public interface RewardsService extends GenericService<Rewards> {
    Rewards findByNameAndSurname(String name, String surname);
}
