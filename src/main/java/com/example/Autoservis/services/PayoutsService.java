package com.example.Autoservis.services;

import com.example.Autoservis.bean.Payouts;
import com.example.Autoservis.generic.GenericService;

public interface PayoutsService extends GenericService<Payouts> {
    Payouts findByNameAndSurname(String name, String surname);
    void deletePayoutMechanic(int mechanic_id,String name, String surname);
    void updatePayoutMechanic(int amount, int mechanic_id,String name, String surname);
}
