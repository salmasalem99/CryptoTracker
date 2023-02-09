package com.task.cryptotracker.service;
import com.task.cryptotracker.entity.*;

import com.task.cryptotracker.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public boolean isPresent(String username){
        boolean returnVal = repository.findByUsername(username).isPresent();
        return returnVal;
    }
    public boolean isAdmin(String username){
        String userType = repository.findByUsername(username).get().getUsertype();
        return userType.equals("admin");
    }
    public List<Alert> getAllAlerts(String username){
        return repository.findByUsername(username).get().getAlerts();
    }
}
