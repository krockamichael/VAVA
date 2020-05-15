package com.example.Autoservis.services;

import com.example.Autoservis.bean.Components;
import com.example.Autoservis.bean.Customers;
import com.example.Autoservis.generic.GenericService;

import java.util.List;

public interface CustomersService extends GenericService<Customers> {
    Customers findByNameAndSurname(String name, String surname);
    List<Customers> getCustomers(String name, String surname);
    Customers findByCustomerId(long customer_id);
}
