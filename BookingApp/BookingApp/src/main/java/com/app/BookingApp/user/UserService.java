package com.app.BookingApp.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private UserResposistory userResposistory;

    @Autowired
    public UserService(UserResposistory userResposistory) {
        this.userResposistory = userResposistory;
    }

    public Iterable<User> getAllUsers() {
        return userResposistory.findAll();
    }

    public void addNewUser(User user) {
        userResposistory.save(user);
    }

}