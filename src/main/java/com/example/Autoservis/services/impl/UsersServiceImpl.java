package com.example.Autoservis.services.impl;

import java.util.List;

import com.example.Autoservis.bean.Users;
import com.example.Autoservis.controller.MainController;
import com.example.Autoservis.repository.UsersRepository;
import com.example.Autoservis.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public Users save(Users entity) {
        return usersRepository.save(entity);
    }

    @Override
    public Users update(Users entity) {
        return usersRepository.save(entity);
    }

    @Override
    public void delete(Users entity) {
        usersRepository.delete(entity);
    }

    @Override
    public void delete(Long id) {
        usersRepository.deleteById(id);
    }

    @Override
    public List<Users> findAll() {
        return usersRepository.findAll();
    }

    @Override
    public Users findByUsernameAndPassword(String username, String password) {
        return usersRepository.findByUsernameAndPassword(username,password);
    }


    @Override
    public int checkCredentials(String name, String password) {
        Users user = this.findByUsernameAndPassword(name, password);
        if(user == null){
            return 3;
        }else{
            if(password.equals(user.getPassword())) {
                MainController.UID = (int) user.getUser_id();
                return user.getType();
            }
            else return 3;
        }
    }

    @Override
    public long getID(String name, String password) {
        Users user = this.findByUsernameAndPassword(name, password);
        if(user == null){
            return 0;
        }else{
            if(password.equals(user.getPassword())) return user.getUser_id();
            else return 0;
        }
    }
}