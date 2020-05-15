package com.example.Autoservis.services;

import com.example.Autoservis.bean.Users;
import com.example.Autoservis.generic.GenericService;

public interface UsersService extends GenericService<Users> {

    int checkCredentials(String name, String password);
    long getID(String name, String password);
    Users findByUsernameAndPassword(String username, String password);
}