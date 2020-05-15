package com.example.Autoservis.services;

import com.example.Autoservis.bean.Payouts;
import com.example.Autoservis.generic.GenericService;

public interface PayoutsService extends GenericService<Payouts> {
    Payouts findByNameAndSurname(String name, String surname);
}
